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
package com.lgc.distarch.streaming.filetransfer;


import org.apache.mina.core.buffer.IoBuffer;
import org.kaazing.gateway.transport.IoHandlerAdapter;
import org.kaazing.gateway.util.LoggingUtils;
import org.kaazing.mina.core.session.IoSessionEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgc.distarch.kafka.CmdProducer;
import com.lgc.distarch.kafka.KTopic;
import com.lgc.distarch.kafka.ReqConsumer;

public class KfkaFileTransferServiceHandler extends IoHandlerAdapter<IoSessionEx>  {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final static String topic = "getFile";
	private CmdProducer producer;
	private String zkUrl = "34.36.105.44:2181";
	private String kafarServers = "34.36.105.44:9092";

	public KfkaFileTransferServiceHandler(String kafkaServers) {
		this.producer = new CmdProducer(kafkaServers);
	}

	@Override
	protected void doExceptionCaught(IoSessionEx session, Throwable cause) throws Exception {
		LoggingUtils.log(logger, cause);
		cause.printStackTrace();
	}

	@Override
	protected void doMessageReceived(final IoSessionEx session, Object message) throws Exception {
		if (message instanceof IoBuffer) {
			final IoBuffer buffer = (IoBuffer) message;
			String msg = new String(buffer.array(), buffer.arrayOffset(), buffer.remaining());
			logger.warn("msg got is " + msg);

			CommandItem item = CommandItem.fromJson(msg);
			item.gateWayUrl = "ws://localhost:8000/filetransfer";

			// create a new topic based on the request id to kafa queue.
			KTopic.createTopic(zkUrl, item.requestId, 1, 1);
			// starting the consumer thread.
			new Thread() {
				public void run() {
					ReqConsumer consumer = new ReqConsumer(zkUrl, kafarServers, item.requestId, session);
					consumer.consume();
				}
			}.start();
			
			producer.publish(topic, item.toJson());
			
		}
	}
}
