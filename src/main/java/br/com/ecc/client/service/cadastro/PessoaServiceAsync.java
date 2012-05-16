package br.com.ecc.client.service.cadastro;

import br.com.ecc.model.Pessoa;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PessoaServiceAsync {
	void salva(Pessoa Pessoa, AsyncCallback<Pessoa> callback);
	void exclui(Pessoa Pessoa, AsyncCallback<Void> asyncCallback);
}