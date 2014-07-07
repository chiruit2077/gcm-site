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
	private Boolean mantemValores = false;
	private Integer maxParcela;
	private Integer qtdeparcelas;

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
		int daybase = 10;
		if (getDataMaxima()!=null) daybase = getDataMaxima().getDate();
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
		/*int qtdeparcelapagas=0;
		for (EncontroInscricaoPagamento pagamento : listPagamento) {
			qtdeparcelapagas++;
			pagamento.setParcela(qtdeparcelapagas);
		}
		*/
		double valorTotal = getEncontroInscricao().getValorEncontro().doubleValue();
		double valorApagar = valorTotal - valorPago;

		int parcelasrestantes = getQtdeparcelas();

		if (valorApagar > 0){
			if (getMaxParcela() != null && parcelasrestantes > getMaxParcela()){
				parcelasrestantes = getMaxParcela();
				setQtdeparcelas(parcelasrestantes);

			}
			if (parcelasrestantes<0) {
				parcelasrestantes = 1;
				setQtdeparcelas(1);
			}
			if (listPagamento.size()==0 && getQtdeparcelas().equals(1)){
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
						if(hoje.getDate()>daybase){
							hoje = new Date(hoje.getYear(), hoje.getMonth()+1, daybase);
						} else {
							hoje = new Date(hoje.getYear(), hoje.getMonth(), daybase);
						}
						parcelaInscricao.setDataVencimento(hoje);
						listPagamento.add(parcelaInscricao);
					}
			}else{
				//suponho q a ultima parcela paga tenha sido paga por ultimo :)
				/*
				for (EncontroInscricaoPagamento pagto : listPagamento) {
					if(pagto.getDataPagamento()!=null){
						hoje = pagto.getDataPagamento();
						hoje.setMonth(hoje.getMonth()+1);
					}
				} */
				hoje = new Date();
				double valor = valorApagar / parcelasrestantes;

				EncontroInscricaoPagamento p;

				BigDecimal valorParcela;
				double total = 0;
				for (int i = 1; i <= getQtdeparcelas(); i++) {
					if(getQtdeparcelas().equals(i)){
						valorParcela = new BigDecimal(valorApagar-total);
					} else {
						valorParcela = new BigDecimal(valor);
						valorParcela = valorParcela.setScale(0, RoundingMode.DOWN);
						total += valorParcela.doubleValue();
					}

					p = new EncontroInscricaoPagamento();
					p.setEncontroInscricao(getEncontroInscricao());
					p.setValorAlterado(false);

					if (i==1){
						if(hoje.getDate()>daybase){
							hoje = new Date(hoje.getYear(), hoje.getMonth()+1, daybase);
						} else {
							hoje = new Date(hoje.getYear(), hoje.getMonth(), daybase);
						}
					}else {
						hoje = new Date(hoje.getYear(), hoje.getMonth()+1, daybase);
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
		if (maxParcela==null) return 1;
		return maxParcela;
	}
	public void setMaxParcela(Integer maxParcela) {
		this.maxParcela = maxParcela;
	}
	public Integer getQtdeparcelas() {
		if (qtdeparcelas==null || qtdeparcelas.equals(0)) return getMaxParcela();
		return qtdeparcelas;
	}
	public void setQtdeparcelas(Integer qtdeparcelas) {
		this.qtdeparcelas = qtdeparcelas;
	}

	@SuppressWarnings("deprecation")
	public void defineParcelasPosiveis() {
		Date hoje = new Date();
		int daybase = hoje.getDate();
		setMaxParcela(null);
		Date dataMaxima = getDataMaxima();
		if (dataMaxima!=null)
			daybase = dataMaxima.getDate();
		int parcelas = 0;
		for(int i=1;i<=10;i++){
			boolean insere = true;
			if (parcelas==0){
				if (hoje.getDate() > daybase)
					hoje = new Date(hoje.getYear(), hoje.getMonth()+1, daybase);
				else
					hoje = new Date(hoje.getYear(), hoje.getMonth(), daybase);
			}else{
				hoje = new Date(hoje.getYear(), hoje.getMonth()+1, daybase);
				if (dataMaxima != null && hoje.compareTo(dataMaxima)>0)
					insere = false;
			}
			if (insere){
				parcelas++;
			}
		}
		setMaxParcela(parcelas);
	}

	public Date getDataMaxima() {
		Date dataMaxima = getEncontroInscricao().getDataMaximaParcela();
		if(dataMaxima==null){
			dataMaxima = getEncontroInscricao().getEncontro().getDataMaximaPagamento();
		}
		return dataMaxima;
	}
	public void geraPagamentoDetalhe(){
		setListaPagamentoDetalhe(geraPagamentoDetalheAux());
	}

	public List<EncontroInscricaoPagamentoDetalhe> geraPagamentoDetalheAux(){
		Encontro encontro = getEncontroInscricao().getEncontro();
		TipoInscricaoEnum tipo = getEncontroInscricao().getTipo();
		List<EncontroInscricaoPagamentoDetalhe> pagamentoDetalheNovo = new ArrayList<EncontroInscricaoPagamentoDetalhe>();
		List<EncontroInscricaoPagamentoDetalhe> pagamentoDetalhe = getListaPagamentoDetalhe();
		for (EncontroInscricaoPagamentoDetalhe eip : pagamentoDetalhe) {
			if (eip.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO) || eip.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.AVULSO)){
				pagamentoDetalheNovo.add(eip);
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
			if(encontro.getValorDiariaCasal()!=null && encontro.getQuantidadeDiarias()!=null){
				detalhe.setValor(new BigDecimal(encontro.getValorDiariaCasal().doubleValue()*encontro.getQuantidadeDiarias()));
			}
			pagamentoDetalheNovo.add(detalhe);
			detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setEditavel(false);
			detalhe.setValorUnitario(encontro.getValorAlimentacao());
			if(encontro.getQuantidadeRefeicoes()!=null){
				detalhe.setQuantidade(encontro.getQuantidadeRefeicoes()*2);
			}
			if(encontro.getValorAlimentacao()!=null && encontro.getQuantidadeRefeicoes()!=null){
				detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()*2));
			}
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
				if(encontro.getValorDiariaCasal()!=null &&  encontro.getQuantidadeDiarias()!=null){
					detalhe.setValor(new BigDecimal(encontro.getValorDiariaCasal().doubleValue()*encontro.getQuantidadeDiarias()));
				}
				pagamentoDetalheNovo.add(detalhe);
			}
			detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setEditavel(false);
			detalhe.setValorUnitario(encontro.getValorAlimentacao());
			if(encontro.getQuantidadeRefeicoes()!=null){
				detalhe.setQuantidade(encontro.getQuantidadeRefeicoes()*2);
			}
			if(encontro.getValorAlimentacao()!=null && encontro.getQuantidadeRefeicoes()!=null){
				detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()*2));
			}
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
				if(encontro.getValorDiariaCasal()!=null && encontro.getQuantidadeDiarias()!=null){
					detalhe.setValor(new BigDecimal(encontro.getValorDiariaCasal().doubleValue()*encontro.getQuantidadeDiarias()));
				}
				pagamentoDetalheNovo.add(detalhe);
			}
			detalhe = new EncontroInscricaoPagamentoDetalhe();
			detalhe.setEncontroInscricao(getEncontroInscricao());
			detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
			detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
			detalhe.setValorUnitario(encontro.getValorAlimentacao());
			if(encontro.getQuantidadeRefeicoes()!=null){
				detalhe.setQuantidade(encontro.getQuantidadeRefeicoes()*2);
			}
			detalhe.setEditavel(false);
			if(encontro.getValorAlimentacao()!=null && encontro.getQuantidadeRefeicoes()!=null){
				detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()*2));
			}
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
					if(encontro.getValorDiariaCasal()!=null && encontro.getQuantidadeDiarias()!=null){
						detalhe.setValor(new BigDecimal(encontro.getValorDiariaCasal().doubleValue()*encontro.getQuantidadeDiarias()));
					}
					pagamentoDetalheNovo.add(detalhe);
				}
				detalhe = new EncontroInscricaoPagamentoDetalhe();
				detalhe.setEncontroInscricao(getEncontroInscricao());
				detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
				detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				detalhe.setValorUnitario(encontro.getValorAlimentacao());
				if(encontro.getQuantidadeRefeicoes()!=null){
					detalhe.setQuantidade(encontro.getQuantidadeRefeicoes()*2);
				}
				detalhe.setEditavel(false);
				if(encontro.getValorAlimentacao()!=null && encontro.getQuantidadeRefeicoes()!=null){
					detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()*2));
				}
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
					if(encontro.getValorDiariaSolteiro()!=null && encontro.getQuantidadeDiarias()!=null){
						detalhe.setValor(new BigDecimal(encontro.getValorDiariaSolteiro().doubleValue()*encontro.getQuantidadeDiarias()));
					}
					pagamentoDetalheNovo.add(detalhe);
				}
				detalhe = new EncontroInscricaoPagamentoDetalhe();
				detalhe.setEncontroInscricao(getEncontroInscricao());
				detalhe.setTipoDetalhe(TipoPagamentoDetalheEnum.ALIMENTACAO);
				detalhe.setTipoLancamento(TipoPagamentoLancamentoEnum.DEBITO);
				detalhe.setValorUnitario(encontro.getValorAlimentacao());
				detalhe.setQuantidade(encontro.getQuantidadeRefeicoes());
				detalhe.setEditavel(false);
				if(encontro.getValorAlimentacao()!=null && encontro.getQuantidadeRefeicoes()!=null){
					detalhe.setValor(new BigDecimal(encontro.getValorAlimentacao().doubleValue()*encontro.getQuantidadeRefeicoes()));
				}
				pagamentoDetalheNovo.add(detalhe);
			}
		}
		return pagamentoDetalheNovo;
	}
	public boolean isAtualizaValores() {
		return !somaValorEncontroAux(getListaPagamentoDetalhe()).equals(somaValorEncontroAux(geraPagamentoDetalheAux())) ||
				!somaValorEncontroAux(geraPagamentoDetalheAux()).equals(somaValorParcelas(getListaPagamento()));
	}

	private BigDecimal somaValorParcelas(List<EncontroInscricaoPagamento> listaPagamento) {
		double valor = 0;
		for (EncontroInscricaoPagamento pagamento : listaPagamento) {
			valor += pagamento.getValor().setScale(0, RoundingMode.DOWN).doubleValue();
		}
		return new BigDecimal(valor);
	}
	public BigDecimal somaValorEncontroAux(List<EncontroInscricaoPagamentoDetalhe> detalhes){
		double valor = 0;
		for (EncontroInscricaoPagamentoDetalhe detalhe: detalhes) {
			if(detalhe.getValor()!=null){
				if(detalhe.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.DEBITO))
					valor += detalhe.getValor().doubleValue();
				else
					valor -= detalhe.getValor().doubleValue();
			}
		}
		return new BigDecimal(valor);
	}
	public Boolean getMantemValores() {
		return mantemValores;
	}
	public void setMantemValores(Boolean mantemValores) {
		this.mantemValores = mantemValores;
	}
}