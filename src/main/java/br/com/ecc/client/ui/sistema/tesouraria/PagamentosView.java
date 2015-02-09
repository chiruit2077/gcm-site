package br.com.ecc.client.ui.sistema.tesouraria;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.ui.component.textbox.NumberTextBox.Formato;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class PagamentosView extends BaseView<PagamentosPresenter> implements PagamentosPresenter.Display {

	@UiTemplate("PagamentosView.ui.xml")
	interface PagamentosViewUiBinder extends UiBinder<Widget, PagamentosView> {}
	private PagamentosViewUiBinder uiBinder = GWT.create(PagamentosViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	
	@UiField Label nomeLabel;
	@UiField ListBox codigoListBox;
	@UiField ListBox parcelaListBox;
	@UiField(provided=true) NumberTextBox valorNumberTextBox;
	@UiField Label vencimentoLabel;
	@UiField DateBox pagamentoDateBox;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	@UiField Label pagadoLabel;
	
	
	@UiField(provided=true) FlexTable pagamentoFlexTable;
	private FlexTableUtil pagamentoTableUtil = new FlexTableUtil();

	DateTimeFormat dfGlobal = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
	NumberFormat dfCurrency = NumberFormat.getCurrencyFormat();
	
	private EncontroInscricaoPagamento entidadeEditada;

	private List<EncontroInscricao> listaInscricao;

	private List<EncontroInscricaoPagamento> listaParcelas;
	
	public PagamentosView() {
		criaTabela();
		
		valorNumberTextBox = new NumberTextBox(true, false, 16, 16, Formato.MOEDA);
		
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		
		pagamentoDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy")));
		pagamentoDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
	}
	
	private void criaTabela() {
		pagamentoFlexTable = new FlexTable();
		pagamentoFlexTable.setStyleName("portal-formSmall");
		pagamentoTableUtil.initialize(pagamentoFlexTable);
		
		pagamentoTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		pagamentoTableUtil.addColumn("Código", "50", HasHorizontalAlignment.ALIGN_CENTER);
		pagamentoTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
		pagamentoTableUtil.addColumn("Parcela", "50", HasHorizontalAlignment.ALIGN_CENTER);
		pagamentoTableUtil.addColumn("Valor", "100", HasHorizontalAlignment.ALIGN_RIGHT, TipoColuna.NUMBER, "");
		pagamentoTableUtil.addColumn("Vencimento", "100", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy");
		pagamentoTableUtil.addColumn("Pagamento", "100", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy");
	}
	
	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event){
		edita(null);
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		entidadeEditada.setDataPagamento(pagamentoDateBox.getValue());
		if(valorNumberTextBox.getNumber()!=null){
			entidadeEditada.setValor(new BigDecimal(valorNumberTextBox.getNumber().doubleValue()));
		}
		presenter.salvar(entidadeEditada);
	}
	private void edita(EncontroInscricaoPagamento pagamento) {
		codigoListBox.setSelectedIndex(-1);
		limpaCampos();
		if(pagamento == null){
			entidadeEditada = new EncontroInscricaoPagamento();
		} else {
			entidadeEditada = pagamento;
			defineCampos(pagamento, true);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		codigoListBox.setFocus(true);
		//codigoListBoxChangeHandler(null);
	}
	
	public void limpaCampos(){
		nomeLabel.setText(null);
		vencimentoLabel.setText(null);
		valorNumberTextBox.setNumber(null);
		pagamentoDateBox.setValue(null);
		parcelaListBox.clear();
	}

	public void defineCampos(EncontroInscricaoPagamento pagamento, boolean combos){
		entidadeEditada = pagamento;
		if(pagamento.getEncontroInscricao().getCasal()!=null){
			nomeLabel.setText(pagamento.getEncontroInscricao().getCasal().getApelidos("e"));
		} else {
			nomeLabel.setText(pagamento.getEncontroInscricao().getPessoa().getNome());
		}
		vencimentoLabel.setText(dfGlobal.format(pagamento.getDataVencimento()));
		valorNumberTextBox.setNumber(pagamento.getValor());
		pagamentoDateBox.setValue(pagamento.getDataPagamento());
		pagadoLabel.setText(null);
		if(pagamento.getDataPagamento()!=null){
			pagadoLabel.setText("Parcela paga");
		}
		if(combos){
			ListBoxUtil.setItemSelected(codigoListBox, pagamento.getEncontroInscricao().getCodigo().toString());
			//ListBoxUtil.setItemSelected(parcelaListBox, pagamento.getParcela().toString());
		}
	}
	
	@Override
	public String getDisplayTitle() {
		return "Pagamentos";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		pagamentoTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<EncontroInscricaoPagamento> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "pagamento", "pagamentos", "");
		pagamentoTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroInscricaoPagamento pagamento: lista) {
			Object dados[] = new Object[7];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(pagamento);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este Pagamento ?")){
						pagamento.setDataPagamento(null);
						presenter.salvar(pagamento);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			dados[1] = pagamento.getEncontroInscricao().getCodigo();
			if(pagamento.getEncontroInscricao().getCasal()!=null){
				dados[2] = pagamento.getEncontroInscricao().getCasal().getApelidos("e");
			} else {
				dados[2] = pagamento.getEncontroInscricao().getPessoa().getNome();
			}
			dados[3] = pagamento.getInscricao()?"Inscrição":"Parcela";
			dados[4] = dfCurrency.format(pagamento.getValor());
			dados[5] = dfGlobal.format(pagamento.getDataVencimento());
			dados[6] = dfGlobal.format(pagamento.getDataPagamento());
			pagamentoTableUtil.addRow(dados,row+1);
			row++;
		}
		pagamentoTableUtil.applyDataRowStyles();
	}

	@Override
	public void populaCodigos(List<EncontroInscricao> result) {
		Collections.sort(result, new Comparator<EncontroInscricao>() {
			@Override
			public int compare(EncontroInscricao o1, EncontroInscricao o2) {
				if(o1.getCodigo()==null) return 1;
				if(o2.getCodigo()==null) return -1;
				return o1.getCodigo().compareTo(o2.getCodigo());
			}
		});

		this.listaInscricao = result;
		codigoListBox.clear();
		for (EncontroInscricao encontroInscricao : result) {
			if(encontroInscricao.getCodigo()!=null && (encontroInscricao.getEsconderPlanoPagamento()!=null && !encontroInscricao.getEsconderPlanoPagamento())){
				codigoListBox.addItem(encontroInscricao.getCodigo().toString());
			}
		}
	}
	@UiHandler("codigoListBox")
	public void codigoListBoxChangeHandler(ChangeEvent event){
		limpaCampos();
		String codigo = codigoListBox.getValue(codigoListBox.getSelectedIndex());
		if(codigo!=null && !codigo.equals("")){
			for (EncontroInscricao inscricao : listaInscricao) {
				if(inscricao.getCodigo()!=null && inscricao.getCodigo().equals(Integer.valueOf(codigo))){
					if(inscricao.getCasal()!=null){
						nomeLabel.setText(inscricao.getCasal().getApelidos("e"));
					} else {
						nomeLabel.setText(inscricao.getPessoa().getNome());
					}
					presenter.buscaParcelas(inscricao);
					break;
				}
			}
		}
	}

	@Override
	public void populaParcelas(List<EncontroInscricaoPagamento> listaPagamento) {
		this.listaParcelas = listaPagamento;
		parcelaListBox.clear();
		for (EncontroInscricaoPagamento parcela : listaPagamento) {
			if(parcela.getInscricao()){
				parcelaListBox.addItem("Inscrição", parcela.getId().toString());
			} else {
				parcelaListBox.addItem(dfGlobal.format(parcela.getDataVencimento()), parcela.getId().toString());
			}
		}
		parcelaListBoxChangeHandler(null);
	}
	
	@UiHandler("parcelaListBox")
	public void parcelaListBoxChangeHandler(ChangeEvent event){
		pagadoLabel.setText(null);
		String id = parcelaListBox.getValue(parcelaListBox.getSelectedIndex());
		for (EncontroInscricaoPagamento pagamento : listaParcelas) {
			if(pagamento.getId().toString().equals(id)){
				defineCampos(pagamento, false);
				break;
			}
		}
	}
}