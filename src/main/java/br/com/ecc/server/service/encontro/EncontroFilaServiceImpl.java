package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroFilaService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroFila;
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
public class EncontroFilaServiceImpl extends SecureRemoteServiceServlet implements EncontroFilaService {
	private static final long serialVersionUID = -590908998820381780L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar filas", operacao=Operacao.VISUALIZAR)
	public List<EncontroFila> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroFila.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir fila", operacao=Operacao.EXCLUIR)
	public void exclui(EncontroFila encontroFila) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("encontroAtividadeInscricao.porEncontroFila");
//		cmdEntidade.addParameter("encontroFila", encontroFila);
//		if(cmdEntidade.call().size()>0){
//			throw new WebException("Erro ao excluir esta inscrição. \nJá existem atividades planilhadas para esta inscrição.");
//		}
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontroFila);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar fila", operacao=Operacao.SALVAR)
	public EncontroFila salva(EncontroFila encontroFila) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(encontroFila);
		return (EncontroFila)cmd.call();
	}
}