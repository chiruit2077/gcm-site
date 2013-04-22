package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.server.command.basico.DeleteEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroInscricaoFichaPagamentoExcluirCommand implements Callable<Void>{

	@Inject Injector injector;
	private EncontroInscricaoFichaPagamento ficha;

	@Override
	@Transactional
	public Void call() throws Exception {
		if (ficha.getEncontroInscricao()!=null) throw new Exception("Ficha em Uso!");
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(getFicha());
		cmd.call();
		return null;
	}

	public EncontroInscricaoFichaPagamento getFicha() {
		return ficha;
	}

	public void setFicha(EncontroInscricaoFichaPagamento ficha) {
		this.ficha = ficha;
	}
}