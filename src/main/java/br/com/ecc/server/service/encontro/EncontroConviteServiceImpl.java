package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroConviteService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConvite;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroConviteExcluirCommand;
import br.com.ecc.server.command.EncontroConviteListarCommand;
import br.com.ecc.server.command.EncontroConviteSalvarCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroConviteServiceImpl extends SecureRemoteServiceServlet implements EncontroConviteService {
	private static final long serialVersionUID = -4224700536662638697L;

	@Inject Injector injector;

	@Override
	@Permissao(nomeOperacao="Listar convites", operacao=Operacao.VISUALIZAR)
	public List<EncontroConvite> lista(Encontro encontro) throws Exception {
		EncontroConviteListarCommand cmd = injector.getInstance(EncontroConviteListarCommand.class);
		cmd.setEncontro(encontro);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir convite", operacao=Operacao.EXCLUIR)
	public void exclui(EncontroConvite encontroConvite) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("encontroAtividadeInscricao.porEncontroConvite");
//		cmdEntidade.addParameter("encontroConvite", encontroConvite);
//		if(cmdEntidade.call().size()>0){
//			throw new WebException("Erro ao excluir esta inscrição. \nJá existem atividades planilhadas para esta inscrição.");
//		}
		
		EncontroConviteExcluirCommand cmd = injector.getInstance(EncontroConviteExcluirCommand.class);
		cmd.setEncontroConvite(encontroConvite);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar convite", operacao=Operacao.SALVAR)
	public EncontroConvite salva(EncontroConvite encontroConvite) throws Exception {
		EncontroConviteSalvarCommand cmd = injector.getInstance(EncontroConviteSalvarCommand.class);
		cmd.setEncontroConvite(encontroConvite);
		return cmd.call();
	}
}