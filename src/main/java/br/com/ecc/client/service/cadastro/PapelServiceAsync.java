package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Papel;
import br.com.ecc.model.Grupo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PapelServiceAsync {
	void lista(Grupo grupo, AsyncCallback<List<Papel>> callback);
	void salva(Papel papel, AsyncCallback<Papel> callback);
	void exclui(Papel papel, AsyncCallback<Void> asyncCallback);
}