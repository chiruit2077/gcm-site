package br.com.ecc.client.service.secretaria;

import java.util.List;

import br.com.ecc.model.Documento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DocumentoServiceAsync {
	void lista(Grupo grupo, Encontro encontro, AsyncCallback<List<Documento>> callback);
	void getDocumento(Integer id, AsyncCallback<Documento> callback);
	void salva(Documento documento, AsyncCallback<Documento> callback);
	void exclui(Documento documento, AsyncCallback<Void> callback);
}