package br.com.ecc.server.service.cadastro;

import br.com.ecc.client.service.cadastro.PessoaService;
import br.com.ecc.model.Pessoa;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class PessoaServiceImpl extends SecureRemoteServiceServlet implements PessoaService {
	private static final long serialVersionUID = -1773172976239108906L;
	
	@Inject Injector injector;

	@Override
	public void exclui(Pessoa pessoa) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(pessoa);
		cmd.call();
	}

	@Override
	public Pessoa salva(Pessoa pessoa) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(pessoa);
		return (Pessoa)cmd.call();
	}
}