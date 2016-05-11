FROM java:openjdk-8-jdk

COPY kafka_2.11-0.9.0.1.tgz jre-8u92-linux-x64.rpm /tmp/ 

RUN cd / \
    &&  tar -zxvf /tmp/kafka_2.11-0.9.0.1.tgz \
    && rm /tmp/kafka_2.11-0.9.0.1.tgz

WORKDIR /kafka_2.11-0.9.0.1

EXPOSE 2181 9092
