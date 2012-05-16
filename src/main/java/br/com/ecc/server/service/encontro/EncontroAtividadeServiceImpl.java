package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroAtividadeService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
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
public class EncontroAtividadeServiceImpl extends SecureRemoteServiceServlet implements EncontroAtividadeService {
	private static final long serialVersionUID = -3780927709658969884L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontroAtividades", operacao=Operacao.VISUALIZAR)
	public List<EncontroAtividade> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroAtividade.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir encontroAtividade", operacao=Operacao.EXCLUIR)
	public void exclui(EncontroAtividade encontroAtividade) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("encontroAtividade.porEncontroAtividade");
//		cmdEntidade.addParameter("encontroAtividade", encontroAtividade);
//		List<EncontroAtividade> encontroAtividades = cmdEntidade.call();
//		if(encontroAtividades!=null && encontroAtividades.size()>0){
//			throw new WebException("Erro ao excluir EncontroAtividade. \nJá existem encontroAtividades neste encontroAtividade.");
//		}
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontroAtividade);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar encontroAtividade", operacao=Operacao.SALVAR)
	public EncontroAtividade salva(EncontroAtividade encontroAtividade) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(encontroAtividade);
		return (EncontroAtividade)cmd.call();
	}
}