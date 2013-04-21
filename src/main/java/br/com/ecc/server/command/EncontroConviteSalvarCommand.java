package br.com.ecc.server.command;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Casal;
import br.com.ecc.model.EncontroConvite;
import br.com.ecc.model.EncontroFila;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.EncontroInscricaoPagamentoDetalhe;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoConfirmacaoEnum;
import br.com.ecc.model.tipo.TipoFilaEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoPagamentoDetalheEnum;
import br.com.ecc.model.tipo.TipoPagamentoLancamentoEnum;
import br.com.ecc.model.tipo.TipoRespostaConviteEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroConviteSalvarCommand implements Callable<EncontroConvite>{

	@Inject EntityManager em;
	@Inject Injector inject;
	private EncontroConvite encontroConvite;
	private Usuario usuarioAtual;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public EncontroConvite call() throws Exception {
		if(encontroConvite.getTipoResposta()==null) return encontroConvite;
		boolean mover = encontroConvite.getMoverFinalFila();
		boolean inscrever = encontroConvite.getGerarInscricao();
		encontroConvite = em.merge(encontroConvite);

		if(encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.ACEITO)){
			if (inscrever) geraInscricoes();
			encontroConvite.getCasalConvidado().setCasalPadrinho(encontroConvite.getCasal());
			em.merge(encontroConvite.getCasalConvidado());
		}
		else if(encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.RECUSADO)){
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

	private void geraInscricoes() throws Exception {
		EncontroInscricao ei;
		EncontroInscricaoPagamentoDetalhe eip;
		EncontroInscricaoPagamentoDetalhe credito = null;
		EncontroInscricaoPagamentoDetalhe debito = null;

		EncontroInscricaoVO vopadrinho = getEncontroInscricaoVO(encontroConvite.getCasal());
		if(vopadrinho==null){
			vopadrinho = new EncontroInscricaoVO();
			ei = new EncontroInscricao();
			vopadrinho.setEncontroInscricao(ei);
			vopadrinho.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
			vopadrinho.setListaPagamento(new ArrayList<EncontroInscricaoPagamento>());
		}else{
			ei = vopadrinho.getEncontroInscricao();
		}
		ei.setEncontro(encontroConvite.getEncontro());
		ei.setTipo(TipoInscricaoEnum.PADRINHO);
		ei.setCasal(encontroConvite.getCasal());
		ei.setEsconderPlanoPagamento(false);
		ei.setTipoConfirmacao(TipoConfirmacaoEnum.CONFIRMADO);

		if (encontroConvite.getEncontro().getUsaDetalheAutomatico().equals(1)){
			if(encontroConvite.getEsconderPlanoPagamento()){
				debito = new EncontroInscricaoPagamentoDetalhe();
				debito.setEncontroInscricao(ei);
				debito.setTipoDetalhe(TipoPagamentoDetalheEnum.OUTRAINSCRICAO);
				debito.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				debito.setValor(encontroConvite.getEncontro().getValorAfilhado());
				debito.setQuantidade(1);
				debito.setEditavel(true);
				debito.setValorUnitario(encontroConvite.getEncontro().getValorAfilhado());
				vopadrinho.getListaPagamentoDetalhe().add(debito);
			}
		}
		else{
			vopadrinho.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
			vopadrinho.setListaPagamento(new ArrayList<EncontroInscricaoPagamento>());
			ei.setValorEncontro(encontroConvite.getEncontro().getValorPadrinho());
			if(ei.getValorEncontro().doubleValue()>0){
				eip = new EncontroInscricaoPagamentoDetalhe();
				eip.setEncontroInscricao(ei);
				eip.setDescricao("Valor do encontro");
				eip.setTipoDetalhe(TipoPagamentoDetalheEnum.AVULSO);
				eip.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				eip.setValor(ei.getValorEncontro());
				eip.setQuantidade(1);
				eip.setEditavel(true);
				eip.setValorUnitario(ei.getValorEncontro());
				vopadrinho.getListaPagamentoDetalhe().add(eip);
			}
			if(encontroConvite.getEsconderPlanoPagamento() && encontroConvite.getEncontro().getValorAfilhado().doubleValue()>0){
				ei.setValorEncontro(new BigDecimal(encontroConvite.getEncontro().getValorPadrinho().doubleValue()+encontroConvite.getEncontro().getValorAfilhado().doubleValue()));
				eip = new EncontroInscricaoPagamentoDetalhe();
				eip.setEncontroInscricao(ei);
				eip.setDescricao("Valor do afilhado");
				eip.setTipoDetalhe(TipoPagamentoDetalheEnum.AVULSO);
				eip.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				eip.setValor(encontroConvite.getEncontro().getValorAfilhado());
				eip.setQuantidade(1);
				eip.setEditavel(true);
				eip.setValorUnitario(encontroConvite.getEncontro().getValorAfilhado());
				vopadrinho.getListaPagamentoDetalhe().add(eip);
			}
		}

		EncontroInscricaoSalvarCommand cmd = inject.getInstance(EncontroInscricaoSalvarCommand.class);
		cmd.setUsuarioAtual(getUsuarioAtual());
		cmd.setEncontroInscricaoVO(vopadrinho);
		vopadrinho = cmd.call();


		//afilhado

		Casal casalConvidado = encontroConvite.getCasalConvidado();
		casalConvidado.setSituacao(TipoSituacaoEnum.ATIVO);
		casalConvidado.setGrupo(encontroConvite.getEncontro().getGrupo());
		casalConvidado = em.merge(casalConvidado);

		EncontroInscricaoVO voafilhado = getEncontroInscricaoVO(casalConvidado);
		if(vopadrinho==null){
			voafilhado = new EncontroInscricaoVO();
			ei = new EncontroInscricao();
			voafilhado.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
			voafilhado.setListaPagamento(new ArrayList<EncontroInscricaoPagamento>());
			voafilhado.setEncontroInscricao(ei);
		}else{
			ei = voafilhado.getEncontroInscricao();
		}

		ei.setEncontro(encontroConvite.getEncontro());
		ei.setTipo(TipoInscricaoEnum.AFILHADO);
		ei.setCasal(casalConvidado);
		ei.setEsconderPlanoPagamento(false);
		ei.setTipoConfirmacao(TipoConfirmacaoEnum.CONFIRMADO);

		if (encontroConvite.getEncontro().getUsaDetalheAutomatico().equals(1)){
			if(encontroConvite.getEsconderPlanoPagamento()){
				credito = new EncontroInscricaoPagamentoDetalhe();
				credito.setEncontroInscricao(ei);
				credito.setTipoDetalhe(TipoPagamentoDetalheEnum.OUTRAINSCRICAO);
				credito.setTipoLancamento(TipoPagamentoLancamentoEnum.CREDITO);
				credito.setValor(encontroConvite.getEncontro().getValorAfilhado());
				credito.setQuantidade(1);
				credito.setEditavel(true);
				credito.setEncontroInscricaoOutra(vopadrinho.getEncontroInscricao());
				credito.setValorUnitario(encontroConvite.getEncontro().getValorAfilhado());
				voafilhado.getListaPagamentoDetalhe().add(credito);
			}
		}else{
			if(encontroConvite.getEsconderPlanoPagamento()){
				ei.setValorEncontro(new BigDecimal(0));
			} else {
				ei.setValorEncontro(encontroConvite.getEncontro().getValorAfilhado());
			}
			if(ei.getValorEncontro().doubleValue()>0){
				eip = new EncontroInscricaoPagamentoDetalhe();
				eip.setEncontroInscricao(ei);
				eip.setDescricao("Valor do encontro");
				eip.setTipoDetalhe(TipoPagamentoDetalheEnum.AVULSO);
				eip.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				eip.setValor(ei.getValorEncontro());
				eip.setQuantidade(1);
				eip.setEditavel(true);
				eip.setValorUnitario(ei.getValorEncontro());
				voafilhado.getListaPagamentoDetalhe().add(eip);
			}
		}

		cmd.setUsuarioAtual(getUsuarioAtual());
		cmd.setEncontroInscricaoVO(voafilhado);
		voafilhado = cmd.call();
		if (voafilhado != null && vopadrinho != null && credito != null && debito != null){
			for (EncontroInscricaoPagamentoDetalhe detalhe : vopadrinho.getListaPagamentoDetalhe()) {
				if (detalhe.getTipoDetalhe().equals(debito.getTipoDetalhe()) &&
						detalhe.getTipoLancamento().equals(debito.getTipoLancamento()) &&
						detalhe.getEncontroInscricaoOutra() == null &&
						detalhe.getValor().equals(credito.getValor())){
					detalhe.setEncontroInscricaoOutra(voafilhado.getEncontroInscricao());
					em.merge(detalhe);
				}

			}
		}
	}

	@SuppressWarnings("unchecked")
	private EncontroInscricaoVO getEncontroInscricaoVO(Casal casal) throws Exception {
		Query q = em.createNamedQuery("encontroInscricao.porEncontroCasal");
		q.setParameter("encontro", encontroConvite.getEncontro());
		q.setParameter("casal", encontroConvite.getCasal());
		List<EncontroInscricao> result = q.getResultList();
		if (result.size()>0){
			EncontroInscricaoCarregaVOCommand cmd = inject.getInstance(EncontroInscricaoCarregaVOCommand.class);
			cmd.setEncontroInscricao(result.get(0));
			return cmd.call();
		}
		return null;
	}

	public EncontroConvite getEncontroConvite() {
		return encontroConvite;
	}
	public void setEncontroConvite(EncontroConvite encontroConvite) {
		this.encontroConvite = encontroConvite;
	}

	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}

	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}
}