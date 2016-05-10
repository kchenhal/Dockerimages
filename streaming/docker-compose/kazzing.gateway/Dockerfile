FROM java:openjdk-8-jdk

COPY gateway.distribution-5.1.0-RC010.tar.gz /

RUN cd / \
    && tar -xvf gateway.distribution-5.1.0-RC010.tar.gz \
    && mv kaazing-gateway-community-5.1.0-RC010 kaazing-gateway \
    && rm gateway.distribution-5.1.0-RC010.tar.gz

WORKDIR /kaazing-gateway

COPY conf/gateway-config.xml conf/gateway-config.xml 
COPY lib/*.jar lib/

EXPOSE 8000

ENV PATH=$PATH:/kaazing-gateway/bin

CMD ["gateway.start"]
