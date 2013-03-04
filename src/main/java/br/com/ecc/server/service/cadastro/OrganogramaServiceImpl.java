package br.com.ecc.server.service.cadastro;

import java.util.List;

import br.com.ecc.client.service.cadastro.OrganogramaService;
import br.com.ecc.model.Organograma;
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
public class OrganogramaServiceImpl extends SecureRemoteServiceServlet implements OrganogramaService {

	private static final long serialVersionUID = -7625976189032315511L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar organograma", operacao=Operacao.VISUALIZAR)
	public List<Organograma> lista() throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("organograma.todos");
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir organograma", operacao=Operacao.EXCLUIR)
	public void exclui(Organograma organograma) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(organograma);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar organograma", operacao=Operacao.SALVAR)
	public Organograma salva(Organograma organograma) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(organograma);
		return (Organograma)cmd.call();
	}

}