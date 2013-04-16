package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroInscricaoFichaPagamentoGeraFichasCommand implements Callable<Void>{

	@Inject Injector injector;

	private Encontro encontroSelecionado;


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Void call() throws Exception {
		EncontroInscricaoFichaPagamentoSalvarCommand cmdSalva = injector.getInstance(EncontroInscricaoFichaPagamentoSalvarCommand.class);
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
					ficha.setTipo(TipoInscricaoCasalEnum.AFILHADO);
				else
					ficha.setTipo(TipoInscricaoCasalEnum.ENCONTRISTA);
				cmdSalva.setFicha(ficha);
				ficha = cmdSalva.call();
				listaaux.add(ficha);
			}
		}
		return null;
	}

	private Integer getQtdeAfilhados(
			List<EncontroInscricaoFichaPagamento> lista) {
		int qtde = 0;
		for (EncontroInscricaoFichaPagamento encontroInscricaoFichaPagamento : lista) {
			if (encontroInscricaoFichaPagamento.getTipo().equals(TipoInscricaoCasalEnum.AFILHADO) && encontroInscricaoFichaPagamento.getStatus().equals(TipoInscricaoFichaStatusEnum.NORMAL))
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

	public Encontro getEncontroSelecionado() {
		return encontroSelecionado;
	}

	public void setEncontroSelecionado(Encontro encontroSelecionado) {
		this.encontroSelecionado = encontroSelecionado;
	}
}