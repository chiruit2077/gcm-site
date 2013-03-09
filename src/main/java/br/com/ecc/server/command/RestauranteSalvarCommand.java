package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Mesa;
import br.com.ecc.model.RestauranteGrupo;
import br.com.ecc.model.RestauranteTitulo;
import br.com.ecc.model.vo.RestauranteVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class RestauranteSalvarCommand implements Callable<RestauranteVO>{

	@Inject EntityManager em;
	private RestauranteVO vo;

	@Override
	@Transactional
	public RestauranteVO call() throws Exception {
		if(vo.getListaMesas()!=null){
			Mesa eip;
			for (int i = 0; i < vo.getListaMesas().size(); i++) {
				eip = vo.getListaMesas().get(i);
				eip.setRestaurante(vo.getRestaurante());
				vo.getListaMesas().set(i, em.merge(eip));
			}
		}

		if (vo.getListaMesas().size()>0){
			Query q = em.createNamedQuery("mesa.deletePorRestauranteNotIn");
			q.setParameter("restaurante", vo.getRestaurante());
			q.setParameter("lista", vo.getListaMesas());
			q.executeUpdate();
		}

		if(vo.getListaGrupos()!=null){
			RestauranteGrupo eip;
			for (int i = 0; i < vo.getListaGrupos().size(); i++) {
				eip = vo.getListaGrupos().get(i);
				eip.setRestaurante(vo.getRestaurante());
				vo.getListaGrupos().set(i, em.merge(eip));
			}
		}
		if (vo.getListaGrupos().size()>0){
			Query q = em.createNamedQuery("restauranteGrupo.deletePorRestauranteNotIn");
			q.setParameter("restaurante", vo.getRestaurante());
			q.setParameter("lista", vo.getListaGrupos());
			q.executeUpdate();
		}

		if(vo.getListaTitulos()!=null){
			RestauranteTitulo eip;
			for (int i = 0; i < vo.getListaTitulos().size(); i++) {
				eip = vo.getListaTitulos().get(i);
				eip.setRestaurante(vo.getRestaurante());
				vo.getListaTitulos().set(i, em.merge(eip));
			}
		}
		if (vo.getListaTitulos().size()>0){
			Query q = em.createNamedQuery("restauranteTitulo.deletePorRestauranteNotIn");
			q.setParameter("restaurante", vo.getRestaurante());
			q.setParameter("lista", vo.getListaTitulos());
			q.executeUpdate();
		}


		return vo;
	}

	public RestauranteVO getVo() {
		return vo;
	}

	public void setVo(RestauranteVO vo) {
		this.vo = vo;
	}

}