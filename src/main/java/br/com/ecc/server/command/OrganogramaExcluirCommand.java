package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Organograma;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class OrganogramaExcluirCommand implements Callable<Void>{

	@Inject EntityManager em;
	private Organograma organograma;

	@Override
	@Transactional
	public Void call() throws Exception {
		Query q = em.createNamedQuery("organogramaCoordenacao.deletePorOrganograma");
		q.setParameter("organograma", getOrganograma());
		q.executeUpdate();
		q = em.createNamedQuery("organogramaArea.deletePorOrganograma");
		q.setParameter("organograma", getOrganograma());
		q.executeUpdate();
		em.remove(em.merge(getOrganograma()));

		return null;
	}

	public Organograma getOrganograma() {
		return organograma;
	}

	public void setOrganograma(Organograma organograma) {
		this.organograma = organograma;
	}

}