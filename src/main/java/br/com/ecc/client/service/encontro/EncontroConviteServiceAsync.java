package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConvite;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroConviteServiceAsync {
	void lista(Encontro encontro, AsyncCallback<List<EncontroConvite>> callback);
	void salva(EncontroConvite encontroConvite, AsyncCallback<EncontroConvite> callback);
	void exclui(EncontroConvite encontroConvite, AsyncCallback<Void> asyncCallback);
}