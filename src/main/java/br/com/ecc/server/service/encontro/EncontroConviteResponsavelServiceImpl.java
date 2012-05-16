package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroConviteResponsavelService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConviteResponsavel;
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
public class EncontroConviteResponsavelServiceImpl extends SecureRemoteServiceServlet implements EncontroConviteResponsavelService {
	private static final long serialVersionUID = 3020519337099385737L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar resonsaveis de convites", operacao=Operacao.VISUALIZAR)
	public List<EncontroConviteResponsavel> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroConviteResponsavel.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir convite", operacao=Operacao.EXCLUIR)
	public void exclui(EncontroConviteResponsavel encontroConviteResponsavel) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("encontroAtividadeInscricao.porEncontroConviteResponsavel");
//		cmdEntidade.addParameter("encontroConviteResponsavel", encontroConviteResponsavel);
//		if(cmdEntidade.call().size()>0){
//			throw new WebException("Erro ao excluir esta inscrição. \nJá existem atividades planilhadas para esta inscrição.");
//		}
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontroConviteResponsavel);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar convite", operacao=Operacao.SALVAR)
	public EncontroConviteResponsavel salva(EncontroConviteResponsavel encontroConviteResponsavel) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(encontroConviteResponsavel);
		return (EncontroConviteResponsavel)cmd.call();
	}
}