package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.EncontroHotelQuarto;
import br.com.ecc.model.vo.EncontroHotelVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroHotelQuartoSalvarCommand implements Callable<EncontroHotelVO>{

	@Inject EntityManager em;
	private EncontroHotelVO encontroHotelVO;

	@Override
	@Transactional
	public EncontroHotelVO call() throws Exception {
		if(encontroHotelVO.getListaEncontroQuartos()!=null){
			EncontroHotelQuarto eip;
			for (int i = 0; i < encontroHotelVO.getListaEncontroQuartos().size(); i++) {
				eip = encontroHotelVO.getListaEncontroQuartos().get(i);
				eip.setEncontroHotel(encontroHotelVO.getEncontroHotel());
				encontroHotelVO.getListaEncontroQuartos().set(i, em.merge(eip));
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