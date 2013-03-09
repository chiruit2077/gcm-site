package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Restaurante;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class RestauranteExcluirCommand implements Callable<Void>{

	@Inject EntityManager em;
	private Restaurante restaurante;

	@Override
	@Transactional
	public Void call() throws Exception {
		Query q = em.createNamedQuery("restauranteTitulo.deletePorRestaurante");
		q.setParameter("restaurante", getRestaurante());
		q.executeUpdate();
		q = em.createNamedQuery("restauranteGrupo.deletePorRestaurante");
		q.setParameter("restaurante", getRestaurante());
		q.executeUpdate();
		q = em.createNamedQuery("mesa.deletePorRestaurante");
		q.setParameter("restaurante", getRestaurante());
		q.executeUpdate();
		em.remove(em.merge(restaurante));

		return null;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

}