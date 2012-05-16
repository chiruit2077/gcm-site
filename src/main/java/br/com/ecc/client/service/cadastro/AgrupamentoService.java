package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.AgrupamentoVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("agrupamento")
public interface AgrupamentoService extends RemoteService {
	public List<Agrupamento> lista(Grupo grupo) throws Exception;
	public List<Agrupamento> lista(Encontro encontro) throws Exception;
	public void exclui(Agrupamento agrupamento) throws Exception;
	public void salva(AgrupamentoVO agrupamentoVO) throws Exception;
	public AgrupamentoVO getVO(Agrupamento agrupamento) throws Exception;
}