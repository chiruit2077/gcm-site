package br.com.ecc.client.ui.sistema.encontro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.sistema.DadosPagamento;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.ui.component.textbox.NumberTextBox.Formato;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.client.util.NavegadorUtil;
import br.com.ecc.model.Casal;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.EncontroInscricaoPagamentoDetalhe;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoConfirmacaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;
import br.com.ecc.model.tipo.TipoMensagemEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.tipo.TipoPagamentoDetalheEnum;
import br.com.ecc.model.tipo.TipoPagamentoLancamentoEnum;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class EncontroInscricaoView extends BaseView<EncontroInscricaoPresenter> implements EncontroInscricaoPresenter.Display {

	@UiTemplate("EncontroInscricaoView.ui.xml")
	interface EncontroInscricaoViewUiBinder extends UiBinder<Widget, EncontroInscricaoView> {}
	private EncontroInscricaoViewUiBinder uiBinder = GWT.create(EncontroInscricaoViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField Label totalLabel;
	@UiField VerticalPanel centralPanel;
	@UiField HTMLPanel casalHTMLPanel;
	@UiField HTMLPanel pessoaHTMLPanel;
	@UiField HTMLPanel fichaHTMLPanel;
	@UiField HTMLPanel dataLimiteHTMLPanel;
	@UiField HTMLPanel detalheHTMLPanel;
	@UiField HTMLPanel valorHTMLPanel;
	@UiField Label emailLabel;
	@UiField ListBox mensagemListBox;
	@UiField DateBox dataMaxParcelaDateBox;
	@UiField CheckBox esconderPagamentoCheckBox;
	@UiField CheckBox hospedagemParticularCheckBox;
	@UiField CheckBox marcaPreenchimentoFichaCheckBox;

	@UiField HTMLPanel participanteHTMLPanel;
	@UiField Label participanteLabel;
	@UiField CheckBox exibeDesistenciaCheckBox;
	@UiField TabLayoutPanel tabPanel;

	@UiField ListBox tipoInscricaoListBox;

	@UiField(provided = true) SuggestBox casalSuggestBox;
	private final GenericEntitySuggestOracle casalSuggest = new GenericEntitySuggestOracle();

	@UiField(provided = true) SuggestBox pessoaSuggestBox;
	private final GenericEntitySuggestOracle pessoaSuggest = new GenericEntitySuggestOracle();

	@UiField ListBox tipoListBox;
	@UiField Label tipoLabel;
	@UiField(provided=true) NumberTextBox codigoNumberTextBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	@UiField Button printButton;
	@UiField Image addDetalheImage;

	@UiField(provided=true) FlexTable encontroInscricaoFlexTable;
	private FlexTableUtil encontroInscricaoTableUtil = new FlexTableUtil();

	@UiField DadosPagamento dadosPagamentoComponent;
	@UiField TextBox dataFichaEnviadaAfilhadoDateBox;
	@UiField TextBox dataFichaRecebidaAfilhadoDateBox;
	@UiField TextBox dataFichaAtualizadaAfilhadoDateBox;

	//detalhe
	@UiField DialogBox detalheDialogBox;
	@UiField TextBox descricaoDetalheTextBox;
	@UiField(provided=true) NumberTextBox valorUnitarioDetalheNumberTextBox;
	@UiField(provided=true) NumberTextBox quantidadeDetalheNumberTextBox;
	@UiField(provided=true) NumberTextBox valorDetalheNumberTextBox;
	@UiField Button salvarDetalheButton;
	@UiField TextBox valorLabel;
	@UiField(provided=true) FlexTable detalheFlexTable;
	private FlexTableUtil detalheTableUtil = new FlexTableUtil();
	@UiField ListBox confirmacaoListBox;
	@UiField ListBox tipoDetalheListBox;
	@UiField ListBox tipoLancamentoListBox;
	@UiField(provided = true) RadioButton casalRadio;
	@UiField(provided = true) RadioButton pessoaRadio;
	@UiField(provided = true) SuggestBox inscricaoOutraSuggestBox;
	@UiField Label labelOutraInscricao;
	private final GenericEntitySuggestOracle inscricaoOutraSuggest = new GenericEntitySuggestOracle();

	NumberFormat dfCurrency = NumberFormat.getCurrencyFormat();

	private EncontroInscricaoVO entidadeEditada;
	private Casal casalEditado;
	private Pessoa pessoaEditada;

	private List<Mensagem> listaMensagem;

	private EncontroInscricaoPagamentoDetalhe entidadeEditadaDetalhe;

	private List<EncontroInscricaoVO> listaEncontroVO;

	public EncontroInscricaoView() {
		criaTabela();
		criaTabelaDetalhe();
		casalSuggest.setMinimoCaracteres(2);
		casalSuggest.setSuggestQuery("casal.porNomeLike");
		casalSuggestBox = new SuggestBox(casalSuggest);

		pessoaSuggest.setMinimoCaracteres(2);
		pessoaSuggest.setSuggestQuery("pessoa.porNomeLike");
		pessoaSuggestBox = new SuggestBox(pessoaSuggest);

		inscricaoOutraSuggest.setMinimoCaracteres(2);
		inscricaoOutraSuggestBox = new SuggestBox(inscricaoOutraSuggest);

		inscricaoOutraSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				EncontroInscricao outra = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoOutraSuggest.getListaEntidades(), inscricaoOutraSuggestBox.getValue());
				presenter.getValorEncontroOutro(entidadeEditada.getEncontroInscricao(),outra);
			}
		});

		casalRadio = new RadioButton("tipo", "Por Casal");
		pessoaRadio = new RadioButton("tipo", "Por Pessoa");

		codigoNumberTextBox = new NumberTextBox(false, false, 2, 2);
		valorUnitarioDetalheNumberTextBox = new NumberTextBox(true, false, 16, 16, Formato.MOEDA);
		quantidadeDetalheNumberTextBox = new NumberTextBox(false, false, 2, 2);
		valorDetalheNumberTextBox = new NumberTextBox(true, false, 16, 16, Formato.MOEDA);
		valorDetalheNumberTextBox.setEnabled(false);

		initWidget(uiBinder.createAndBindUi(this));

		dataMaxParcelaDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		dataMaxParcelaDateBox.getTextBox().setAlignment(TextAlignment.CENTER);

		tituloFormularioLabel.setText(getDisplayTitle());

		casalSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				casalEditado = null;
				pessoaEditada = null;
				if(!casalSuggestBox.getValue().equals("")){
					casalEditado = (Casal)ListUtil.getEntidadePorNome(casalSuggest.getListaEntidades(), casalSuggestBox.getValue());
					emailLabel.setText(casalEditado.getEmails(","));
					pessoaSuggestBox.setValue(null);
				}
				if(entidadeEditada.getEncontroInscricao().getId()==null){
					for (EncontroInscricaoVO ei : listaEncontroVO) {
						if(ei.getEncontroInscricao().getCasal()!=null && 
						   ei.getEncontroInscricao().getCasal().getId().equals(casalEditado.getId()) &&
						   !ei.getEncontroInscricao().getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
							Window.alert(casalEditado.toString() + "\nEste casal já tem inscrição neste encontro !");
							emailLabel.setText(null);
							casalSuggestBox.setValue(null);
							casalEditado = null;
							pessoaEditada = null;
							return;
						}
					}
				}
				dadosPagamentoComponent.setPessoaInscrita(pessoaEditada);
				dadosPagamentoComponent.setCasalInscrito(casalEditado);
				if (casalEditado.getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA))
					ListBoxUtil.setItemSelected(tipoListBox, TipoInscricaoEnum.APOIO.toString());
				else if (casalEditado.getTipoCasal().equals(TipoCasalEnum.CONVIDADO))
					ListBoxUtil.setItemSelected(tipoListBox, TipoInscricaoEnum.AFILHADO.toString());
				tipoListBox.setEnabled(true);
				entidadeEditada.getEncontroInscricao().setTipo((TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values()));
				entidadeEditada.geraPagamentoDetalhe();
				populaDetalhes(entidadeEditada.getListaPagamentoDetalhe(),true);
			}
		});
		pessoaSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				pessoaEditada = null;
				casalEditado = null;
				if(!pessoaSuggestBox.getValue().equals("")){
					pessoaEditada = (Pessoa)ListUtil.getEntidadePorNome(pessoaSuggest.getListaEntidades(), pessoaSuggestBox.getValue());
					emailLabel.setText(pessoaEditada.getEmail());
					casalSuggestBox.setValue(null);
				}
				if(entidadeEditada.getEncontroInscricao().getId()==null){
					for (EncontroInscricaoVO ei : listaEncontroVO) {
						if(ei.getEncontroInscricao().getPessoa()!=null && 
						   ei.getEncontroInscricao().getPessoa().getId().equals(pessoaEditada.getId()) &&
						   !ei.getEncontroInscricao().getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
							Window.alert(pessoaEditada.toString() + "\nEsta pessoa já tem inscrição neste encontro !");
							emailLabel.setText(null);
							pessoaSuggestBox.setValue(null);
							casalEditado = null;
							pessoaEditada = null;
							return;
						}
					}
				}
				dadosPagamentoComponent.setPessoaInscrita(pessoaEditada);
				dadosPagamentoComponent.setCasalInscrito(casalEditado);
				ListBoxUtil.setItemSelected(tipoListBox, TipoInscricaoEnum.APOIO.toString());
				tipoListBox.setEnabled(false);
				entidadeEditada.getEncontroInscricao().setTipo((TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values()));
				entidadeEditada.geraPagamentoDetalhe();
				populaDetalhes(entidadeEditada.getListaPagamentoDetalhe(),true);
			}
		});
		codigoNumberTextBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent arg0) {
				if(codigoNumberTextBox.getNumber()!=null){
					dadosPagamentoComponent.defineAvisos(codigoNumberTextBox.getNumber().intValue());
				} else {
					dadosPagamentoComponent.defineAvisos(null);
				}
			}
		});
		dataMaxParcelaDateBox.addDomHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent arg0) {
				dadosPagamentoComponent.defineMaximaDataPagamento(dataMaxParcelaDateBox.getValue());
			}
		}, BlurEvent.getType());
		ListBoxUtil.populate(tipoListBox, false, TipoInscricaoEnum.values());
		ListBoxUtil.populate(confirmacaoListBox, false, TipoConfirmacaoEnum.values());

		tipoInscricaoListBox.addItem("");
		for (TipoInscricaoEnum tipo : TipoInscricaoEnum.values()) {
			tipoInscricaoListBox.addItem(tipo.toString());
		}
	}

	private void criaTabela() {
		encontroInscricaoFlexTable = new FlexTable();
		encontroInscricaoFlexTable.setStyleName("portal-formSmall");
		encontroInscricaoTableUtil.initialize(encontroInscricaoFlexTable);

		encontroInscricaoTableUtil.addColumn("", "10px", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoTableUtil.addColumn("Código", "50px", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.NUMBER, null);
		encontroInscricaoTableUtil.addColumn("Tipo", "80px", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoTableUtil.addColumn("Nome", "500px", HasHorizontalAlignment.ALIGN_LEFT);
		encontroInscricaoTableUtil.addColumn("Env. Ficha", "120px", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
		encontroInscricaoTableUtil.addColumn("Rec. Ficha", "120px", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
		encontroInscricaoTableUtil.addColumn("Pre. Ficha", "120px", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
		encontroInscricaoTableUtil.addColumn("Confirmação", "70", HasHorizontalAlignment.ALIGN_CENTER);
	}
	private void criaTabelaDetalhe() {
		detalheFlexTable = new FlexTable();
		detalheFlexTable.setStyleName("portal-formSmall");
		detalheTableUtil.initialize(detalheFlexTable);

		detalheTableUtil.addColumn("", "10px", HasHorizontalAlignment.ALIGN_CENTER);
		detalheTableUtil.addColumn("Descrição", "200px", HasHorizontalAlignment.ALIGN_LEFT);
		detalheTableUtil.addColumn("T", "20px", HasHorizontalAlignment.ALIGN_RIGHT, TipoColuna.NUMBER, null);
		detalheTableUtil.addColumn("Valor Unit.", "100px", HasHorizontalAlignment.ALIGN_RIGHT, TipoColuna.NUMBER, null);
		detalheTableUtil.addColumn("Qtde", "50px", HasHorizontalAlignment.ALIGN_RIGHT, TipoColuna.NUMBER, null);
		detalheTableUtil.addColumn("Valor", "100px", HasHorizontalAlignment.ALIGN_RIGHT, TipoColuna.NUMBER, null);
		detalheTableUtil.addColumn("Inscrição", "300px", HasHorizontalAlignment.ALIGN_LEFT);
	}
	@Override
	public void init(){
		if(presenter.getDadosLoginVO().getUsuario()==null) return;
		centralPanel.setVisible(false);
		addDetalheImage.setVisible(false);
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
				presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
			centralPanel.setVisible(true);
			addDetalheImage.setVisible(true);
		}
	}

	@UiHandler("printButton")
	public void printButtonClickHandler(ClickEvent event){
		printWidget(encontroInscricaoFlexTable);
	}


	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		if(dadosPagamentoComponent.getDadosAlterados()){
			if(!Window.confirm("Foram alteradas as opções de pagamento mas não foram salvas.\nDeseja sair mesmo assim?")){
				return;
			}
		}
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
		entidadeEditada.getEncontroInscricao().setEncontro(presenter.getEncontroSelecionado());
		entidadeEditada.getEncontroInscricao().setCasal(casalEditado);
		entidadeEditada.getEncontroInscricao().setPessoa(pessoaEditada);
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
				presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
			entidadeEditada.getEncontroInscricao().setTipo((TipoInscricaoEnum)ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values()));
		}
		entidadeEditada.getEncontroInscricao().setCodigo(null);
		if(codigoNumberTextBox.getNumber()!=null){
			entidadeEditada.getEncontroInscricao().setCodigo(codigoNumberTextBox.getNumber().intValue());
		}
		//salvando dados do pagamento
		entidadeEditada.getEncontroInscricao().setDataMaximaParcela(dataMaxParcelaDateBox.getValue());
		entidadeEditada.getEncontroInscricao().setEsconderPlanoPagamento(esconderPagamentoCheckBox.getValue());
		entidadeEditada.getEncontroInscricao().setHospedagemParticular(hospedagemParticularCheckBox.getValue());
		entidadeEditada.getEncontroInscricao().setTipoConfirmacao((TipoConfirmacaoEnum) ListBoxUtil.getItemSelected(confirmacaoListBox, TipoConfirmacaoEnum.values()));
		entidadeEditada.setMarcaFichaPreenchida(marcaPreenchimentoFichaCheckBox.getValue());

		if(!verificaDadosPagamento()){
			if(!Window.confirm("Não foram definidas suas opções de pagamento.\nDeseja sair mesmo assim?")){
				return;
			}
		}
		entidadeEditada.setMantemValores(true);
		presenter.salvar(entidadeEditada);
	}

	private Boolean verificaDadosPagamento(){
		boolean verificar = false;
		if(entidadeEditada.getEncontroInscricao().getCasal()!=null){
			if(presenter.getDadosLoginVO().getCasal().getId().equals(entidadeEditada.getEncontroInscricao().getCasal().getId())){
				verificar = true;
			}
		} else if (entidadeEditada.getEncontroInscricao().getPessoa()!=null){
			if(presenter.getDadosLoginVO().getUsuario().getPessoa().getId().equals(entidadeEditada.getEncontroInscricao().getPessoa().getId())){
				verificar = true;
			}
		}
		boolean dadosPagamento = true;
		if(verificar){
			dadosPagamento = false;
			for (EncontroInscricaoPagamento pagamento : entidadeEditada.getListaPagamento()) {
				if(!pagamento.getInscricao()){
					dadosPagamento = true;
					break;
				}
			}
		}
		return dadosPagamento;
	}

	@Override
	public void edita(EncontroInscricao encontroInscricao) {
		limpaCampos();
		if(encontroInscricao == null){
			entidadeEditada = new EncontroInscricaoVO();
			entidadeEditada.setEncontroInscricao(new EncontroInscricao());
			entidadeEditada.getEncontroInscricao().setEncontro(presenter.getEncontroSelecionado());
			entidadeEditada.setListaPagamento(new ArrayList<EncontroInscricaoPagamento>());
			entidadeEditada.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
			entidadeEditada.getEncontroInscricao().setHospedagemParticular(false);
			dadosPagamentoComponent.setUsuario(presenter.getDadosLoginVO().getUsuario());
			dadosPagamentoComponent.setCasal(presenter.getDadosLoginVO().getCasal());
			dadosPagamentoComponent.setEncontroInscricaoVO(entidadeEditada);

			casalSuggestBox.setEnabled(true);
			pessoaSuggestBox.setEnabled(true);

			editaDialogBox.center();
			editaDialogBox.show();
			if (presenter.getEncontroSelecionado().getUsaFichaPagamento().equals(1))
				casalSuggestBox.setFocus(true);
			else
				codigoNumberTextBox.setFocus(true);
		} else {
			presenter.getVO(encontroInscricao);
		}
	}

	public void limpaCampos(){
		casalSuggestBox.setEnabled(false);
		pessoaSuggestBox.setEnabled(false);
		ListBoxUtil.setItemSelected(tipoListBox, TipoInscricaoEnum.APOIO.toString());
		tabPanel.selectTab(0);
		tabPanel.getTabWidget(1).getParent().setVisible(false);
		pessoaSuggestBox.setValue(null);
		casalSuggestBox.setValue(null);
		codigoNumberTextBox.setNumber(null);
		tipoLabel.setVisible(false);
		tipoLabel.setText(null);
		emailLabel.setText(null);
		tipoListBox.setVisible(false);
		participanteHTMLPanel.setVisible(false);
		participanteLabel.setText(null);
		dataMaxParcelaDateBox.setValue(null);
		esconderPagamentoCheckBox.setValue(false);
		esconderPagamentoCheckBox.setVisible(false);
		hospedagemParticularCheckBox.setValue(false);
		hospedagemParticularCheckBox.setVisible(false);
		detalheTableUtil.clearData();
		detalheHTMLPanel.setVisible(false);
		valorHTMLPanel.setVisible(false);
		confirmacaoListBox.setSelectedIndex(0);
		ListBoxUtil.setItemSelected(confirmacaoListBox, TipoConfirmacaoEnum.CONFIRMADO.toString());
		tabPanel.setHeight("460px");


		dataFichaEnviadaAfilhadoDateBox.setValue(null);
		dataFichaRecebidaAfilhadoDateBox.setValue(null);
		dataFichaAtualizadaAfilhadoDateBox.setValue(null);
		dataLimiteHTMLPanel.setVisible(false);
		marcaPreenchimentoFichaCheckBox.setValue(false);

		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
				presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
			tabPanel.getTabWidget(1).getParent().setVisible(true);
			dataLimiteHTMLPanel.setVisible(true);
			tipoLabel.setVisible(false);
			tipoListBox.setVisible(true);
			casalHTMLPanel.setVisible(true);
			pessoaHTMLPanel.setVisible(true);
			fichaHTMLPanel.setVisible(true);
			codigoNumberTextBox.setEnabled(true);
			detalheHTMLPanel.setVisible(true);
			if (presenter.getEncontroSelecionado().getUsaFichaPagamento().equals(1))
				codigoNumberTextBox.setEnabled(false);
			marcaPreenchimentoFichaCheckBox.setVisible(true);
			esconderPagamentoCheckBox.setVisible(true);
			hospedagemParticularCheckBox.setVisible(true);
			tabPanel.setHeight("530px");
			valorHTMLPanel.setVisible(true);
		} else {
			participanteHTMLPanel.setVisible(true);
			casalHTMLPanel.setVisible(false);
			pessoaHTMLPanel.setVisible(false);
			fichaHTMLPanel.setVisible(false);
			tipoLabel.setVisible(true);
			tipoListBox.setVisible(false);
			codigoNumberTextBox.setEnabled(false);
		}
		dadosPagamentoComponent.limpaCampos();
	}

	public void defineCampos(EncontroInscricaoVO encontroInscricaoVO){
		entidadeEditada = encontroInscricaoVO;
		pessoaEditada = encontroInscricaoVO.getEncontroInscricao().getPessoa();
		casalEditado = encontroInscricaoVO.getEncontroInscricao().getCasal();
		if(casalEditado!=null){
			casalSuggestBox.setValue(casalEditado.toString());
			participanteLabel.setText(casalEditado.toString());
			emailLabel.setText(casalEditado.getEmails(","));
			pessoaHTMLPanel.setVisible(false);
		} else {
			pessoaSuggestBox.setValue(pessoaEditada.getNome());
			participanteLabel.setText(pessoaEditada.getNome());
			emailLabel.setText(pessoaEditada.getEmail());
			casalHTMLPanel.setVisible(false);
		}
		codigoNumberTextBox.setNumber(encontroInscricaoVO.getEncontroInscricao().getCodigo());
		if(encontroInscricaoVO.getEncontroInscricao().getTipo()!=null){
			tipoLabel.setText(encontroInscricaoVO.getEncontroInscricao().getTipo().getNome());
			ListBoxUtil.setItemSelected(tipoListBox, encontroInscricaoVO.getEncontroInscricao().getTipo().getNome());
			buscaMensagem(encontroInscricaoVO.getEncontroInscricao().getTipo());
		}
		esconderPagamentoCheckBox.setValue(encontroInscricaoVO.getEncontroInscricao().getEsconderPlanoPagamento());
		hospedagemParticularCheckBox.setValue(encontroInscricaoVO.getEncontroInscricao().getHospedagemParticular());
		dataMaxParcelaDateBox.setValue(encontroInscricaoVO.getEncontroInscricao().getDataMaximaParcela());
		if (!encontroInscricaoVO.getEncontroInscricao().getEsconderPlanoPagamento()){
			tabPanel.getTabWidget(1).getParent().setVisible(true);
			valorHTMLPanel.setVisible(true);
		}
		if (encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.AFILHADO)){
			hospedagemParticularCheckBox.setVisible(false);
		}

		//iniciando componente
		dadosPagamentoComponent.setUsuario(presenter.getDadosLoginVO().getUsuario());
		dadosPagamentoComponent.setCasal(presenter.getDadosLoginVO().getCasal());
		dadosPagamentoComponent.setEncontroInscricaoVO(encontroInscricaoVO);

		setDadosFicha(encontroInscricaoVO.getEncontroInscricao());

		if(entidadeEditada.getEncontroInscricao().getTipoConfirmacao()!=null){
			ListBoxUtil.setItemSelected(confirmacaoListBox, entidadeEditada.getEncontroInscricao().getTipoConfirmacao().getNome());
		}

		boolean atualiza = encontroInscricaoVO.isAtualizaValores();
		if (atualiza) {
			if (Window.confirm("Deseja atualizar os valor para esta inscrição?")){
				encontroInscricaoVO.geraPagamentoDetalhe();
				encontroInscricaoVO.defineParcelasPosiveis();
				encontroInscricaoVO.geraParcelas();
			}else
				atualiza = false;
		}
		populaDetalhes(encontroInscricaoVO.getListaPagamentoDetalhe(), atualiza);
	}

	@UiHandler("exibeDesistenciaCheckBox")
	public void exibeDesistenciaCheckBoxClickHandler(ClickEvent event){
		populaEntidades(listaEncontroVO);
	}


	@UiHandler("tipoInscricaoListBox")
	public void tipoInscricaoListBoxChangeHandler(ChangeEvent event) {
		populaEntidades(listaEncontroVO);
	}

	@UiHandler("hospedagemParticularCheckBox")
	public void hospedagemParticularCheckBoxClickHandler(ClickEvent event){
		TipoInscricaoEnum tipo = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values());
		if ((tipo!=null) && presenter.getEncontroSelecionado().getUsaDetalheAutomatico().equals(1)){
			entidadeEditada.getEncontroInscricao().setHospedagemParticular(hospedagemParticularCheckBox.getValue());
			entidadeEditada.geraPagamentoDetalhe();
			populaDetalhes(entidadeEditada.getListaPagamentoDetalhe(),true);
		}
	}

	@Override
	public void setDadosFicha(EncontroInscricao encontroInscricao) {
		entidadeEditada.setEncontroInscricao(encontroInscricao);
		if(entidadeEditada.getEncontroInscricao()!=null){
			if(entidadeEditada.getEncontroInscricao().getMensagemDestinatario()!=null){
				dataFichaEnviadaAfilhadoDateBox.setValue(entidadeEditada.getEncontroInscricao().getMensagemDestinatario().getDataEnvioStr());
				dataFichaRecebidaAfilhadoDateBox.setValue(entidadeEditada.getEncontroInscricao().getMensagemDestinatario().getDataConfirmacaoStr());
			}
			dataFichaAtualizadaAfilhadoDateBox.setValue(entidadeEditada.getEncontroInscricao().getDataPrenchimentoFichaStr());
		}
	}

	@Override
	public String getDisplayTitle() {
		return "Inscrição no Encontro";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		encontroInscricaoTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<EncontroInscricaoVO> lista) {
		listaEncontroVO = lista;
		Collections.sort(lista, new Comparator<EncontroInscricaoVO>() {
			@Override
			public int compare(EncontroInscricaoVO o1, EncontroInscricaoVO o2) {
				String s1="", s2="";
				if(o1.getEncontroInscricao().getCodigo()!=null) s1 = o1.getEncontroInscricao().getCodigo().toString();
				if(o2.getEncontroInscricao().getCodigo()!=null) s2 = o2.getEncontroInscricao().getCodigo().toString();
				if (o1.getEncontroInscricao().getTipo().equals(o2.getEncontroInscricao().getTipo())){
					if(o1.getEncontroInscricao().getCasal()!=null){
						s1 = s1 + o1.getEncontroInscricao().getCasal().toString();
					} else {
						s1 = s1 + o1.getEncontroInscricao().getPessoa().toString();
					}
					if(o2.getEncontroInscricao().getCasal()!=null){
						s2 = s2 + o2.getEncontroInscricao().getCasal().toString();
					} else {
						s2 = s2 + o2.getEncontroInscricao().getPessoa().toString();
					}
				} else {
					s1 = o1.getEncontroInscricao().getTipo().getCodigo() + s1;
					s2 = o2.getEncontroInscricao().getTipo().getCodigo() + s2;
				}
				return s1.compareTo(s2);
			}
		});

		boolean podeEditar = false;
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
				presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
			podeEditar = true;
		}

		TipoInscricaoEnum tipoInscricao = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoInscricaoListBox, TipoInscricaoEnum.values());

		encontroInscricaoTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		boolean ok = false, exibeLinha;
		int coordenador = 0, padrinho=0, apoio=0, afilhado=0, desistencia=0, externo=0, doacao = 0;
		for (EncontroInscricaoVO encontroInscricaoVO: lista) {
			final EncontroInscricao encontroInscricao = encontroInscricaoVO.getEncontroInscricao();
			exibeLinha = true;
			if((encontroInscricao.getTipoConfirmacao()!=null) && encontroInscricao.getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA) && !exibeDesistenciaCheckBox.getValue()){
				exibeLinha = false;
			}
			if(tipoInscricao!=null){
				if(!tipoInscricao.equals(encontroInscricaoVO.getEncontroInscricao().getTipo())){
					exibeLinha = false;
				}
			}
			if((encontroInscricao.getTipoConfirmacao()!=null) && encontroInscricao.getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
				desistencia++;
			} else {
				if(encontroInscricao.getTipo().equals(TipoInscricaoEnum.COORDENADOR)){
					coordenador++;
				} else if(encontroInscricao.getTipo().equals(TipoInscricaoEnum.PADRINHO)){
					padrinho++;
				} else if(encontroInscricao.getTipo().equals(TipoInscricaoEnum.APOIO)){
					apoio++;
				} else if(encontroInscricao.getTipo().equals(TipoInscricaoEnum.AFILHADO)){
					afilhado++;
				} else if(encontroInscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO)){
					externo++;
				} else if(encontroInscricao.getTipo().equals(TipoInscricaoEnum.DOACAO)){
					doacao++;
				}
			}
			if(exibeLinha){
				row++;
				Object dados[] = new Object[8];
				hp = new HorizontalPanel();
				hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				hp.setSpacing(1);

				ok = false;
				if(encontroInscricao.getCasal()!=null){
					if(presenter.getDadosLoginVO().getCasal().getId().equals(encontroInscricao.getCasal().getId())){
						ok = true;
					}
				} else {
					if(presenter.getDadosLoginVO().getUsuario().getPessoa().getId().equals(encontroInscricao.getPessoa().getId())){
						ok = true;
					}
				}
				if(podeEditar || ok){
					editar = new Image(NavegadorUtil.makeUrlResource("images/edit.png"));
					editar.setTitle("Editar as informações da inscrição");
					editar.setStyleName("portal-ImageCursor");
					editar.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent arg0) {
							edita(encontroInscricao);
						}
					});
					hp.add(editar);
				}

				if(podeEditar){
					excluir = new Image(NavegadorUtil.makeUrlResource("images/delete.png"));
					excluir.setTitle("Excluir esta inscrição");
					excluir.setStyleName("portal-ImageCursor");
					excluir.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent arg0) {
							if(Window.confirm("Deseja excluir esta inscrição ?\n(Todas as informações de pagamento\n e das atividades relacionadas serão removidas)")){
								presenter.excluir(encontroInscricao);
							}
						}
					});
					hp.add(excluir);
				}

				dados[0] = hp;
				if (encontroInscricao.getFichaPagamento()!=null){
					if (encontroInscricao.getFichaPagamento().getStatus().equals(TipoInscricaoFichaStatusEnum.LIBERADO))
						dados[1] = encontroInscricao.getFichaPagamento().toString() + "-L";
					else
						dados[1] = encontroInscricao.getFichaPagamento().toString();
				}else
					dados[1] = encontroInscricao.getCodigo();
				dados[2] = encontroInscricao.getTipo().getNome();
				dados[3] = encontroInscricao.getCasal()==null?encontroInscricao.getPessoa().getNome():encontroInscricao.getCasal().toString();

				if(encontroInscricao.getMensagemDestinatario()!=null){
					dados[4] = encontroInscricao.getMensagemDestinatario().getDataEnvioStr();
					dados[5] = encontroInscricao.getMensagemDestinatario().getDataConfirmacaoStr();
				}
				dataFichaAtualizadaAfilhadoDateBox.setValue(encontroInscricao.getDataPrenchimentoFichaStr());
				dados[6] = encontroInscricao.getDataPrenchimentoFichaStr();

				if(encontroInscricao.getTipoConfirmacao()!=null){
					dados[7] = encontroInscricao.getTipoConfirmacao().getNome();
				} else {
					dados[7] = TipoConfirmacaoEnum.CONFIRMADO.getNome();
				}
				encontroInscricaoTableUtil.addRow(dados,row);
				if((encontroInscricao.getTipoConfirmacao()!=null) && encontroInscricao.getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
					encontroInscricaoTableUtil.setRowSpecialStyle(row, "FlexTable-RowSpecialNormalGrayLineThrough");
				}
				if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
						presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
					if (encontroInscricaoVO.isAtualizaValores())
						encontroInscricaoTableUtil.setRowSpecialStyle(row, "FlexTable-RowSpecialNormalRed");
				}

			}
		}
		LabelTotalUtil.setTotal(itemTotal, row, "inscrição", "inscrições", "a");
		String totais = "";
		if((coordenador==1) || (coordenador==0)){
			totais += coordenador + " coordenador";
		} else {
			totais += coordenador + " coordenadores";
		}
		if((padrinho==1) || (padrinho==0)){
			totais += " / " + padrinho + " padrinho";
		} else {
			totais += " / " + padrinho + " padrinhos";
		}
		if((apoio==1) || (apoio==0)){
			totais += " / " + apoio + " apoio";
		} else {
			totais += " / " + apoio + " apoios";
		}
		if((afilhado==1) || (afilhado==0)){
			totais += " / " + afilhado + " afilhado";
		} else {
			totais += " / " + afilhado + " afilhados";
		}
		if((externo==1) || (externo==0)){
			totais += " / " + externo + " externo";
		} else {
			totais += " / " + externo + " externos";
		}
		if((desistencia==1) || (desistencia==0)){
			totais += " / " + desistencia + " desistencia";
		} else {
			totais += " / " + desistencia + " desistencias";
		}
		if((doacao==1) || (doacao==0)){
			totais += " / " + doacao + " doação";
		} else {
			totais += " / " + doacao + " doações";
		}
		if(((coordenador+apoio+padrinho)==1) || ((coordenador+apoio+padrinho)==0)){
			totais += " / " + (coordenador+apoio+padrinho) + " encontrista";
		} else {
			totais += " / " + (coordenador+apoio+padrinho) + " encontristas";
		}
		totalLabel.setText(totais);
		encontroInscricaoTableUtil.applyDataRowStyles();
		showWaitMessage(false);
	}

	@Override
	public void setVO(EncontroInscricaoVO vo) {
		defineCampos(vo);
		editaDialogBox.center();
		editaDialogBox.show();
		if (presenter.getEncontroSelecionado().getUsaFichaPagamento().equals(1))
			casalSuggestBox.setFocus(true);
		else
			codigoNumberTextBox.setFocus(true);
	}

	@UiHandler("enviarFichaButton")
	public void enviarFichaButtonClickHandler(ClickEvent event){
		if(entidadeEditada.getEncontroInscricao().getId()==null){
			Window.alert("Esta inscrição nunca foi salva.\nNão será possível enviar a ficha.");
			return;
		}
		if(codigoNumberTextBox.getNumber()==null){
			if(!Window.confirm("Não foi definido CÓDIGO para esta inscrição.\n\n" +
					"Caso deseje enviar a mensagem de inscrição mesmo assim, verifique a mensagem selecionada.\n\n" +
					"Deseja enviar a mensagem mesmo assim?")){
				return;
			}
		}
		Mensagem mensagem = (Mensagem) ListBoxUtil.getItemSelected(mensagemListBox, listaMensagem);
		if(mensagem==null){
			Window.alert("Não foi definida a mensagem da FICHA a enviar.\nNão será possível enviar a ficha.");
			return;
		}
		String email = emailLabel.getText();
		if(email!=null){
			email = email.trim().replace(",", "");
		}
		if((email==null) || email.equals("")){
			Window.alert("O participante não possui email cadastrado.\nNão será possível enviar a ficha.");
			return;
		}
		if(entidadeEditada.getEncontroInscricao().getCodigo()!=null && codigoNumberTextBox.getNumber() !=null && 
		   codigoNumberTextBox.getNumber().intValue()!=entidadeEditada.getEncontroInscricao().getCodigo().intValue()){
			Window.alert("O CÓDIGO para esta inscrição foi alterado.\nSalve esta inscrição antes de tentar enviá-la novamente.");
			return;
		}
		if((dataFichaEnviadaAfilhadoDateBox.getValue()!=null) && !dataFichaEnviadaAfilhadoDateBox.getValue().equals("")){
			if(!Window.confirm("A ficha já foi enviada anteriormente.\nDeseja re-enviar a ficha ?")){
				return;
			}
		}
		if(!entidadeEditada.getEncontroInscricao().getTipo().equals(ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values()))){
			if(Window.confirm("O tipo selecionado é diferente do tipo salvo. Deseja alterar o tipo desta inscrição?")){
				entidadeEditada.getEncontroInscricao().setTipo((TipoInscricaoEnum) (ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values())));
			} else {
				return;
			}
		}
		presenter.enviaFicha(mensagem, entidadeEditada.getEncontroInscricao());
	}

	@UiHandler("tipoListBox")
	public void tipoListBoxChangeHandler(ChangeEvent event) {
		TipoInscricaoEnum tipo = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values());
		if ((tipo!=null) && presenter.getEncontroSelecionado().getUsaDetalheAutomatico().equals(1)){
			entidadeEditada.getEncontroInscricao().setTipo(tipo);
			entidadeEditada.geraPagamentoDetalhe();
			populaDetalhes(entidadeEditada.getListaPagamentoDetalhe(),true);
		}
		buscaMensagem(tipo);
	}
	
	@UiHandler("confirmacaoListBox")
	public void confirmacaoListBoxChangeHandler(ChangeEvent event) {
		TipoConfirmacaoEnum tipoconfirmacao = (TipoConfirmacaoEnum) ListBoxUtil.getItemSelected(confirmacaoListBox, TipoConfirmacaoEnum.values());
		if(entidadeEditada.getEncontroInscricao()!=null && 
		   entidadeEditada.getEncontroInscricao().getId()!=null && 
		   !tipoconfirmacao.equals(TipoConfirmacaoEnum.DESISTENCIA)){
			if(entidadeEditada.getEncontroInscricao().getCasal()!=null){
				for (EncontroInscricaoVO ei : listaEncontroVO) {
					if( ei.getEncontroInscricao().getCasal()!=null && 
						ei.getEncontroInscricao().getCasal().getId().equals(casalEditado.getId()) &&
						!ei.getEncontroInscricao().getId().equals(entidadeEditada.getEncontroInscricao().getId()) &&
						!ei.getEncontroInscricao().getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
						Window.alert(ei.getEncontroInscricao().getCasal().toString() + "\nEste casal já tem inscrição CONFIRMADA neste encontro !");
						if(entidadeEditada.getEncontroInscricao().getTipoConfirmacao()!=null){
							ListBoxUtil.setItemSelected(confirmacaoListBox, entidadeEditada.getEncontroInscricao().getTipoConfirmacao().toString());
						} else {
							confirmacaoListBox.setSelectedIndex(-1);
						}
						return;
					}
				}
			} else if(pessoaEditada!=null){
				for (EncontroInscricaoVO ei : listaEncontroVO) {
					if( ei.getEncontroInscricao().getPessoa()!=null && 
						ei.getEncontroInscricao().getPessoa().getId().equals(pessoaEditada.getId()) &&
						!ei.getEncontroInscricao().getId().equals(entidadeEditada.getEncontroInscricao().getId()) &&
						!ei.getEncontroInscricao().getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
						Window.alert(ei.getEncontroInscricao().getPessoa().toString() + "\nEsta pessoa já tem inscrição CONFIRMADA neste encontro !");
						if(entidadeEditada.getEncontroInscricao().getTipoConfirmacao()!=null){
							ListBoxUtil.setItemSelected(confirmacaoListBox, entidadeEditada.getEncontroInscricao().getTipoConfirmacao().toString());
						} else {
							confirmacaoListBox.setSelectedIndex(-1);
						}
						return;
					}
				}
			}
		}
	}

	private void buscaMensagem(TipoInscricaoEnum tipo) {
		mensagemListBox.clear();
		TipoMensagemEnum tipoMensagem = TipoMensagemEnum.FICHA_AFILHADO;
		if(!tipo.equals(TipoInscricaoEnum.AFILHADO)){
			tipoMensagem = TipoMensagemEnum.FICHA_ENCONTRISTA;
		}
		for (Mensagem mensagem : listaMensagem) {
			if(mensagem.getTipoMensagem().equals(tipoMensagem)){
				mensagemListBox.addItem(mensagem.toString());
			}
		}
	}

	@Override
	public void populaMensagem(List<Mensagem> result) {
		listaMensagem = result;
	}

	public void populaDetalhes(List<EncontroInscricaoPagamentoDetalhe> lista, boolean atualiza) {
		boolean podeEditar = false;
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR) ||
				presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ROOT)){
			podeEditar = true;
		}
		detalheTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroInscricaoPagamentoDetalhe detalhe: lista) {
			Object dados[] = new Object[7];
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);

			if(podeEditar && detalhe.getEditavel()){
				editar = new Image(NavegadorUtil.makeUrlResource("images/edit.png"));
				editar.setTitle("Editar este detalhe");
				editar.setStyleName("portal-ImageCursor");
				editar.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						editaDetalhe(detalhe);
					}
				});
				hp.add(editar);
			}

			if(podeEditar && detalhe.getEditavel()){
				excluir = new Image(NavegadorUtil.makeUrlResource("images/delete.png"));
				excluir.setTitle("Excluir este detalhe");
				excluir.setStyleName("portal-ImageCursor");
				excluir.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						if(Window.confirm("Deseja excluir este detalhamento ?")){
							entidadeEditada.getListaPagamentoDetalhe().remove(detalhe);
							populaDetalhes(entidadeEditada.getListaPagamentoDetalhe(),true);
						}
					}
				});
				hp.add(excluir);
			}

			dados[0] = hp;
			if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.AVULSO))
				dados[1] = detalhe.getDescricao();
			else
				dados[1] = detalhe.getTipoDetalhe().getNome();

			dados[2] = detalhe.getTipoLancamento().getNome();

			if( detalhe.getValorUnitario()!=null){
				dados[3] = dfCurrency.format(detalhe.getValorUnitario());
			}
			if( detalhe.getQuantidade()!=null){
				dados[4] = detalhe.getQuantidade().toString();
			}
			if( detalhe.getValor()!=null){
				dados[5] = dfCurrency.format(detalhe.getValor());
			}
			if( detalhe.getEncontroInscricaoOutra()!=null){
				dados[6] = detalhe.getEncontroInscricaoOutra().toStringApelidos();
			}
			detalheTableUtil.addRow(dados,row+1);
			row++;
		}
		detalheTableUtil.applyDataRowStyles();
		somaValorEncontro(atualiza);
	}

	private void somaValorEncontro(boolean atualiza){
		double valor = 0;
		for (EncontroInscricaoPagamentoDetalhe detalhe: entidadeEditada.getListaPagamentoDetalhe()) {
			if(detalhe.getTipoLancamento().equals(TipoPagamentoLancamentoEnum.DEBITO))
				valor += detalhe.getValor().doubleValue();
			else
				valor -= detalhe.getValor().doubleValue();
		}
		dadosPagamentoComponent.setValorEncontro(new BigDecimal(valor),atualiza);
		valorLabel.setText(dfCurrency.format(entidadeEditada.getEncontroInscricao().getValorEncontro()));
	}

	public void limpaCamposDetalhe(){
		descricaoDetalheTextBox.setValue(null);
		valorUnitarioDetalheNumberTextBox.setValue(null);
		quantidadeDetalheNumberTextBox.setNumber(1);
		valorDetalheNumberTextBox.setValue(null);
		inscricaoOutraSuggestBox.setVisible(false);
		labelOutraInscricao.setVisible(false);
		casalRadio.setVisible(false);
		pessoaRadio.setVisible(false);
		casalRadio.setValue(true);
		labelOutraInscricao.setText("Casal:");
		inscricaoOutraSuggestBox.setValue(null);
		TipoInscricaoEnum tipo = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values());
		if ((tipo != null) && (tipo.equals(TipoInscricaoEnum.DOACAO) || tipo.equals(TipoInscricaoEnum.EXTERNO))){
			descricaoDetalheTextBox.setEnabled(false);
			descricaoDetalheTextBox.setValue(TipoPagamentoDetalheEnum.OUTRAINSCRICAO.toString());
			tipoDetalheListBox.clear();
			tipoDetalheListBox.addItem(TipoPagamentoDetalheEnum.OUTRAINSCRICAO.toString());
			tipoLancamentoListBox.clear();
			tipoLancamentoListBox.addItem(TipoPagamentoLancamentoEnum.DEBITO.toString());
		}else
			ListBoxUtil.populate(tipoDetalheListBox, false, TipoPagamentoDetalheEnum.values());
		ListBoxUtil.populate(tipoLancamentoListBox, false, TipoPagamentoLancamentoEnum.values());
	}
	public void defineCamposDetalhe(EncontroInscricaoPagamentoDetalhe detalhe){
		if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.AVULSO)){
			descricaoDetalheTextBox.setValue(detalhe.getDescricao());
			descricaoDetalheTextBox.setEnabled(true);
		}
		else{
			descricaoDetalheTextBox.setValue(detalhe.getTipoDetalhe().getNome());
			descricaoDetalheTextBox.setEnabled(false);
		}
		valorUnitarioDetalheNumberTextBox.setNumber(detalhe.getValorUnitario());
		quantidadeDetalheNumberTextBox.setNumber(detalhe.getQuantidade());
		quantidadeDetalheNumberTextBox.setEnabled(detalhe.getTipoDetalhe().getQuantidade());
		valorDetalheNumberTextBox.setNumber(detalhe.getValor());
		ListBoxUtil.setItemSelected(tipoDetalheListBox, detalhe.getTipoDetalhe().toString());
		ListBoxUtil.setItemSelected(tipoLancamentoListBox, detalhe.getTipoLancamento().toString());
		if (detalhe.getTipoDetalhe().equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO)){
			inscricaoOutraSuggestBox.setVisible(true);
			labelOutraInscricao.setVisible(true);
			casalRadio.setVisible(true);
			pessoaRadio.setVisible(true);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("encontro", presenter.getEncontroSelecionado());
			inscricaoOutraSuggest.setQueryParams(params);
			inscricaoOutraSuggest.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
			if(detalhe.getEncontroInscricaoOutra() != null){
				inscricaoOutraSuggestBox.setValue(detalhe.getEncontroInscricaoOutra().toString());
				inscricaoOutraSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
				inscricaoOutraSuggest.getListaEntidades().add(detalhe.getEncontroInscricaoOutra());
			}
		}
	}

	public void editaDetalhe(EncontroInscricaoPagamentoDetalhe detalhe) {
		limpaCamposDetalhe();
		if(detalhe == null){
			entidadeEditadaDetalhe = new EncontroInscricaoPagamentoDetalhe();
			entidadeEditadaDetalhe.setEncontroInscricao(entidadeEditada.getEncontroInscricao());
			entidadeEditadaDetalhe.setEditavel(true);
			TipoInscricaoEnum tipo = (TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoInscricaoEnum.values());
			if ((tipo != null) && (tipo.equals(TipoInscricaoEnum.DOACAO) || tipo.equals(TipoInscricaoEnum.EXTERNO)) ){
				inscricaoOutraSuggestBox.setVisible(true);
				labelOutraInscricao.setVisible(true);
				casalRadio.setVisible(true);
				pessoaRadio.setVisible(true);
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("encontro", presenter.getEncontroSelecionado());
				inscricaoOutraSuggest.setQueryParams(params);
				inscricaoOutraSuggest.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
				ListBoxUtil.setItemSelected(tipoDetalheListBox, TipoPagamentoDetalheEnum.OUTRAINSCRICAO.toString());
			}
			else
				ListBoxUtil.setItemSelected(tipoDetalheListBox, TipoPagamentoDetalheEnum.AVULSO.toString());
			ListBoxUtil.setItemSelected(tipoLancamentoListBox, TipoPagamentoLancamentoEnum.DEBITO.toString());
		} else {
			if (!detalhe.getEditavel()) return;
			entidadeEditadaDetalhe = detalhe;
			defineCamposDetalhe(detalhe);
		}
		detalheDialogBox.center();
		detalheDialogBox.show();
		descricaoDetalheTextBox.setFocus(true);
	}

	@UiHandler("tipoDetalheListBox")
	public void tipoDetalheListBoxChangeHandler(ChangeEvent event) {
		TipoPagamentoDetalheEnum tipo = (TipoPagamentoDetalheEnum) ListBoxUtil.getItemSelected(tipoDetalheListBox, TipoPagamentoDetalheEnum.values());
		inscricaoOutraSuggestBox.setVisible(false);
		labelOutraInscricao.setVisible(false);
		casalRadio.setVisible(false);
		pessoaRadio.setVisible(false);
		inscricaoOutraSuggestBox.setValue(null);
		quantidadeDetalheNumberTextBox.setEnabled(tipo.getQuantidade());
		if (tipo.equals(TipoPagamentoDetalheEnum.AVULSO)){
			descricaoDetalheTextBox.setValue("");
			descricaoDetalheTextBox.setEnabled(true);
		}
		else{
			descricaoDetalheTextBox.setValue(tipo.getNome());
			descricaoDetalheTextBox.setEnabled(false);
			if (tipo.equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO)){
				inscricaoOutraSuggestBox.setVisible(true);
				labelOutraInscricao.setVisible(true);
				casalRadio.setVisible(true);
				pessoaRadio.setVisible(true);
				casalRadio.setValue(true);
				labelOutraInscricao.setText("Casal:");
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("encontro", presenter.getEncontroSelecionado());
				inscricaoOutraSuggest.setQueryParams(params);
				inscricaoOutraSuggest.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
			}
		}
	}

	@UiHandler("addDetalheImage")
	public void addDetalheImageClickHandler(ClickEvent event){
		editaDetalhe(null);
	}
	@UiHandler("fecharDetalheButton")
	public void fecharDetalheButtonClickHandler(ClickEvent event){
		detalheDialogBox.hide();
	}
	@UiHandler("salvarDetalheButton")
	public void salvarDetalheButtonClickHandler(ClickEvent event){
		EncontroInscricao inscricao = null;
		if(!inscricaoOutraSuggestBox.getValue().equals("")){
			inscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoOutraSuggest.getListaEntidades(), inscricaoOutraSuggestBox.getValue());
		}
		TipoPagamentoDetalheEnum tipoDetalhe = (TipoPagamentoDetalheEnum)ListBoxUtil.getItemSelected(tipoDetalheListBox, TipoPagamentoDetalheEnum.values());
		TipoPagamentoLancamentoEnum tipoLancamento = (TipoPagamentoLancamentoEnum)ListBoxUtil.getItemSelected(tipoLancamentoListBox, TipoPagamentoLancamentoEnum.values());
		if ((quantidadeDetalheNumberTextBox.getNumber() == null) || (valorUnitarioDetalheNumberTextBox.getNumber()==null) || (valorDetalheNumberTextBox.getNumber()==null) || (tipoDetalhe == null) || (tipoLancamento == null) )
			return;
		if (tipoDetalhe.equals(TipoPagamentoDetalheEnum.OUTRAINSCRICAO) && ( (inscricao==null) || inscricao.equals(entidadeEditada.getEncontroInscricao())) && tipoLancamento.equals(TipoPagamentoLancamentoEnum.CREDITO))
			return;
		entidadeEditadaDetalhe.setEncontroInscricaoOutra(inscricao);
		entidadeEditadaDetalhe.setTipoDetalhe(tipoDetalhe);
		entidadeEditadaDetalhe.setTipoLancamento(tipoLancamento);
		entidadeEditadaDetalhe.setValorUnitario(new BigDecimal(valorUnitarioDetalheNumberTextBox.getNumber().doubleValue()));
		entidadeEditadaDetalhe.setQuantidade(quantidadeDetalheNumberTextBox.getNumber().intValue());
		entidadeEditadaDetalhe.setDescricao(descricaoDetalheTextBox.getValue());
		entidadeEditadaDetalhe.setValor(new BigDecimal(valorDetalheNumberTextBox.getNumber().doubleValue()));
		if(entidadeEditada.getListaPagamentoDetalhe()==null){
			entidadeEditada.setListaPagamentoDetalhe(new ArrayList<EncontroInscricaoPagamentoDetalhe>());
		} else {
			entidadeEditada.getListaPagamentoDetalhe().remove(entidadeEditadaDetalhe);
		}
		entidadeEditada.getListaPagamentoDetalhe().add(entidadeEditadaDetalhe);
		detalheDialogBox.hide();
		populaDetalhes(entidadeEditada.getListaPagamentoDetalhe(),true);
	}

	@UiHandler(value={"valorUnitarioDetalheNumberTextBox","quantidadeDetalheNumberTextBox"})
	public void valorChangeEvent(ChangeEvent event){
		valorDetalheNumberTextBox.setNumber(valorUnitarioDetalheNumberTextBox.getNumber().doubleValue()*quantidadeDetalheNumberTextBox.getNumber().intValue());
	}

	@UiHandler("casalRadio")
	public void casalRadioClickHandler(ClickEvent event){
		labelOutraInscricao.setText("Casal:");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoOutraSuggest.setQueryParams(params);
		inscricaoOutraSuggest.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
	}

	@UiHandler("pessoaRadio")
	public void pessoaRadioClickHandler(ClickEvent event){
		labelOutraInscricao.setText("Pessoa:");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoOutraSuggest.setQueryParams(params);
		inscricaoOutraSuggest.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
	}

	@Override
	public void setValorEncontroOutro(double valorEncontro) {
		valorUnitarioDetalheNumberTextBox.setNumber(valorEncontro);
		valorDetalheNumberTextBox.setNumber(valorUnitarioDetalheNumberTextBox.getNumber().doubleValue()*quantidadeDetalheNumberTextBox.getNumber().intValue());
	}
}