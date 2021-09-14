# 6. Building the images
# -- Software Stack Version 
SCALA_VERSION="2.12.3"
SBT_VERSION="1.0.2"
SPARK_VERSION="3.1.2"
HADOOP_VERSION="3.2"

docker build \
  --build-arg scala_version="${SCALA_VERSION}" \
  --build-arg sbt_version="${SBT_VERSION}" \
  --network host \
  -f cluster-base.Dockerfile \
  -t cluster-base .

docker build \
  --build-arg spark_version="${SPARK_VERSION}" \
  --build-arg hadoop_version="${HADOOP_VERSION}" \
  --network host \
  -f spark-base.Dockerfile \
  -t spark-base .

docker build \
  --network host \
  -f spark-master.Dockerfile \
  -t spark-master .

docker build \
  --network host \
  -f spark-worker.Dockerfile \
  -t spark-worker .

docker build \
  --build-arg spark_version="${SPARK_VERSION}" \
  --network host \
  -f jupyterlab.Dockerfile \
  -t jupyterlab .

docker build \
  --build-arg spark_version="${SPARK_VERSION}" \
  --network host \
  -f zeppelin.Dockerfile \
  -t zeppelin .

