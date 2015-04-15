package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.CaixaItem;
import br.com.ecc.model.vo.CaixaVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class CaixaSalvarCommand implements Callable<Void>{

	@Inject EntityManager em;
	private CaixaVO caixaVO;

	@Override
	@Transactional
	public Void call() throws Exception {
		Query q;

		getCaixaVO().setCaixa(em.merge(getCaixaVO().getCaixa()));
		
		List<CaixaItem> novaLista = new ArrayList<CaixaItem>();
		if(getCaixaVO().getListaCaixaItem()!=null){
			for (CaixaItem caixaItem : getCaixaVO().getListaCaixaItem()) {
				if(caixaItem.getId()!=null){
					novaLista.add(caixaItem);
				}
			}
		}
		if(novaLista.size()>0){
			q = em.createNamedQuery("caixaItem.deletePorCaixatoNotIn");
			q.setParameter("caixa", getCaixaVO().getCaixa());
			q.setParameter("lista", novaLista);
			q.executeUpdate();
		} else {
			q = em.createNamedQuery("caixaItem.deletePorCaixa");
			q.setParameter("caixa", getCaixaVO().getCaixa());
			q.executeUpdate();
		}
		if(getCaixaVO().getListaCaixaItem()!=null){
			for (CaixaItem caixaItem : getCaixaVO().getListaCaixaItem()) {
				caixaItem.setCaixa(getCaixaVO().getCaixa());
				caixaItem = em.merge(caixaItem);
			}
		}
		return null;
	}

	public CaixaVO getCaixaVO() {
		return caixaVO;
	}

	public void setCaixaVO(CaixaVO caixaVO) {
		this.caixaVO = caixaVO;
	}
	
}