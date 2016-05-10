FROM centos:7

COPY kafka_2.11-0.9.0.1.tgz jre-8u92-linux-x64.rpm /tmp/ 

RUN yum -y update \
    && rpm -ivh /tmp/jre-8u92-linux-x64.rpm \
    && rm /tmp/jre-8u92-linux-x64.rpm \
    && cd / \
    &&  tar -zxvf /tmp/kafka_2.11-0.9.0.1.tgz \
    && rm /tmp/kafka_2.11-0.9.0.1.tgz

WORKDIR /kafka_2.11-0.9.0.1

EXPOSE 2181 9092

# COPY run_kafka.sh .

# ENTRYPOINT ./run_kafka.sh
