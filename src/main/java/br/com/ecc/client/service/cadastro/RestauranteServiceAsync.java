package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Restaurante;
import br.com.ecc.model.vo.RestauranteVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RestauranteServiceAsync {
	void getVO(Restaurante restaurante,	AsyncCallback<RestauranteVO> callback);
	void lista(AsyncCallback<List<Restaurante>> callback);
	void salvaRestaurante(RestauranteVO vo, AsyncCallback<RestauranteVO> callback);
	void salva(Restaurante hotelEditado, AsyncCallback<Restaurante> webAsyncCallback);
	void exclui(Restaurante hotelEditado,AsyncCallback<Void> webAsyncCallback);
}
