package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Atividade;
import br.com.ecc.model.Grupo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("atividade")
public interface AtividadeService extends RemoteService {
	public List<Atividade> lista(Grupo grupo) throws Exception;
	public void exclui(Atividade atividade) throws Exception;
	public Atividade salva(Atividade atividade) throws Exception;
}