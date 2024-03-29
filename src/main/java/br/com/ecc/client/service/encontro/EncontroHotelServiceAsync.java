package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.vo.EncontroHotelVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroHotelServiceAsync {
	void getVO(EncontroHotel encontroHotel,	AsyncCallback<EncontroHotelVO> callback);
	void lista(Encontro encontro, AsyncCallback<List<EncontroHotel>> callback);
	void salvaEncontroHotelQuarto(EncontroHotelVO encontroHotelVO,	AsyncCallback<EncontroHotelVO> callback);
	void getEncontroHotelEvento(Encontro encontroSelecionado, AsyncCallback<EncontroHotel> callback);
	void salva(EncontroHotel hotelEditado, AsyncCallback<EncontroHotel> webAsyncCallback);
	void exclui(EncontroHotel hotelEditado,AsyncCallback<Void> webAsyncCallback);
}
