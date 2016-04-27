package com.lgc.distarch.streaming.filetransfer;

import org.apache.mina.core.session.IoSession;
import org.kaazing.gateway.service.Service;
import org.kaazing.gateway.service.ServiceContext;
import org.kaazing.gateway.transport.IoHandlerAdapter;

public class FileTransferService implements Service{

	private IoHandlerAdapter handler;
	private ServiceContext serviceContext;

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getType() {
		return "filetransfer-extension";
	}

	@Override
	public void init(ServiceContext serviceContext) throws Exception {
		this.serviceContext = serviceContext;
//		handler = new FileTransferServiceHandler();
		handler = new KfkaFileTransferServiceHandler("34.36.105.44:9092");
		
	}

	@Override
	public void quiesce() throws Exception {
        if (serviceContext != null) {
            serviceContext.unbind(serviceContext.getAccepts(), handler);
        }
	}

	@Override
	public void start() throws Exception {
		serviceContext.bind(serviceContext.getAccepts(), handler);		
	}

	@Override
	public void stop() throws Exception {
        quiesce();

        if (serviceContext != null) {
            for (IoSession session : serviceContext.getActiveSessions()) {
                session.close(true);
            }
        }
	}

}
