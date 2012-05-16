package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.Quarto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("quarto")
public interface QuartoService extends RemoteService {
	public List<Quarto> lista(Grupo grupo) throws Exception;
	public void exclui(Quarto quarto) throws Exception;
	public Quarto salva(Quarto quarto) throws Exception;
}