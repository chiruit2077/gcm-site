package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.EncontroRestauranteGarcon;
import br.com.ecc.model.vo.EncontroHotelVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroHotelRestauranteSalvarCommand implements Callable<EncontroHotelVO>{

	@Inject EntityManager em;
	private EncontroHotelVO encontroHotelVO;

	@Override
	@Transactional
	public EncontroHotelVO call() throws Exception {
		if(encontroHotelVO.getListaEncontroRestauranteGarcon() !=null){
			EncontroRestauranteGarcon eip;
			for (int i = 0; i < encontroHotelVO.getListaEncontroRestauranteGarcon().size(); i++) {
				eip = encontroHotelVO.getListaEncontroRestauranteGarcon().get(i);
				eip.setEncontroHotel(encontroHotelVO.getEncontroHotel());
				encontroHotelVO.getListaEncontroRestauranteGarcon().set(i, em.merge(eip));
			}
		}
		return encontroHotelVO;
	}

	public EncontroHotelVO getEncontroHotelVO() {
		return encontroHotelVO;
	}

	public void setEncontroHotelVO(EncontroHotelVO encontroHotelVO) {
		this.encontroHotelVO = encontroHotelVO;
	}
}