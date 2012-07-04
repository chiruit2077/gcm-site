package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Casal;
import br.com.ecc.model.vo.CasalOpcaoRelatorioVO;
import br.com.ecc.model.vo.CasalParamVO;
import br.com.ecc.model.vo.CasalVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("casal")
public interface CasalService extends RemoteService {
	public List<Casal> lista(CasalParamVO casalParamVO) throws Exception;
	public CasalVO getVO(Casal casal) throws Exception;
	public void exclui(Casal casal) throws Exception;
	public CasalVO salva(CasalVO casalVO) throws Exception;
	public Casal salva(Casal casal) throws Exception;
	public Integer imprimeLista(List<Casal> listaCasal, CasalOpcaoRelatorioVO casalOpcaoRelatorioVO) throws Exception;
}