package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroTotalizacao;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.model.vo.EncontroTotalizacaoVO;
import br.com.ecc.model.vo.EncontroVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroCarregaVOCommand implements Callable<EncontroVO>{

	@Inject EntityManager em;
	private Encontro encontro;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public EncontroVO call() throws Exception {
		EncontroVO vo = new EncontroVO();
		vo.setEncontro(em.find(Encontro.class, encontro.getId()));
		
		Query q;
		//Periodos
		q = em.createNamedQuery("encontroPeriodo.porEncontro");
		q.setParameter("encontro", vo.getEncontro());
		vo.setListaPeriodo(q.getResultList());
		
		//Totalizacoes
		q = em.createNamedQuery("encontroTotalizacao.porEncontro");
		q.setParameter("encontro",vo.getEncontro());
		List<EncontroTotalizacao> listaTotalizacaos = q.getResultList();
		EncontroTotalizacaoVO encontroTotalizacaoVO;
		vo.setListaTotalizacao(new ArrayList<EncontroTotalizacaoVO>());
		for (EncontroTotalizacao encontroTotalizacao : listaTotalizacaos) {
			encontroTotalizacaoVO = new EncontroTotalizacaoVO();
			encontroTotalizacaoVO.setEncontroTotalizacao(encontroTotalizacao);
			
			q = em.createNamedQuery("encontroTotalizacaoAtividade.porEncontroTotalizacao");
			q.setParameter("encontroTotalizacao", encontroTotalizacao);
			encontroTotalizacaoVO.setListaAtividade(q.getResultList());
			
			vo.getListaTotalizacao().add(encontroTotalizacaoVO);
		}
		
		//Atividades
		q = em.createNamedQuery("encontroAtividade.porEncontro");
		q.setParameter("encontro", vo.getEncontro());
		vo.setListaEncontroAtividade(q.getResultList());
		Collections.sort(vo.getListaEncontroAtividade(), new Comparator<EncontroAtividade>() {
			@Override
			public int compare(EncontroAtividade o1, EncontroAtividade o2) {
				return o1.getInicio().compareTo(o2.getInicio());
			}
		});
		
		//Inscricao
		q = em.createNamedQuery("encontroInscricao.porEncontro");
		q.setParameter("encontro", vo.getEncontro());
		vo.setListaInscricao(q.getResultList());
		Collections.sort(vo.getListaInscricao(), new Comparator<EncontroInscricao>() {
			@Override
			public int compare(EncontroInscricao o1, EncontroInscricao o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		
		//coordenadores
		vo.setListaCoordenadores(new ArrayList<Casal>());
		for (EncontroInscricao encontroInscricao : vo.getListaInscricao()) {
			if(encontroInscricao.getTipo().equals(TipoInscricaoEnum.COORDENADOR)){
				vo.getListaCoordenadores().add(encontroInscricao.getCasal());
			}
		}
		
		//agrupamentos
		vo.setListaAgrupamentoVOEncontro(new ArrayList<AgrupamentoVO>());
		q = em.createNamedQuery("agrupamento.porEncontro");
		q.setParameter("encontro", vo.getEncontro());
		List<Agrupamento> listaAgrupamento = q.getResultList();
		AgrupamentoVO agrupamentoVO;
		for (Agrupamento agrupamento : listaAgrupamento) {
			agrupamentoVO = new AgrupamentoVO();
			agrupamentoVO.setAgrupamento(agrupamento);
			
			q = em.createNamedQuery("agrupamentoMembro.porAgrupamento");
			q.setParameter("agrupamento", agrupamento);
			agrupamentoVO.setListaMembros(q.getResultList());
			
			vo.getListaAgrupamentoVOEncontro().add(agrupamentoVO);
		}
		
		//resonsaveis pro convites
		vo.setListaAgrupamentoVOEncontro(new ArrayList<AgrupamentoVO>());
		q = em.createNamedQuery("encontroConviteResponsavel.porEncontro");
		q.setParameter("encontro", vo.getEncontro());
		vo.setListaResponsavelConvite(q.getResultList());
		
		return vo;
	}

	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
}