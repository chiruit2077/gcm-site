package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroAtividadeServiceAsync {
	void lista(Encontro encontro, AsyncCallback<List<EncontroAtividade>> callback);
	void salva(EncontroAtividade encontroAtividade, AsyncCallback<EncontroAtividade> callback);
	void exclui(EncontroAtividade encontroAtividade, AsyncCallback<Void> asyncCallback);
}