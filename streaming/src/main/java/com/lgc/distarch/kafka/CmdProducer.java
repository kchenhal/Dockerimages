package com.lgc.distarch.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmdProducer {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private KafkaProducer<String, String> producer;

	public CmdProducer(String servers) {
		Properties props = new Properties();
		props.put("bootstrap.servers", servers);
//		props.put("acks", "1");y
		
		props.put("retries", 1);
//		props.put("batch.size", 16384);
//		props.put("linger.ms", 1);
//		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		this.producer = new KafkaProducer<>(props);
	}
	
	public void close() {
		if (producer != null) {
			producer.close();
		}
	}

	public void publish(String topic, String msg) {
		producer.send(new ProducerRecord<String, String>(topic, msg), new Callback() {

			@Override
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (e != null) {
					logger.error("failed to deliver the msg", e);
					e.printStackTrace();
				} else {
					System.out.println("The offset of the record we just sent is: " + metadata.offset());
					logger.debug("The offset of the record we just sent is: " + metadata.offset());
				}
			}
		});


	}

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "54.208.250.120:9092");
		props.put("acks", "1");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 1; i++)
			producer.send(new ProducerRecord<String, String>("test", "message from java"), new Callback() {

				@Override
				public void onCompletion(RecordMetadata metadata, Exception e) {
					if (e != null) {
						e.printStackTrace();
					} else {
						System.out.println("The offset of the record we just sent is: " + metadata.offset());
					}
				}
			});

		producer.close();

	}

}
