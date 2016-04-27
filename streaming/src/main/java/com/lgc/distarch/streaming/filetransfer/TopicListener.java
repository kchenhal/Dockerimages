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


