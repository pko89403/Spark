# 스파크 용 클러스터를 도커로 구축
- Spark 마스터 노드 ( port - 9090 )
- Spark 워커 노드 ( port - 9091,9092 )
- JupyterLab 인터페이스 ( port - 3306 )
- zeppelin 인터페이스 ( port - 3307 )

```
    build.sh
    docker-compose up
```

- jupyter at localhost:3306
- zeppelin at localhost:3307
- spark master at localhost:9090
- spark worker1 at localhost:9091
- spark worker2 at localhost:9092

```

hadoop fs -ls hdfs://namenode:8020/
hadoop fs -put *.xml /medline
```

```
import org.apache.spark.sql.SparkSession

val spark= SparkSession
    .builder()
    .appName("spark get xml data from hdfs")
    .getOrCreate()

val rawXML = spark.read
    .format("com.databricks.spark.xml")
    .option("rowTag", "MedlineCitation")
    .load("hdfs://namenode:8020/medline/medsamp2016a.xml")
```

[참고 사이트]('https://towardsdatascience.com/apache-spark-cluster-on-docker-ft-a-juyterlab-interface-418383c95445')