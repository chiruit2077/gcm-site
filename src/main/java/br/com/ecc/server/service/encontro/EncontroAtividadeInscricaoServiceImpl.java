package br.com.ecc.server.service.encontro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.encontro.EncontroAtividadeInscricaoService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.Papel;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.tipo.TipoExibicaoPlanilhaEnum;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.SessionHelper;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroPlanilhaCommand;
import br.com.ecc.server.command.PlanilhaImprimirCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.ExecuteUpdateCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroAtividadeInscricaoServiceImpl extends SecureRemoteServiceServlet implements EncontroAtividadeInscricaoService {
	private static final long serialVersionUID = -3780927709658969884L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontroAtividadeInscricaos", operacao=Operacao.VISUALIZAR)
	public List<EncontroAtividadeInscricao> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroAtividadeInscricao.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir encontroAtividadeInscricao", operacao=Operacao.EXCLUIR)
	public void exclui(EncontroAtividadeInscricao encontroAtividadeInscricao) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("encontroAtividadeInscricao.porEncontroAtividadeInscricao");
//		cmdEntidade.addParameter("encontroAtividadeInscricao", encontroAtividadeInscricao);
//		List<EncontroAtividadeInscricao> encontroAtividadeInscricaos = cmdEntidade.call();
//		if(encontroAtividadeInscricaos!=null && encontroAtividadeInscricaos.size()>0){
//			throw new WebException("Erro ao excluir EncontroAtividadeInscricao. \nJá existem encontroAtividadeInscricaos neste encontroAtividadeInscricao.");
//		}
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontroAtividadeInscricao);
		cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Salvar encontroAtividadeInscricao", operacao=Operacao.SALVAR)
	public EncontroAtividadeInscricao salva(EncontroAtividadeInscricao encontroAtividadeInscricao) throws Exception {
		GetEntityListCommand cmdVerifica = injector.getInstance(GetEntityListCommand.class);
		cmdVerifica.setNamedQuery("encontroAtividadeInscricao.porAtividadeInscricao");
		cmdVerifica.addParameter("encontroAtividade", encontroAtividadeInscricao.getEncontroAtividade());
		cmdVerifica.addParameter("encontroInscricao", encontroAtividadeInscricao.getEncontroInscricao());
		List<EncontroAtividadeInscricao> lista = cmdVerifica.call();
		if (lista.size()>0){
			Papel papel = encontroAtividadeInscricao.getPapel();
			encontroAtividadeInscricao = lista.get(0);
			encontroAtividadeInscricao.setPapel(papel);
		}
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(encontroAtividadeInscricao);
		return (EncontroAtividadeInscricao)cmd.call();
	}
	
	@Override
	public void salvaInscricoes(EncontroAtividade encontroAtividade, EncontroInscricao encontroInscricao, List<EncontroAtividadeInscricao> listaParticipantes) throws Exception {
		List<EncontroAtividadeInscricao> novaLista = new ArrayList<EncontroAtividadeInscricao>();
		for (EncontroAtividadeInscricao encontroAtividadeInscricao : listaParticipantes) {
			if(encontroAtividadeInscricao.getId()!=null){
				novaLista.add(encontroAtividadeInscricao);
			}
		}
		ExecuteUpdateCommand cmd = injector.getInstance(ExecuteUpdateCommand.class);
		if(novaLista.size()>0){
			if(encontroAtividade!=null){
				cmd.setNamedQuery("encontroAtividadeInscricao.deletePorEncontroAtividadeNotIn");
				cmd.addParameter("encontroAtividade", encontroAtividade);
				cmd.addParameter("lista", novaLista );
				cmd.call();
			} else {
				cmd.setNamedQuery("encontroAtividadeInscricao.deletePorEncontroInscricaoNotIn");
				cmd.addParameter("encontroInscricao", encontroInscricao);
				cmd.addParameter("lista", novaLista );
				cmd.call();
			}
		} else {
			if(encontroAtividade!=null){
				cmd.setNamedQuery("encontroAtividadeInscricao.deletePorEncontroAtividade");
				cmd.addParameter("encontroAtividade", encontroAtividade);
				cmd.call();
			} else {
				cmd.setNamedQuery("encontroAtividadeInscricao.deletePorEncontroInscricao");
				cmd.addParameter("encontroInscricao", encontroInscricao);
				cmd.call();
			}
		}
		for (EncontroAtividadeInscricao encontroAtividadeInscricao : listaParticipantes) {
			salva(encontroAtividadeInscricao);
		}
	}

	@Override
	@Permissao(nomeOperacao="Dados da planilha", operacao=Operacao.OUTRO)
	public List<EncontroAtividadeInscricao> listaFiltrado(Encontro encontro, EncontroPeriodo encontroPeriodo, TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum) throws Exception {
		EncontroPlanilhaCommand cmd = injector.getInstance(EncontroPlanilhaCommand.class);
		cmd.setEncontro(encontro);
		cmd.setEncontroPeriodo(encontroPeriodo);
		cmd.setTipoExibicaoPlanilhaEnum(tipoExibicaoPlanilhaEnum);
		cmd.setUsuarioAtual(SessionHelper.getUsuario(getThreadLocalRequest().getSession()));
		
		return cmd.call();
	}
	
	@Override
	@Permissao(nomeOperacao="Impressão da planilha", operacao=Operacao.OUTRO)
	public Integer imprimePlanilha(Encontro encontro, EncontroPeriodo encontroPeriodo, TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum, Boolean exportarExcel) throws Exception {
		PlanilhaImprimirCommand cmd = injector.getInstance(PlanilhaImprimirCommand.class);
		cmd.setEncontro(encontro);
		cmd.setEncontroPeriodo(encontroPeriodo);
		cmd.setTipoExibicaoPlanilhaEnum(tipoExibicaoPlanilhaEnum);
		cmd.setUsuarioAtual(SessionHelper.getUsuario(getThreadLocalRequest().getSession()));
		cmd.setExportaExcel(exportarExcel);
		return cmd.call();		
	}
}