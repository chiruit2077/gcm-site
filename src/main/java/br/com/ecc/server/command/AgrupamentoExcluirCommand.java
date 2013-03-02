package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Agrupamento;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class AgrupamentoExcluirCommand implements Callable<Void>{

	@Inject EntityManager em;
	private Agrupamento agrupamento;

	@Override
	@Transactional
	public Void call() throws Exception {
		Query q = em.createNamedQuery("agrupamentoMembro.deletePorAgrupamento");
		q.setParameter("agrupamento", getAgrupamento());
		q.executeUpdate();
		em.remove(em.merge(agrupamento));

		return null;
	}

	public Agrupamento getAgrupamento() {
		return agrupamento;
	}

	public void setAgrupamento(Agrupamento agrupamento) {
		this.agrupamento = agrupamento;
	}
}