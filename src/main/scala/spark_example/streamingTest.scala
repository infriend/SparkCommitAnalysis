package spark_example

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{StringType, StructType}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object streamingTest {
    def main(args: Array[String]): Unit = {
        Logger.getLogger("org").setLevel(Level.ERROR)
        val sparkConf: SparkConf = new SparkConf().setAppName("HdfsWordCount").setMaster("local[2]")
        val ssc = new StreamingContext(sparkConf, Seconds(4))

        val schema = new StructType()
                .add("sha", StringType)
                .add("nodeid", StringType)
                .add("commit", new StructType())

        val textFileStream: DStream[String] = ssc.textFileStream("dataset")

        textFileStream.flatMap(_.split(","))

        textFileStream.foreachRDD(rdd => rdd.foreach(println))
        ssc.start()

        ssc.awaitTermination()
    }
}
