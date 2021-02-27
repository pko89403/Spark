#!bin/bash

# download file
mkdir taxidata;
cd taxidata
wget https://s3.amazonaws.com/nyc-tlc/trip+data/yellow_tripdata_2013-01.csv
head -n 10 yellow_tripdata_2013-01.csv
# put hdfs dfs
hdfs dfs -mkdir -p taxidata
hdfs dfs -put yellow_tripdata_2013-01.csv taxidata