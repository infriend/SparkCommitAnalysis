import com.alibaba.fastjson.JSON
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{IOUtils, LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Durations, StreamingContext}

import java.io.{BufferedInputStream, File, FileInputStream, FileNotFoundException, IOException, InputStream, OutputStream}
import java.net.URI
import java.sql.DriverManager

/**
 * SparkStreamingFile
 */
object SparkStreamingFile {
  def main(args: Array[String]): Unit = {
    // 0.初始化
    init()
    // 1.获取Class
    val classes: Array[Class[_]] = Array[Class[_]](classOf[LongWritable], classOf[Text])
    // 2.创建配置类
    val conf = new SparkConf().setAppName("SparkStreamingFile").setMaster("local[*]")
    // 2.配置参数
    conf.set("spark.streaming.fileStream.minRememberDuration", "2592000s")
    conf.set("spark.serialize", classOf[KryoSerializer].getName())
    conf.registerKryoClasses(classes)
    // 3.设置批次间隔时间
    val streamingContext = new StreamingContext(conf, Durations.seconds(5))
    // 4.设置输入路径
    val inputPath = "hdfs://114.116.51.171:8020/commit"
    // 5.创建hadoopConf
    // 5.创建hadoopConf
    val hadoopConf = new Configuration()
    // 6.监听目录读取数据
    val fileStream: InputDStream[(LongWritable, Text)] = streamingContext
      .fileStream[LongWritable, Text, TextInputFormat](inputPath, (path: Path) => {
        path.getName.endsWith(".json")
      }, false, hadoopConf)
    // 7.对数据进行解析
    val mapStream: DStream[(String, String, String)] = fileStream.map(value => {
      // 8.解析第一层Json
      val dataJson = JSON.parseObject(value._2.toString())
      // 9.解析第二层Json
      val commitJson = JSON.parseObject(dataJson.getString("commit"))
      // 10.解析第三次Json
      val committerJson = JSON.parseObject(commitJson.getString("committer"))
      // 11.获取每次
      val name = committerJson.getString("name")
      // 12.获取日期
      val date = committerJson.getString("date")
      // 13.获取年份
      val year = date.substring(0, 4)
      // 14.获取年月
      val month = date.substring(0, 7).replace("-", "")
      //15.构建清洗数据
      (name, year, month)
    })
    // 16.统计每人每年总提交次数
    val task1Stream: DStream[(String, String, Int)] = mapStream
      .map(value => ((value._1, value._2), 1))
      .reduceByKey(_ + _)
      .map(value => (value._1._1, value._1._2, value._2))
    // 17.统计)统计每年每月系统总的commit总次数
    val task2Stream: DStream[(String, Int)] = mapStream
      .map(value => (value._3, 1))
      .reduceByKey(_ + _)
      .map(value => (value._1, value._2))
    // 18.统计结果写入mysql
    task1Stream.foreachRDD(rdd => {
      rdd.foreachPartition(i => {
        // 1.定义连接
        val connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/commit?allowMultiQueries=true", "root", "root")
        // 2.创建SQL
        val sql = "insert into tbPersioncommit(name,commit_year,commit_counter) values(?,?,?)"
        // 3.设置参数
        val ps = connection.prepareStatement(sql)
        for (a <- i) {
          ps.setString(1, a._1)
          ps.setString(2, a._2)
          ps.setInt(3, a._3)
          ps.addBatch()
        }
        println(ps)
        // 4.批量执行
        ps.executeBatch()
        // 5.关闭连接
        ps.close()
        connection.close()
      })
    })
    // 19.统计结果写入mysql
    task2Stream.foreachRDD(rdd => {
      rdd.foreachPartition(i => {
        // 1.定义连接
        val connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/commit?allowMultiQueries=true&serverTimezone=UTC", "root", "root")
        // 2.创建SQL
        val sql = "insert into tbSyscommit(commit_ym,commit_counter) values(?,?)"
        val ps = connection.prepareStatement(sql)
        for (a <- i) {
          ps.setString(1, a._1)
          ps.setInt(2, a._2)
          ps.addBatch()
        }
        // 4.批量执行
        ps.executeBatch()
        // 5.关闭连接
        ps.close()
        connection.close()
      })
    })
    // 20.是否触发job取决于设置的Duration时间间隔
    streamingContext.start()
    // 21.等待程序结束
    streamingContext.awaitTermination()
  }

  def init(): Unit = {
    // 1.定义连接
    val connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/commit?allowMultiQueries=true&serverTimezone=UTC&useSSL=false", "root", "root")
    // 2.清空表数据
    val deleteTask1 = "delete from tbPersioncommit"
    val deleteTask2 = "delete from tbSyscommit"
    // 3.设置SQL
    connection.prepareStatement(deleteTask1).execute()
    connection.prepareStatement(deleteTask2).execute()
  }


}