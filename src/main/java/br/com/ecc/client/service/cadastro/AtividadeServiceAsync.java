package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Atividade;
import br.com.ecc.model.Grupo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AtividadeServiceAsync {
	void lista(Grupo grupo, AsyncCallback<List<Atividade>> callback);
	void salva(Atividade atividade, AsyncCallback<Atividade> callback);
	void exclui(Atividade atividade, AsyncCallback<Void> asyncCallback);
}