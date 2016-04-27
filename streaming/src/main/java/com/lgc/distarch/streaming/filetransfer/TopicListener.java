package com.lgc.distarch.streaming.filetransfer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.kaazing.mina.core.buffer.IoBufferAllocatorEx;
import org.kaazing.mina.core.buffer.IoBufferEx;
import org.kaazing.mina.core.session.IoSessionEx;

public class TopicListener {
	private String topic;
	private IoSessionEx session;
	IoBufferAllocatorEx<?> allocator;

	public TopicListener(String topic, IoSessionEx session) {
		this.topic = topic;
		this.session = session;
		allocator = session.getBufferAllocator();
	}

	public void send(byte[] msg) throws IOException {
		FileMessage fmsg = new FileMessage(topic, msg);
		
		IoBufferEx ioBuf = allocator.wrap(fromObject(fmsg), IoBufferEx.FLAG_SHARED);
    	session.write(ioBuf);
	}

	  public ByteBuffer fromObject(Object mf) throws IOException {
		    ByteArrayOutputStream b = new ByteArrayOutputStream();
		    ObjectOutputStream o;
		      o = new ObjectOutputStream(b);
		      o.writeObject(mf);

		      return ByteBuffer.wrap(b.toByteArray());
	  }
	  
}


