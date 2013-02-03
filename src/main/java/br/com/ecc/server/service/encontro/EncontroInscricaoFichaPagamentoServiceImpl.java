package br.com.ecc.server.service.encontro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.encontro.EncontroInscricaoFichaPagamentoService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.tipo.TipoInscricaoFichaEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroInscricaoFichaPagamentoServiceImpl extends SecureRemoteServiceServlet implements EncontroInscricaoFichaPagamentoService {

	private static final long serialVersionUID = 3763111944496091407L;
	@Inject Injector injector;



	@Override
	@Permissao(nomeOperacao="Excluir encontroInscricaoFichaPagamento", operacao=Operacao.EXCLUIR)
	public void exclui(EncontroInscricaoFichaPagamento encontroInscricaoFichaPagamento) throws Exception {
		/*//removendo atividades no encontro
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

		*/
	}




	@SuppressWarnings("unchecked")
	@Override
	public List<EncontroInscricaoFichaPagamento> listaFichas(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricaoFichaPagamento.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	public void salvaFicha(EncontroInscricaoFichaPagamento ficha) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(ficha);
		cmd.call();
	}




	@SuppressWarnings("unchecked")
	@Override
	public void geraFichasVagas(Encontro encontroSelecionado) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricaoFichaPagamento.porEncontro");
		cmd.addParameter("encontro", encontroSelecionado);
		List<EncontroInscricaoFichaPagamento> lista = cmd.call();
		ArrayList<EncontroInscricaoFichaPagamento> listaaux = new ArrayList<EncontroInscricaoFichaPagamento>();
		listaaux.addAll(lista);
		for (int i = 1; i <= 99; i++) {
			EncontroInscricaoFichaPagamento ficha = getEncontroInscricaoFichaPagamento(listaaux,i);
			if (ficha == null){
				ficha = new EncontroInscricaoFichaPagamento();
				ficha.setFicha(i);
				ficha.setEncontro(encontroSelecionado);
				ficha.setStatus(TipoInscricaoFichaStatusEnum.NORMAL);
				if (getQtdeAfilhados(listaaux) < encontroSelecionado.getQuantidadeAfilhados())
					ficha.setTipo(TipoInscricaoFichaEnum.AFILHADO);
				else
					ficha.setTipo(TipoInscricaoFichaEnum.ENCONTRISTA);
				salvaFicha(ficha);
				listaaux.add(ficha);
			}
		}
	}

	private Integer getQtdeAfilhados(
			List<EncontroInscricaoFichaPagamento> lista) {
		int qtde = 0;
		for (EncontroInscricaoFichaPagamento encontroInscricaoFichaPagamento : lista) {
			if (encontroInscricaoFichaPagamento.getTipo().equals(TipoInscricaoFichaEnum.AFILHADO) && encontroInscricaoFichaPagamento.getStatus().equals(TipoInscricaoFichaStatusEnum.NORMAL))
				qtde++;
		}
		return qtde;
	}

	private EncontroInscricaoFichaPagamento getEncontroInscricaoFichaPagamento(
			List<EncontroInscricaoFichaPagamento> lista, int i) {
		for (EncontroInscricaoFichaPagamento encontroInscricaoFichaPagamento : lista) {
			if (encontroInscricaoFichaPagamento.getFicha().equals(i) && !encontroInscricaoFichaPagamento.getStatus().equals(TipoInscricaoFichaStatusEnum.LIBERADO))
				return encontroInscricaoFichaPagamento;
		}
		return null;

	}

}