package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConvite;
import br.com.ecc.model.Usuario;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroConviteServiceAsync {
	void lista(Encontro encontro, AsyncCallback<List<EncontroConvite>> callback);
	void salva(EncontroConvite encontroConvite, Usuario usuarioAtual, AsyncCallback<EncontroConvite> callback);
	void exclui(EncontroConvite encontroConvite, Usuario usuarioAtual, AsyncCallback<Void> asyncCallback);
}