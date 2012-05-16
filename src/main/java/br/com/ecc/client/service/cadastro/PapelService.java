package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Papel;
import br.com.ecc.model.Grupo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("papel")
public interface PapelService extends RemoteService {
	public List<Papel> lista(Grupo grupo) throws Exception;
	public void exclui(Papel papel) throws Exception;
	public Papel salva(Papel papel) throws Exception;
}