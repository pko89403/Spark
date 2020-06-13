# Kafka를 이용해서 Data Pipeline을 만들어보기 
https://eyeballs.tistory.com/209
## Zookeeper 설치하는 방법
~~~sh
 wget http://apache.mirror.cdnetworks.com/zookeeper/stable/apache-zookeeper-3.5.8-bin.tar.gz

 tar -zxvf apache-zookeeper-3.5.8-bin.tar.gz

 rm *.gz

~~~
주키퍼의 스냅샷과 트랜잭션 로그 등등 을 저장할 패스를 만든다.
~~~sh
mkdir zookeeper_data_1
echo 1 > zookeeper_data_1 > myid

mkdir zookeeper_data_2
echo 2 > zookeeper_data_2 > myid
~~~

/etc/hosts에 다음과 같이 nodes의 ip 주소들을 적용한다.
~~~sh
 # Added by Zookeeper , By User
 55.55.55.2 zookeeper01
 55.55.55.4 zookeeper02
~~~
주키퍼를설치한 path의 conf 디렉토리의 zoo_sample.cfg를 zoo.cfg로 복사한다.
~~~sh
dataDir=/Users/kangseokwoo/SparkDefinitiveGuide/ScalaAndSpark/spark-streaming-env/apache-zookeeper-3.5.8-bin/zookeeper_data
server.1 = zookeeper01:2888:3888
server.2 = zookeeper02:2888:3888
~~~
실행해보기
~~~sh
cd bin
zkServer.sh start
zkServer.sh stop
~~~
jps 명령을 통해 QuarumPeerMain을 확인한다.
~~~sh
jps 
netstat
zkServer.sh status
~~~