package br.com.ecc.server.service.cadastro;

import java.util.List;

import br.com.ecc.client.service.cadastro.PapelService;
import br.com.ecc.model.Papel;
import br.com.ecc.model.Grupo;
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
public class PapelServiceImpl extends SecureRemoteServiceServlet implements PapelService {
	private static final long serialVersionUID = 3513245582463568390L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar papels", operacao=Operacao.VISUALIZAR)
	public List<Papel> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("papel.porGrupo");
		cmd.addParameter("grupo", grupo);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir papel", operacao=Operacao.EXCLUIR)
	public void exclui(Papel papel) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("papel.porPapel");
//		cmdEntidade.addParameter("papel", papel);
//		List<Papel> papels = cmdEntidade.call();
//		if(papels!=null && papels.size()>0){
//			throw new WebException("Erro ao excluir Papel. \nJá existem papels neste papel.");
//		}
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(papel);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar papel", operacao=Operacao.SALVAR)
	public Papel salva(Papel papel) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(papel);
		return (Papel)cmd.call();
	}
}