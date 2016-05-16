package com.lgc.distarch.streaming.message;

import java.nio.ByteBuffer;

/**
 * the class define how to send binay data back and forth, here is the layout of the message
 * 1st bye: 0 indicate this is a command, then the rest is json format of the command defined in CommandItem.
 *            for example: "{\"requestId\":\"request-25\",\"cmd\":\"getFile\",\"cmdArguments\":\"e:\\\\downloads\\\\test.xml\"}
 * 			1 indicate this is binary data (streaming package)
 * 
 * for binary data,
 * the 2nd byte: 1 indicate that this is the last package for the particular streaming opertion
 *               0 indicate more data.  
 * the 3rd byte: is the length in byte of the identifier name, the identifier name is the requestId value(request-25) in the above
 *               example, which will be used by client to decide who this message belongs to.
 * from the 3rd byte to the lenghth of the identifier name, it is the identifier name.
 * the rest are the actually streaming data, if this is the last package, it may not be present.
 * 
 * @author HB36382
 *
 */
public class DaMessages {
	// offsets
	public static final int msg_type_offset = 0;
	public static final int end_of_streaming_offset = 1;
	public static final int id_length_offset = 2;
	public static final int id_offset = 3;
	
	// data values
	public static final byte command_message_type = 0;
	public static final byte streaming_message_type = 1;
	public static final byte end_of_streaming = 1;
	public static final byte more_of_streaming = 0;
	
	public static ByteBuffer buildCommand(String cmd) {
		byte[] bytes = cmd.getBytes();
		ByteBuffer buf = ByteBuffer.allocate(1 + bytes.length);
		buf.put(command_message_type);
		buf.put(bytes);
		
		buf.flip();
		return buf;
	}
	
	/**
	 * return if the message is command message, or streaming message
	 * @param msg the message received.
	 * @return true if it is command type, false if it is not
	 */
	public static boolean isCommand(byte[] msg) {
		return isCommand(msg, 0);
	}
	
	public static boolean isCommand(byte[] msg, int offset) {
		if (msg[offset] == command_message_type) {
			return true;
		} else{
			return false;
		}
	}
	/**
	 * Return if it is streaming msg;
	 * @param msg
	 * @return
	 */
	public static boolean isStreamingMsg(byte[] msg) {
		if (msg[0] == streaming_message_type) {
			return true;
		} else{
			return false;
		}
		
	}

	
	public static ByteBuffer buildBinaryData(String id, byte[] data) {
		byte[] idBytes = id.getBytes();
		
		if (idBytes.length >= 256) {
			throw new IllegalArgumentException("id is too long, the id.getBytes() should be less than 256:"+id);
		}
		byte endOfStreaming = end_of_streaming;
		
		// calculate total length
		int len = 3 + idBytes.length;
		if (data != null) {
			endOfStreaming = more_of_streaming;
			len += data.length;
		}
		
		ByteBuffer buf = ByteBuffer.allocate(len);
		// binary data
		buf.put(streaming_message_type);
		// is this the last package
		buf.put(endOfStreaming);
		// the length of the id 
		buf.put((byte)idBytes.length);
		// the id itself
		buf.put(idBytes);
		// the actual payload
		if (data != null) {
			buf.put(data);
		}
		
		buf.flip();
		return buf;
	}
	
	public static ByteBuffer buildEndOfStreamingMsg(String id) {
		return buildBinaryData(id, null);
	}
	
	/**
	 * Get the command from the message.
	 * @param msg
	 * @return the command
	 */
	public static String getCommand(byte[] msg) {
		return getCommand(msg, 0, msg.length);
	}
	
	/**
	 * get the command, but there is offset in the msg, need skip them
	 * @param msg
	 * @param offset
	 * @return
	 */
	public static String getCommand(byte[] msg, int offset, int length) {
		if (isCommand(msg, offset)) {
			String cmd = new String(msg, offset+1, length-1);
			return cmd;
		}
		throw new IllegalArgumentException("the message is not command type");
	}

	public static boolean isStreamingEnd(byte[] msg) {
		if (!isStreamingMsg(msg)) {
			throw new IllegalArgumentException("the message is command type");
		}
		
		byte end = msg[end_of_streaming_offset];
		if (end == end_of_streaming) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get the stream id from the streaming message.
	 * @param msg
	 * @return the id;
	 */
	public static String getStreamingId(byte[] msg) {
		if (!isStreamingMsg(msg)) {
			throw new IllegalArgumentException("the message is command type");
		}
		
		int len = msg[2];
		String id = new String(msg, 3, len);
		return id;
	}
	
	/**
	 * Get the offset of the streaming data's starting point. 
	 * @param msg
	 * @return
	 */
	public static int getStreamingDataOffset(byte[] msg) {
		if (!isStreamingMsg(msg)) {
			throw new IllegalArgumentException("the message is command type");
		}

		int len = msg[2];
		return len+3;
	}

	public static void main(String[] args) {
		
		// test 1: build command message
		String cmd = "test";
		ByteBuffer b = buildCommand(cmd);
		byte[] cmdBytes = cmd.getBytes();
		byte[] msgBytes = b.array();
		if (!isCommand(b.array())) {
			System.out.println("cmd type is wrong");
		}
		String getCmd = getCommand(b.array());
		if (!getCmd.equals(cmd)) {
			System.out.println("cmd is wrong");
		}
		
		
		// test 2-- build binary message
		String requestId ="8723b111-9578-4fdc-ba73-80b59df9818a";
		String data = "this is a test, it will be transformed to binary data";
		
		ByteBuffer bdata = buildBinaryData(requestId, data.getBytes());
		byte[] bytes = bdata.array();
		if (!isStreamingMsg(bytes)) {
			System.out.println("binary indicator is wrong");
		}
		
		System.out.println("1:"+bdata.position()+":"+bdata.remaining()+":"+bdata.limit()+":"+bdata.arrayOffset());
		
		if (isStreamingEnd(bytes)) {
			System.out.println("end indicator is wrong");
		}
		System.out.println("2:"+bdata.position()+":"+bdata.remaining()+":"+bdata.limit()+":"+bdata.arrayOffset());

		int len = bdata.get(2);
		if (bdata.get(2) != ((byte) requestId.getBytes().length)) {
			System.out.println("id length is wrong");
		}
		System.out.println("3:"+bdata.position()+":"+bdata.remaining()+":"+bdata.limit()+":"+bdata.arrayOffset());
		
		String id = getStreamingId(bytes);
		if (!id.equals(requestId)) {
			System.out.println("id is wrong:"+id);
		}
		
		System.out.println("4:"+bdata.position()+":"+bdata.remaining()+":"+bdata.limit()+":"+bdata.arrayOffset());
		
		int offset = getStreamingDataOffset(bytes);
		String value = new String(bdata.array(), offset, bdata.array().length-offset);
		if (!value.equals(data)) {
			System.out.println("id is wrong");
		}
		
		System.out.println("5:"+bdata.position()+":"+bdata.remaining()+":"+bdata.limit()+":"+bdata.arrayOffset());
		
	}

}
