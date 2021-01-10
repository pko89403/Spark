import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.collection.mutable.{ListBuffer, Seq}


object etl_parquet_jdbc {
  def guide_args(args: Array[String]): Unit = {
    println("Argument Length : " + args.length )

    println("Current Arguments " )
    for (arg <- args.toList){
      println(args)
    }

    val usage = """
       --source_bucket : Data root path
       --rdb_endpoint : rdb sink endpoint
       --rdb_database : rdb database name
       --rdb_table : rdb table name
       --rdb_user : rdb user id
       --rdb_pwd : rdb pwd
       --days_ago : data range from today to start day
    """
    if (args.length != 14){
      println(usage)
    }
    return
  }

  def get_full_paths(daysAgo: Int, prefix: String, suffix: String): Seq[String] = {

    val fixDate = LocalDate.of(2018, 12, 25)

    val fullPaths = new ListBuffer[String]()

    for (day <- 0 to daysAgo) {
      val date_temp = fixDate.minusDays(day) //val date = LocalDateTime.now().minusDays(day);

      val year = DateTimeFormatter.ofPattern("YYYY").format(date_temp)
      val mm = DateTimeFormatter.ofPattern("MM").format(date_temp)
      val dd = DateTimeFormatter.ofPattern("dd").format(date_temp)

      val filePath = prefix + "/sal_y=" + year + "/sal_m=" + mm + "/sal_d=" + dd + "/" + suffix

      fullPaths += filePath
    }

    return fullPaths
  }

  def main(args: Array[String]): Unit = {
    guide_args(args)

    var source_bucket: String = ""
    var rdb_endpoint: String = ""
    var rdb_database: String = ""
    var rdb_table: String = ""
    var rdb_user: String = ""
    var rdb_pwd: String = ""
    var days_ago: Int = 20

    args.sliding(2, 2).toList.collect {
      case Array("--source_bucket", arg: String) => source_bucket = arg
      case Array("--rdb_endpoint", arg: String) => rdb_endpoint = arg
      case Array("--rdb_database", arg: String) => rdb_database = arg
      case Array("--rdb_table", arg: String) => rdb_table = arg
      case Array("--rdb_user", arg: String) => rdb_user = arg
      case Array("--rdb_pwd", arg: String) => rdb_pwd = arg
      case Array("--days_ago", arg: String) => days_ago = arg.toInt
    }

    val jdbc_endpoint: String = rdb_endpoint + "/" + rdb_database

    val spark = SparkSession.builder.appName("").master("yarn").getOrCreate()

    val fullPaths = get_full_paths(20, "", "*.parquet")

    // Load Parquet Data
    val rawDF = spark.read.format("parquet").load(fullPaths : _*)
    val selectedCol = rawDF.select("")
      .where(col("") > 0.0)
      .where(col("") > 0.0)
      .where(col("") > 0.0)
      .where(col("") > 0.0)
      .where(col("") > 0.0)

    // Data Transformation
    val filterCols = selectedCol
      .withColumn("", regexp_replace(col(""), "ZZ", "N"))

    // Data Filtering
    val filteredCols = filterCols.filter( filterCols("") =!= "N")

    // TypeCasting
    val typeCasted = filteredCols.withColumn("", filteredCols("").cast(StringType))

    // ETL TIME ADD
    val addTimeData = typeCasted.withColumn("",current_timestamp().as("current_timestamp"))
    addTimeData.show(1)

    // Rename Columns
    val finalData = addTimeData.withColumnRenamed("", "")

    // write to rds
    finalData.write.format("jdbc")
      .option("url", jdbc_endpoint)
      .option("driver", "com.mysql.jdbc.Driver")
      .option("dbtable", rdb_table)
      .option("user", rdb_user)
      .option("password", rdb_pwd)
      .option("createTableOptions", "CHARACTER SET utf8")
      .option("createTableColumnTypes",
        """ CHAR(5),
          | INT,
          |etl_timestamp VARCHAR(128)""".stripMargin)
      .mode("overwrite")
      .save()


  }
}
