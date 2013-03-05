package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.EncontroRestauranteMesa;
import br.com.ecc.model.vo.EncontroRestauranteVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroRestauranteMesaSalvarCommand implements Callable<EncontroRestauranteVO>{

	@Inject EntityManager em;
	private EncontroRestauranteVO vo;

	@Override
	@Transactional
	public EncontroRestauranteVO call() throws Exception {
		if(vo.getListaEncontroRestauranteMesa() !=null){
			EncontroRestauranteMesa eip;
			for (int i = 0; i < vo.getListaEncontroRestauranteMesa().size(); i++) {
				eip = vo.getListaEncontroRestauranteMesa().get(i);
				eip.setEncontroRestaurante(vo.getEncontroRestaurante());
				vo.getListaEncontroRestauranteMesa().set(i, em.merge(eip));
			}
		}
		return vo;
	}

	public EncontroRestauranteVO getVo() {
		return vo;
	}

	public void setVo(EncontroRestauranteVO vo) {
		this.vo = vo;
	}

}