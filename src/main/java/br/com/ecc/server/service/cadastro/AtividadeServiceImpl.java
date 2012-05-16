package br.com.ecc.server.service.cadastro;

import java.util.List;

import br.com.ecc.client.service.cadastro.AtividadeService;
import br.com.ecc.model.Atividade;
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
public class AtividadeServiceImpl extends SecureRemoteServiceServlet implements AtividadeService {
	private static final long serialVersionUID = 3513245582463568390L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar atividades", operacao=Operacao.VISUALIZAR)
	public List<Atividade> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("atividade.porGrupo");
		cmd.addParameter("grupo", grupo);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir atividade", operacao=Operacao.EXCLUIR)
	public void exclui(Atividade atividade) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("atividade.porAtividade");
//		cmdEntidade.addParameter("atividade", atividade);
//		List<Atividade> atividades = cmdEntidade.call();
//		if(atividades!=null && atividades.size()>0){
//			throw new WebException("Erro ao excluir Atividade. \nJá existem atividades neste atividade.");
//		}
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(atividade);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar atividade", operacao=Operacao.SALVAR)
	public Atividade salva(Atividade atividade) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(atividade);
		return (Atividade)cmd.call();
	}
}