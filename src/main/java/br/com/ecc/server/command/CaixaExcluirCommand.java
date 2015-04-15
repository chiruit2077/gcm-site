package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Caixa;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class CaixaExcluirCommand implements Callable<Void>{

	@Inject EntityManager em;
	private Caixa caixa;

	@Override
	@Transactional
	public Void call() throws Exception {
		Query q = em.createNamedQuery("caixaItem.deletePorCaixa");
		q.setParameter("caixa", getCaixa());
		q.executeUpdate();
		em.remove(em.merge(getCaixa()));

		return null;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	
}