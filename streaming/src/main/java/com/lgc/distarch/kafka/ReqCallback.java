package com.lgc.distarch.kafka;

import org.kaazing.mina.core.session.IoSessionEx;

import com.lgc.distarch.streaming.filetransfer.CommandItem;

public class ReqCallback implements CmdCallback {
	
	private IoSessionEx session;

	public ReqCallback(IoSessionEx session ) {
		this.session = session;
	}

	@Override
	public void run(CommandItem item) {

	}

}
