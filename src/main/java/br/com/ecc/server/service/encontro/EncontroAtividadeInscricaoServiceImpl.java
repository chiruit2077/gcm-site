package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroAtividadeInscricaoService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividadeInscricao;
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
			Boolean revisado = encontroAtividadeInscricao.getRevisado();
			encontroAtividadeInscricao = lista.get(0);
			encontroAtividadeInscricao.setPapel(papel);
			encontroAtividadeInscricao.setRevisado(revisado);
		}
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(encontroAtividadeInscricao);
		return (EncontroAtividadeInscricao)cmd.call();
	}

	@Override
	public void salvaInscricoes(Encontro encontro, List<EncontroAtividadeInscricao> listaParticipantes, List<EncontroAtividadeInscricao> listaParticipantesOriginal) throws Exception {
		int i = 0;
		while (i < listaParticipantes.size()) {
			listaParticipantes.set(i, salva(listaParticipantes.get(i)));
			i++;
		}

		DeleteEntityCommand cmdDelete = injector.getInstance(DeleteEntityCommand.class);
		for (EncontroAtividadeInscricao encontroAtividadeInscricao : listaParticipantesOriginal) {
			if(!listaParticipantes.contains(encontroAtividadeInscricao)){
				cmdDelete.setBaseEntity(encontroAtividadeInscricao);
				cmdDelete.call();
			}
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