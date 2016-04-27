/**
 * Copyright 2007-2016, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lgc.distarch.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReqProducer {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private KafkaProducer<String, byte[]> producer;

	public ReqProducer(String servers) {
		Properties props = new Properties();
		props.put("bootstrap.servers", servers);
		props.put("acks", "1");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		this.producer = new KafkaProducer<>(props);
	}
	
	public void close() {
		if (producer != null) {
			producer.close();
		}
	}

	public void publish(String topic, Integer idx, byte[] msg) {
		producer.send(new ProducerRecord<String, byte[]>(topic, idx.toString(), msg), new Callback() {

			@Override
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (e != null) {
					logger.error("failed to deliver the msg", e);
				}
				logger.debug("The offset of the record we just sent is: " + metadata.offset());
			}
		});


	}

}
