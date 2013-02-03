package br.com.ecc.server.command;

import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConvite;
import br.com.ecc.model.EncontroFila;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.model.EncontroInscricaoPagamentoDetalhe;
import br.com.ecc.model.tipo.TipoConfirmacaoEnum;
import br.com.ecc.model.tipo.TipoFilaEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;
import br.com.ecc.model.tipo.TipoRespostaConviteEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroConviteSalvarCommand implements Callable<EncontroConvite>{

	@Inject EntityManager em;
	private EncontroConvite encontroConvite;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public EncontroConvite call() throws Exception {
		boolean mover = encontroConvite.getMoverFinalFila();
		boolean inscrever = encontroConvite.getGerarInscricao();
		encontroConvite = em.merge(encontroConvite);

		if(encontroConvite.getTipoResposta()!=null &&
		   encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.ACEITO) &&
		   inscrever){

			EncontroInscricao ei;
			EncontroInscricaoPagamentoDetalhe eip;

			//padrinho
			Query q = em.createNamedQuery("encontroInscricao.porEncontroCasal");
			q.setParameter("encontro", encontroConvite.getEncontro());
			q.setParameter("casal", encontroConvite.getCasal());
			if(q.getResultList().size()==0){
				ei = new EncontroInscricao();
				ei.setEncontro(encontroConvite.getEncontro());
				ei.setTipo(TipoInscricaoEnum.PADRINHO);
				ei.setCasal(encontroConvite.getCasal());
				ei.setEsconderPlanoPagamento(encontroConvite.getEsconderPlanoPagamento());
				if(encontroConvite.getEsconderPlanoPagamento()){
					ei.setValorEncontro(null);
				} else {
					ei.setValorEncontro(encontroConvite.getEncontro().getValorPadrinho());
				}
				ei.setTipoConfirmacao(TipoConfirmacaoEnum.CONFIRMADO);
				EncontroInscricaoFichaPagamento ficha = getFichaVaga(TipoInscricaoFichaEnum.ENCONTRISTA,encontroConvite.getEncontro());
				if (ficha!=null){
					ei.setFichaPagamento(ficha);
					ei.setCodigo(ficha.getFicha());
				}
				ei = em.merge(ei);
				if (ficha != null){
					ficha.setEncontroInscricao(ei);
					ficha = em.merge(ficha);
				}

				if(ei.getValorEncontro()!=null){
					eip = new EncontroInscricaoPagamentoDetalhe();
					eip.setEncontroInscricao(ei);
					eip.setDescricao("Valor do encontro");
					eip.setValor(ei.getValorEncontro());
					em.merge(eip);
					if(encontroConvite.getEsconderPlanoPagamento()){
						eip = new EncontroInscricaoPagamentoDetalhe();
						eip.setEncontroInscricao(ei);
						eip.setDescricao("Valor do afilhado");
						eip.setValor(encontroConvite.getEncontro().getValorAfilhado());
						em.merge(eip);
					}
				}
			}

			//afilhado
			q = em.createNamedQuery("encontroInscricao.porEncontroCasal");
			q.setParameter("encontro", encontroConvite.getEncontro());
			q.setParameter("casal", encontroConvite.getCasalConvidado());
			if(q.getResultList().size()==0){
				Casal casalConvidado = encontroConvite.getCasalConvidado();
				casalConvidado.setSituacao(TipoSituacaoEnum.ATIVO);
				casalConvidado = em.merge(casalConvidado);

				ei = new EncontroInscricao();
				ei.setEncontro(encontroConvite.getEncontro());
				ei.setTipo(TipoInscricaoEnum.AFILHADO);
				ei.setCasal(encontroConvite.getCasalConvidado());
				ei.setEsconderPlanoPagamento(encontroConvite.getEsconderPlanoPagamento());
				if(encontroConvite.getEsconderPlanoPagamento()){
					ei.setValorEncontro(null);
				} else {
					ei.setValorEncontro(encontroConvite.getEncontro().getValorAfilhado());
				}
				ei.setTipoConfirmacao(TipoConfirmacaoEnum.CONFIRMADO);
				EncontroInscricaoFichaPagamento ficha = getFichaVaga(TipoInscricaoFichaEnum.AFILHADO,encontroConvite.getEncontro());
				if (ficha!=null){
					ei.setFichaPagamento(ficha);
					ei.setCodigo(ficha.getFicha());

				}
				ei = em.merge(ei);
				if (ficha != null){
					ficha.setEncontroInscricao(ei);
					ficha = em.merge(ficha);
				}

				if(ei.getValorEncontro()!=null){
					eip = new EncontroInscricaoPagamentoDetalhe();
					eip.setEncontroInscricao(ei);
					eip.setDescricao("Valor do encontro");
					eip.setValor(ei.getValorEncontro());
					em.merge(eip);
				}
			}

			encontroConvite.getCasalConvidado().setCasalPadrinho(encontroConvite.getCasal());
			em.merge(encontroConvite.getCasalConvidado());
		}
		if(encontroConvite.getTipoResposta()!=null &&
		   encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.RECUSADO)){

			Query q;
			if(mover){
				q = em.createNamedQuery("encontroFila.porEncontroFilaNormal");
				q.setParameter("encontro", encontroConvite.getEncontro());
				q.setMaxResults(1);
				List<EncontroFila> listaFila = q.getResultList();
				if(listaFila.size()>0){
					EncontroFila encontroFila = listaFila.get(0);

					q = em.createQuery("Select max(ordem) from EncontroConvite e where e.encontro = :encontro and e.encontroFila.tipoFila = :tipoFila");
					q.setParameter("encontro", encontroConvite.getEncontro());
					q.setParameter("tipoFila", TipoFilaEnum.NORMAL);
					Integer max = (Integer) q.getSingleResult();

					EncontroConvite ec = new EncontroConvite();
					ec.setCasal(encontroConvite.getCasal());
					ec.setEncontro(encontroConvite.getEncontro());
					ec.setEncontroFila(encontroFila);
					ec.setOrdem(max+1);

					em.merge(ec);
				}
			}

			q = em.createNamedQuery("encontroConvite.porEncontro");
			q.setParameter("encontro", encontroConvite.getEncontro());
			List<EncontroConvite> listaConvite = q.getResultList();

			boolean achei = false;
			for (EncontroConvite ec : listaConvite) {
				if(ec.getId().equals(encontroConvite.getId())){
					achei = true;
				} else {
					if(achei){
						if(ec.getEncontroFila().getId().equals(encontroConvite.getEncontroFila().getId())){
							ec.setOrdem(ec.getOrdem()-1);
							em.merge(ec);
						}
					}
				}
			}
		}
		return encontroConvite;
	}

	@SuppressWarnings("unchecked")
	private EncontroInscricaoFichaPagamento getFichaVaga(
			TipoInscricaoFichaEnum tipo, Encontro encontro) {
		if (encontro.getUsaFichaPagamento()!= null && encontro.getUsaFichaPagamento().equals(1)){
			Query q = em.createNamedQuery("encontroInscricaoFichaPagamento.porVagalLivre");
			q.setParameter("encontro", encontro);
			q.setParameter("tipo", tipo);
			q.setParameter("status", TipoInscricaoFichaStatusEnum.NORMAL);
			List<EncontroInscricaoFichaPagamento> fichas = q.getResultList();
			if (fichas.size()>0)
				return fichas.get(0);
		}
		return null;
	}

	public EncontroConvite getEncontroConvite() {
		return encontroConvite;
	}
	public void setEncontroConvite(EncontroConvite encontroConvite) {
		this.encontroConvite = encontroConvite;
	}
}