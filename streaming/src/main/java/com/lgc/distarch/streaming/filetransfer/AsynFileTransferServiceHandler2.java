package com.lgc.distarch.streaming.filetransfer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;
import org.kaazing.gateway.transport.IoHandlerAdapter;
import org.kaazing.gateway.util.LoggingUtils;
import org.kaazing.mina.core.buffer.IoBufferAllocatorEx;
import org.kaazing.mina.core.buffer.IoBufferEx;
import org.kaazing.mina.core.session.IoSessionEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsynFileTransferServiceHandler2 extends IoHandlerAdapter<IoSessionEx> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private FileQueue fileQueue = new FileQueue();

	@Override
	protected void doExceptionCaught(IoSessionEx session, Throwable cause) throws Exception {
		LoggingUtils.log(logger, cause);
	}

	@Override
	protected void doMessageReceived(final IoSessionEx session, Object message) throws Exception {
		if (message instanceof IoBuffer) {
			final IoBuffer buffer = (IoBuffer) message;
			String fileName = new String(buffer.array(), buffer.arrayOffset(), buffer.remaining());
			logger.warn("file name got is " + fileName);
			fileQueue.addTopic(fileName);
			fileQueue.addListener(fileName, new TopicListener(fileName, session));

			new Thread() {
				public void run() {
					try {
						InputStream is = new FileInputStream(new File(fileName));
						BufferedInputStream bis = new BufferedInputStream(is);

						int len = 1024;
						int offset = 0;
						byte[] outBuf = new byte[len];
						IoBufferAllocatorEx<?> allocator = session.getBufferAllocator();
						int readLen = -1;
						while ((readLen = bis.read(outBuf, offset, len)) != -1) {
							if (readLen < len) {
								ByteBuffer bb = ByteBuffer.wrap(outBuf, offset, readLen);
								byte[] targetBuf = new byte[readLen];
								bb.get(targetBuf, offset, readLen);
								fileQueue.addMessage(fileName,  targetBuf);
							} else { 
								fileQueue.addMessage(fileName,  outBuf);
							}
							outBuf = new byte[len];
						}
						
						fileQueue.addMessage(fileName, null);
						logger.warn("finished writing the image back");
						bis.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}.start();
		} else {
			logger.error("invalid message read, type is " + message.getClass().getCanonicalName());
		}

	}

}
