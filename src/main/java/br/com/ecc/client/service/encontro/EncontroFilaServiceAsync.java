package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroFila;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroFilaServiceAsync {
	void lista(Encontro encontro, AsyncCallback<List<EncontroFila>> callback);
	void salva(EncontroFila encontroFila, AsyncCallback<EncontroFila> callback);
	void exclui(EncontroFila encontroFila, AsyncCallback<Void> asyncCallback);
}