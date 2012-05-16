package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.Quarto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface QuartoServiceAsync {
	void lista(Grupo grupo, AsyncCallback<List<Quarto>> callback);
	void salva(Quarto quarto, AsyncCallback<Quarto> callback);
	void exclui(Quarto quarto, AsyncCallback<Void> asyncCallback);
}