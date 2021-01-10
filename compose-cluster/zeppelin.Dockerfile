FROM cluster-base

# -- Layer: zeppelin

ARG spark_version=3.0.0
ARG zeppelin_version=0.9.0
ARG zeppelin=3307

RUN apt-get update -y && \
    apt-get install -y curl && \
    curl http://mirror.softaculous.com/apache/zeppelin/zeppelin-${zeppelin_version}/zeppelin-${zeppelin_version}-bin-all.tgz -o zeppelin.tgz && \
    tar -xf zeppelin.tgz && \
    mv zeppelin-${zeppelin_version}-bin-all /usr/bin/ &&
RUN echo '{ "allow_root": true }' > /root/.bowerrc


ENV ZEPPELIN_HOME /usr/bin/zeppelin-${zeppelin_version}-bin-all
ENV ZEPPELIN_CONF_DIR $ZEPPELIN_HOME/conf

ENV ZEPPELIN_WORK_DIR ${SHARED_WORKSPACE}/zeppelin
ENV ZEPPELIN_LOG_DIR $ZEPPELIN_WORK_DIR/logs
ENV ZEPPELIN_NOTEBOOK_DIR $ZEPPELIN_WORK_DIR/notebook

# -- Runtime
EXPOSE ${zeppelin}

RUN mkdir -p $ZEPPELIN_WORK_DIR \
  && mkdir -p $ZEPPELIN_LOG_DIR \
  && mkdir -p $ZEPPELIN_NOTEBOOK_DIR

# my WorkDir
RUN mkdir -p $ZEPPELIN_WORK_DIR/work 
WORKDIR $ZEPPELIN_WORK_DIR/work

CMD $ZEPPELIN_HOME/bin/zeppelin-daemon.sh start && bash


