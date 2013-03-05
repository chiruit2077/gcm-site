package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.vo.EncontroRestauranteVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroRestaurante")
public interface EncontroRestauranteService extends RemoteService {
	public List<EncontroRestaurante> lista(Encontro encontro) throws Exception;
	public EncontroRestauranteVO getVO(EncontroRestaurante restaurante) throws Exception;
	public EncontroRestauranteVO salvaRestaurante(EncontroRestauranteVO vo) throws Exception;
	public EncontroRestaurante salva(EncontroRestaurante restaurante);
	public void exclui(EncontroRestaurante restaurante) throws Exception;
}