package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.EncontroConviteResponsavel;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.EncontroTotalizacao;
import br.com.ecc.model.EncontroTotalizacaoAtividade;
import br.com.ecc.model.vo.EncontroTotalizacaoVO;
import br.com.ecc.model.vo.EncontroVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroSalvarCommand implements Callable<Void>{

	@Inject EntityManager em;
	private EncontroVO encontroVO;

	@Override
	@Transactional
	public Void call() throws Exception {
		Query q;

		encontroVO.setEncontro(em.merge(encontroVO.getEncontro()));
		
		//periodos
		List<EncontroPeriodo> novaLista = new ArrayList<EncontroPeriodo>();
		if(encontroVO.getListaPeriodo()!=null){
			for (EncontroPeriodo encontroPeriodo : encontroVO.getListaPeriodo()) {
				if(encontroPeriodo.getId()!=null){
					novaLista.add(encontroPeriodo);
				}
			}
		}
		if(novaLista.size()>0){
			q = em.createNamedQuery("encontroPeriodo.deletePorEncontroNotIn");
			q.setParameter("encontro", encontroVO.getEncontro());
			q.setParameter("lista", novaLista);
			q.executeUpdate();
		} else {
			q = em.createNamedQuery("encontroPeriodo.deletePorEncontro");
			q.setParameter("encontro", encontroVO.getEncontro());
			q.executeUpdate();
		}
		if(encontroVO.getListaPeriodo()!=null){
			for (EncontroPeriodo encontroPeriodo : encontroVO.getListaPeriodo()) {
				encontroPeriodo.setEncontro(encontroVO.getEncontro());
				encontroPeriodo = em.merge(encontroPeriodo);
			}
		}
		
		//totalizações
		List<EncontroTotalizacao> novaListaTotalizacao = new ArrayList<EncontroTotalizacao>();
		if(encontroVO.getListaTotalizacao()!=null){
			for (EncontroTotalizacaoVO encontroTotalizacao : encontroVO.getListaTotalizacao()) {
				if(encontroTotalizacao.getEncontroTotalizacao().getId()!=null){
					novaListaTotalizacao.add(encontroTotalizacao.getEncontroTotalizacao());
				}
			}
		}
		if(novaListaTotalizacao.size()>0){
			q = em.createNamedQuery("encontroTotalizacaoAtividade.deletePorEncontroNotIn");
			q.setParameter("encontro", encontroVO.getEncontro());
			q.setParameter("lista", novaListaTotalizacao);
			q.executeUpdate();

			q = em.createNamedQuery("encontroTotalizacao.deletePorEncontroNotIn");
			q.setParameter("encontro", encontroVO.getEncontro());
			q.setParameter("lista", novaListaTotalizacao);
			q.executeUpdate();
		} else {
			q = em.createNamedQuery("encontroTotalizacaoAtividade.deletePorEncontro");
			q.setParameter("encontro", encontroVO.getEncontro());
			q.executeUpdate();

			q = em.createNamedQuery("encontroTotalizacao.deletePorEncontro");
			q.setParameter("encontro", encontroVO.getEncontro());
			q.executeUpdate();
		}
		if(encontroVO.getListaTotalizacao()!=null){
			for (EncontroTotalizacaoVO encontroTotalizacao : encontroVO.getListaTotalizacao()) {
				encontroTotalizacao.getEncontroTotalizacao().setEncontro(encontroVO.getEncontro());
				encontroTotalizacao.setEncontroTotalizacao(em.merge(encontroTotalizacao.getEncontroTotalizacao()));
				
				//participantes
				List<EncontroTotalizacaoAtividade> novaListaTotalizacaoAtividade = new ArrayList<EncontroTotalizacaoAtividade>();
				if(encontroTotalizacao.getListaAtividade()!=null){
					for (EncontroTotalizacaoAtividade eai : encontroTotalizacao.getListaAtividade()) {
						if(eai.getId()!=null){
							novaListaTotalizacaoAtividade.add(eai);
						}
					}
				}
				if(novaListaTotalizacaoAtividade.size()>0){
					q = em.createNamedQuery("encontroTotalizacaoAtividade.deletePorEncontroTotalizacaoNotIn");
					q.setParameter("encontroTotalizacao", encontroTotalizacao.getEncontroTotalizacao());
					q.setParameter("lista", novaListaTotalizacaoAtividade);
					q.executeUpdate();
				} else {
					q = em.createNamedQuery("encontroTotalizacaoAtividade.deletePorEncontroTotalizacao");
					q.setParameter("encontroTotalizacao", encontroTotalizacao.getEncontroTotalizacao());
					q.executeUpdate();
				}
				if(encontroTotalizacao.getListaAtividade()!=null){
					for (EncontroTotalizacaoAtividade eai : encontroTotalizacao.getListaAtividade()) {
						eai.setEncontroTotalizacao(encontroTotalizacao.getEncontroTotalizacao());
						em.merge(eai);
					}
				}
			}
		}
		
		//responsaveis
		List<EncontroConviteResponsavel> novaListaResponsavel = new ArrayList<EncontroConviteResponsavel>();
		if(encontroVO.getListaResponsavelConvite()!=null){
			for (EncontroConviteResponsavel responsavel : encontroVO.getListaResponsavelConvite()) {
				if(responsavel.getId()!=null){
					novaListaResponsavel.add(responsavel);
				}
			}
		}
		if(novaListaResponsavel.size()>0){
			q = em.createNamedQuery("encontroConviteResponsavel.deletePorEncontroNotIn");
			q.setParameter("encontro", encontroVO.getEncontro());
			q.setParameter("lista", novaListaResponsavel);
			q.executeUpdate();
		} else {
			q = em.createNamedQuery("encontroConviteResponsavel.deletePorEncontro");
			q.setParameter("encontro", encontroVO.getEncontro());
			q.executeUpdate();
		}
		if(encontroVO.getListaResponsavelConvite()!=null){
			for (EncontroConviteResponsavel responsavelPeriodo : encontroVO.getListaResponsavelConvite()) {
				responsavelPeriodo.setEncontro(encontroVO.getEncontro());
				responsavelPeriodo = em.merge(responsavelPeriodo);
			}
		}
		
		return null;
	}

	public EncontroVO getEncontroVO() {
		return encontroVO;
	}

	public void setEncontroVO(EncontroVO encontroVO) {
		this.encontroVO = encontroVO;
	}
}