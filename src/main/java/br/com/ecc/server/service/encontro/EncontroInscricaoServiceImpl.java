package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroInscricaoService;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.EncontroInscricaoVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.SessionHelper;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroInscricaoSalvarCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.ExecuteUpdateCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroInscricaoServiceImpl extends SecureRemoteServiceServlet implements EncontroInscricaoService {
	private static final long serialVersionUID = -3780927709658969884L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontroInscricaos", operacao=Operacao.VISUALIZAR)
	public List<EncontroInscricao> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir encontroInscricao", operacao=Operacao.EXCLUIR)
	public void exclui(EncontroInscricao encontroInscricao) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("encontroAtividadeInscricao.porEncontroInscricao");
//		cmdEntidade.addParameter("encontroInscricao", encontroInscricao);
//		if(cmdEntidade.call().size()>0){
//			throw new WebException("Erro ao excluir esta inscrição. \nJá existem atividades planilhadas para esta inscrição.");
//		}
		//removendo atividades no encontro
		ExecuteUpdateCommand cmdDelete = injector.getInstance(ExecuteUpdateCommand.class);
		cmdDelete.setNamedQuery("encontroAtividadeInscricao.deletePorEncontroInscricao");
		cmdDelete.addParameter("encontroInscricao", encontroInscricao);
		cmdDelete.call();
		
		//exclusão
		cmdDelete = injector.getInstance(ExecuteUpdateCommand.class);
		cmdDelete.setNamedQuery("encontroInscricaoPagamento.deletePorEncontroInscricao");
		cmdDelete.addParameter("encontroInscricao", encontroInscricao);
		cmdDelete.call();
		
		cmdDelete = injector.getInstance(ExecuteUpdateCommand.class);
		cmdDelete.setNamedQuery("encontroInscricaoPagamentoDetalhe.deletePorEncontroInscricao");
		cmdDelete.addParameter("encontroInscricao", encontroInscricao);
		cmdDelete.call();
		
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontroInscricao);
		cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public EncontroInscricaoVO getVO(EncontroInscricao encontroInscricao) throws Exception {
		EncontroInscricaoVO vo = new EncontroInscricaoVO();
		
		GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
		cmd.setClazz(EncontroInscricao.class);
		cmd.setId(encontroInscricao.getId());
		vo.setEncontroInscricao((EncontroInscricao) cmd.call());
		
		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
		cmdEntidade.setNamedQuery("encontroInscricaoPagamento.porEncontroInscricao");
		cmdEntidade.addParameter("encontroInscricao", vo.getEncontroInscricao());
		vo.setListaPagamento(cmdEntidade.call());
		
		cmdEntidade = injector.getInstance(GetEntityListCommand.class);
		cmdEntidade.setNamedQuery("encontroInscricaoPagamentoDetalhe.porEncontroInscricao");
		cmdEntidade.addParameter("encontroInscricao", vo.getEncontroInscricao());
		vo.setListaPagamentoDetalhe(cmdEntidade.call());
		
		return vo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca inscricao pro casal - VO", operacao=Operacao.VISUALIZAR)
	public EncontroInscricaoVO getVO(Encontro encontro, Casal casal) throws Exception {
		EncontroInscricaoVO vo = null;
		
		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
		cmdEntidade.setNamedQuery("encontroInscricao.porEncontroCasal");
		cmdEntidade.addParameter("encontro", encontro);
		cmdEntidade.addParameter("casal", casal);
		List<EncontroInscricao> lista = cmdEntidade.call();
		
		if(lista.size()>0){
			return getVO(lista.get(0));
		}
		return vo;
	}
	
	@Override
	@Permissao(nomeOperacao="Salvar encontroInscricao", operacao=Operacao.SALVAR)
	public EncontroInscricaoVO salva(EncontroInscricaoVO encontroInscricaoVO) throws Exception {
		EncontroInscricaoSalvarCommand cmd = injector.getInstance(EncontroInscricaoSalvarCommand.class);
		cmd.setEncontroInscricaoVO(encontroInscricaoVO);
		cmd.setUsuarioAtual(SessionHelper.getUsuario(getThreadLocalRequest().getSession()));
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EncontroInscricaoPagamento> listaPagamentos(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricaoPagamento.listaPagamentosPorEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	public void salvaPagamento(EncontroInscricaoPagamento pagamento) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(pagamento);
		cmd.call();
	}

}