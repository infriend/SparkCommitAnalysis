## Dependency

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

## Src

```scala
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions.explode

object Data_Read_Example {
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
    val files: DataFrame = df.select(df("commit.committer.date"), explode(df("files"))).toDF("date","file")
    files.printSchema()
    val filesRDD: RDD[Row] = files.rdd
    println(filesRDD.first().getAs("date"),filesRDD.first().getAs("file"))
  }
}
```

## Output

```
root
 |-- name: string (nullable = true)
 |-- date: timestamp (nullable = true)
 
(2021-11-04 12:18:44.0,Linus Torvalds)


root
 |-- date: timestamp (nullable = true)
 |-- file: struct (nullable = true)
 |    |-- additions: long (nullable = true)
 |    |-- changes: long (nullable = true)
 |    |-- deletions: long (nullable = true)
 |    |-- filename: string (nullable = true)
 |    |-- previous_filename: string (nullable = true)
 |    |-- sha: string (nullable = true)
 |    |-- status: string (nullable = true)

(2021-11-04 12:18:44.0,[254,254,0,Documentation/devicetree/bindings/arm/mediatek/mediatek,mt8195-clock.yaml,null,17fcbb45d121c9a35075395f6117bf86254384d2,added])

```

