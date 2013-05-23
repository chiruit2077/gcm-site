package br.com.ecc.client.ui.component.sistema;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.StringUtil;
import br.com.ecc.model.Casal;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class DadosPagamento extends Composite {

	@UiTemplate("DadosPagamento.ui.xml")
	interface DadosPagamentoUiBinder extends UiBinder<Widget, DadosPagamento> {}
	private DadosPagamentoUiBinder uiBinder = GWT.create(DadosPagamentoUiBinder.class);

	@UiField HTMLPanel formularioHTMLPanel;
	@UiField Label codigoLabel;

	@UiField Label itemPagamentoTotal;
	@UiField(provided=true) FlexTable pagamentoFlexTable;
	private FlexTableUtil pagamentoTableUtil = new FlexTableUtil();

	@UiField HTML instrucaoHTML;
	@UiField Label vencimentoMaximoLabel;
	@UiField Label inscricaoLabel;
	@UiField ListBox parcelaListBox;

	@UiField Label bancoLabel;
	@UiField Label agenciaLabel;
	@UiField Label contaLabel;
	@UiField Label favorecidoLabel;
	@UiField Label valorLabel;
	@UiField TextBox valorParcelasTextBox;
	@UiField TextBox valorPagoTextBox;
	@UiField TextBox valorPagarTextBox;
	@UiField TextBox valorCreditoTextBox;

	@UiField DialogBox editaPagamentoDialogBox;
	@UiField Label parcelaLabel;
	@UiField DateBox dataVencimentoDateBox;
	@UiField(provided=true) NumberTextBox valorParcelaNumberTextBox;

	private EncontroInscricaoVO entidadeEditada;
	DateTimeFormat dfGlobal = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
	NumberFormat dfCurrency = NumberFormat.getCurrencyFormat();

	private EncontroInscricaoPagamento pagamentoEditada;
	private Usuario usuario;
	private Casal casal;
	private Boolean dadosAlterados = false;

	public DadosPagamento() {
		criaTabelaPagamento();
		valorParcelaNumberTextBox = new NumberTextBox(false, false, 5, 5);

		initWidget(uiBinder.createAndBindUi(this));

		dataVencimentoDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		dataVencimentoDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
	}

	public void setValorEncontro(BigDecimal valor, boolean geraparcelas) {
		entidadeEditada.getEncontroInscricao().setValorEncontro(valor);
		if(entidadeEditada.getEncontroInscricao().getValorEncontro()!=null){
			valorLabel.setText(dfCurrency.format(entidadeEditada.getEncontroInscricao().getValorEncontro()));
		}
		if (geraparcelas) {
			defineParcelasPosiveis();
			entidadeEditada.defineParcelasPosiveis();
			entidadeEditada.geraParcelas();
			dadosAlterados = true;
		}
		populaPagamentos();
	}

	private void criaTabelaPagamento() {
		pagamentoFlexTable = new FlexTable();
		pagamentoFlexTable.setStyleName("portal-formSmall");
		pagamentoTableUtil.initialize(pagamentoFlexTable);

		pagamentoTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		pagamentoTableUtil.addColumn("Parcela", "50", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.NUMBER, null);
		pagamentoTableUtil.addColumn("Vencimento", "80", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy");
		pagamentoTableUtil.addColumn("Valor", "100", HasHorizontalAlignment.ALIGN_RIGHT, TipoColuna.NUMBER, null);
		pagamentoTableUtil.addColumn("Pagamento", "80", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy");
	}


	public void limpaCampos() {
		codigoLabel.setText(null);
		instrucaoHTML.setHTML("");
		valorLabel.setText(null);
		vencimentoMaximoLabel.setText(null);
		inscricaoLabel.setText(null);
		parcelaListBox.clear();
		valorCreditoTextBox.setText(dfCurrency.format(0));
		valorPagoTextBox.setText(dfCurrency.format(0));
		valorPagarTextBox.setText(dfCurrency.format(0));
		valorParcelasTextBox.setText(dfCurrency.format(0));

		bancoLabel.setText(null);
		agenciaLabel.setText(null);
		contaLabel.setText(null);
		favorecidoLabel.setText(null);
	}

	public EncontroInscricaoVO getEncontroInscricaoVO(){
		return entidadeEditada;
	}
	public void setEncontroInscricaoVO(EncontroInscricaoVO encontroInscricaoVO){
		dadosAlterados = false;
		this.entidadeEditada = encontroInscricaoVO;
		setCodigo(encontroInscricaoVO.getEncontroInscricao().getCodigo());

		if(encontroInscricaoVO.getEncontroInscricao().getValorEncontro()!=null){
			valorLabel.setText(dfCurrency.format(encontroInscricaoVO.getEncontroInscricao().getValorEncontro()));
		} else {
			if(encontroInscricaoVO.getEncontroInscricao().getId()==null && encontroInscricaoVO.getEncontroInscricao().getTipo()!=null){
				if(encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.AFILHADO)){
					valorLabel.setText(dfCurrency.format(encontroInscricaoVO.getEncontroInscricao().getEncontro().getValorAfilhado()));
				} else if(encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.PADRINHO)){
					valorLabel.setText(dfCurrency.format(encontroInscricaoVO.getEncontroInscricao().getEncontro().getValorPadrinho()));
				} else if(encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.APOIO)){
					valorLabel.setText(dfCurrency.format(encontroInscricaoVO.getEncontroInscricao().getEncontro().getValorApoio()));
				}
			}
		}
		Date dataMaxima = entidadeEditada.getEncontroInscricao().getDataMaximaParcela();
		if(dataMaxima==null){
			dataMaxima = entidadeEditada.getEncontroInscricao().getEncontro().getDataMaximaPagamento();
		}
		if(dataMaxima!=null){
			vencimentoMaximoLabel.setText(dfGlobal.format(dataMaxima));
		}
		defineAvisos(encontroInscricaoVO.getEncontroInscricao().getCodigo());
		parcelaListBox.clear();

		bancoLabel.setText(encontroInscricaoVO.getEncontroInscricao().getEncontro().getGrupo().getBanco());
		agenciaLabel.setText(encontroInscricaoVO.getEncontroInscricao().getEncontro().getGrupo().getAgencia());
		contaLabel.setText(encontroInscricaoVO.getEncontroInscricao().getEncontro().getGrupo().getConta());
		favorecidoLabel.setText(encontroInscricaoVO.getEncontroInscricao().getEncontro().getGrupo().getFavorecidoConta());

		defineParcelasPosiveis();
		populaPagamentos();
	}

	public void defineAvisos(Integer codigoParam){
		entidadeEditada.getEncontroInscricao().setCodigo(codigoParam);
		setCodigo(codigoParam);
		String codigo = "00";
		String aux= "se seu código for ";
		if(codigoParam!=null){
			codigo = StringUtil.right("00" + codigoParam.toString(),2);
			aux= "o seu código é ";
		}
		instrucaoHTML.setHTML("2. Qualquer depósito deverá conter, obrigatoriamente o <span style='color:red;'>CÓDIGO</span> de identificação do casal, nos centavos. " +
				"Assim, " + aux + "<span style='color:red;'>" + codigo + "</span>, então, qualquer valor a depositar deverá ser <span style='color:red;'>R$###," + codigo + ".</span>");
		if(entidadeEditada!=null &&
				entidadeEditada.getEncontroInscricao().getEncontro().getValorInscricao()!=null &&
				entidadeEditada.getEncontroInscricao().getEncontro().getDataPagamentoInscricao()!=null){
			inscricaoLabel.setText("3. A vaga somente se concretiza no pagamento da inscrição [" +
					dfCurrency.format(entidadeEditada.getEncontroInscricao().getEncontro().getValorInscricao()) +
					"] que deverá ser efetuada até no máximo no dia " +
					dfGlobal.format(entidadeEditada.getEncontroInscricao().getEncontro().getDataPagamentoInscricao()) +".");
		} else {
			inscricaoLabel.setText("3. A vaga somente se concretiza no pagamento da primeira parcela.");
		}
	}

	private void defineParcelasPosiveis() {
		parcelaListBox.clear();
		parcelaListBox.addItem("");
		vencimentoMaximoLabel.setText(null);
		entidadeEditada.defineParcelasPosiveis();
		Date dataMaxima = entidadeEditada.getDataMaxima();
		if(dataMaxima!=null){
			vencimentoMaximoLabel.setText(dfGlobal.format(dataMaxima));
		}
		for(int i=1;i<=entidadeEditada.getMaxParcela();i++){
			parcelaListBox.addItem(i+"");
		}
	}

	public void populaPagamentos() {
		Collections.sort(entidadeEditada.getListaPagamento(), new Comparator<EncontroInscricaoPagamento>() {
			@Override
			public int compare(EncontroInscricaoPagamento o1, EncontroInscricaoPagamento o2) {
				return o1.getParcela().compareTo(o2.getParcela());
			}
		});

		double valorparcelas = 0;
		double valorpago = 0;
		double valorpagar = 0;
		double valorcredito = 0;

		if(usuario==null) return ;
		boolean podeEditar = false;
		if(usuario.getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			podeEditar = true;
		}

		pagamentoTableUtil.clearData();

		if(entidadeEditada.getEncontroInscricao().getValorEncontro()==null){
			return;
		}

		LabelTotalUtil.setTotal(itemPagamentoTotal, entidadeEditada.getListaPagamento().size(), "parcela", "parcelas", "a");
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		boolean ok;
		for (final EncontroInscricaoPagamento pagamento: entidadeEditada.getListaPagamento()) {
			Object dados[] = new Object[5];
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);

			ok = false;
			if(pagamento.getEncontroInscricao().getCasal()!=null){
				if(casal!=null && casal.getId().equals(pagamento.getEncontroInscricao().getCasal().getId())){
					ok = true;
				}
			} else {
				if(usuario.getPessoa().getId().equals(pagamento.getEncontroInscricao().getPessoa().getId())){
					ok = true;
				}
			}
			if(pagamento.getDataPagamento()==null){
				if(podeEditar || ok){
					editar = new Image("images/edit.png");
					editar.setTitle("Editar as informações desta parcela");
					editar.setStyleName("portal-ImageCursor");
					editar.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent arg0) {
							editaPagamento(pagamento);
						}
					});
					hp.add(editar);
				}

				if((podeEditar || ok ) && !pagamento.getParcela().equals(0)){
					excluir = new Image("images/delete.png");
					excluir.setTitle("Excluir esta parcela");
					excluir.setStyleName("portal-ImageCursor");
					excluir.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent arg0) {
							if(Window.confirm("Deseja excluir esta parcela ?")){
								dadosAlterados = true;
								entidadeEditada.getListaPagamento().remove(pagamento);
								entidadeEditada.defineParcelasPosiveis();
								entidadeEditada.geraParcelas();
								populaPagamentos();
							}
						}
					});
					hp.add(excluir);
				}
			}else{
				valorpago += pagamento.getValor().setScale(0, RoundingMode.HALF_UP).doubleValue();
			}
			dados[0] = hp;
			if(pagamento.getParcela().equals(0)){
				dados[1] = "Inscrição";
			} else {
				dados[1] = pagamento.getParcela();
			}
			if(pagamento.getDataVencimento()!=null){
				dados[2] = dfGlobal.format(pagamento.getDataVencimento());
			}
			if(pagamento.getValor()!=null){
				dados[3] = dfCurrency.format(pagamento.getValor());
				valorparcelas += pagamento.getValor().setScale(0, RoundingMode.DOWN).doubleValue();
			}
			if(pagamento.getDataPagamento()!=null){
				dados[4] = dfGlobal.format(pagamento.getDataPagamento());
			}
			pagamentoTableUtil.addRow(dados,row+1);
			row++;
		}
		if (valorpago > entidadeEditada.getEncontroInscricao().getValorEncontro().doubleValue())
			valorcredito = valorpago - entidadeEditada.getEncontroInscricao().getValorEncontro().doubleValue();
		if (entidadeEditada.getEncontroInscricao().getValorEncontro().doubleValue() > valorpago)
			valorpagar = entidadeEditada.getEncontroInscricao().getValorEncontro().doubleValue() - valorpago;
		entidadeEditada.setQtdeparcelas(row);
		pagamentoTableUtil.applyDataRowStyles();
		if (row>0){
			ListBoxUtil.setItemSelected(parcelaListBox,row+"");
			if (parcelaListBox.getSelectedIndex()==-1){
				parcelaListBox.addItem(row+"");
				ListBoxUtil.setItemSelected(parcelaListBox,row+"");
			}
		}
		else
			ListBoxUtil.setItemSelected(parcelaListBox,"1");
		valorCreditoTextBox.setText(dfCurrency.format(valorcredito));
		valorPagoTextBox.setText(dfCurrency.format(valorpago));
		valorPagarTextBox.setText(dfCurrency.format(valorpagar));
		valorParcelasTextBox.setText(dfCurrency.format(valorparcelas));
	}

	private void editaPagamento(EncontroInscricaoPagamento encontroInscricaoPagamento) {
		limpaCamposPagamento();
		if(encontroInscricaoPagamento == null){
			pagamentoEditada = new EncontroInscricaoPagamento();
		} else {
			if(encontroInscricaoPagamento.getParcela()==0){
				valorParcelaNumberTextBox.setEnabled(false);
			} else {
				valorParcelaNumberTextBox.setEnabled(true);
			}
			pagamentoEditada = encontroInscricaoPagamento;
			defineCamposPagamento(encontroInscricaoPagamento);
		}
		editaPagamentoDialogBox.center();
		editaPagamentoDialogBox.show();
		if(valorParcelaNumberTextBox.isEnabled()){
			valorParcelaNumberTextBox.setFocus(true);
		}
	}

	public void limpaCamposPagamento(){
		parcelaLabel.setText(null);
		dataVencimentoDateBox.setValue(null);
		valorParcelaNumberTextBox.setNumber(null);
	}

	public void defineCamposPagamento(EncontroInscricaoPagamento encontroInscricaoPagamento){
		if(encontroInscricaoPagamento.getParcela()!=null){
			if(encontroInscricaoPagamento.getParcela().equals(0)){
				parcelaLabel.setText("Inscrição");
			} else {
				parcelaLabel.setText(encontroInscricaoPagamento.getParcela().toString());
			}
		}
		dataVencimentoDateBox.setValue(encontroInscricaoPagamento.getDataVencimento());
		valorParcelaNumberTextBox.setNumber(encontroInscricaoPagamento.getValor());
	}
	@UiHandler("salvarPagamentoButton")
	public void salvarPagamentoButtonClickHandler(ClickEvent event){
		Date dataMaxima = entidadeEditada.getEncontroInscricao().getDataMaximaParcela();
		if(dataMaxima==null){
			dataMaxima = entidadeEditada.getEncontroInscricao().getEncontro().getDataMaximaPagamento();
		}
		if(dataVencimentoDateBox.getValue().after(dataMaxima)){
			Window.alert("A data dos vencimentos não pode exceder: " + dfGlobal.format(entidadeEditada.getEncontroInscricao().getEncontro().getDataMaximaPagamento()));
			return;
		}
		if(pagamentoEditada.getParcela().equals(0)){
			if(dataVencimentoDateBox.getValue().after(entidadeEditada.getEncontroInscricao().getEncontro().getDataPagamentoInscricao())){
				Window.alert("A data de vencimento da inscrição não pode exceder: " + dfGlobal.format(entidadeEditada.getEncontroInscricao().getEncontro().getDataPagamentoInscricao()));
				return;
			}
		}
		entidadeEditada.getListaPagamento().remove(pagamentoEditada);
		pagamentoEditada.setValorAlterado(true);
		pagamentoEditada.setDataVencimento(dataVencimentoDateBox.getValue());
		if(valorParcelaNumberTextBox.getNumber()!=null){
			double centavos = 0;
			if(entidadeEditada.getEncontroInscricao().getCodigo()!=null){
				centavos = entidadeEditada.getEncontroInscricao().getCodigo().intValue();
			}
			pagamentoEditada.setValor(new BigDecimal(valorParcelaNumberTextBox.getNumber().doubleValue()+(centavos/100)));
		}
		entidadeEditada.getListaPagamento().add(pagamentoEditada);
		entidadeEditada.geraParcelas();
		populaPagamentos();
		dadosAlterados = true;
		editaPagamentoDialogBox.hide();
	}

	@UiHandler("fecharPagamentoButton")
	public void fecharPagamentoButtonClickHandler(ClickEvent event){
		editaPagamentoDialogBox.hide();
	}

	@UiHandler("parcelaListBox")
	public void parcelaListBoxChangeHandler(ChangeEvent event){
		dadosAlterados = true;
		if(parcelaListBox.getValue(parcelaListBox.getSelectedIndex())==null || parcelaListBox.getValue(parcelaListBox.getSelectedIndex()).equals("") ){
			return;
		}
		getEncontroInscricaoVO().setQtdeparcelas(Integer.parseInt(parcelaListBox.getValue(parcelaListBox.getSelectedIndex())));
		getEncontroInscricaoVO().defineParcelasPosiveis();
		getEncontroInscricaoVO().geraParcelas();
		populaPagamentos();
	}

	private void setCodigo(Integer codigo){
		if(codigo!=null){
			if(entidadeEditada.getListaPagamento().size()>0){
				double centavos = 0;
				if(entidadeEditada.getEncontroInscricao().getCodigo()!=null){
					centavos = entidadeEditada.getEncontroInscricao().getCodigo().intValue();
				}
				BigDecimal valorParcela;
				for (EncontroInscricaoPagamento pagamento : entidadeEditada.getListaPagamento()) {
					if(pagamento.getDataPagamento()!=null){
						valorParcela = pagamento.getValor().setScale(0, RoundingMode.HALF_UP);
						pagamento.setValor(new BigDecimal(valorParcela.doubleValue()+(centavos/100)));
					}
				}
				populaPagamentos();
			}
			codigoLabel.setText(codigo.toString());
		} else {
			codigoLabel.setText(null);
		}
	}

	public void defineMaximaDataPagamento(Date value) {
		entidadeEditada.getEncontroInscricao().setDataMaximaParcela(value);
		defineParcelasPosiveis();
	}
	public void setCasalInscrito(Casal casal){
		entidadeEditada.getEncontroInscricao().setCasal(casal);
	}
	public void setPessoaInscrita(Pessoa pessoa){
		entidadeEditada.getEncontroInscricao().setPessoa(pessoa);
	}
	public void setVisible(Boolean visible){
		formularioHTMLPanel.setVisible(visible);
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Boolean getDadosAlterados() {
		return dadosAlterados;
	}
	public void setDadosAlterados(Boolean dadosAlterados) {
		this.dadosAlterados = dadosAlterados;
	}
}