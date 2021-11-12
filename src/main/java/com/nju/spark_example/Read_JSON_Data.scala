package com.nju.spark_example

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions.explode

object Read_JSON_Data {
  def main(args:Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL01_Demo")
    val spark : SparkSession = SparkSession.builder().config(conf).getOrCreate()

    val filepath = "data/example/commit-v.file-example.json"
    val df: DataFrame = spark.read.json(filepath)

    // 读取commit.committer下的name和date
    val committer: DataFrame = df.select("commit.committer.name", "commit.committer.date")
    committer.printSchema()
    val committerRDD: RDD[Row] = committer.rdd
    println(committerRDD.first().getAs("date"),committerRDD.first().getAs("name"))

    println("--------------------------------------------")
    // 读取commit的date和file
    val files: DataFrame = df.select(df("commit.committer.date"), explode(df("files"))).toDF("date","file")
    files.printSchema()
    val filesRDD: RDD[Row] = files.rdd
    println(filesRDD.first().getAs("date"),filesRDD.first().getAs("file"))
  }
}
