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
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.vo.AniversarianteVO;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class AniversariantePessoaCommand implements Callable<List<AniversarianteVO>> {
	
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
			q = em.createQuery("Select u from Pessoa u " +
					"where month(u.nascimento) >= month(:d1) or month(u.nascimento) <= month(:d2) ");
		} else {
			q = em.createQuery("Select u from Pessoa u " +
				"where month(u.nascimento) between month(:d1) and month(:d2) ");
		}
		q.setParameter("d1", d1.getTime());
		q.setParameter("d2", d2.getTime());
		List<Pessoa> lista = q.getResultList();
		List<Casal> listaR = new ArrayList<Casal>();
		Calendar nascimento = Calendar.getInstance();
		
		AniversarianteVO vo;
		boolean go;
		for (Pessoa pessoa : lista) {
			nascimento.setTime(pessoa.getNascimento());
			nascimento.set(Calendar.YEAR, 2000);
			go = false;
			if(d2.get(Calendar.MONTH)<d1.get(Calendar.MONTH)){
				if(nascimento.get(Calendar.MONTH)>=d1.get(Calendar.MONTH) || nascimento.get(Calendar.MONTH)<= d2.get(Calendar.MONTH)){
					if(nascimento.get(Calendar.MONTH)>0){
						nascimento.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)-1);
					} else {
						nascimento.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
					}
					go=true;
				}
			} else {
				nascimento.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
				if(nascimento.after(d1) && nascimento.before(d2)){
					go=true;
				}
			}
			if(go){
				pessoa.setNascimento(nascimento.getTime());
				q = em.createNamedQuery("casal.porPessoa");
				q.setParameter("pessoa", pessoa);
				q.setMaxResults(1);
				listaR = q.getResultList();
				if(listaR.size()>0){
					vo = new AniversarianteVO();
					vo.setCasal(listaR.get(0));
					pessoa.setNascimento(nascimento.getTime());
					vo.setPessoa(pessoa);
					if (nascimento.get(Calendar.MONTH)==hoje.get(Calendar.MONTH) && nascimento.get(Calendar.DATE)== hoje.get(Calendar.DATE)){
						vo.setHoje(true);
					} else  if(nascimento.before(hoje)){
						vo.setAntigo(true);
					}
					
					listaEnviar.add(vo);
				}
			}
		}
		Collections.sort(listaEnviar, new Comparator<AniversarianteVO>() {
			@Override
			public int compare(AniversarianteVO o1, AniversarianteVO o2) {
				if(o1.getPessoa().getNascimento().equals(o2.getPessoa().getNascimento())){
					return o1.getPessoa().getApelido().compareTo(o2.getPessoa().getApelido());
				}
				return o1.getPessoa().getNascimento().compareTo(o2.getPessoa().getNascimento());
			}
		});
		return listaEnviar;
	}

}