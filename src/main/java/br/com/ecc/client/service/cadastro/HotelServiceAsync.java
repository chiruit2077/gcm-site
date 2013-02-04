package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.Hotel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HotelServiceAsync {
	void lista(Grupo grupo, AsyncCallback<List<Hotel>> callback);
	void salva(Hotel hotel, AsyncCallback<Hotel> callback);
	void exclui(Hotel hotel, AsyncCallback<Void> asyncCallback);
}