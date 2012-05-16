package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.EncontroVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontro")
public interface EncontroService extends RemoteService {
	public List<Encontro> lista(Grupo grupo) throws Exception;
	public void exclui(Encontro encontro) throws Exception;
	public void salva(EncontroVO encontroVO) throws Exception;
	public EncontroVO getVO(Encontro encontro) throws Exception;
	public List<EncontroInscricao> listaInscricoes(Encontro encontroSelecionado) throws Exception;
}