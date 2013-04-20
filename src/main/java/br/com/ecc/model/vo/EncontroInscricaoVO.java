package br.com.ecc.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.EncontroInscricaoPagamentoDetalhe;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoPagamentoDetalheEnum;
import br.com.ecc.model.tipo.TipoPagamentoLancamentoEnum;

public class EncontroInscricaoVO implements Serializable {
	private static final long serialVersionUID = 2042741114587479558L;

	private EncontroInscricao encontroInscricao;
	private List<EncontroInscricaoPagamento> listaPagamento;
	private List<EncontroInscricaoPagamentoDetalhe> listaPagamentoDetalhe;
	private Boolean marcaFichaPreenchida = false;
	private Integer maxParcela;
	private int qtdeparcelas;

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

	public void somaValorEncontro(){
		double valor = 0;
		for (EncontroInscricaoPagamentoDetalhe detalhe: getListaPagamentoDetalhe()) {
			if(detalhe.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.DEBITO))
				valor += detalhe.getValor().doubleValue();
			else
				valor -= detalhe.getValor().doubleValue();
		}
		getEncontroInscricao().setValorEncontro(new BigDecimal(valor));
	}

	public double getValorEncontro(EncontroInscricao inscricaoOrigem){
		double valor = 0;
		for (EncontroInscricaoPagamentoDetalhe detalhe: getListaPagamentoDetalhe()) {
			if(detalhe.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.DEBITO))
				valor += detalhe.getValor().doubleValue();
			else if (detalhe.getEncontroInscricaoOutra()==null || ( detalhe.getEncontroInscricaoOutra().equals(inscricaoOrigem))){
				valor -= detalhe.getValor().doubleValue();
			}
		}
		return valor;
	}

	@SuppressWarnings("deprecation")
	public void geraParcelas() {
		int daybase = getEncontroInscricao().getEncontro().getDataMaximaPagamento().getDate();
		Date hoje = new Date();
		double centavos = 0;
		if(getEncontroInscricao().getCodigo()!=null){
			centavos = getEncontroInscricao().getCodigo().intValue();
		}

		EncontroInscricaoPagamento parcelaInscricao = null;

		double valorPago = 0;
		List<EncontroInscricaoPagamento> listPagamento = new ArrayList<EncontroInscricaoPagamento>();
		for (EncontroInscricaoPagamento pagamento : getListaPagamento()) {
			if(pagamento.getDataPagamento()!=null || pagamento.getValorAlterado()){
				listPagamento.add(pagamento);
				valorPago += pagamento.getValor().setScale(0, RoundingMode.DOWN).doubleValue();
			}
		}
		int qtdeparcelapagas=0;
		for (EncontroInscricaoPagamento pagamento : listPagamento) {
			qtdeparcelapagas++;
			pagamento.setParcela(qtdeparcelapagas);
		}
		double valorTotal = getEncontroInscricao().getValorEncontro().doubleValue();
		double valorApagar = valorTotal - valorPago;

		int parcelasrestantes = qtdeparcelas - qtdeparcelapagas;

		if (valorApagar > 0){
			if (parcelasrestantes<0) {
				parcelasrestantes = 1;
				qtdeparcelas = qtdeparcelapagas + 1;
			}
			if (getMaxParcela() != null && parcelasrestantes+qtdeparcelapagas > getMaxParcela()){
				parcelasrestantes = 1;
				qtdeparcelas = qtdeparcelapagas + 1;

			}
			if (listPagamento.size()==0 && qtdeparcelas==1){
					if(getEncontroInscricao().getEncontro().getDataPagamentoInscricao()!=null){
						parcelaInscricao = new EncontroInscricaoPagamento();
						parcelaInscricao.setDataVencimento(getEncontroInscricao().getEncontro().getDataPagamentoInscricao());
						parcelaInscricao.setEncontroInscricao(getEncontroInscricao());
						parcelaInscricao.setValorAlterado(false);
						parcelaInscricao.setParcela(1);
						parcelaInscricao.setValor(new BigDecimal(getEncontroInscricao().getValorEncontro().doubleValue()+(centavos/100)));
						listPagamento.add(parcelaInscricao);
					}else{
						parcelaInscricao = new EncontroInscricaoPagamento();
						parcelaInscricao.setDataVencimento(getEncontroInscricao().getEncontro().getDataPagamentoInscricao());
						parcelaInscricao.setEncontroInscricao(getEncontroInscricao());
						parcelaInscricao.setParcela(1);
						parcelaInscricao.setValorAlterado(false);
						parcelaInscricao.setValor(new BigDecimal(getEncontroInscricao().getValorEncontro().doubleValue()+(centavos/100)));
						if(hoje.getDate()<daybase){
							hoje = new Date(hoje.getYear(), hoje.getMonth(), daybase, 0, 0, 0);
						} else {
							hoje = new Date(hoje.getYear(), hoje.getMonth()+1, daybase, 0, 0, 0);
						}
						parcelaInscricao.setDataVencimento(hoje);
						listPagamento.add(parcelaInscricao);
					}
			}else{

				double valor = valorApagar / parcelasrestantes;

				EncontroInscricaoPagamento p;

				BigDecimal valorParcela;
				double total = 0;
				for (int i = qtdeparcelapagas+1; i <= qtdeparcelas; i++) {
					if(i==qtdeparcelas){
						valorParcela = new BigDecimal(valorApagar-total);
					} else {
						valorParcela = new BigDecimal(valor);
						valorParcela = valorParcela.setScale(0, RoundingMode.DOWN);
						total += valorParcela.doubleValue();
					}

					p = new EncontroInscricaoPagamento();
					p.setEncontroInscricao(getEncontroInscricao());
					p.setValorAlterado(false);

					if(hoje.getDate() < daybase && i==qtdeparcelas){
						hoje = new Date(hoje.getYear(), hoje.getMonth(), daybase, 0, 0, 0);
					} else {
						hoje = new Date(hoje.getYear(), hoje.getMonth()+1, daybase, 0, 0, 0);
					}
					p.setDataVencimento(hoje);

					p.setParcela(i);
					p.setValor(new BigDecimal(valorParcela.doubleValue() + (centavos/100)));
					listPagamento.add(p);
				}
			}
		}
		setListaPagamento(listPagamento);
	}

	public Integer getMaxParcela() {
		return maxParcela;
	}
	public void setMaxParcela(Integer maxParcela) {
		this.maxParcela = maxParcela;
	}
	public int getQtdeparcelas() {
		return qtdeparcelas;
	}
	public void setQtdeparcelas(int qtdeparcelas) {
		this.qtdeparcelas = qtdeparcelas;
	}

	@SuppressWarnings("deprecation")
	public void defineParcelasPosiveis() {
		setMaxParcela(null);
		Date dataMaxima = getEncontroInscricao().getDataMaximaParcela();
		int daybase = getEncontroInscricao().getEncontro().getDataMaximaPagamento().getDate();
		if(dataMaxima==null){
			dataMaxima = getEncontroInscricao().getEncontro().getDataMaximaPagamento();
		}
		if(dataMaxima!=null){
			Date hoje = new Date();
			hoje = new Date(hoje.getYear(), hoje.getMonth(), daybase, 0, 0, 0);
			int parcelas = 0;
			while(hoje.compareTo(dataMaxima)<=0){
				parcelas++;
				hoje = new Date(hoje.getYear(), hoje.getMonth()+1, daybase, 0, 0, 0);
			}
			setMaxParcela(parcelas);
		}else{
			setMaxParcela(12);
		}
	}

	public void geraPagamentoDetalhe(){
		Encontro encontro = getEncontroInscricao().getEncontro();
		TipoInscricaoEnum tipo = getEncontroInscricao().getTipo();
		List<EncontroInscricaoPagamentoDetalhe> pagamentoDetalheNovo = new ArrayList<EncontroInscricaoPagamentoDetalhe>();
		List<EncontroInscricaoPagamentoDetalhe> pagamentoDetalhe = getListaPagamentoDetalhe();
		if (pagamentoDetalhe!=null){
			for (EncontroInscricaoPagamentoDetalhe encontroInscricaoPagamentoDetalhe : pagamentoDetalhe) {
				if (encontroInscricaoPagamentoDetalhe.getEditavel()){
					pagamentoDetalheNovo.add(encontroInscricaoPagamentoDetalhe);
				}
			}
		}
		if (tipo.equals(TipoInscricaoEnum.AFILHADO)){
			EncontroInscricaoPagamentoDetalhe detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.TAXAENCONTRO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setValorUnitario(encontro.getValorTaxaEncontroAfilhado());
			detalhe.setQuantidade(1);
			detalhe.setValor(encontro.getValorTaxaEncontroAfilhado());
			detalhe.setEditavel(false);
			pagamentoDetalheNovo.add(detalhe);
			detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.HOSPEDAGEM);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setValorUnitario(encontro.getValorDiariaCasal());
			detalhe.setEditavel(false);
			detalhe.setQuantidade(encontro.getQuantidadeDiarias());
			detalhe.setValor(new BigDecimal(encontro.getValorDiariaCasal().doubleValue()*encontro.getQuantidadeDiarias()));
			pagamentoDetalheNovo.add(detalhe);
			detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setEditavel(false);
			detalhe.setValorUnitario(encontro.getValorAlimentacao());
			detalhe.setQuantidade(encontro.getQuantidadeRefeicoes()*2);
			detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()*2));
			pagamentoDetalheNovo.add(detalhe);
		}else if (tipo.equals(TipoInscricaoEnum.PADRINHO)){
			EncontroInscricaoPagamentoDetalhe detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.TAXAENCONTRO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setValor(encontro.getValorTaxaEncontroCasal());
			detalhe.setValorUnitario(encontro.getValorTaxaEncontroCasal());
			detalhe.setQuantidade(1);
			detalhe.setEditavel(false);
			pagamentoDetalheNovo.add(detalhe);
			if (!getEncontroInscricao().getHospedagemParticular()){
				detalhe = new EncontroInscricaoPagamentoDetalhe();
				detalhe.setEncontroInscricao(getEncontroInscricao());
				detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.HOSPEDAGEM);
				detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				detalhe.setValorUnitario(encontro.getValorDiariaCasal());
				detalhe.setEditavel(false);
				detalhe.setQuantidade(encontro.getQuantidadeDiarias());
				detalhe.setValor(new BigDecimal(encontro.getValorDiariaCasal().doubleValue()*encontro.getQuantidadeDiarias()));
				pagamentoDetalheNovo.add(detalhe);
			}
			detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setEditavel(false);
			detalhe.setValorUnitario(encontro.getValorAlimentacao());
			detalhe.setQuantidade(encontro.getQuantidadeRefeicoes()*2);
			detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()*2));
			pagamentoDetalheNovo.add(detalhe);
		}else if (tipo.equals(TipoInscricaoEnum.COORDENADOR)){
			EncontroInscricaoPagamentoDetalhe detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.TAXAENCONTRO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setValor(encontro.getValorTaxaEncontroCasalApoio());
			detalhe.setValorUnitario(encontro.getValorTaxaEncontroCasalApoio());
			detalhe.setEditavel(false);
			detalhe.setQuantidade(1);
			pagamentoDetalheNovo.add(detalhe);
			if (!getEncontroInscricao().getHospedagemParticular()){
				detalhe = new EncontroInscricaoPagamentoDetalhe();
				detalhe.setEncontroInscricao(getEncontroInscricao());
				detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.HOSPEDAGEM);
				detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				detalhe.setValorUnitario(encontro.getValorDiariaCasal());
				detalhe.setQuantidade(encontro.getQuantidadeDiarias());
				detalhe.setEditavel(false);
				detalhe.setValor(new BigDecimal(encontro.getValorDiariaCasal().doubleValue()*encontro.getQuantidadeDiarias()));
				pagamentoDetalheNovo.add(detalhe);
			}
			detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setValorUnitario(encontro.getValorAlimentacao());
			detalhe.setQuantidade(encontro.getQuantidadeRefeicoes()*2);
			detalhe.setEditavel(false);
			detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()*2));
			pagamentoDetalheNovo.add(detalhe);
		}else if (tipo.equals(TipoInscricaoEnum.APOIO)){
			if (getEncontroInscricao().getCasal() != null){
				EncontroInscricaoPagamentoDetalhe detalhe = new EncontroInscricaoPagamentoDetalhe();
				detalhe.setEncontroInscricao(getEncontroInscricao());
				detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.TAXAENCONTRO);
				detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				detalhe.setValor(encontro.getValorTaxaEncontroCasalApoio());
				detalhe.setValorUnitario(encontro.getValorTaxaEncontroCasalApoio());
				detalhe.setQuantidade(1);
				detalhe.setEditavel(false);
				pagamentoDetalheNovo.add(detalhe);
				if (!getEncontroInscricao().getHospedagemParticular()){
					detalhe = new EncontroInscricaoPagamentoDetalhe();
					detalhe.setEncontroInscricao(getEncontroInscricao());
					detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.HOSPEDAGEM);
					detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
					detalhe.setValorUnitario(encontro.getValorDiariaCasal());
					detalhe.setQuantidade(encontro.getQuantidadeDiarias());
					detalhe.setEditavel(false);
					detalhe.setValor(new BigDecimal(encontro.getValorDiariaCasal().doubleValue()*encontro.getQuantidadeDiarias()));
					pagamentoDetalheNovo.add(detalhe);
				}
				detalhe = new EncontroInscricaoPagamentoDetalhe();
				detalhe.setEncontroInscricao(getEncontroInscricao());
				detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
				detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				detalhe.setValorUnitario(encontro.getValorAlimentacao());
				detalhe.setQuantidade(encontro.getQuantidadeRefeicoes()*2);
				detalhe.setEditavel(false);
				detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()*2));
				pagamentoDetalheNovo.add(detalhe);
			}else{
				EncontroInscricaoPagamentoDetalhe detalhe = new EncontroInscricaoPagamentoDetalhe();
				detalhe.setEncontroInscricao(getEncontroInscricao());
				detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.TAXAENCONTRO);
				detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				detalhe.setValor(encontro.getValorTaxaEncontroSolteiro());
				detalhe.setValorUnitario(encontro.getValorTaxaEncontroSolteiro());
				detalhe.setQuantidade(1);
				detalhe.setEditavel(false);
				pagamentoDetalheNovo.add(detalhe);
				if (!getEncontroInscricao().getHospedagemParticular()){
					detalhe = new EncontroInscricaoPagamentoDetalhe();
					detalhe.setEncontroInscricao(getEncontroInscricao());
					detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.HOSPEDAGEM);
					detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
					detalhe.setValorUnitario(encontro.getValorDiariaSolteiro());
					detalhe.setEditavel(false);
					detalhe.setQuantidade(encontro.getQuantidadeDiarias());
					detalhe.setValor(new BigDecimal(encontro.getValorDiariaSolteiro().doubleValue()*encontro.getQuantidadeDiarias()));
					pagamentoDetalheNovo.add(detalhe);
				}
				detalhe = new EncontroInscricaoPagamentoDetalhe();
				detalhe.setEncontroInscricao(getEncontroInscricao());
				detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
				detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				detalhe.setValorUnitario(encontro.getValorAlimentacao());
				detalhe.setQuantidade(encontro.getQuantidadeRefeicoes());
				detalhe.setEditavel(false);
				detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()));
				pagamentoDetalheNovo.add(detalhe);
			}
		}
		setListaPagamentoDetalhe(pagamentoDetalheNovo);
	}
}