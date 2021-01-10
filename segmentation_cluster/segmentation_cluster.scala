import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Window
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import scala.collection.mutable.{ListBuffer, Seq}
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.apache.spark


object segementation_clustering {

  def separate_padding = udf { seqArr : Seq[Row] => seqArr.size }

  def padding (seq: Seq[Row], seq_cnt: Int) : Int = {
    if(seq_cnt == 5) {
      println("Seq_cnt Leng is 10")
    }
    else {
      println("Seq_cnt Length is ", seq_cnt)
    }

    return seq_cnt
  }

  def padSeq2(seq : Seq[String], seq_cnt: Long) : Seq[String] = {
    val seq_length = seq_cnt
    val fixed_length = 5
    if ( seq_length == fixed_length ) {
      println("5")
      return seq
    }
    else if( seq_length > fixed_length) {
      val res = seq.takeRight(fixed_length)
      return res
    }
    else {
      var temp : Seq[String] = Seq()
      val n = fixed_length - seq_length
      for(i <- 0 to 5)
      {
        temp = temp ++ seq
      }
      val res =temp.takeRight(fixed_length)
      return res
    }
  }

  def main(args: Array[String]): Unit = {

    val salinfoCSV = ""
    val rawDF = spark.read.option("header", "true").csv(salinfoCSV)
    println(rawDF.printSchema())

    // Initialize Desired Feature ( Column ) List as ArrayBuffer
    val featureList = Seq[String]("comcsno",
      "saldt", "saltm",
      "agecd", "agemclcd", "sexcd",
      "prdnm", "prdcd", "prdstypcd", "cem_prdcd", "brandcd",
      "routeclcd", "routenm",
      "dasblbef_pmtamt", "dasblbef_saluc", "dasblbef_salqty", "dasblbef_salamt", "dasblbef_net_salamt")
    val features = featureList.map(name => col(name))

    // Select DataFrame's Column & Init val
    val raw2sel = rawDF.select(features: _*)
      .where(col("comcsno") =!= "ZZ")
      .where(col("prdcd") =!= "ZZ")
      .where(col("prdstypcd") =!= "ZZ")
      .where(col("cem_prdcd") =!= "ZZ")
      .where(col("brandcd") =!= "ZZ") 
      .where(col("routenm") =!= "ZZ")
      .where(col("dasblbef_pmtamt") > 0.0)
      .where(col("dasblbef_saluc") > 0.0)
      .where(col("dasblbef_salqty") > 0.0)
      .where(col("dasblbef_salamt") > 0.0)
      .where(col("dasblbef_net_salamt") > 0.0)

    // prdstypcd -> prdltypcd (0,1), prdmtypcd (0,1,2), prdstypcd(0,1,2,3,4)
    val sel_with_prdtypcd = raw2sel.withColumn("prdltypcd",substring(col("prdstypcd"), 1, 2))
      .withColumn("prdmtypcd", substring(col("prdstypcd"), 1, 3))

    // concat saldt & saltm as log_dttm
    // if saltm's value is ZZ(NULL), then value is 23, else value is saltm
    val prdtypcd_logdttm = sel_with_prdtypcd.withColumn("HH", when(col("saltm") === "ZZ", "23").otherwise(col("saltm")))
    val saldt_split = prdtypcd_logdttm.withColumn("YYYY", substring(col("saldt"), 1, 4))
      .withColumn("MM", substring(col("saldt"), 5, 2))
      .withColumn("DD", substring(col("saldt"), 7, 2))
    val saldt_concat = saldt_split.withColumn("saldt",concat(col("YYYY"), lit("-"), col("MM"), lit("-"), col("DD"), lit(":"), col("HH"), lit(":00")))
    val saldt_timestamp = saldt_concat.withColumn("log_dttm", to_timestamp(col("saldt"), "yyyy-MM-dd:HH:mm"))
    val sel_logdttm = saldt_timestamp.drop(col("saldt"))
      .drop(col("saltm"))
      .drop(col("YYYY"))
      .drop(col("MM"))
      .drop(col("DD"))
      .drop(col("HH"))


    import spark.sqlContext.implicits._

    val sel_ETLtime = sel_logdttm.withColumn("etl_timestamp",current_timestamp())

    // sex coded
    val sel_sexcd = sel_ETLtime.withColumn("sexcd", when(col("sexcd") === "1", "M").when(col("sexcd") === "2", "F").otherwise("N"))
    sel_sexcd.printSchema()

    // code 2 sequence order by log_dttm
    val code2seq = sel_sexcd.withColumn("prdcd_seq", collect_list($"prdcd").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("cem_prdcd_seq", collect_list($"cem_prdcd").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("prdltypcd_seq", collect_list($"prdltypcd").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("prdmtypcd_seq", collect_list($"prdmtypcd").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("prdstypcd_seq", collect_list($"prdstypcd").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("brandcd_seq", collect_list($"brandcd").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("routeclcd_seq", collect_list($"routeclcd").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("dasblbef_pmtamt_seq", collect_list($"dasblbef_pmtamt").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("dasblbef_saluc_seq", collect_list($"dasblbef_saluc").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("dasblbef_salqty_seq", collect_list($"dasblbef_salqty").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("dasblbef_salamt_seq", collect_list($"dasblbef_salamt").over( Window.partitionBy("comcsno").orderBy("log_dttm")))
      .withColumn("dasblbef_net_salamt_seq", collect_list($"dasblbef_net_salamt").over( Window.partitionBy("comcsno").orderBy("log_dttm")))

    val groupCom = code2seq.groupBy("comcsno").agg(count("comcsno").as("seq_cnt"))
      .withColumn("idx", monotonically_increasing_id())
      .withColumn("etl_timestamp",current_timestamp())
    val w = Window.orderBy(groupCom("idx"))
    val groupCom_idx = groupCom.withColumn("item_index", row_number().over(w))
    val customerNum = groupCom_idx.drop("idx")


    val arrToString = udf((arrOfStr : Seq[String]) => arrOfStr.mkString(","))

    val groupByJoin = customerNum.join(code2seq.groupBy("comcsno").agg(max($"cem_prdcd_seq").as("seq")),"comcsno").orderBy("item_index")
      .withColumn("seq", $"seq")

    val checkSeqDIST = groupByJoin.withColumn("seq_cnt", col("seq_cnt").cast(StringType))
    checkSeqDIST.groupBy("seq_cnt").count().orderBy($"count".desc).show(100)

    val seqLength = 5
    groupByJoin.withColumn("seq_length", separate_padding($"seq").show(false)


    val test = groupByJoin.select("seq").take(1)
    println ( padding(test, 1))
  }

  val pad2Seq = udf(padSeq2 _)
  groupByJoin.withColumn("seq_length", pad2Seq(col("seq"), col("seq_cnt"))).show(false)
}