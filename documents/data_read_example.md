```xml
<dependencies>
    <dependency>
        <groupId>org.apache.spark</groupId>
        <artifactId>spark-core_2.12</artifactId>
        <version>3.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.spark</groupId>
        <artifactId>spark-sql_2.12</artifactId>
        <version>3.0.0</version>
    </dependency>
</dependencies>
```

```scala
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object Spark02_RDD_JSON_File {
  def main(args:Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL01_Demo")
    val spark : SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._

    val filepath = "C:\\Users\\Administrator\\Desktop\\research\\data\\post-data\\github-linux-commit-v.files-1.0.json"
    val df: DataFrame = spark.read.json(filepath)

    // 读取commit.committer下的name和date
    val committer: DataFrame = df.select("commit.committer.name", "commit.committer.date")
    committer.printSchema()
    val committerRDD: RDD[Row] = committer.rdd
    println(committerRDD.first().getAs("date"),committerRDD.first().getAs("name"))

    // 读取commit的date和file
    val files: DataFrame = df.select("commit.committer.date", "files")
    files.printSchema()
    val filesRDD: RDD[Row] = files.rdd
    println(filesRDD.first().getAs("date"),filesRDD.first().getAs("files"))
  }
}
```