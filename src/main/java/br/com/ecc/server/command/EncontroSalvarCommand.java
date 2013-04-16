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

public class EncontroSalvarCommand implements Callable<EncontroVO>{

	@Inject EntityManager em;
	private EncontroVO encontroVO;
	private Boolean copia = false;

	@Override
	@Transactional
	public EncontroVO call() throws Exception {
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
			for (int i = 0; i < encontroVO.getListaPeriodo().size(); i++) {
				EncontroPeriodo ep = encontroVO.getListaPeriodo().get(i);
				ep.setEncontro(encontroVO.getEncontro());
				encontroVO.getListaPeriodo().set(i, em.merge(ep));
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
			for (int i = 0; i < encontroVO.getListaTotalizacao().size(); i++) {
				EncontroTotalizacaoVO encontroTotalizacao = encontroVO.getListaTotalizacao().get(i);
				encontroTotalizacao.getEncontroTotalizacao().setEncontro(encontroVO.getEncontro());
				encontroTotalizacao.setEncontroTotalizacao(em.merge(encontroTotalizacao.getEncontroTotalizacao()));
				encontroVO.getListaTotalizacao().set(i,encontroTotalizacao);

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
					for (int j = 0; j < encontroTotalizacao.getListaAtividade().size(); j++) {
						EncontroTotalizacaoAtividade eai = encontroTotalizacao.getListaAtividade().get(j);
						eai.setEncontroTotalizacao(encontroTotalizacao.getEncontroTotalizacao());
						encontroTotalizacao.getListaAtividade().set(j, em.merge(eai));
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
			for (int j = 0; j < encontroVO.getListaResponsavelConvite().size(); j++) {
				EncontroConviteResponsavel responsavelPeriodo = encontroVO.getListaResponsavelConvite().get(j);
				responsavelPeriodo.setEncontro(encontroVO.getEncontro());
				encontroVO.getListaResponsavelConvite().set(j, em.merge(responsavelPeriodo));
			}
		}
		return encontroVO;
	}

	public EncontroVO getEncontroVO() {
		return encontroVO;
	}

	public void setEncontroVO(EncontroVO encontroVO) {
		this.encontroVO = encontroVO;
	}

	public Boolean getCopia() {
		return copia;
	}

	public void setCopia(Boolean copia) {
		this.copia = copia;
	}
}