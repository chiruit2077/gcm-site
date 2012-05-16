package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.GrupoVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("grupo")
public interface GrupoService extends RemoteService {
	public List<Grupo> lista() throws Exception;
	public void exclui(Grupo grupo) throws Exception;
	public Grupo salva(Grupo grupo) throws Exception;
	public GrupoVO getVO(Grupo grupo) throws Exception;
}