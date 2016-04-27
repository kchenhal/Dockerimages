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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.kaazing.mina.core.buffer.IoBufferAllocatorEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgc.distarch.streaming.filetransfer.CommandItem;

public class CmdConsumer {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private KafkaConsumer<String, String> consumer;
	private boolean stop = false;

	public CmdConsumer(String servers, String topic) {
		Properties props = new Properties();
		props.put("bootstrap.servers", servers);
		props.put("group.id", topic);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		this.consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topic));

	}

	public void close() {
		this.stop = true;
		this.consumer.close();
	}

	public void consume(CmdCallback callback) {
		while (!this.stop) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				if (logger.isDebugEnabled()) {
					System.out.println("got a msg: "+record.value());
					logger.debug("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
				}

				String jsonCmd = record.value();
				if (!jsonCmd.isEmpty()) {
					try {
						CommandItem item = CommandItem.fromJson(jsonCmd);
						// TODO: we can also run this in a separate thread
						callback.run(item);
					} catch (Exception e) {
						System.out.println("got an exception, ignore, will continue "+e.getLocalizedMessage());
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("missing arguments: servers topic");
		}

		String servers = args[0];
		String topic = args[1];

		CmdConsumer consumer = new CmdConsumer(servers, topic);
		consumer.consume( new CmdCallback() {

			@Override
			public void run(CommandItem item) {
				try {
					ReqProducer producer = new ReqProducer(servers);

					InputStream is = new FileInputStream(new File(item.cmdArguments));
					BufferedInputStream bis = new BufferedInputStream(is);

					int len = 1024;
					int offset = 0;
					byte[] outBuf = new byte[len];
					int readLen = -1;
					int i=0;
					while ((readLen = bis.read(outBuf, offset, len)) != -1) {
						if (readLen < len) {
							ByteBuffer bb = ByteBuffer.wrap(outBuf, offset, readLen);
							byte[] targetBuf = new byte[readLen];
							bb.get(targetBuf, offset, readLen);
							producer.publish(item.requestId, i++, targetBuf);
						} else {
							producer.publish(item.requestId, i++, outBuf);
						}
						outBuf = new byte[len];
					}
					
					producer.publish(item.requestId, i++, null);

					System.out.println("finished writing the image back");
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

}
