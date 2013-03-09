package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Restaurante;
import br.com.ecc.model.vo.RestauranteVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("restaurante")
public interface RestauranteService extends RemoteService {
	public List<Restaurante> lista() throws Exception;
	public RestauranteVO getVO(Restaurante restaurante) throws Exception;
	public RestauranteVO salvaRestaurante(RestauranteVO vo) throws Exception;
	public Restaurante salva(Restaurante restaurante);
	public void exclui(Restaurante restaurante) throws Exception;
}