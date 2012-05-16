package br.com.ecc.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("readresources")
public interface ResourceReaderService extends RemoteService {
	public String readPaginaInicial(String path, String name) throws Exception;
}
