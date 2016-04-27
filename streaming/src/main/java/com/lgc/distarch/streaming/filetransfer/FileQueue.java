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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class FileQueue {
	private HashMap<String, Queue<byte[]>> _queueMap = new HashMap<>();
	private HashMap<String, List<TopicListener>> _listener = new HashMap<>();
	
	public void addTopic (String topic) {
		LinkedBlockingQueue queue = new LinkedBlockingQueue();
		_queueMap.put(topic, queue);
		_listener.put(topic, new ArrayList<TopicListener>());
		
		new Thread()
        {
            public void run() {
            	Queue<byte[]> queue = _queueMap.get(topic);
            	List<TopicListener> list = _listener.get(topic);
            	while (true){
	            	byte[] msg;
					try {
						msg = (byte[]) ((LinkedBlockingQueue) queue).take();
		            	if (msg == null) {
		            		System.out.println("finisthed");
		            		break;
		            	}
		            	for (TopicListener listern : list) {
		            		try {
		            			new Random().nextInt();
								listern.send(msg);
							} catch (IOException e) {
								e.printStackTrace();
							}
		            	}
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
            }
        }.start();

	}
	
	public void addMessage(String topic, byte[] msg) {
		Queue<byte[]> queue = _queueMap.get(topic);
		queue.add(msg);
	}
	
	public byte[] getMessage(String topic) {
		Queue<byte[]> queue = _queueMap.get(topic);
		return queue.remove();
	}
	
	public void addListener(String topic, TopicListener listener) {
		List<TopicListener> list = _listener.get(topic);
		list.add(listener);
	}
}
