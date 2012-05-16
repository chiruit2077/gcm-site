package br.com.ecc.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EmailServiceAsync {
	void enviaEmail(String destino, String assunto, String mensagem, AsyncCallback<Void> callback);
}
