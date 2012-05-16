package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConviteResponsavel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroConviteResponsavelServiceAsync {
	void lista(Encontro encontro, AsyncCallback<List<EncontroConviteResponsavel>> callback);
	void salva(EncontroConviteResponsavel encontroConviteResponsavel, AsyncCallback<EncontroConviteResponsavel> callback);
	void exclui(EncontroConviteResponsavel encontroConviteResponsavel, AsyncCallback<Void> asyncCallback);
}