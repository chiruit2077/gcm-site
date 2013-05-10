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
		boolean mover = encontroConvite.getMoverFinalFila();
		if (encontroConvite.getTipoResposta()==null) encontroConvite.setDataResposta(null);

		if (encontroConvite.getTipoConfirmacao()!=null && encontroConvite.getTipoConfirmacao().equals(TipoConfirmacaoEnum.CONFIRMADO)){
			geraInscricaoApoio();
			if( encontroConvite.getTipoResposta()!= null && encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.ACEITO)){
				encontroConvite.getCasalConvidado().setCasalPadrinho(encontroConvite.getCasal());
				encontroConvite.setCasalConvidado(em.merge(encontroConvite.getCasalConvidado()));
				geraInscricoesAceito();
			}
			else if(encontroConvite.getTipoResposta()!= null && encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.RECUSADO)){
				desisteInscricaoConvidado(encontroConvite.getCasalConvidado());
				desisteInscricaoEncontrista(encontroConvite.getCasalDoacao());
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

				/*q = em.createNamedQuery("encontroConvite.porEncontro");
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
				}*/
			}
		}else if (encontroConvite.getTipoConfirmacao()!=null && encontroConvite.getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
			if (!encontroConvite.getVaiComoApoio()) desisteInscricaoEncontrista(encontroConvite.getCasal());
			desisteInscricaoConvidado(encontroConvite.getCasalConvidado());
			desisteInscricaoEncontrista(encontroConvite.getCasalDoacao());
		}
		return em.merge(encontroConvite);
	}

	private void desisteInscricaoConvidado(Casal casal) throws Exception {
		EncontroInscricaoVO vo = getEncontroInscricaoVO(casal);
		if (vo!=null){
			vo.getEncontroInscricao().setTipoConfirmacao(TipoConfirmacaoEnum.DESISTENCIA);
			EncontroInscricaoSalvarCommand cmd = inject.getInstance(EncontroInscricaoSalvarCommand.class);
			cmd.setUsuarioAtual(getUsuarioAtual());
			cmd.setEncontroInscricaoVO(vo);
			cmd.call();
		}
	}

	private void desisteInscricaoEncontrista(Casal casal) throws Exception {
		EncontroInscricaoVO vo = getEncontroInscricaoVO(casal);
		if (vo!=null && vo.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.DOACAO)){
			vo.getEncontroInscricao().setTipoConfirmacao(TipoConfirmacaoEnum.DESISTENCIA);
			EncontroInscricaoSalvarCommand cmd = inject.getInstance(EncontroInscricaoSalvarCommand.class);
			cmd.setUsuarioAtual(getUsuarioAtual());
			cmd.setEncontroInscricaoVO(vo);
			cmd.call();
		}
	}

	@Transactional
	private void geraInscricoesAceito() throws Exception {
		if (encontroConvite.getCasal() != null && encontroConvite.getCasalConvidado() != null){
			EncontroInscricao inscricaopadrinho = null;
			EncontroInscricaoVO vodoacao = null;

			EncontroInscricaoVO vopadrinho = getEncontroInscricaoVO(encontroConvite.getCasal());
			if(vopadrinho==null){
				vopadrinho = new EncontroInscricaoVO();
				inscricaopadrinho = new EncontroInscricao();
				inscricaopadrinho.setHospedagemParticular(false);
				inscricaopadrinho.setGeraFicha(true);
				vopadrinho.setEncontroInscricao(inscricaopadrinho);
				vopadrinho.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
				vopadrinho.setListaPagamento(new ArrayList<EncontroInscricaoPagamento>());
			}else{
				inscricaopadrinho = vopadrinho.getEncontroInscricao();
			}
			inscricaopadrinho.setEncontro(encontroConvite.getEncontro());
			inscricaopadrinho.setTipo(TipoInscricaoEnum.PADRINHO);
			inscricaopadrinho.setCasal(encontroConvite.getCasal());
			inscricaopadrinho.setEsconderPlanoPagamento(false);
			inscricaopadrinho.setTipoConfirmacao(TipoConfirmacaoEnum.CONFIRMADO);

			if (!encontroConvite.getEncontro().getUsaDetalheAutomatico().equals(1)){
				inscricaopadrinho.setValorEncontro(encontroConvite.getEncontro().getValorPadrinho());
				if(inscricaopadrinho.getValorEncontro().doubleValue()>0){
					EncontroInscricaoPagamentoDetalhe detalhepadrinho = new EncontroInscricaoPagamentoDetalhe();
					detalhepadrinho.setEncontroInscricao(inscricaopadrinho);
					detalhepadrinho.setDescricao("Valor do encontro");
					detalhepadrinho.setTipoDetalhe(TipoPagamentoDetalheEnum.AVULSO);
					detalhepadrinho.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
					detalhepadrinho.setValor(inscricaopadrinho.getValorEncontro());
					detalhepadrinho.setQuantidade(1);
					detalhepadrinho.setEditavel(true);
					detalhepadrinho.setValorUnitario(inscricaopadrinho.getValorEncontro());
					vopadrinho.getListaPagamentoDetalhe().add(detalhepadrinho);
				}
				if(encontroConvite.getEsconderPlanoPagamento()){
					inscricaopadrinho.setValorEncontro(new BigDecimal(encontroConvite.getEncontro().getValorPadrinho().doubleValue()+encontroConvite.getEncontro().getValorAfilhado().doubleValue()));
					EncontroInscricaoPagamentoDetalhe detalhepadrinho = new EncontroInscricaoPagamentoDetalhe();
					detalhepadrinho.setEncontroInscricao(inscricaopadrinho);
					detalhepadrinho.setDescricao("Valor do afilhado");
					detalhepadrinho.setTipoDetalhe(TipoPagamentoDetalheEnum.AVULSO);
					detalhepadrinho.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
					detalhepadrinho.setValor(encontroConvite.getEncontro().getValorAfilhado());
					detalhepadrinho.setQuantidade(1);
					detalhepadrinho.setEditavel(true);
					detalhepadrinho.setValorUnitario(encontroConvite.getEncontro().getValorAfilhado());
					vopadrinho.getListaPagamentoDetalhe().add(detalhepadrinho);
				}
			}

			EncontroInscricaoSalvarCommand cmd = inject.getInstance(EncontroInscricaoSalvarCommand.class);
			cmd.setUsuarioAtual(getUsuarioAtual());
			cmd.setEncontroInscricaoVO(vopadrinho);
			vopadrinho = cmd.call();


			//afilhado

			EncontroInscricao inscricaoafilhado = null;

			Casal casalConvidado = encontroConvite.getCasalConvidado();
			casalConvidado.setSituacao(TipoSituacaoEnum.ATIVO);
			casalConvidado.setGrupo(encontroConvite.getEncontro().getGrupo());
			casalConvidado = em.merge(casalConvidado);

			EncontroInscricaoVO voafilhado = getEncontroInscricaoVO(casalConvidado);
			if(voafilhado==null){
				voafilhado = new EncontroInscricaoVO();
				inscricaoafilhado = new EncontroInscricao();
				inscricaoafilhado.setHospedagemParticular(false);
				inscricaoafilhado.setGeraFicha(true);
				voafilhado.setEncontroInscricao(inscricaoafilhado);
				voafilhado.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
				voafilhado.setListaPagamento(new ArrayList<EncontroInscricaoPagamento>());
			}else{
				inscricaoafilhado = voafilhado.getEncontroInscricao();
			}

			inscricaoafilhado.setEncontro(encontroConvite.getEncontro());
			inscricaoafilhado.setTipo(TipoInscricaoEnum.AFILHADO);
			inscricaoafilhado.setCasal(casalConvidado);
			inscricaoafilhado.setEsconderPlanoPagamento(false);
			inscricaoafilhado.setTipoConfirmacao(TipoConfirmacaoEnum.CONFIRMADO);

			if (encontroConvite.getEncontro().getUsaDetalheAutomatico().equals(1)){
				if(encontroConvite.getEsconderPlanoPagamento() && encontroConvite.getValorAfilhadoPodePagar()==null){
					if (encontroConvite.getValorDoacao()!=null && encontroConvite.getValorDoacao().doubleValue()> 0 && encontroConvite.getValorDoacao().doubleValue() < encontroConvite.getEncontro().getValorAfilhado().doubleValue()){
						encontroConvite.setEsconderPlanoPagamento(false);
					}else{
						encontroConvite.setValorDoacao(encontroConvite.getEncontro().getValorAfilhado());
					}
				}
				else if(encontroConvite.getValorAfilhadoPodePagar()!=null) {
					encontroConvite.setValorDoacao(new BigDecimal(encontroConvite.getEncontro().getValorAfilhado().doubleValue() - encontroConvite.getValorAfilhadoPodePagar().doubleValue()));
				}
				if(encontroConvite.getValorDoacao()!=null && encontroConvite.getValorDoacao().doubleValue()>0){
					if (encontroConvite.getValorDoacao().doubleValue() > encontroConvite.getEncontro().getValorAfilhado().doubleValue())
						encontroConvite.setValorDoacao(encontroConvite.getEncontro().getValorAfilhado());
					vodoacao = null;
					EncontroInscricao inscricaodoacao = null;
					if (encontroConvite.getCasalDoacao()!=null){
						if (encontroConvite.getCasal().equals(encontroConvite.getCasalDoacao())) vodoacao = vopadrinho;
						else vodoacao = getEncontroInscricaoVO(encontroConvite.getCasalDoacao());
						if (vodoacao == null){
							vodoacao = new EncontroInscricaoVO();
							inscricaodoacao = new EncontroInscricao();
							inscricaodoacao.setEsconderPlanoPagamento(false);
							inscricaodoacao.setHospedagemParticular(false);
							inscricaodoacao.setGeraFicha(false);
							inscricaodoacao.setCasal(encontroConvite.getCasalDoacao());
							inscricaodoacao.setTipo(TipoInscricaoEnum.DOACAO);
							inscricaodoacao.setTipoConfirmacao(TipoConfirmacaoEnum.CONFIRMADO);
							inscricaodoacao.setEncontro(encontroConvite.getEncontro());
							vodoacao.setEncontroInscricao(inscricaodoacao);
							vodoacao.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
							vodoacao.setListaPagamento(new ArrayList<EncontroInscricaoPagamento>());
							cmd = inject.getInstance(EncontroInscricaoSalvarCommand.class);
							cmd.setUsuarioAtual(getUsuarioAtual());
							cmd.setEncontroInscricaoVO(vodoacao);
							vodoacao = cmd.call();
						}
						inscricaodoacao = vodoacao.getEncontroInscricao();
					}

					EncontroInscricaoPagamentoDetalhe credito=null;
					for (EncontroInscricaoPagamentoDetalhe detalhe : voafilhado.getListaPagamentoDetalhe()) {
						if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO) &&
								detalhe.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.CREDITO) &&
								( detalhe.getEncontroInscricaoOutra() == null || detalhe.getEncontroInscricaoOutra().equals(inscricaodoacao))){
							credito = detalhe;
						}
					}
					if (credito==null){
						credito = new EncontroInscricaoPagamentoDetalhe();
						credito.setEncontroInscricao(voafilhado.getEncontroInscricao());
						credito.setTipoDetalhe(TipoPagamentoDetalheEnum.OUTRAINSCRICAO);
						credito.setTipoLancamento(TipoPagamentoLancamentoEnum.CREDITO);
						credito.setEditavel(false);
						voafilhado.getListaPagamentoDetalhe().add(credito);
					}
					if (inscricaodoacao==null) credito.setEditavel(true);
					credito.setEncontroInscricaoOutra(inscricaodoacao);
					credito.setValorUnitario(encontroConvite.getValorDoacao());
					credito.setValor(encontroConvite.getValorDoacao());
					credito.setQuantidade(1);
				}
			}else{
				if(encontroConvite.getEsconderPlanoPagamento()){
					inscricaoafilhado.setValorEncontro(new BigDecimal(0));
				} else if(encontroConvite.getValorAfilhadoPodePagar()!=null) {
					inscricaoafilhado.setValorEncontro(encontroConvite.getValorAfilhadoPodePagar());
				}else{
					inscricaoafilhado.setValorEncontro(encontroConvite.getEncontro().getValorAfilhado());
				}
				if(inscricaoafilhado.getValorEncontro().doubleValue()>0){
					EncontroInscricaoPagamentoDetalhe detalheafilhado = new EncontroInscricaoPagamentoDetalhe();
					detalheafilhado.setEncontroInscricao(inscricaoafilhado);
					detalheafilhado.setDescricao("Valor do encontro");
					detalheafilhado.setTipoDetalhe(TipoPagamentoDetalheEnum.AVULSO);
					detalheafilhado.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
					detalheafilhado.setValor(inscricaoafilhado.getValorEncontro());
					detalheafilhado.setQuantidade(1);
					detalheafilhado.setEditavel(true);
					detalheafilhado.setValorUnitario(inscricaoafilhado.getValorEncontro());
					voafilhado.getListaPagamentoDetalhe().add(detalheafilhado);
				}
			}
			cmd = inject.getInstance(EncontroInscricaoSalvarCommand.class);
			cmd.setUsuarioAtual(getUsuarioAtual());
			cmd.setEncontroInscricaoVO(voafilhado);
			voafilhado = cmd.call();

			if (vodoacao!=null){
				EncontroInscricaoPagamentoDetalhe debito = null;
				for (EncontroInscricaoPagamentoDetalhe detalhe : vodoacao.getListaPagamentoDetalhe()) {
					if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO) &&
							detalhe.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.DEBITO) &&
							( detalhe.getEncontroInscricaoOutra() == null || detalhe.getEncontroInscricaoOutra().equals(voafilhado.getEncontroInscricao()))){
						debito = detalhe;
					}
				}
				if (debito==null){
					debito = new EncontroInscricaoPagamentoDetalhe();
					debito.setEncontroInscricao(vodoacao.getEncontroInscricao());
					debito.setTipoDetalhe(TipoPagamentoDetalheEnum.OUTRAINSCRICAO);
					debito.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
					debito.setEditavel(true);
					vodoacao.getListaPagamentoDetalhe().add(debito);
				}
				debito.setEncontroInscricaoOutra(voafilhado.getEncontroInscricao());
				debito.setQuantidade(1);
				debito.setValorUnitario(encontroConvite.getValorDoacao());
				debito.setValor(encontroConvite.getValorDoacao());
				cmd = inject.getInstance(EncontroInscricaoSalvarCommand.class);
				cmd.setUsuarioAtual(getUsuarioAtual());
				cmd.setEncontroInscricaoVO(vodoacao);
				vodoacao = cmd.call();
			}
		}
	}

	@Transactional
	private void geraInscricaoApoio() throws Exception {
		if (encontroConvite.getCasal()!=null){
			EncontroInscricao inscricao = null;

			EncontroInscricaoVO vo = getEncontroInscricaoVO(encontroConvite.getCasal());
			if(vo==null){
				vo = new EncontroInscricaoVO();
				inscricao = new EncontroInscricao();
				inscricao.setHospedagemParticular(false);
				inscricao.setGeraFicha(true);
				vo.setEncontroInscricao(inscricao);
				vo.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
				vo.setListaPagamento(new ArrayList<EncontroInscricaoPagamento>());
				inscricao.setEncontro(encontroConvite.getEncontro());
				inscricao.setTipo(TipoInscricaoEnum.APOIO);
				inscricao.setCasal(encontroConvite.getCasal());
				inscricao.setEsconderPlanoPagamento(false);
				inscricao.setTipoConfirmacao(TipoConfirmacaoEnum.CONFIRMADO);

				if (!encontroConvite.getEncontro().getUsaDetalheAutomatico().equals(1)){
					inscricao.setValorEncontro(encontroConvite.getEncontro().getValorPadrinho());
					if(inscricao.getValorEncontro().doubleValue()>0){
						EncontroInscricaoPagamentoDetalhe detalhe = new EncontroInscricaoPagamentoDetalhe();
						detalhe.setEncontroInscricao(inscricao);
						detalhe.setDescricao("Valor do encontro");
						detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.AVULSO);
						detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
						detalhe.setValor(inscricao.getValorEncontro());
						detalhe.setQuantidade(1);
						detalhe.setEditavel(true);
						detalhe.setValorUnitario(inscricao.getValorEncontro());
						vo.getListaPagamentoDetalhe().add(detalhe);
					}
				}

				EncontroInscricaoSalvarCommand cmd = inject.getInstance(EncontroInscricaoSalvarCommand.class);
				cmd.setUsuarioAtual(getUsuarioAtual());
				cmd.setEncontroInscricaoVO(vo);
				vo = cmd.call();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private EncontroInscricaoVO getEncontroInscricaoVO(Casal casal) throws Exception {
		Query q = em.createNamedQuery("encontroInscricao.porEncontroCasal");
		q.setParameter("encontro", encontroConvite.getEncontro());
		q.setParameter("casal", casal);
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