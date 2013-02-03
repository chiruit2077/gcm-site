package br.com.ecc.client.ui.sistema.cadastro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.sistema.DadosPagamento;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.ui.component.upload.UploadImagePreview;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Casal;
import br.com.ecc.model.CasalContato;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoCasalContatoEnum;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;
import br.com.ecc.model.vo.CasalParamVO;
import br.com.ecc.model.vo.CasalVO;
import br.com.ecc.model.vo.EncontroInscricaoVO;
import br.com.freller.tool.client.Print;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class CasalView extends BaseView<CasalPresenter> implements CasalPresenter.Display {

	@UiTemplate("CasalView.ui.xml")
	interface CasalViewUiBinder extends UiBinder<Widget, CasalView> {}
	private CasalViewUiBinder uiBinder = GWT.create(CasalViewUiBinder.class);

	@UiField FlowPanel formularioFlowPanel;
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField VerticalPanel centralPanel;
	@UiField(provided=true) FlexTable casalFlexTable;
	private FlexTableUtil casalTableUtil = new FlexTableUtil();

	@UiField TextBox nomeTextBox;
	@UiField ListBox agrupamentoListBox;
	@UiField ListBox tipoInscricaoListBox;
	@UiField ListBox tipoCasalListBox;

	//ele
	@UiField TextBox eleNomeTextBox;
	@UiField(provided = true) SuggestBox eleSuggestBox;
	private final GenericEntitySuggestOracle eleSuggest = new GenericEntitySuggestOracle();

	@UiField TextBox eleApelidoTextBox;
	@UiField DateBox eleNascimentoDateBox;
	@UiField TextBox eleRgTextBox;
	@UiField TextBox eleExpedidorTextBox;
	@UiField TextBox eleNaturalidadeTextBox;
	@UiField TextBox eleProfissaoTextBox;
	@UiField TextBox eleCpfTextBox;
	@UiField TextBox eleEmailTextBox;
	@UiField TextBox eleTelefoneComercialTextBox;
	@UiField TextBox eleTelefoneCelularTextBox;
	@UiField CheckBox eleVegetarianoCheckBox;
	@UiField CheckBox eleDiabeticoCheckBox;
	@UiField CheckBox eleAlergicoCheckBox;
	@UiField CheckBox eleHipertensoCheckBox;
	@UiField TextBox eleAlergiaTextBox;
	@UiField TextBox eleNecessidadesTextBox;

	//ela
	@UiField TextBox elaNomeTextBox;
	@UiField(provided = true) SuggestBox elaSuggestBox;
	private final GenericEntitySuggestOracle elaSuggest = new GenericEntitySuggestOracle();

	@UiField TextBox elaApelidoTextBox;
	@UiField DateBox elaNascimentoDateBox;
	@UiField TextBox elaRgTextBox;
	@UiField TextBox elaExpedidorTextBox;
	@UiField TextBox elaNaturalidadeTextBox;
	@UiField TextBox elaProfissaoTextBox;
	@UiField TextBox elaCpfTextBox;
	@UiField TextBox elaEmailTextBox;
	@UiField TextBox elaTelefoneComercialTextBox;
	@UiField TextBox elaTelefoneCelularTextBox;
	@UiField CheckBox elaVegetarianoCheckBox;
	@UiField CheckBox elaDiabeticoCheckBox;
	@UiField CheckBox elaAlergicoCheckBox;
	@UiField CheckBox elaHipertensoCheckBox;
	@UiField TextBox elaAlergiaTextBox;
	@UiField TextBox elaNecessidadesTextBox;

	//casal
	@UiField HTMLPanel situacaoHTMLPanel;
	@UiField ListBox situacaoListBox;
	@UiField ListBox tipoListBox;
	@UiField TextBox enderecoTextBox;
	@UiField TextBox bairroTextBox;
	@UiField TextBox cepTextBox;
	@UiField TextBox cidadeTextBox;
	@UiField TextBox estadoTextBox;
	@UiField TextBox telefoneTextBox;
	@UiField DateBox casamentoDateBox;
	@UiField TextBox corTextBox;
	@UiField TextBox lugarTextBox;
	@UiField TextBox atividadeTextBox;
	@UiField TextBox musicaTextBox;
	@UiField HTMLPanel dadosAfilhadoHTMLPanel;

	@UiField(provided=true) FlexTable filhosFlexTable;
	private FlexTableUtil filhosTableUtil = new FlexTableUtil();

	@UiField(provided=true) FlexTable elePaisFlexTable;
	private FlexTableUtil elePaisTableUtil = new FlexTableUtil();

	@UiField(provided=true) FlexTable elaPaisFlexTable;
	private FlexTableUtil elaPaisTableUtil = new FlexTableUtil();

	@UiField(provided=true) FlexTable responsaveisFlexTable;
	private FlexTableUtil responsaveisTableUtil = new FlexTableUtil();

	@UiField(provided=true) FlexTable emergenciaFlexTable;
	private FlexTableUtil emergenciaTableUtil = new FlexTableUtil();

//	@UiField(provided=true) FlexTable indicacoesFlexTable;
//	private FlexTableUtil indicacoesTableUtil = new FlexTableUtil();

	@UiField DialogBox editaDialogBox;
	@UiField Button novoButton;
	@UiField Button salvarButton;
	@UiField Button printButton;
	@UiField HorizontalPanel ferramentasHorizontalPanel;

	//Contato
	@UiField DialogBox editaContatoDialogBox;
	@UiField HTMLPanel idadeHTMLPanel;
	@UiField TextBox nomeContatoTextBox;
	@UiField TextBox emailContatoTextBox;
	@UiField TextBox residencialContatoTextBox;
	@UiField TextBox comercialContatoTextBox;
	@UiField TextBox celularContatoTextBox;
	@UiField(provided=true) NumberTextBox idadeNumberTextBox;

	@UiField Image casalImage;
	//@UiField UploadArquivoDigital logotipoUploadArquivoDigital;

	//dados do pagamento
	@UiField HTMLPanel pagamentoHTMLPanel;
	@UiField DadosPagamento dadosPagamentoComponent;


	@UiField DialogBox imageDialogBox;
	@UiField UploadImagePreview uploadImagePreview;

	private CasalVO entidadeEditada;
	private CasalContato contatoEditado;

	private List<Agrupamento> listaAgrupamento;

	public CasalView() {
		criaTabela();
		criaTabelaFilhos();
		criaTabelaElePais();
		criaTabelaElaPais();
		criaTabelaResponsaveis();
		criaTabelaEmergencia();
//		criaTabelaIndicacoes();

		eleSuggest.setMinimoCaracteres(2);
		eleSuggest.setSuggestQuery("pessoa.porNomeLike");
		eleSuggestBox = new SuggestBox(eleSuggest);

		elaSuggest.setMinimoCaracteres(2);
		elaSuggest.setSuggestQuery("pessoa.porNomeLike");
		elaSuggestBox = new SuggestBox(elaSuggest);

		idadeNumberTextBox = new NumberTextBox(false, false, 5, 5);

		initWidget(uiBinder.createAndBindUi(this));

		eleNascimentoDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		eleNascimentoDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		elaNascimentoDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		elaNascimentoDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		casamentoDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		casamentoDateBox.getTextBox().setAlignment(TextAlignment.CENTER);

		tituloFormularioLabel.setText(getDisplayTitle());

		ListBoxUtil.populate(situacaoListBox, false, TipoSituacaoEnum.values());
		ListBoxUtil.populate(tipoListBox, false, TipoCasalEnum.values());

		tipoInscricaoListBox.addItem("");
		tipoInscricaoListBox.addItem("Todos os inscritos");
		for (TipoInscricaoEnum tipo : TipoInscricaoEnum.values()) {
			tipoInscricaoListBox.addItem(tipo.toString());
		}

		tipoCasalListBox.addItem("");
		for (TipoCasalEnum tipo : TipoCasalEnum.values()) {
			tipoCasalListBox.addItem(tipo.toString());
		}

		nomeTextBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				tipoInscricaoListBox.setSelectedIndex(0);
				int keyCode = event.getNativeKeyCode();
				if (keyCode == KeyCodes.KEY_ENTER) {
					buscarButtonClickHandler(null);
				}
			}
		});
		eleSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!eleSuggestBox.getValue().equals("")){
					definePessoa((Pessoa)ListUtil.getEntidadePorNome(eleSuggest.getListaEntidades(), eleSuggestBox.getValue()), true);
				}
			}
		});
		elaSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!elaSuggestBox.getValue().equals("")){
					definePessoa((Pessoa)ListUtil.getEntidadePorNome(elaSuggest.getListaEntidades(), elaSuggestBox.getValue()), true);
				}
			}
		});
		formularioFlowPanel.setHeight((this.getWindowHeight() - 150) + "px");
	}

	private void criaTabelaFilhos() {
		filhosFlexTable = new FlexTable();
		filhosFlexTable.setStyleName("portal-formSmall");
		filhosTableUtil.initialize(filhosFlexTable);

		filhosTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		filhosTableUtil.addColumn("Nome", "160", HasHorizontalAlignment.ALIGN_LEFT);
		filhosTableUtil.addColumn("Idade", "30", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.NUMBER, null);
		filhosTableUtil.addColumn("Residencial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		filhosTableUtil.addColumn("Comercial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		filhosTableUtil.addColumn("Celular", "50", HasHorizontalAlignment.ALIGN_LEFT);
		filhosTableUtil.addColumn("Email", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}
	private void criaTabelaElePais() {
		elePaisFlexTable = new FlexTable();
		elePaisFlexTable.setStyleName("portal-formSmall");
		elePaisTableUtil.initialize(elePaisFlexTable);

		elePaisTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		elePaisTableUtil.addColumn("Nome", "160", HasHorizontalAlignment.ALIGN_LEFT);
		elePaisTableUtil.addColumn("Residencial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		elePaisTableUtil.addColumn("Comercial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		elePaisTableUtil.addColumn("Celular", "50", HasHorizontalAlignment.ALIGN_LEFT);
		elePaisTableUtil.addColumn("Email", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}
	private void criaTabelaElaPais() {
		elaPaisFlexTable = new FlexTable();
		elaPaisFlexTable.setStyleName("portal-formSmall");
		elaPaisTableUtil.initialize(elaPaisFlexTable);

		elaPaisTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		elaPaisTableUtil.addColumn("Nome", "160", HasHorizontalAlignment.ALIGN_LEFT);
		elaPaisTableUtil.addColumn("Residencial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		elaPaisTableUtil.addColumn("Comercial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		elaPaisTableUtil.addColumn("Celular", "50", HasHorizontalAlignment.ALIGN_LEFT);
		elaPaisTableUtil.addColumn("Email", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}
	private void criaTabelaResponsaveis() {
		responsaveisFlexTable = new FlexTable();
		responsaveisFlexTable.setStyleName("portal-formSmall");
		responsaveisTableUtil.initialize(responsaveisFlexTable);

		responsaveisTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		responsaveisTableUtil.addColumn("Nome", "160", HasHorizontalAlignment.ALIGN_LEFT);
		responsaveisTableUtil.addColumn("Residencial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		responsaveisTableUtil.addColumn("Comercial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		responsaveisTableUtil.addColumn("Celular", "50", HasHorizontalAlignment.ALIGN_LEFT);
		responsaveisTableUtil.addColumn("Email", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}
	private void criaTabelaEmergencia() {
		emergenciaFlexTable = new FlexTable();
		emergenciaFlexTable.setStyleName("portal-formSmall");
		emergenciaTableUtil.initialize(emergenciaFlexTable);

		emergenciaTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		emergenciaTableUtil.addColumn("Nome", "160", HasHorizontalAlignment.ALIGN_LEFT);
		emergenciaTableUtil.addColumn("Residencial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		emergenciaTableUtil.addColumn("Comercial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		emergenciaTableUtil.addColumn("Celular", "50", HasHorizontalAlignment.ALIGN_LEFT);
		emergenciaTableUtil.addColumn("Email", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}
	/*
	private void criaTabelaIndicacoes() {
		indicacoesFlexTable = new FlexTable();
		indicacoesFlexTable.setStyleName("portal-formSmall");
		indicacoesTableUtil.initialize(indicacoesFlexTable);

		indicacoesTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		indicacoesTableUtil.addColumn("Nome", null, HasHorizontalAlignment.ALIGN_LEFT);
		indicacoesTableUtil.addColumn("Residencial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		indicacoesTableUtil.addColumn("Comercial", "50", HasHorizontalAlignment.ALIGN_LEFT);
		indicacoesTableUtil.addColumn("Celular", "50", HasHorizontalAlignment.ALIGN_LEFT);
		indicacoesTableUtil.addColumn("Email", "50", HasHorizontalAlignment.ALIGN_LEFT);
	}
	*/
	private void criaTabela() {
		casalFlexTable = new FlexTable();
		casalFlexTable.setStyleName("portal-formSmall");
		casalTableUtil.initialize(casalFlexTable);

		casalTableUtil.addColumn("", "10", HasHorizontalAlignment.ALIGN_CENTER);
		casalTableUtil.addColumn("Apelidos", "140", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Nomes", "230", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Emails", "200", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Telefone", "150", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Residencial", "100", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Cidade", "100", HasHorizontalAlignment.ALIGN_LEFT);
		casalTableUtil.addColumn("Estado", "50", HasHorizontalAlignment.ALIGN_CENTER);
		casalTableUtil.addColumn("Tipo", "70", HasHorizontalAlignment.ALIGN_LEFT);
	}

	@UiHandler("buscarButton")
	public void buscarButtonClickHandler(ClickEvent event){
		CasalParamVO vo = new CasalParamVO();
		vo.setGrupo(presenter.getGrupoSelecionado());
		vo.setNome(nomeTextBox.getValue());
		vo.setAgrupamento((Agrupamento) ListBoxUtil.getItemSelected(agrupamentoListBox, listaAgrupamento));
		vo.setTipoInscricao((TipoInscricaoEnum) ListBoxUtil.getItemSelected(tipoInscricaoListBox, TipoInscricaoEnum.values()));
		vo.setTipoCasal((TipoCasalEnum) ListBoxUtil.getItemSelected(tipoCasalListBox, TipoCasalEnum.values()));
		vo.setTodosInscritos(false);
		if(tipoInscricaoListBox.getSelectedIndex()==1){
			vo.setTodosInscritos(true);
		}
		if(vo.getTipoInscricao()!=null || vo.getTodosInscritos()){
			vo.setEncontro(presenter.getEncontroSelecionado());
		}
		presenter.buscaCasais(vo);
	}

	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		if(presenter.getDadosLoginVO().getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
			if(dadosPagamentoComponent.getDadosAlterados()){
				if(!Window.confirm("Foram alteradas as informações de pagamento mas não foram salvas.\nDeseja sair mesmo assim?")){
					return;
				}
			}
			if(!dadosPagamentoComponent.getDadosAlterados() && !verificaDadosPagamento()){
				if(!Window.confirm("Não foram definidas suas opções de pagamento.\nDeseja sair mesmo assim?")){
					return;
				}
			}
		}
		editaDialogBox.hide();
		casalImage.setUrl("images/blank.png");
		if(presenter.getDadosLoginVO().getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
			presenter.fechar();
		}
	}

	@UiHandler("printButton")
	public void printButtonClickHandler(ClickEvent event){
		Print.it("","<link rel=styleSheet type=text/css media=paper href=/paperStyle.css>",formularioFlowPanel.getElement());
	}

	private Boolean verificaDadosPagamento(){
		boolean verificar = false;
		if(entidadeEditada.getCasal()!=null){
			if(presenter.getDadosLoginVO().getCasal().getId().equals(entidadeEditada.getCasal().getId())){
				verificar = true;
			}
		}
		boolean dadosPagamento = true;
		if(getEncontroInscricaoVO()!=null){
			if(getEncontroInscricaoVO().getEncontroInscricao().getValorEncontro()==null ||
			   (getEncontroInscricaoVO().getEncontroInscricao().getEsconderPlanoPagamento()!=null &&
			    getEncontroInscricaoVO().getEncontroInscricao().getEsconderPlanoPagamento())){
				verificar = false;
			}
			if(verificar){
				dadosPagamento = false;
				for (EncontroInscricaoPagamento pagamento : getEncontroInscricaoVO().getListaPagamento()) {
					if(!pagamento.getParcela().equals(0)){
						dadosPagamento = true;
						break;
					}
				}
			}
		}
		return dadosPagamento;
	}
	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event){
		edita(null);
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@UiHandler("alterarImagemButton")
	public void alterarImagemButtonClickHandler(ClickEvent event){
		//uploadImagePreview.setMultiple(true);
		uploadImagePreview.clear();
		imageDialogBox.center();
		imageDialogBox.show();
		uploadImagePreview.adicionaImage();
	}

	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		if(!presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			if(eleRgTextBox.getValue()==null || eleRgTextBox.getValue().equals("")){
				Window.alert("Informe o RG do homem");
				return;
			}
			if(eleCpfTextBox.getValue()==null || eleCpfTextBox.getValue().equals("")){
				Window.alert("Informe o CPF do homem");
				return;
			}
			if(elaRgTextBox.getValue()==null || elaRgTextBox.getValue().equals("")){
				Window.alert("Informe o RG da mulher");
				return;
			}
			if(elaCpfTextBox.getValue()==null || elaCpfTextBox.getValue().equals("")){
				Window.alert("Informe o CPF da mulher");
				return;
			}
		}
		eleEmailTextBox.setValue(eleEmailTextBox.getValue().trim());
		if(!eleEmailTextBox.getValue().equals("")){
			if(!eleEmailTextBox.getValue().matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$")){
				Window.alert("Endereço eletrônico do homem inválido");
				eleEmailTextBox.setFocus(true);
				return;
			}
		}
		elaEmailTextBox.setValue(elaEmailTextBox.getValue().trim());
		if(!elaEmailTextBox.getValue().equals("")){
			if(!elaEmailTextBox.getValue().matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$")){
				Window.alert("Endereço eletrônico da mulher inválido");
				elaEmailTextBox.setFocus(true);
				return;
			}
		}
		entidadeEditada.getCasal().setEle(salvarPessoa(entidadeEditada.getCasal().getEle(), true));
		entidadeEditada.getCasal().setEla(salvarPessoa(entidadeEditada.getCasal().getEla(), false));
		entidadeEditada.getCasal().setTelefone(telefoneTextBox.getValue());
		entidadeEditada.getCasal().setEndereco(enderecoTextBox.getValue());
		entidadeEditada.getCasal().setBairro(bairroTextBox.getValue());
		entidadeEditada.getCasal().setCidade(cidadeTextBox.getValue());
		entidadeEditada.getCasal().setCep(cepTextBox.getValue());
		entidadeEditada.getCasal().setEstado(estadoTextBox.getValue());
		entidadeEditada.getCasal().setCasamento(casamentoDateBox.getValue());
		entidadeEditada.getCasal().setGrupo(presenter.getGrupoSelecionado());
		entidadeEditada.getCasal().setSituacao((TipoSituacaoEnum)ListBoxUtil.getItemSelected(situacaoListBox, TipoSituacaoEnum.values()));
		entidadeEditada.getCasal().setTipoCasal((TipoCasalEnum)ListBoxUtil.getItemSelected(tipoListBox, TipoCasalEnum.values()));

		entidadeEditada.getCasal().setMusica(musicaTextBox.getValue());
		entidadeEditada.getCasal().setCor(corTextBox.getValue());
		entidadeEditada.getCasal().setLugar(lugarTextBox.getValue());
		entidadeEditada.getCasal().setAtividade(atividadeTextBox.getValue());

		presenter.salvar(entidadeEditada);
	}
	@Override
	public void edita(Casal casal) {
		limpaCampos();
		if(casal == null){
			entidadeEditada = new CasalVO();
			entidadeEditada.setCasal(new Casal());
			entidadeEditada.getCasal().setEle(new Pessoa());
			entidadeEditada.getCasal().setEla(new Pessoa());
			entidadeEditada.setListaContatos(new ArrayList<CasalContato>());

			cidadeTextBox.setValue(presenter.getGrupoSelecionado().getCidade());
			estadoTextBox.setValue(presenter.getGrupoSelecionado().getEstado());
			editaDialogBox.center();
			editaDialogBox.show();
		} else {
			presenter.getVO(casal);
		}
	}

	@Override
	public void init(){
		ferramentasHorizontalPanel.clear();
		if(presenter.getDadosLoginVO().getUsuario()==null) return;
		if(presenter.getDadosLoginVO().getCasal()==null) return;
		if(presenter.getDadosLoginVO().getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
			centralPanel.setVisible(false);
			novoButton.setVisible(false);
		} else {
			centralPanel.setVisible(true);
			if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
				novoButton.setVisible(true);
				Button b = new Button("Redimensionar");
				b.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						presenter.redimensiona();
					}
				});
				ferramentasHorizontalPanel.add(b);
				b = new Button("Limpar lixo de imágens");
				b.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						presenter.limpaLixo();
					}
				});
				ferramentasHorizontalPanel.add(b);
			} else {
				novoButton.setVisible(false);
			}
		}
		nomeTextBox.setFocus(true);
	}

	public void limpaCampos(){
		//logotipoUploadArquivoDigital.limpaCampos();
		salvarButton.setVisible(true);
		casalImage.setUrl("images/blank.png");
		eleSuggestBox.setValue(null);
		elaSuggestBox.setValue(null);
		casamentoDateBox.setValue(null);
		telefoneTextBox.setValue(null);
		enderecoTextBox.setValue(null);
		bairroTextBox.setValue(null);
		cidadeTextBox.setValue(null);
		cepTextBox.setValue(null);
		estadoTextBox.setValue(null);
		situacaoListBox.setSelectedIndex(0);
		ListBoxUtil.setItemSelected(tipoListBox, TipoCasalEnum.CONVIDADO.getNome());
		filhosTableUtil.clearData();
		elePaisTableUtil.clearData();
		elaPaisTableUtil.clearData();
		responsaveisTableUtil.clearData();
		emergenciaTableUtil.clearData();
		limpaPessoa();

		dadosPagamentoComponent.limpaCampos();
		pagamentoHTMLPanel.setVisible(false);
		dadosAfilhadoHTMLPanel.setVisible(false);
		situacaoHTMLPanel.setVisible(false);

		musicaTextBox.setValue(null);
		corTextBox.setValue(null);
		lugarTextBox.setValue(null);
		atividadeTextBox.setValue(null);

//		indicacoesTableUtil.clearData();
	}

	public void defineCampos(CasalVO casalVO){
		if(casalVO.getCasal().getEle()!=null){
			definePessoa(casalVO.getCasal().getEle(), true);
		}
		if(casalVO.getCasal().getEla()!=null){
			definePessoa(casalVO.getCasal().getEla(), false);
		}
		//casal
		if(casalVO.getCasal().getSituacao()!=null){
			ListBoxUtil.setItemSelected(situacaoListBox, casalVO.getCasal().getSituacao().getNome());
		}
		if(casalVO.getCasal().getTipoCasal()!=null){
			ListBoxUtil.setItemSelected(tipoListBox, casalVO.getCasal().getTipoCasal().getNome());
		}
		if(casalVO.getCasal().getIdArquivoDigital()!=null){
			casalImage.setUrl("eccweb/downloadArquivoDigital?id="+casalVO.getCasal().getIdArquivoDigital());
		}

		enderecoTextBox.setValue(casalVO.getCasal().getEndereco());
		bairroTextBox.setValue(casalVO.getCasal().getBairro());
		cepTextBox.setValue(casalVO.getCasal().getCep());
		cidadeTextBox.setValue(casalVO.getCasal().getCidade());
		estadoTextBox.setValue(casalVO.getCasal().getEstado());
		telefoneTextBox.setValue(casalVO.getCasal().getTelefone());
		casamentoDateBox.setValue(casalVO.getCasal().getCasamento());

		corTextBox.setValue(casalVO.getCasal().getCor());
		lugarTextBox.setValue(casalVO.getCasal().getLugar());
		atividadeTextBox.setValue(casalVO.getCasal().getAtividade());
		musicaTextBox.setValue(casalVO.getCasal().getMusica());

		salvarButton.setVisible(false);
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			situacaoHTMLPanel.setVisible(true);
			salvarButton.setVisible(true);
		} else {
			if(presenter.getDadosLoginVO().getCasal().getId().equals(casalVO.getCasal().getId())){
				salvarButton.setVisible(true);
			}
		}
		populaContatos();
	}
	private void limpaPessoa(){
		eleNomeTextBox.setValue(null);
		eleSuggestBox.setValue(null);
		eleApelidoTextBox.setValue(null);
		eleNascimentoDateBox.setValue(null);
		eleRgTextBox.setValue(null);
		eleExpedidorTextBox.setValue(null);
		eleNaturalidadeTextBox.setValue(null);
		eleProfissaoTextBox.setValue(null);
		eleCpfTextBox.setValue(null);
		eleEmailTextBox.setValue(null);
		eleTelefoneComercialTextBox.setValue(null);
		eleTelefoneCelularTextBox.setValue(null);
		eleVegetarianoCheckBox.setValue(null);
		eleDiabeticoCheckBox.setValue(null);
		eleAlergicoCheckBox.setValue(null);
		eleHipertensoCheckBox.setValue(null);
		eleAlergiaTextBox.setValue(null);
		eleNecessidadesTextBox.setValue(null);

		elaNomeTextBox.setValue(null);
		elaSuggestBox.setValue(null);
		elaApelidoTextBox.setValue(null);
		elaNascimentoDateBox.setValue(null);
		elaRgTextBox.setValue(null);
		elaExpedidorTextBox.setValue(null);
		elaNaturalidadeTextBox.setValue(null);
		elaProfissaoTextBox.setValue(null);
		elaCpfTextBox.setValue(null);
		elaEmailTextBox.setValue(null);
		elaTelefoneComercialTextBox.setValue(null);
		elaTelefoneCelularTextBox.setValue(null);
		elaVegetarianoCheckBox.setValue(null);
		elaDiabeticoCheckBox.setValue(null);
		elaAlergicoCheckBox.setValue(null);
		elaHipertensoCheckBox.setValue(null);
		elaAlergiaTextBox.setValue(null);
		elaNecessidadesTextBox.setValue(null);
	}
	private void definePessoa(Pessoa pessoa, boolean ele){
		if(ele){
			eleNomeTextBox.setValue(pessoa.getNome());
			eleSuggestBox.setValue(pessoa.getNome());
			eleSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
			eleSuggest.getListaEntidades().add(pessoa);
			eleApelidoTextBox.setValue(pessoa.getApelido());
			eleNascimentoDateBox.setValue(pessoa.getNascimento());
			eleRgTextBox.setValue(pessoa.getRg());
			eleExpedidorTextBox.setValue(pessoa.getExpedidor());
			eleNaturalidadeTextBox.setValue(pessoa.getNaturalidade());
			eleProfissaoTextBox.setValue(pessoa.getProfissao());
			eleCpfTextBox.setValue(pessoa.getCpf());
			eleEmailTextBox.setValue(pessoa.getEmail());
			eleTelefoneComercialTextBox.setValue(pessoa.getTelefoneComercial());
			eleTelefoneCelularTextBox.setValue(pessoa.getTelefoneCelular());
			eleVegetarianoCheckBox.setValue(pessoa.getVegetariano());
			eleDiabeticoCheckBox.setValue(pessoa.getDiabetico());
			eleAlergicoCheckBox.setValue(pessoa.getAlergico());
			eleHipertensoCheckBox.setValue(pessoa.getHipertenso());
			eleAlergiaTextBox.setValue(pessoa.getAlergia());
			eleNecessidadesTextBox.setValue(pessoa.getNecessidadesEspeciais());
		} else {
			elaNomeTextBox.setValue(pessoa.getNome());
			elaSuggestBox.setValue(pessoa.getNome());
			elaSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
			elaSuggest.getListaEntidades().add(pessoa);
			elaApelidoTextBox.setValue(pessoa.getApelido());
			elaNascimentoDateBox.setValue(pessoa.getNascimento());
			elaRgTextBox.setValue(pessoa.getRg());
			elaExpedidorTextBox.setValue(pessoa.getExpedidor());
			elaNaturalidadeTextBox.setValue(pessoa.getNaturalidade());
			elaProfissaoTextBox.setValue(pessoa.getProfissao());
			elaCpfTextBox.setValue(pessoa.getCpf());
			elaEmailTextBox.setValue(pessoa.getEmail());
			elaTelefoneComercialTextBox.setValue(pessoa.getTelefoneComercial());
			elaTelefoneCelularTextBox.setValue(pessoa.getTelefoneCelular());
			elaVegetarianoCheckBox.setValue(pessoa.getVegetariano());
			elaDiabeticoCheckBox.setValue(pessoa.getDiabetico());
			elaAlergicoCheckBox.setValue(pessoa.getAlergico());
			elaHipertensoCheckBox.setValue(pessoa.getHipertenso());
			elaAlergiaTextBox.setValue(pessoa.getAlergia());
			elaNecessidadesTextBox.setValue(pessoa.getNecessidadesEspeciais());
		}
	}
	private Pessoa salvarPessoa(Pessoa pessoa, boolean ele){
		if(ele){
			pessoa.setNome(eleNomeTextBox.getValue());
			pessoa.setApelido(eleApelidoTextBox.getValue());
			pessoa.setNascimento(eleNascimentoDateBox.getValue());
			pessoa.setRg(eleRgTextBox.getValue());
			pessoa.setExpedidor(eleExpedidorTextBox.getValue());
			pessoa.setNaturalidade(eleNaturalidadeTextBox.getValue());
			pessoa.setProfissao(eleProfissaoTextBox.getValue());
			pessoa.setCpf(eleCpfTextBox.getValue());
			pessoa.setEmail(eleEmailTextBox.getValue().trim());
			pessoa.setTelefoneComercial(eleTelefoneComercialTextBox.getValue());
			pessoa.setTelefoneCelular(eleTelefoneCelularTextBox.getValue());
			pessoa.setVegetariano(eleVegetarianoCheckBox.getValue());
			pessoa.setDiabetico(eleDiabeticoCheckBox.getValue());
			pessoa.setAlergico(eleAlergicoCheckBox.getValue());
			pessoa.setHipertenso(eleHipertensoCheckBox.getValue());
			pessoa.setAlergia(eleAlergiaTextBox.getValue());
			pessoa.setNecessidadesEspeciais(eleNecessidadesTextBox.getValue());
		} else {
			pessoa.setNome(elaNomeTextBox.getValue());
			pessoa.setApelido(elaApelidoTextBox.getValue());
			pessoa.setNascimento(elaNascimentoDateBox.getValue());
			pessoa.setRg(elaRgTextBox.getValue());
			pessoa.setExpedidor(elaExpedidorTextBox.getValue());
			pessoa.setNaturalidade(elaNaturalidadeTextBox.getValue());
			pessoa.setProfissao(elaProfissaoTextBox.getValue());
			pessoa.setCpf(elaCpfTextBox.getValue());
			pessoa.setEmail(elaEmailTextBox.getValue().trim());
			pessoa.setTelefoneComercial(elaTelefoneComercialTextBox.getValue());
			pessoa.setTelefoneCelular(elaTelefoneCelularTextBox.getValue());
			pessoa.setVegetariano(elaVegetarianoCheckBox.getValue());
			pessoa.setDiabetico(elaDiabeticoCheckBox.getValue());
			pessoa.setAlergico(elaAlergicoCheckBox.getValue());
			pessoa.setHipertenso(elaHipertensoCheckBox.getValue());
			pessoa.setAlergia(elaAlergiaTextBox.getValue());
			pessoa.setNecessidadesEspeciais(elaNecessidadesTextBox.getValue());
		}
		return pessoa;
	}

	@Override
	public String getDisplayTitle() {
		return "Cadastro de Casais";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		casalTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<Casal> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "casal", "casais", "");
		casalTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		boolean podeEditar = false;
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			podeEditar = true;
		}
		for (final Casal casal: lista) {
			Object dados[] = new Object[9];

			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);

			if(podeEditar || presenter.getDadosLoginVO().getCasal().getId().equals(casal.getId()) ||
			  (casal.getCasalPadrinho()!=null && casal.getCasalPadrinho().getId().equals(presenter.getDadosLoginVO().getCasal().getId()))){
				editar = new Image("images/edit.png");
				editar.setTitle("Editar informações deste casal");
				editar.setStyleName("portal-ImageCursor");
				editar.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						edita(casal);
					}
				});
				hp.add(editar);

			}
			if(podeEditar){
				excluir = new Image("images/delete.png");
				excluir.setTitle("Excluir este casal");
				excluir.setStyleName("portal-ImageCursor");
				excluir.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						if(Window.confirm("Deseja excluir este casal ?")){
							presenter.excluir(casal);
						}
					}
				});
				hp.add(excluir);
			}

			dados[0] = hp;
			dados[1] = casal.getApelidos("e");
			dados[2] = new HTML(casal.getEle().getNome() + "<br>" + casal.getEla().getNome());
			dados[3] = new HTML((casal.getEle().getEmail()==null?"&nbsp;":casal.getEle().getEmail()) + "<br>" + (casal.getEla().getEmail()==null?"&nbsp;":casal.getEla().getEmail()));
			dados[4] = new HTML((casal.getEle().getTelefoneCelular()==null?"&nbsp;":casal.getEle().getTelefoneCelular()) + "<br>" + (casal.getEla().getTelefoneCelular()==null?"&nbsp;":casal.getEla().getTelefoneCelular()));
			dados[5] = casal.getTelefone();
			dados[6] = casal.getCidade();
			dados[7] = casal.getEstado();
			if(casal.getTipoCasal()!=null){
				dados[8] = casal.getTipoCasal().getNome();
			}
			casalTableUtil.addRow(dados,row+1);
			if(casal.getSituacao()!=null && !casal.getSituacao().equals(TipoSituacaoEnum.ATIVO)){
				casalTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialNormalGray");
			}
			row++;
		}
		casalTableUtil.applyDataRowStyles();
	}

	@UiHandler("selecionarUploadButton")
	public void selecionarUploadButtonClickHandler(ClickEvent event){
		uploadImagePreview.adicionaImage();
	}

	@UiHandler("aceitarUploadButton")
	public void aceitarUploadButtonClickHandler(ClickEvent event){
		if(uploadImagePreview.getListaImagens().size()>0 &&
		   uploadImagePreview.getListaImagens().get(0)!=null &&
		   uploadImagePreview.getListaImagens().get(0).getIdArquivoDigital() != null &&
		   !uploadImagePreview.getListaImagens().get(0).getIdArquivoDigital().equals(new Integer(0) )){
			Integer id = uploadImagePreview.getListaImagens().get(0).getIdArquivoDigital();
			casalImage.setUrl("eccweb/downloadArquivoDigital?id="+id);
			entidadeEditada.getCasal().setIdArquivoDigital(id);
		} else {
			casalImage.setUrl("images/blank.png");
			entidadeEditada.getCasal().setIdArquivoDigital(null);
		}
		imageDialogBox.hide();
	}

	@UiHandler("cancelarUploadButton")
	public void cancelarUploadButtonClickHandler(ClickEvent event){
		imageDialogBox.hide();
	}

	@UiHandler("addFilhoButton")
	public void addFilhoButtonClickHandler(ClickEvent event){
		editaContato(null, TipoCasalContatoEnum.FILHO);
	}
	@UiHandler("addElePaisButton")
	public void addElePaisButtonClickHandler(ClickEvent event){
		editaContato(null, TipoCasalContatoEnum.PAI_DELE);
	}
	@UiHandler("addElaPaisButton")
	public void addElaPaisButtonClickHandler(ClickEvent event){
		editaContato(null, TipoCasalContatoEnum.PAI_DELA);
	}
	@UiHandler("addResponsavelButton")
	public void addResponsavelButtonClickHandler(ClickEvent event){
		editaContato(null, TipoCasalContatoEnum.RESPONSAVEL_FILHOS);
	}
	@UiHandler("addEmergenciaButton")
	public void addEmergenciaButtonClickHandler(ClickEvent event){
		editaContato(null, TipoCasalContatoEnum.EMERGENCIA);
	}
	/*
	@UiHandler("addIndicacaoButton")
	public void addIndicacaoButtonClickHandler(ClickEvent event){
		editaContato(null, TipoCasalContatoEnum.INDICACAO);
	}
	*/
	@UiHandler("salvarContatoButton")
	public void salvarContatoClickHandler(ClickEvent event){
		entidadeEditada.getListaContatos().remove(contatoEditado);

		contatoEditado.setNome(nomeContatoTextBox.getValue());
		contatoEditado.setEmail(emailContatoTextBox.getValue());
		contatoEditado.setTelefoneResidencial(residencialContatoTextBox.getValue());
		contatoEditado.setTelefoneComercial(comercialContatoTextBox.getValue());
		contatoEditado.setTelefoneCelular(celularContatoTextBox.getValue());
		if(idadeNumberTextBox.getNumber()!=null && contatoEditado.getTipoContato().equals(TipoCasalContatoEnum.FILHO)){
			contatoEditado.setIdade(idadeNumberTextBox.getNumber().intValue());
		}
		entidadeEditada.getListaContatos().add(contatoEditado);
		editaContatoDialogBox.hide();
		populaContatos();
	}
	@UiHandler("fecharContatoButton")
	public void fecharContatoClickHandler(ClickEvent event){
		editaContatoDialogBox.hide();
	}

	private void editaContato(CasalContato contato, TipoCasalContatoEnum tipo) {
		contatoEditado = contato;
		limpaCamposContato();
		if(tipo.equals(TipoCasalContatoEnum.FILHO)){
			idadeHTMLPanel.setVisible(true);
		}
		if(contato!=null){
			defineCamposContato(contato, tipo);
		} else {
			contatoEditado = new CasalContato();
			contatoEditado.setTipoContato(tipo);
		}
		editaContatoDialogBox.center();
		editaContatoDialogBox.show();
		nomeContatoTextBox.setFocus(true);
	}

	private void defineCamposContato(CasalContato contato, TipoCasalContatoEnum tipo) {
		nomeContatoTextBox.setValue(contato.getNome());
		emailContatoTextBox.setValue(contato.getEmail());
		residencialContatoTextBox.setValue(contato.getTelefoneResidencial());
		comercialContatoTextBox.setValue(contato.getTelefoneComercial());
		celularContatoTextBox.setValue(contato.getTelefoneCelular());
		if(tipo.equals(TipoCasalContatoEnum.FILHO)){
			idadeNumberTextBox.setNumber(contato.getIdade());
		}
	}

	private void limpaCamposContato() {
		nomeContatoTextBox.setValue(null);
		emailContatoTextBox.setValue(null);
		residencialContatoTextBox.setValue(null);
		comercialContatoTextBox.setValue(null);
		celularContatoTextBox.setValue(null);
		idadeNumberTextBox.setNumber(null);
		idadeHTMLPanel.setVisible(false);
	}

	@Override
	public void setVO(CasalVO vo) {
		entidadeEditada = vo;
		defineCampos(vo);
		editaDialogBox.center();
		editaDialogBox.show();
	}

	public void populaContatos() {
		filhosTableUtil.clearData();
		elePaisTableUtil.clearData();
		elaPaisTableUtil.clearData();
		responsaveisTableUtil.clearData();
		emergenciaTableUtil.clearData();
//		indicacoesTableUtil.clearData();

		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final CasalContato contato: entidadeEditada.getListaContatos()) {

			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);

			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.setTitle("Editar dados desta pessoa");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					editaContato(contato, contato.getTipoContato());
				}
			});
			hp.add(editar);
			excluir = new Image("images/delete.png");
			editar.setTitle("Excluir este item");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir esta pessoa ?")){
						entidadeEditada.getListaContatos().remove(contato);
						populaContatos();
					}
				}
			});
			hp.add(excluir);

			if(contato.getTipoContato().equals(TipoCasalContatoEnum.FILHO)){
				Object dados[] = new Object[7];
				dados[0] = hp;
				dados[1] = contato.getNome();
				dados[2] = contato.getIdade();
				dados[3] = contato.getTelefoneResidencial();
				dados[4] = contato.getTelefoneComercial();
				dados[5] = contato.getTelefoneCelular();
				dados[6] = contato.getEmail();
				filhosTableUtil.addRow(dados,row+1);
			} else {
				Object dados[] = new Object[6];
				dados[0] = hp;
				dados[1] = contato.getNome();
				dados[2] = contato.getTelefoneResidencial();
				dados[3] = contato.getTelefoneComercial();
				dados[4] = contato.getTelefoneCelular();
				dados[5] = contato.getEmail();
				if(contato.getTipoContato().equals(TipoCasalContatoEnum.PAI_DELE)){
					elePaisTableUtil.addRow(dados,row+1);
				} else if(contato.getTipoContato().equals(TipoCasalContatoEnum.PAI_DELA)){
					elaPaisTableUtil.addRow(dados,row+1);
				} else if(contato.getTipoContato().equals(TipoCasalContatoEnum.RESPONSAVEL_FILHOS)){
					responsaveisTableUtil.addRow(dados,row+1);
				} else if(contato.getTipoContato().equals(TipoCasalContatoEnum.EMERGENCIA)){
					emergenciaTableUtil.addRow(dados,row+1);
//				} else if(contato.getTipoContato().equals(TipoCasalContatoEnum.INDICACAO)){
//					indicacoesTableUtil.addRow(dados,row+1);
				}
			}
			row++;
		}
		filhosTableUtil.applyDataRowStyles();
		elePaisTableUtil.applyDataRowStyles();
		elaPaisTableUtil.applyDataRowStyles();
		responsaveisTableUtil.applyDataRowStyles();
		emergenciaTableUtil.applyDataRowStyles();
