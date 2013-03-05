package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.vo.EncontroRestauranteVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroRestauranteServiceAsync {
	void getVO(EncontroRestaurante restaurante,	AsyncCallback<EncontroRestauranteVO> callback);
	void lista(Encontro encontro, AsyncCallback<List<EncontroRestaurante>> callback);
	void salvaRestaurante(EncontroRestauranteVO vo, AsyncCallback<EncontroRestauranteVO> callback);
	void salva(EncontroRestaurante hotelEditado, AsyncCallback<EncontroRestaurante> webAsyncCallback);
	void exclui(EncontroRestaurante hotelEditado,AsyncCallback<Void> webAsyncCallback);
}
