package br.com.ecc.client.service;

import br.com.ecc.model.ArquivoDigital;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ArquivoDigitalServiceAsync {

	void removeArquivosDigitais(Integer[] ids, AsyncCallback<Void> callback);
	void getInfo(Integer id, AsyncCallback<ArquivoDigital> callback);

}
