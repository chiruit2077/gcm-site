package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.vo.EncontroOrganogramaVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroOrganograma")
public interface EncontroOrganogramaService extends RemoteService {
	public List<EncontroOrganograma> lista(Encontro encontro) throws Exception;
	public EncontroOrganogramaVO salvaEncontroOrganogramaCoordenacao(EncontroOrganogramaVO encontroOrganogramaVO) throws Exception;
	public EncontroOrganogramaVO getVO(EncontroOrganograma encontroOrganograma) throws Exception;
	public EncontroOrganograma salva(EncontroOrganograma encontroOrganograma);
	public void exclui(EncontroOrganograma encontroOrganograma) throws Exception;
}