package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.vo.EncontroHotelVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroHotel")
public interface EncontroHotelService extends RemoteService {
	public List<EncontroHotel> lista(Encontro encontro) throws Exception;
	public EncontroHotelVO salvaEncontroHotelQuarto(EncontroHotelVO encontroHotelVO) throws Exception;
	public EncontroHotelVO getVO(EncontroHotel encontroHotel) throws Exception;
	public EncontroHotel getEncontroHotelEvento(Encontro encontroSelecionado) throws Exception;
	public EncontroHotel salva(EncontroHotel hotelEditado);
	public void exclui(EncontroHotel hotelEditado) throws Exception;
}