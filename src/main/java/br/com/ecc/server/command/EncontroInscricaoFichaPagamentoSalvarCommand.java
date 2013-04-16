package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroInscricaoFichaPagamentoSalvarCommand implements Callable<EncontroInscricaoFichaPagamento>{

	@Inject Injector injector;
	private EncontroInscricaoFichaPagamento ficha;

	@Override
	@Transactional
	public EncontroInscricaoFichaPagamento call() throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(getFicha());
		return (EncontroInscricaoFichaPagamento) cmd.call();
	}

	public EncontroInscricaoFichaPagamento getFicha() {
		return ficha;
	}

	public void setFicha(EncontroInscricaoFichaPagamento ficha) {
		this.ficha = ficha;
	}
}