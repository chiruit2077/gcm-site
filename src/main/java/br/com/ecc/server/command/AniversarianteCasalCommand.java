package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Casal;
import br.com.ecc.model.vo.AniversarianteVO;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class AniversarianteCasalCommand implements Callable<List<AniversarianteVO>> {
	
	@Inject Injector injector;
	@Inject EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AniversarianteVO> call() throws Exception {
		List<AniversarianteVO> listaEnviar = new ArrayList<AniversarianteVO>();
		
		Query q;
		Calendar d1 = Calendar.getInstance();
		d1.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE)-5);
		
		Calendar d2 = Calendar.getInstance();
		d2.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE)+15);
		
		Calendar hoje = Calendar.getInstance();
		
		if(d2.get(Calendar.MONTH)<d1.get(Calendar.MONTH)){
			q = em.createQuery("Select u from Casal u " +
					"where month(u.casamento) >= month(:d1) or month(u.casamento) <= month(:d2) and u.situacao = 'ATIVO' ");
		} else {
			q = em.createQuery("Select u from Casal u " +
				"where month(u.casamento) between month(:d1) and month(:d2) and u.situacao = 'ATIVO' ");
		}
		q.setParameter("d1", d1.getTime());
		q.setParameter("d2", d2.getTime());
		List<Casal> lista = q.getResultList();
		Calendar casamento = Calendar.getInstance();
		
		AniversarianteVO vo;
		boolean go;
		for (Casal casal : lista) {
			casamento.setTime(casal.getCasamento());
			casamento.set(Calendar.YEAR, 2000);
			go = false;
			if(d2.get(Calendar.MONTH)<d1.get(Calendar.MONTH)){
				if(casamento.get(Calendar.MONTH)>=d1.get(Calendar.MONTH) || casamento.get(Calendar.MONTH)<= d2.get(Calendar.MONTH)){
					if(casamento.get(Calendar.MONTH)>0){
						casamento.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)-1);
					} else {
						casamento.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
					}
					go=true;
				}
			} else {
				casamento.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
				if(casamento.after(d1) && casamento.before(d2)){
					go=true;
				}
			}
			if(go){
				vo = new AniversarianteVO();
				casal.setCasamento(casamento.getTime());
				vo.setCasal(casal);
				if (casamento.get(Calendar.MONTH)==hoje.get(Calendar.MONTH) && casamento.get(Calendar.DATE)== hoje.get(Calendar.DATE)){
					vo.setHoje(true);
				} else  if(casamento.before(hoje)){
					vo.setAntigo(true);
				} 
				listaEnviar.add(vo);
			}
		}
		Collections.sort(listaEnviar, new Comparator<AniversarianteVO>() {
			@Override
			public int compare(AniversarianteVO o1, AniversarianteVO o2) {
				if(o1.getCasal().getCasamento().equals(o2.getCasal().getCasamento())){
					return o1.getCasal().getApelidos("e").compareTo(o2.getCasal().getApelidos("2"));
				}
				return o1.getCasal().getCasamento().compareTo(o2.getCasal().getCasamento());
			}
		});
		return listaEnviar;
	}

}