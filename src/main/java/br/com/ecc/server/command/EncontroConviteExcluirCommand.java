package br.com.ecc.server.command;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.EncontroConvite;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroConviteExcluirCommand implements Callable<Void>{

	@Inject EntityManager em;
	private EncontroConvite encontroConvite;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Void call() throws Exception {
		if(encontroConvite.getOrdem()!=null){
			Query q = em.createNamedQuery("encontroConvite.porEncontro");
			q.setParameter("encontro", encontroConvite.getEncontro());
			List<EncontroConvite> listaConvite = q.getResultList();
			
			boolean achei = false;
			for (EncontroConvite ec : listaConvite) {
				if(ec.getId().equals(encontroConvite.getId())){
					achei = true;
				} else {
					if(achei){
						if(ec.getEncontroFila().getId().equals(encontroConvite.getEncontroFila().getId())){
							ec.setOrdem(ec.getOrdem()-1);
							em.merge(ec);
						}
					}
				}
			}
		}
		em.remove(em.merge(encontroConvite));
		return null;
	}

	public EncontroConvite getEncontroConvite() {
		return encontroConvite;
	}
	public void setEncontroConvite(EncontroConvite encontroConvite) {
		this.encontroConvite = encontroConvite;
	}
}