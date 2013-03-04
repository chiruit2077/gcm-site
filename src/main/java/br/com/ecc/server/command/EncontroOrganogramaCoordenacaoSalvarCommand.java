package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.EncontroOrganogramaArea;
import br.com.ecc.model.EncontroOrganogramaCoordenacao;
import br.com.ecc.model.vo.EncontroOrganogramaVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroOrganogramaCoordenacaoSalvarCommand implements Callable<EncontroOrganogramaVO>{

	@Inject EntityManager em;
	private EncontroOrganogramaVO vo;

	@Override
	@Transactional
	public EncontroOrganogramaVO call() throws Exception {
		if(vo.getListaEncontroOrganogramaArea()!=null){
			EncontroOrganogramaArea eip;
			for (int i = 0; i < vo.getListaEncontroOrganogramaArea().size(); i++) {
				eip = vo.getListaEncontroOrganogramaArea().get(i);
				eip.setEncontroOrganograma(vo.getEncontroOrganograma());
				vo.getListaEncontroOrganogramaArea().set(i, em.merge(eip));
			}
		}
		if(vo.getListaEncontroOrganogramaCoordenacao()!=null){
			EncontroOrganogramaCoordenacao eip;
			for (int i = 0; i < vo.getListaEncontroOrganogramaCoordenacao().size(); i++) {
				eip = vo.getListaEncontroOrganogramaCoordenacao().get(i);
				eip.setEncontroOrganograma(vo.getEncontroOrganograma());
				vo.getListaEncontroOrganogramaCoordenacao().set(i, em.merge(eip));
			}
		}
		return vo;
	}

	public EncontroOrganogramaVO getVo() {
		return vo;
	}

	public void setEncontroOrganogramaVO(EncontroOrganogramaVO vo) {
		this.vo = vo;
	}
}