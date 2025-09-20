
download kafka by browse this usl https://kafka.apache.org/downloads
choose 3.7 version and download this jar (Scala 2.13  - kafka_2.13-3.7.2.tgz (asc, sha512))
once download then extract and paste into C drive
run below command
cd kafka_2.13-3.7.2 -> it moove to kafka directory then run below commnad to start zookeeper

.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

run this command in saperate cmd to check if zookeeper server has started or not
netstat -an | findstr 2181
you will see this then zookeeper is started sucessfully

 TCP    0.0.0.0:2181    0.0.0.0:0    LISTENING
 
 Next kafka server need to start run below command 
 cd C:\kafka_2.13-3.7.2 then 
 .\bin\windows\kafka-server-start.bat .\config\server.properties
  check if kafka server has start or not by this command
  netstat -an | findstr 9092
cross check the output

TCP    0.0.0.0:9092    0.0.0.0:0    LISTENING
TCP    [::]:9092       [::]:0       LISTENING

then kafka server has also started 

create the topic by below command
cd C:\kafka_2.13-3.7.2
.\bin\windows\kafka-topics.bat --create --topic product-events --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

to check the topic what all message has delivered and received by below command


cd C:\kafka_2.13-3.7.2
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic product-events --from-beginning
