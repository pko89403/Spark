FROM cluster-base

# -- Layer: JupyterLab

ARG spark_version=3.0.0
ARG jupyterlab_version=2.1.5
ARG jupyter=3306

RUN apt-get update -y && \
    apt-get install -y python3-pip && \
    pip3 install wget pyspark==${spark_version} jupyterlab==${jupyterlab_version}

# -- Runtime

EXPOSE ${jupyter}
WORKDIR ${SHARED_WORKSPACE}/jupyter
CMD jupyter lab --ip=0.0.0.0 --port=3306 --no-browser --allow-root --NotebookApp.token=