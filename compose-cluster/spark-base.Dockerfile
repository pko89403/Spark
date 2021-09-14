# 2. Spark base image
FROM cluster-base 


# download, unpack, move apache spark latest version with official repository
ARG spark_version=3.1.2
ARG hadoop_version=3.2 

COPY requirements.txt requirements.txt

RUN apt-get update -y && \
    apt-get install -y curl && \
    pip3 install --upgrade pip && \
    pip3 install -r requirements.txt && \
    curl -L -o spark.tgz https://archive.apache.org/dist/spark/spark-${spark_version}/spark-${spark_version}-bin-hadoop${hadoop_version}.tgz && \
    tar -xf spark.tgz && \
    mv spark-${spark_version}-bin-hadoop${hadoop_version} /usr/bin/ && \
    mkdir /usr/bin/spark-${spark_version}-bin-hadoop${hadoop_version}/logs && \
    rm spark.tgz

# configure spark variable 
ENV SPARK_HOME /usr/bin/spark-${spark_version}-bin-hadoop${hadoop_version}
ENV SPARK_MASTER_HOST spark-master
ENV SPARK_MASTER_PORT 7077
ENV PYSPARK_PYTHON python3

WORKDIR ${SPARK_HOME}