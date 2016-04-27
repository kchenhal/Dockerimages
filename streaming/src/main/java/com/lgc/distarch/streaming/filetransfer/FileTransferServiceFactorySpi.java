package com.lgc.distarch.streaming.filetransfer;

import java.util.Collection;
import java.util.Collections;

import org.kaazing.gateway.service.Service;
import org.kaazing.gateway.service.ServiceFactorySpi;

public class FileTransferServiceFactorySpi  extends ServiceFactorySpi{

	@Override
	public Collection<String> getServiceTypes() {
		return Collections.singletonList("filetransfer-extension");
	}

	@Override
	public Service newService(String serviceType) {
		assert "filetransfer-extension".equals(serviceType);
		return new FileTransferService();
	}

}
