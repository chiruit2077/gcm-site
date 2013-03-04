package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Organograma;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OrganogramaServiceAsync {
	void lista(AsyncCallback<List<Organograma>> callback);
	void salva(Organograma organograma, AsyncCallback<Organograma> callback);
	void exclui(Organograma organograma, AsyncCallback<Void> asyncCallback);
}