package com.lgc.distarch.kafka;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.kaazing.mina.core.buffer.IoBufferAllocatorEx;
import org.kaazing.mina.core.buffer.IoBufferEx;
import org.kaazing.mina.core.session.IoSessionEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgc.distarch.streaming.filetransfer.CommandItem;
import com.lgc.distarch.streaming.message.DaMessages;

public class ReqConsumer {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private KafkaConsumer<String, byte[]> consumer;
	private boolean stop = false;

	private IoSessionEx session;

	private String zkUrl;

	private String topic;

	public ReqConsumer(String zkUrl, String servers, String topic, IoSessionEx session) {
		this.session = session;
		this.zkUrl = zkUrl;
		this.topic = topic;
		Properties props = new Properties();
		props.put("bootstrap.servers", servers);
		props.put("group.id", topic);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		this.consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topic));
	}

	public void close() {
		this.stop = true;
		this.consumer.close();
	}

	public void consume() {
		IoBufferAllocatorEx<?> allocator = session.getBufferAllocator();
		while (!this.stop) {
			ConsumerRecords<String, byte[]> records = consumer.poll(100);
			for (ConsumerRecord<String, byte[]> record : records) {
				if (logger.isDebugEnabled()) {
					logger.debug("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
				}

				byte[] bytes = record.value();
				ByteBuffer msg = DaMessages.buildBinaryData(topic, bytes);
				if (bytes == null) {
					this.stop = true;
					System.out.println("end of the topic for " + topic);
					KTopic.deleteTopic(zkUrl, topic);
					// delete the queue topic
				}
				// send it to client
				IoBufferEx ioBuf = allocator.wrap(msg, IoBufferEx.FLAG_NONE);
				session.write(ioBuf);
			}
		}
	}

}