//		indicacoesTableUtil.applyDataRowStyles();
	}

	@Override
	public void setEncontroInscricaoVO(EncontroInscricaoVO encontroInscricaoVO) {
		dadosPagamentoComponent.setDadosAlterados(false);
		if(encontroInscricaoVO==null){
			pagamentoHTMLPanel.setVisible(false);
			dadosPagamentoComponent.setVisible(false);
		} else {
			if(encontroInscricaoVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.AFILHADO)){
				dadosAfilhadoHTMLPanel.setVisible(true);
				if(encontroInscricaoVO.getEncontroInscricao().getValorEncontro()!=null && encontroInscricaoVO.getEncontroInscricao().getValorEncontro().doubleValue()!=0){
					pagamentoHTMLPanel.setVisible(true);
					dadosPagamentoComponent.setVisible(true);
				}
				dadosPagamentoComponent.limpaCampos();
				dadosPagamentoComponent.setUsuario(presenter.getDadosLoginVO().getUsuario());
				dadosPagamentoComponent.setCasal(entidadeEditada.getCasal());
				dadosPagamentoComponent.setEncontroInscricaoVO(encontroInscricaoVO);
			} else {
				dadosAfilhadoHTMLPanel.setVisible(false);
				pagamentoHTMLPanel.setVisible(false);
				dadosPagamentoComponent.setVisible(false);
			}
		}
	}

	@Override
	public EncontroInscricaoVO getEncontroInscricaoVO() {
		return dadosPagamentoComponent.getEncontroInscricaoVO();
	}

	@Override
	public void populaAgrupamento(List<Agrupamento> result) {
		this.listaAgrupamento = result;
		agrupamentoListBox.clear();
		agrupamentoListBox.addItem("");
		for(Agrupamento agrupamento : result) {
			agrupamentoListBox.addItem(agrupamento.toString());
		}
	}

	@UiHandler("agrupamentoListBox")
	public void agrupamentoListBoxChangeHandler(ChangeEvent event) {
		nomeTextBox.setValue(null);
		tipoInscricaoListBox.setSelectedIndex(0);
	}
	@UiHandler("tipoInscricaoListBox")
	public void tipoInscricaoListBoxChangeHandler(ChangeEvent event) {
		agrupamentoListBox.setSelectedIndex(0);
		nomeTextBox.setValue(null);
	}
}