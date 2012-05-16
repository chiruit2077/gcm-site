package br.com.ecc.server.command;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConvite;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroConviteListarCommand implements Callable<List<EncontroConvite>>{

	@Inject EntityManager em;
	
	private Encontro encontro;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<EncontroConvite> call() throws Exception {
		Query q;
		q = em.createNamedQuery("encontroConvite.porEncontro");
		q.setParameter("encontro", encontro);
		List<EncontroConvite> listaConvites = q.getResultList();
		return listaConvites;
	}
	
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
}