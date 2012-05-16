package br.com.ecc.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ResourceReaderServiceAsync {
	void readPaginaInicial(String path, String name, AsyncCallback<String> callback);
}
