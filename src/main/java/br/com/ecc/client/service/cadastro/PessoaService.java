package br.com.ecc.client.service.cadastro;

import br.com.ecc.model.Pessoa;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("pessoa")
public interface PessoaService extends RemoteService {
	public void exclui(Pessoa pessoa) throws Exception;
	public Pessoa salva(Pessoa pessoa) throws Exception;
}