package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.vo.AgrupamentoVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class AgrupamentoSalvarCommand implements Callable<Void>{

	@Inject EntityManager em;
	private AgrupamentoVO agrupamentoVO;

	@Override
	@Transactional
	public Void call() throws Exception {
		Query q;

		agrupamentoVO.setAgrupamento(em.merge(agrupamentoVO.getAgrupamento()));
		
		//destinatarios
		List<AgrupamentoMembro> novaLista = new ArrayList<AgrupamentoMembro>();
		if(agrupamentoVO.getListaMembros()!=null){
			for (AgrupamentoMembro destinatario : agrupamentoVO.getListaMembros()) {
				if(destinatario.getId()!=null){
					novaLista.add(destinatario);
				}
			}
		}
		if(novaLista.size()>0){
			q = em.createNamedQuery("agrupamentoMembro.deletePorAgrupamentoNotIn");
			q.setParameter("agrupamento", agrupamentoVO.getAgrupamento());
			q.setParameter("lista", novaLista);
			q.executeUpdate();
		} else {
			q = em.createNamedQuery("agrupamentoMembro.deletePorAgrupamento");
			q.setParameter("agrupamento", agrupamentoVO.getAgrupamento());
			q.executeUpdate();
		}
		if(agrupamentoVO.getListaMembros()!=null){
			for (AgrupamentoMembro destinatario : agrupamentoVO.getListaMembros()) {
				destinatario.setAgrupamento(agrupamentoVO.getAgrupamento());
				destinatario = em.merge(destinatario);
			}
		}
		return null;
	}
	public AgrupamentoVO getAgrupamentoVO() {
		return agrupamentoVO;
	}
	public void setAgrupamentoVO(AgrupamentoVO agrupamentoVO) {
		this.agrupamentoVO = agrupamentoVO;
	}
}