package br.com.ecc.server.service.cadastro;

import java.util.List;

import br.com.ecc.client.service.cadastro.QuartoService;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Quarto;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class QuartoServiceImpl extends SecureRemoteServiceServlet implements QuartoService {
	
	private static final long serialVersionUID = 4816266976128281951L;
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar quartos", operacao=Operacao.VISUALIZAR)
	public List<Quarto> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("quarto.porGrupo");
		cmd.addParameter("grupo", grupo);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir quarto", operacao=Operacao.EXCLUIR)
	public void exclui(Quarto quarto) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(quarto);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar quarto", operacao=Operacao.SALVAR)
	public Quarto salva(Quarto quarto) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(quarto);
		return (Quarto)cmd.call();
	}

}