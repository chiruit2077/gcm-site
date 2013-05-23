package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.EncontroInscricaoPagamentoDetalhe;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoConfirmacaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.tipo.TipoPagamentoDetalheEnum;
import br.com.ecc.model.tipo.TipoPagamentoLancamentoEnum;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroInscricaoSalvarCommand implements Callable<EncontroInscricaoVO>{

	@Inject EntityManager em;
	@Inject Injector injector;
	private EncontroInscricaoVO encontroInscricaoVO;
	private Usuario usuarioAtual;

	@Override
	@Transactional
	public EncontroInscricaoVO call() throws Exception {
		Query q;
		//verificações
		if(encontroInscricaoVO.getEncontroInscricao().getId()==null)
			encontroInscricaoVO.setEncontroInscricao(em.merge(encontroInscricaoVO.getEncontroInscricao()));
		boolean atualiza = false;
		if(encontroInscricaoVO.getEncontroInscricao().getCasal()!=null){
			if(usuarioAtual.getPessoa().getId().equals(encontroInscricaoVO.getEncontroInscricao().getCasal().getEle().getId()) ||
					usuarioAtual.getPessoa().getId().equals(encontroInscricaoVO.getEncontroInscricao().getCasal().getEla().getId())){
				atualiza = true;
			}
		} else if(encontroInscricaoVO.getEncontroInscricao().getPessoa()!=null){
			if(usuarioAtual.getPessoa().getId().equals(encontroInscricaoVO.getEncontroInscricao().getPessoa().getId())){
				atualiza = true;
			}
		}

		if(atualiza){
/*			if(encontroInscricaoVO.getEncontroInscricao().getValorEncontro()!=null ||
			   (encontroInscricaoVO.getEncontroInscricao().getEsconderPlanoPagamento()!=null &&
			    !encontroInscricaoVO.getEncontroInscricao().getEsconderPlanoPagamento())){
				atualiza = false;
				for (EncontroInscricaoPagamento pagamento : encontroInscricaoVO.getListaPagamento()) {
					if(!pagamento.getParcela().equals(0)){
						atualiza = true;
						break;
					}
				}
			}
			if(atualiza){
			}
*/			encontroInscricaoVO.getEncontroInscricao().setDataPrenchimentoFicha(new Date());
		} else {
			if (usuarioAtual.getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) && encontroInscricaoVO.getEncontroInscricao().getId()!=null){
				if (encontroInscricaoVO.getEncontroInscricao().getCasal() != null &&
					encontroInscricaoVO.getEncontroInscricao().getMensagemDestinatario() != null &&
					encontroInscricaoVO.getEncontroInscricao().getMensagemDestinatario().getDataEnvio() != null &&
					encontroInscricaoVO.getEncontroInscricao().getDataPrenchimentoFicha()==null &&
					encontroInscricaoVO.getEncontroInscricao().getCasal().getAtualizacaoCadastro() != null &&
					encontroInscricaoVO.getEncontroInscricao().getCasal().getAtualizacaoCadastro().after(getEncontroInscricaoVO().getEncontroInscricao().getMensagemDestinatario().getDataEnvio())){
					encontroInscricaoVO.getEncontroInscricao().setDataPrenchimentoFicha(getEncontroInscricaoVO().getEncontroInscricao().getCasal().getAtualizacaoCadastro());
				}
				if (encontroInscricaoVO.getEncontroInscricao().getPessoa() != null &&
						encontroInscricaoVO.getEncontroInscricao().getMensagemDestinatario() != null &&
						encontroInscricaoVO.getEncontroInscricao().getMensagemDestinatario().getDataEnvio() != null &&
						encontroInscricaoVO.getEncontroInscricao().getDataPrenchimentoFicha()==null &&
						encontroInscricaoVO.getEncontroInscricao().getPessoa().getDataAtualizacao() != null &&
						encontroInscricaoVO.getEncontroInscricao().getPessoa().getDataAtualizacao().after(getEncontroInscricaoVO().getEncontroInscricao().getMensagemDestinatario().getDataEnvio())){
						encontroInscricaoVO.getEncontroInscricao().setDataPrenchimentoFicha(getEncontroInscricaoVO().getEncontroInscricao().getPessoa().getDataAtualizacao());
					}
				if (encontroInscricaoVO.getMarcaFichaPreenchida() && encontroInscricaoVO.getEncontroInscricao().getDataPrenchimentoFicha()==null)
					encontroInscricaoVO.getEncontroInscricao().setDataPrenchimentoFicha(new Date());
			}
		}

		EncontroInscricaoFichaPagamento ficha = null;

		if(encontroInscricaoVO.getEncontroInscricao().getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
			if (encontroInscricaoVO.getEncontroInscricao().getFichaPagamento()!=null){
				ficha = encontroInscricaoVO.getEncontroInscricao().getFichaPagamento();
				ficha.setStatus(TipoInscricaoFichaStatusEnum.LIBERADO);
				ficha.setObservacao(TipoConfirmacaoEnum.DESISTENCIA.getNome());
				if (!exiteFichaVaga(ficha.getFicha(), encontroInscricaoVO.getEncontroInscricao().getEncontro())){
					EncontroInscricaoFichaPagamento vaga = new EncontroInscricaoFichaPagamento();
					vaga.setFicha(ficha.getFicha());
					vaga.setEncontro(ficha.getEncontro());
					vaga.setStatus(TipoInscricaoFichaStatusEnum.NORMAL);
					vaga.setTipo(ficha.getTipo());
					vaga = em.merge(vaga);
				}
				ficha.setEncontroInscricao(encontroInscricaoVO.getEncontroInscricao());
				encontroInscricaoVO.getEncontroInscricao().setFichaPagamento(em.merge(ficha));
			}
			q = em.createNamedQuery("encontroAtividadeInscricao.deletePorEncontroInscricao");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroHotelQuarto.updatePorEncontroInscricao1");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroHotelQuarto.updatePorEncontroInscricao2");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroHotelQuarto.updatePorEncontroInscricao3");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroHotelQuarto.updatePorEncontroInscricao4");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroRestauranteMesa.updatePorEncontroGarcon");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroRestauranteMesa.updatePorEncontroAfilhado1");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroRestauranteMesa.updatePorEncontroAfilhado2");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroOrganogramaCoordenacao.updatePorEncontroInscricao1");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroOrganogramaCoordenacao.updatePorEncontroInscricao2");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroOrganogramaArea.updatePorEncontroInscricao1");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
			q = em.createNamedQuery("encontroOrganogramaArea.updatePorEncontroInscricao2");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
		}else{
			if (encontroInscricaoVO.getEncontroInscricao().getEncontro().getUsaFichaPagamento().equals(1) && encontroInscricaoVO.getEncontroInscricao().getGeraFicha()){
				ficha = encontroInscricaoVO.getEncontroInscricao().getFichaPagamento();
				if (encontroInscricaoVO.getEncontroInscricao().getValorEncontro().doubleValue() > 0 &&
						(ficha==null || ficha.getStatus().equals(TipoInscricaoFichaStatusEnum.LIBERADO)) &&
						!encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.EXTERNO)){
					if (encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.AFILHADO) ){
						ficha = getFichaVaga(TipoInscricaoCasalEnum.AFILHADO,encontroInscricaoVO.getEncontroInscricao().getEncontro());
					}else{
						ficha = getFichaVaga(TipoInscricaoCasalEnum.ENCONTRISTA,encontroInscricaoVO.getEncontroInscricao().getEncontro());
					}
					encontroInscricaoVO.getEncontroInscricao().setFichaPagamento(ficha);
					encontroInscricaoVO.getEncontroInscricao().setCodigo(ficha.getFicha());
					if (encontroInscricaoVO.getEncontroInscricao().getEncontro().getUsaDetalheAutomatico().equals(1)){
						encontroInscricaoVO.defineParcelasPosiveis();
						if (encontroInscricaoVO.getListaPagamento().size()>0)
							encontroInscricaoVO.setQtdeparcelas(encontroInscricaoVO.getListaPagamento().size());
						else
							encontroInscricaoVO.setQtdeparcelas(encontroInscricaoVO.getMaxParcela());
						encontroInscricaoVO.geraParcelas();
					}
				}
				if (ficha!=null){
					encontroInscricaoVO.getEncontroInscricao().setCodigo(ficha.getFicha());
					if (encontroInscricaoVO.getEncontroInscricao().getValorEncontro().doubleValue()==0){
						ficha = encontroInscricaoVO.getEncontroInscricao().getFichaPagamento();
						ficha.setStatus(TipoInscricaoFichaStatusEnum.LIBERADO);
						ficha.setObservacao("SEM VALOR A PAGAR");
						if (!exiteFichaVaga(ficha.getFicha(),encontroInscricaoVO.getEncontroInscricao().getEncontro())){
							EncontroInscricaoFichaPagamento vaga = new EncontroInscricaoFichaPagamento();
							vaga.setFicha(ficha.getFicha());
							vaga.setEncontro(ficha.getEncontro());
							vaga.setStatus(TipoInscricaoFichaStatusEnum.NORMAL);
							vaga.setTipo(ficha.getTipo());
							vaga = em.merge(vaga);
						}
						if (ficha!=null){
						}
					}
					ficha.setEncontroInscricao(encontroInscricaoVO.getEncontroInscricao());
					encontroInscricaoVO.getEncontroInscricao().setFichaPagamento(em.merge(ficha));

				}else{
					encontroInscricaoVO.getEncontroInscricao().setCodigo(null);
				}
			}
		}
		encontroInscricaoVO.getEncontroInscricao().setEsconderPlanoPagamento(encontroInscricaoVO.getEncontroInscricao().getValorEncontro().doubleValue()==0);
		if (encontroInscricaoVO.getEncontroInscricao().getEncontro().getUsaDetalheAutomatico().equals(1) ){
			if (!encontroInscricaoVO.getMantemValores() && encontroInscricaoVO.isAtualizaValores()) {
				encontroInscricaoVO.geraPagamentoDetalhe();
				encontroInscricaoVO.defineParcelasPosiveis();
				encontroInscricaoVO.geraParcelas();
			}
			removeCreditosExistentes(encontroInscricaoVO.getListaPagamentoDetalhe());
		}
		encontroInscricaoVO.somaValorEncontro();
		encontroInscricaoVO.setEncontroInscricao(em.merge(encontroInscricaoVO.getEncontroInscricao()));

		List<EncontroInscricaoPagamentoDetalhe> novaListaDetalhe = new ArrayList<EncontroInscricaoPagamentoDetalhe>();
		if(encontroInscricaoVO.getListaPagamentoDetalhe()!=null){
			for (EncontroInscricaoPagamentoDetalhe eid : encontroInscricaoVO.getListaPagamentoDetalhe()) {
				if(eid.getId()!=null){
					novaListaDetalhe.add(eid);
				}
			}
		}

		if(novaListaDetalhe.size()>0){
			q = em.createNamedQuery("encontroInscricaoPagamentoDetalhe.deletePorEncontroInscricaoNotIn");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.setParameter("lista", novaListaDetalhe);
			q.executeUpdate();
		} else {
			q = em.createNamedQuery("encontroInscricaoPagamentoDetalhe.deletePorEncontroInscricao");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
		}
		if(encontroInscricaoVO.getListaPagamentoDetalhe()!=null){
			EncontroInscricaoPagamentoDetalhe eip;
			for (int i = 0; i < encontroInscricaoVO.getListaPagamentoDetalhe().size(); i++) {
				eip = encontroInscricaoVO.getListaPagamentoDetalhe().get(i);
				eip.setEncontroInscricao(encontroInscricaoVO.getEncontroInscricao());
				encontroInscricaoVO.getListaPagamentoDetalhe().set(i, em.merge(eip));
				eip = encontroInscricaoVO.getListaPagamentoDetalhe().get(i);
				if (encontroInscricaoVO.getEncontroInscricao().getEncontro().getUsaDetalheAutomatico().equals(1) &&
					eip.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO) &&
					eip.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.DEBITO) &&
					eip.getEncontroInscricaoOutra() != null){
					EncontroInscricaoCarregaVOCommand cmd = injector.getInstance(EncontroInscricaoCarregaVOCommand.class);
					cmd.setEncontroInscricao(eip.getEncontroInscricaoOutra());
					EncontroInscricaoVO inscricaoVO = cmd.call();
					if (inscricaoVO != null){
						EncontroInscricaoPagamentoDetalhe detalheCredito = null;
						for (EncontroInscricaoPagamentoDetalhe detalhe : inscricaoVO.getListaPagamentoDetalhe()) {
							if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO) &&
									detalhe.getEncontroInscricaoOutra().equals(encontroInscricaoVO.getEncontroInscricao()) &&
									detalhe.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.CREDITO)){
								detalheCredito = detalhe;
							}
						}
						if (detalheCredito == null ) {
							detalheCredito = new EncontroInscricaoPagamentoDetalhe();
							inscricaoVO.getListaPagamentoDetalhe().add(detalheCredito);
						}
						detalheCredito.setEditavel(false);
						detalheCredito.setQuantidade(eip.getQuantidade());
						detalheCredito.setValorUnitario(eip.getValorUnitario());
						detalheCredito.setValor(eip.getValor());
						detalheCredito.setEncontroInscricao(inscricaoVO.getEncontroInscricao());
						detalheCredito.setEncontroInscricaoOutra(encontroInscricaoVO.getEncontroInscricao());
						detalheCredito.setTipoDetalhe(TipoPagamentoDetalheEnum.OUTRAINSCRICAO);
						detalheCredito.setTipoLancamento(TipoPagamentoLancamentoEnum.CREDITO);
						inscricaoVO.somaValorEncontro();
						inscricaoVO.defineParcelasPosiveis();
						int parcelas = inscricaoVO.getListaPagamento().size();
						inscricaoVO.defineParcelasPosiveis();
						if (parcelas == 0) {
							parcelas = inscricaoVO.getMaxParcela();
						}
						inscricaoVO.setQtdeparcelas(parcelas);
						inscricaoVO.geraParcelas();
						EncontroInscricaoSalvarCommand cmdOutra = injector.getInstance(EncontroInscricaoSalvarCommand.class);
						cmdOutra.setEncontroInscricaoVO(inscricaoVO);
						cmdOutra.setUsuarioAtual(getUsuarioAtual());
						cmdOutra.call();
					}
				}
			}
		}

		//pagamentos
		List<EncontroInscricaoPagamento> novaLista = new ArrayList<EncontroInscricaoPagamento>();
		if(encontroInscricaoVO.getListaPagamento()!=null){
			for (EncontroInscricaoPagamento eip : encontroInscricaoVO.getListaPagamento()) {
				if(eip.getId()!=null){
					novaLista.add(eip);
				}
			}
		}
		if(novaLista.size()>0){
			q = em.createNamedQuery("encontroInscricaoPagamento.deletePorEncontroInscricaoNotIn");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.setParameter("lista", novaLista);
			q.executeUpdate();
		} else {
			q = em.createNamedQuery("encontroInscricaoPagamento.deletePorEncontroInscricao");
			q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
			q.executeUpdate();
		}
		if(encontroInscricaoVO.getListaPagamento()!=null){
			EncontroInscricaoPagamento eip;
			for (int i = 0; i < encontroInscricaoVO.getListaPagamento().size(); i++) {
				eip = encontroInscricaoVO.getListaPagamento().get(i);
				eip.setEncontroInscricao(encontroInscricaoVO.getEncontroInscricao());
				encontroInscricaoVO.getListaPagamento().set(i, em.merge(eip));
			}
		}
		return encontroInscricaoVO;
	}

	@SuppressWarnings("unchecked")
	private void removeCreditosExistentes(List<EncontroInscricaoPagamentoDetalhe> detalhes) throws Exception {
		Query q = em.createNamedQuery("encontroInscricaoPagamentoDetalhe.porEncontroInscricaoOutraCredito");
		q.setParameter("encontroInscricao", encontroInscricaoVO.getEncontroInscricao());
		List<EncontroInscricaoPagamentoDetalhe> listCredito = q.getResultList();
		for (EncontroInscricaoPagamentoDetalhe credito : listCredito) {
			EncontroInscricaoPagamentoDetalhe creditoEncontrado = credito;
			for (EncontroInscricaoPagamentoDetalhe debito : detalhes) {
				if (debito.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO) &&
						debito.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.DEBITO) &&
						credito.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.CREDITO) &&
						debito.getEncontroInscricaoOutra().equals(credito.getEncontroInscricao()) &&
						debito.getEncontroInscricao().equals(credito.getEncontroInscricaoOutra())){
					creditoEncontrado = null;
				}
			}
			if (creditoEncontrado!=null){
				EncontroInscricaoCarregaVOCommand cmd = injector.getInstance(EncontroInscricaoCarregaVOCommand.class);
				cmd.setEncontroInscricao(credito.getEncontroInscricao());
				EncontroInscricaoVO inscricaoVO = cmd.call();
				if (inscricaoVO != null){
					inscricaoVO.getListaPagamentoDetalhe().remove(credito);
					inscricaoVO.somaValorEncontro();
					int parcelas = inscricaoVO.getListaPagamento().size();
					inscricaoVO.defineParcelasPosiveis();
					if (parcelas == 0) {
						parcelas = inscricaoVO.getMaxParcela();
					}
					inscricaoVO.setQtdeparcelas(parcelas);
					inscricaoVO.geraParcelas();
					EncontroInscricaoSalvarCommand cmdOutra = injector.getInstance(EncontroInscricaoSalvarCommand.class);
					cmdOutra.setEncontroInscricaoVO(inscricaoVO);
					cmdOutra.setUsuarioAtual(getUsuarioAtual());
					cmdOutra.call();
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private EncontroInscricaoFichaPagamento getFichaVaga(
			TipoInscricaoCasalEnum tipo, Encontro encontro) {
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

	@SuppressWarnings("unchecked")
	private boolean exiteFichaVaga(Integer codigo, Encontro encontro) {
		if (encontro.getUsaFichaPagamento()!= null && encontro.getUsaFichaPagamento().equals(1)){
			Query q = em.createNamedQuery("encontroInscricaoFichaPagamento.porVagalLivre");
			q.setParameter("encontro", encontro);
			q.setParameter("tipo", TipoInscricaoCasalEnum.AFILHADO);
			q.setParameter("status", TipoInscricaoFichaStatusEnum.NORMAL);
			List<EncontroInscricaoFichaPagamento> fichas = q.getResultList();
			if (fichas.size()>0)
				return true;
			q = em.createNamedQuery("encontroInscricaoFichaPagamento.porVagalLivre");
			q.setParameter("encontro", encontro);
			q.setParameter("tipo", TipoInscricaoCasalEnum.ENCONTRISTA);
			q.setParameter("status", TipoInscricaoFichaStatusEnum.NORMAL);
			fichas = q.getResultList();
			if (fichas.size()>0)
				return true;
		}
		return false;
	}

	public EncontroInscricaoVO getEncontroInscricaoVO() {
		return encontroInscricaoVO;
	}
	public void setEncontroInscricaoVO(EncontroInscricaoVO encontroInscricaoVO) {
		this.encontroInscricaoVO = encontroInscricaoVO;
	}
	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}
	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}
}