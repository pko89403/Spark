FROM spark-base

# -- Layer: JupyterLab

ARG spark_version=3.1.2
ARG jupyter=3306

RUN apt-get update -y && \
    apt-get install -y python3-pip && \
    pip3 install wget pyspark==${spark_version} jupyterlab numpy spylon_kernel && \
    python -m spylon_kernel install

# -- Runtime

EXPOSE ${jupyter}
WORKDIR ${SHARED_WORKSPACE}/jupyter
CMD jupyter lab --ip=0.0.0.0 --port=3306 --no-browser --allow-root --NotebookApp.token=
