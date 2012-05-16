package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("encontroAtividade")
public interface EncontroAtividadeService extends RemoteService {
	public List<EncontroAtividade> lista(Encontro encontro) throws Exception;
	public void exclui(EncontroAtividade encontroAtividade) throws Exception;
	public EncontroAtividade salva(EncontroAtividade encontroAtividade) throws Exception;
}