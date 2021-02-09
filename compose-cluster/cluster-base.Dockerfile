# 1. using Java8 for spark
FROM ubuntu:latest

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME


ARG shared_workspace=/opt/workspace

ARG scala_version=2.13.3
ARG sbt_version=1.3.13


COPY requirements.txt requirements.txt

# 2. install python3 for pyspark
RUN mkdir -p ${shared_workspace} && \
    apt-get install -y curl tar python3 python3-pip && \
    ln -s /usr/bin/python3 /usr/bin/python && \
    pip3 install --upgrade pip && \
    pip3 install -r requirements.txt && \
    rm -rf /var/lib/apt/lists/*x 


RUN curl -L -o scala-${scala_version}.deb https://downloads.lightbend.com/scala/${scala_version}/scala-${scala_version}.deb && \
    dpkg -i scala-${scala_version}.deb && \
    rm scala-${scala_version}.deb && \
    apt-get install scala && \
    scala -version 

RUN curl -L -o sbt-${sbt_version}.deb http://dl.bintray.com/sbt/debian/sbt-${sbt_version}.deb && \
    dpkg -i sbt-${sbt_version}.deb && \
    rm sbt-${sbt_version}.deb && \
    apt-get install sbt && \
    sbt sbtVersion


# create shared volume to simulate HDFS
ENV SHARED_WORKSPACE=${shared_workspace}
VOLUME ${shared_workspace} 
CMD ["bash"]