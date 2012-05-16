package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.ArquivoDigital;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ArquivoDigitalAtualizaLinkCommand implements Callable<ArquivoDigital>{

	@Inject EntityManager em;
	
	private Integer id;
	private Integer idLink;
	private String entidadeLink;
	
	@Override
	@Transactional
	public ArquivoDigital call() throws Exception {
		if(id == null) {
			throw new WebException("Use o metodo setId() antes de invocar esse command.");
		}
		
		ArquivoDigital arquivoDigital = em.find(ArquivoDigital.class, id);
		if(arquivoDigital != null) {
			arquivoDigital.setIdLink(idLink);
			arquivoDigital.setEntidadeLink(entidadeLink);
			arquivoDigital = em.merge(arquivoDigital);
		}
		
		return arquivoDigital;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdLink() {
		return idLink;
	}

	public void setIdLink(Integer idLink) {
		this.idLink = idLink;
	}

	public String getEntidadeLink() {
		return entidadeLink;
	}

	public void setEntidadeLink(String entidadeLink) {
		this.entidadeLink = entidadeLink;
	}

}
