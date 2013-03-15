package br.com.ecc.server.command;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.OrganogramaArea;
import br.com.ecc.model.OrganogramaCoordenacao;
import br.com.ecc.model.vo.OrganogramaVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class OrganogramaSalvarCommand implements Callable<OrganogramaVO>{

	@Inject EntityManager em;
	private OrganogramaVO vo;

	@Override
	@Transactional
	public OrganogramaVO call() throws Exception {
		vo.setOrganograma(em.merge(vo.getOrganograma()));
		if(vo.getListaAreas()!=null){
			OrganogramaArea eip;
			for (int i = 0; i < vo.getListaAreas().size(); i++) {
				eip = vo.getListaAreas().get(i);
				eip.setOrganograma(vo.getOrganograma());
				vo.getListaAreas().set(i, em.merge(eip));
			}
		}

		if(vo.getListaCoordenacoes()!=null){
			OrganogramaCoordenacao eip;
			for (int i = 0; i < vo.getListaCoordenacoes().size(); i++) {
				eip = vo.getListaCoordenacoes().get(i);
				eip.setOrganogramaArea(getOrganogramaArea(eip.getOrganogramaArea(),vo.getListaAreas()));
				vo.getListaCoordenacoes().set(i, em.merge(eip));
			}
		}

		if (vo.getListaCoordenacoes().size()>0){
			Query q = em.createNamedQuery("organogramaCoordenacao.deletePorOrganogramaNotIn");
			q.setParameter("organograma", vo.getOrganograma());
			q.setParameter("lista", vo.getListaCoordenacoes());
			q.executeUpdate();
		}
		if (vo.getListaAreas().size()>0){
			Query q = em.createNamedQuery("organogramaArea.deletePorOrganogramaNotIn");
			q.setParameter("organograma", vo.getOrganograma());
			q.setParameter("lista", vo.getListaAreas());
			q.executeUpdate();
		}
		return vo;
	}

	private OrganogramaArea getOrganogramaArea(OrganogramaArea organogramaArea,
			List<OrganogramaArea> listaAreas) {
		for (OrganogramaArea organogramaAreaaux : listaAreas) {
			if (organogramaArea.equals(organogramaAreaaux)) return organogramaAreaaux;
		}
		return null;
	}

	public OrganogramaVO getVo() {
		return vo;
	}

	public void setVo(OrganogramaVO vo) {
		this.vo = vo;
	}

}