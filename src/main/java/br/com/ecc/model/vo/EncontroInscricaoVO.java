package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.EncontroInscricaoPagamentoDetalhe;

public class EncontroInscricaoVO implements Serializable {
	private static final long serialVersionUID = 2042741114587479558L;

	private EncontroInscricao encontroInscricao;
	private List<EncontroInscricaoPagamento> listaPagamento;
	private List<EncontroInscricaoPagamentoDetalhe> listaPagamentoDetalhe;
	private Boolean marcaFichaPreenchida;

	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}
	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
	public List<EncontroInscricaoPagamento> getListaPagamento() {
		return listaPagamento;
	}
	public void setListaPagamento(List<EncontroInscricaoPagamento> listaPagamento) {
		this.listaPagamento = listaPagamento;
	}
	public List<EncontroInscricaoPagamentoDetalhe> getListaPagamentoDetalhe() {
		return listaPagamentoDetalhe;
	}
	public void setListaPagamentoDetalhe(List<EncontroInscricaoPagamentoDetalhe> listaPagamentoDetalhe) {
		this.listaPagamentoDetalhe = listaPagamentoDetalhe;
	}
	public Boolean getMarcaFichaPreenchida() {
		return marcaFichaPreenchida;
	}
	public void setMarcaFichaPreenchida(Boolean marcaFichaPreenchida) {
		this.marcaFichaPreenchida = marcaFichaPreenchida;
	}
}