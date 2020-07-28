name := "mongo_spark_batch"
version := "1.0"
scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.0"
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "2.4.0"

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  "io_mongo.jar"
}