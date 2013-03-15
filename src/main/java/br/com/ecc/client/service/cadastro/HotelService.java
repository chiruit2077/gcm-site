package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.Hotel;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("hotel")
public interface HotelService extends RemoteService {
	public List<Hotel> lista(Grupo grupo) throws Exception;
	public void exclui(Hotel hotel) throws Exception;
	public Hotel salva(Hotel hotel) throws Exception;
}