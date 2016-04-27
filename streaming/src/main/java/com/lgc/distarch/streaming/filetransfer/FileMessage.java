package com.lgc.distarch.streaming.filetransfer;

import java.io.Serializable;

public class FileMessage implements Serializable{
	public String topic;
	public byte[] msg;

	public FileMessage(String topic, byte[] msg) {
		this.topic = topic;
		this.msg = msg;
	}
}
