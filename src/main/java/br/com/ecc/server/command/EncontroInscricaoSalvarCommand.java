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
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EncontroInscricaoSalvarCommand implements Callable<EncontroInscricaoVO>{

	@Inject EntityManager em;
	private EncontroInscricaoVO encontroInscricaoVO;
	private Usuario usuarioAtual;

	@Override
	@Transactional
	public EncontroInscricaoVO call() throws Exception {
		Query q;
		//verificações
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
			if(encontroInscricaoVO.getEncontroInscricao().getValorEncontro()!=null ||
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
				encontroInscricaoVO.getEncontroInscricao().setDataPrenchimentoFicha(new Date());
			}
		} else {
			if (usuarioAtual.getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
				if (encontroInscricaoVO.getEncontroInscricao().getMensagemDestinatario() != null &&
					encontroInscricaoVO.getEncontroInscricao().getMensagemDestinatario().getDataEnvio() != null &&
					encontroInscricaoVO.getEncontroInscricao().getDataPrenchimentoFicha()==null &&
					encontroInscricaoVO.getEncontroInscricao().getCasal().getAtualizacaoCadastro() != null &&
					encontroInscricaoVO.getEncontroInscricao().getCasal().getAtualizacaoCadastro().after(getEncontroInscricaoVO().getEncontroInscricao().getMensagemDestinatario().getDataEnvio())){
					encontroInscricaoVO.getEncontroInscricao().setDataPrenchimentoFicha(getEncontroInscricaoVO().getEncontroInscricao().getCasal().getAtualizacaoCadastro());
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
				EncontroInscricaoFichaPagamento vaga = new EncontroInscricaoFichaPagamento();
				ficha.setEncontroInscricao(null);
				vaga.setFicha(ficha.getFicha());
				vaga.setEncontro(ficha.getEncontro());
				vaga.setStatus(TipoInscricaoFichaStatusEnum.NORMAL);
				vaga.setTipo(ficha.getTipo());
				vaga = em.merge(vaga);
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
			ficha = encontroInscricaoVO.getEncontroInscricao().getFichaPagamento();
			if (ficha==null && !encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.EXTERNO)){
				if (encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.AFILHADO) ){
					ficha = getFichaVaga(TipoInscricaoCasalEnum.AFILHADO,encontroInscricaoVO.getEncontroInscricao().getEncontro());
				}else{
					ficha = getFichaVaga(TipoInscricaoCasalEnum.ENCONTRISTA,encontroInscricaoVO.getEncontroInscricao().getEncontro());
				}
			}
			if (ficha!=null){
				encontroInscricaoVO.getEncontroInscricao().setFichaPagamento(ficha);
				encontroInscricaoVO.getEncontroInscricao().setCodigo(ficha.getFicha());
			}else{
				encontroInscricaoVO.getEncontroInscricao().setFichaPagamento(null);
				encontroInscricaoVO.getEncontroInscricao().setCodigo(null);
			}
		}
		encontroInscricaoVO.setEncontroInscricao(em.merge(encontroInscricaoVO.getEncontroInscricao()));
		if (ficha!=null){
			if(!encontroInscricaoVO.getEncontroInscricao().getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA))
				ficha.setEncontroInscricao(encontroInscricaoVO.getEncontroInscricao());
			ficha = em.merge(ficha);
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

		//detalhe
		List<EncontroInscricaoPagamentoDetalhe> novaListaDetalhe = new ArrayList<EncontroInscricaoPagamentoDetalhe>();
		if(encontroInscricaoVO.getListaPagamentoDetalhe()!=null){
			for (EncontroInscricaoPagamentoDetalhe eip : encontroInscricaoVO.getListaPagamentoDetalhe()) {
				if(eip.getId()!=null){
					novaListaDetalhe.add(eip);
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
			}
		}

		return encontroInscricaoVO;
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