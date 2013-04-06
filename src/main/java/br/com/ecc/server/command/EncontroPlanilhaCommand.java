package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoExibicaoPlanilhaEnum;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroPlanilhaCommand implements Callable<List<EncontroAtividadeInscricao>>{

	@Inject EntityManager em;
	private Encontro encontro;
	private EncontroPeriodo encontroPeriodo;
	private TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum;
	private Usuario usuarioAtual;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional
	public List<EncontroAtividadeInscricao> call() throws Exception {
		List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao = new ArrayList<EncontroAtividadeInscricao>();
		Query q;

		Date inicio = null, fim = new Date(3000,1,1);;
		if(encontroPeriodo!=null){
			q = em.createNamedQuery("encontroPeriodo.porEncontro");
			q.setParameter("encontro", getEncontro());
			List<EncontroPeriodo> listaPeriodo = q.getResultList();
			boolean achou = false;
			inicio = encontroPeriodo.getInicio();
			for (EncontroPeriodo ep : listaPeriodo) {
				if(ep.getId().equals(encontroPeriodo.getId())){
					achou = true;
				}
				if(achou && ep.getInicio().after(inicio)){
					fim = ep.getInicio();
					break;
				}
			}
		}

		q = em.createNamedQuery("casal.porPessoa");
		q.setParameter("pessoa", usuarioAtual.getPessoa());
		q.setMaxResults(1);
		Casal casal = (Casal) q.getSingleResult();

		String query = "select u from EncontroAtividadeInscricao u where u.encontroInscricao.encontro = :encontro ";
		if(tipoExibicaoPlanilhaEnum.equals(TipoExibicaoPlanilhaEnum.MINHA_ATIVIDADE_MINHA_COLUNA) ||
		   tipoExibicaoPlanilhaEnum.equals(TipoExibicaoPlanilhaEnum.TODAS_ATIVIDADES_MINHA_COLUNA)){
			query += " and u.encontroInscricao.casal = :casal ";
		}
		if(encontroPeriodo!=null){
			query += " and u.encontroAtividade.inicio between :inicio and :fim ";
		}
		query += "order by u.encontroAtividade.inicio";

		q = em.createQuery(query);
		q.setParameter("encontro", encontro);

		if(tipoExibicaoPlanilhaEnum.equals(TipoExibicaoPlanilhaEnum.MINHA_ATIVIDADE_MINHA_COLUNA) ||
		   tipoExibicaoPlanilhaEnum.equals(TipoExibicaoPlanilhaEnum.TODAS_ATIVIDADES_MINHA_COLUNA)){
			q.setParameter("casal", casal);
		}
		if(encontroPeriodo!=null){
			q.setParameter("inicio", inicio);
			q.setParameter("fim", fim);
		}

		listaEncontroAtividadeInscricao = q.getResultList();

		Collections.sort(listaEncontroAtividadeInscricao, new Comparator<EncontroAtividadeInscricao>() {
			@Override
			public int compare(EncontroAtividadeInscricao o1, EncontroAtividadeInscricao o2) {
				return o1.getEncontroInscricao().toString().compareTo(o2.getEncontroInscricao().toString());
			}
		});

		return listaEncontroAtividadeInscricao;
	}

	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public EncontroPeriodo getEncontroPeriodo() {
		return encontroPeriodo;
	}
	public void setEncontroPeriodo(EncontroPeriodo encontroPeriodo) {
		this.encontroPeriodo = encontroPeriodo;
	}
	public TipoExibicaoPlanilhaEnum getTipoExibicaoPlanilhaEnum() {
		return tipoExibicaoPlanilhaEnum;
	}
	public void setTipoExibicaoPlanilhaEnum(TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum) {
		this.tipoExibicaoPlanilhaEnum = tipoExibicaoPlanilhaEnum;
	}
	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}
	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}
}