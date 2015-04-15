package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Caixa;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.CaixaVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("caixa")
public interface CaixaService extends RemoteService {
	public List<Caixa> lista(Grupo grupo) throws Exception;
	public void exclui(Caixa caixa) throws Exception;
	public void salva(CaixaVO caixaVO) throws Exception;
	public CaixaVO getVO(Caixa caixa) throws Exception;
}