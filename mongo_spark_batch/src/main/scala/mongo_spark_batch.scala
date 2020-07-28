import com.mongodb.spark._
import com.mongodb.spark.config.{ReadConfig, WriteConfig}
import org.apache.spark.sql._

object mongo_spark_batch {
  def main(args: Array[String]): Unit = {

    var userId : String  = "root"
    var passwd : String = "root12345!"
    var hostIP : String = "localhost"
    var database : String = "RECSYS"
    var collection : String = "Collection"

    val spark = SparkSession.builder()
      .master("local")
      .appName("MongoSparkConnectorIntro")
      .config("spark.mongodb.input.uri", "")
      .config("spark.mongodb.output.uri", "")
      .getOrCreate()

    val readConfig = ReadConfig(Map(
      "uri" -> "",
      "database" -> "",
      "collection" -> ""))

    val df = MongoSpark.load(spark, readConfig)
    df.show(5)


    MongoSpark.save(df.write.option("database", "").option("collection", "").mode("overwrite").format("mongo"))
  }
}
