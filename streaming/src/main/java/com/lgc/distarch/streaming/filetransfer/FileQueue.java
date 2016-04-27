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
