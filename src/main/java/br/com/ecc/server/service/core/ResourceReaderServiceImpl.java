package br.com.ecc.server.service.core;

import java.io.IOException;

import br.com.ecc.client.service.ResourceReaderService;
import br.com.ecc.server.ResourceLoaderUtil;
import br.com.ecc.server.SecureRemoteServiceServlet;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class ResourceReaderServiceImpl extends SecureRemoteServiceServlet implements ResourceReaderService {

	private static final long serialVersionUID = -4840013477999927720L;
	
	@Inject Injector injector;

	@Override
	public String readPaginaInicial(String path, String name) throws Exception {
		String htmlDefault = "";
		try {
			htmlDefault = ResourceLoaderUtil.loadResource(path,name);
		} catch (IOException e) {
		}
		return htmlDefault;
	}
}