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

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

public class KTopic {
	
	private static int sessionTimeoutMs = 10 * 1000;
	private static int connectionTimeoutMs = 8 * 1000;
	private static boolean isSecure = false;

	public KTopic(String zkUrl) {
	}
	
	public static void createTopic(String zkUrl, String topic, int partitions, int replication) {
		ZkClient zkClient = new ZkClient(zkUrl, sessionTimeoutMs, connectionTimeoutMs,  ZKStringSerializer$.MODULE$);
		ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zkUrl), isSecure );
		Properties props = new Properties();
		AdminUtils.createTopic(zkUtils, topic, partitions, replication, props);
		zkClient.close();
	}

	public static void deleteTopic(String zkUrl, String topic) {
		ZkClient zkClient = new ZkClient(zkUrl, sessionTimeoutMs, connectionTimeoutMs,  ZKStringSerializer$.MODULE$);
		ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zkUrl), isSecure );
		Properties props = new Properties();

		AdminUtils.deleteTopic(zkUtils, topic);
		zkClient.close();
	}
	
	public static void main(String[] args) {
		createTopic("34.36.105.44:2181", "test2", 2, 1);
//		deleteTopic("34.36.105.44:2181", "test");
	}
}
