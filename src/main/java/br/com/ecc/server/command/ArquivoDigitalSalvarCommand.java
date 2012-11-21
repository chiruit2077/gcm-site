package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.ArquivoDigital;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ArquivoDigitalSalvarCommand implements Callable<ArquivoDigital>{
	
	@Inject EntityManager em;
	
	private ArquivoDigital arquivoDigital;

	@Override
	@Transactional
	public ArquivoDigital call() throws Exception {
		if (arquivoDigital!=null){
			arquivoDigital.setTamanho(arquivoDigital.getDados().length);
		}
		return em.merge(arquivoDigital);
	}
	public void setArquivoDigital(ArquivoDigital arquivoDigital) {
		this.arquivoDigital = arquivoDigital;
	}
	public ArquivoDigital getArquivoDigital() {
		return arquivoDigital;
	}
}