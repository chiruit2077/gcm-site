package br.com.ecc.client.ui.sistema.encontro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.Casal;
import br.com.ecc.model.EncontroConvite;
import br.com.ecc.model.EncontroConviteResponsavel;
import br.com.ecc.model.EncontroFila;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoConfirmacaoEnum;
import br.com.ecc.model.tipo.TipoFilaEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.tipo.TipoRespostaConviteEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class EncontroConviteView extends BaseView<EncontroConvitePresenter> implements EncontroConvitePresenter.Display {

	@UiTemplate("EncontroConviteView.ui.xml")
	interface EncontroConviteViewUiBinder extends UiBinder<Widget, EncontroConviteView> {}
	private EncontroConviteViewUiBinder uiBinder = GWT.create(EncontroConviteViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField Label itemFilaTotal;
	@UiField VerticalPanel centralPanel;
	
	@UiField HTMLPanel respostaHTMLPanel;
	@UiField CheckBox gerarInscricaoCheckBox;
	@UiField CheckBox moverCheckBox;
	
	@UiField CheckBox exibeRecusadosCheckBox;
	@UiField CheckBox exibeDesistenciaCheckBox;
	
	//Convite
	@UiField(provided = true) SuggestBox casalSuggestBox;
	private final GenericEntitySuggestOracle casalSuggest = new GenericEntitySuggestOracle();
	
	@UiField ListBox responsavelListBox;
	
	@UiField(provided = true) SuggestBox casalConvidadoSuggestBox;
	private final GenericEntitySuggestOracle casalConvidadoSuggest = new GenericEntitySuggestOracle();
	
	@UiField DateBox dataConviteDateBox;
	@UiField DateBox dataRespostaDateBox;
	@UiField ListBox filaListBox;
	@UiField(provided=true) NumberTextBox prioridadeNumberTextBox;
	@UiField CheckBox esconderPagamentoCheckBox;
	@UiField ListBox respostaListBox;
	@UiField TextArea observacaoTextArea;
	@UiField ListBox confirmacaoListBox;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	@UiField HTMLPanel fichaHTMLPanel;
	
	@UiField(provided=true) FlexTable encontroConviteFlexTable;
	private FlexTableUtil encontroConviteTableUtil = new FlexTableUtil();
	
	@UiField(provided=true) FlexTable filaFlexTable;
	private FlexTableUtil filaTableUtil = new FlexTableUtil();
	
	@UiField Label casalConvidadoTelefoneLabel;
	
	//Fila
	@UiField DialogBox filasDialogBox;
	
	@UiField DialogBox editaFilaDialogBox;
	@UiField TextBox nomeTextBox;
	@UiField(provided=true) NumberTextBox qtdeVagasNumberTextBox;
	@UiField(provided=true) NumberTextBox ordemNumberTextBox;
	@UiField ListBox tipoListBox;
	@UiField TextBox dataFichaEnviadaAfilhadoDateBox;
	@UiField TextBox dataFichaRecebidaAfilhadoDateBox;
	@UiField TextBox dataFichaAtualizadaAfilhadoDateBox;
	@UiField TextBox dataFichaEnviadaPadrinhoDateBox;
	@UiField TextBox dataFichaRecebidaPadrinhoDateBox;
	@UiField TextBox dataFichaAtualizadaPadrinhoDateBox;
	
	@UiField Button salvarFilaButton;
	@UiField Button fecharFilaButton;
	@UiField Button novaFilaButton;

	//Casal
	@UiField DialogBox editaCasalDialogBox;
	@UiField TextBox telefoneTextBox;
	@UiField TextBox eleNomeTextBox;
	@UiField TextBox eleEmailTextBox;
	@UiField TextBox eleTelefoneTextBox;
	@UiField TextBox elaNomeTextBox;
	@UiField TextBox elaEmailTextBox;
	@UiField TextBox elaTelefoneTextBox;
	
	DateTimeFormat dfGlobal = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm");
	
	private EncontroConvite entidadeEditada;
	private EncontroFila entidadeFilaEditada;
	private List<EncontroFila> listaFilas;

	public EncontroConviteView() {
		criaTabela();
		criaTabelaFila();
		casalSuggest.setMinimoCaracteres(2);
		casalSuggest.setSuggestQuery("casal.porNomeLike");
		casalSuggestBox = new SuggestBox(casalSuggest);
		
		casalConvidadoSuggest.setMinimoCaracteres(2);
		casalConvidadoSuggest.setSuggestQuery("casal.porNomeLike");
		casalConvidadoSuggestBox = new SuggestBox(casalConvidadoSuggest);
		
		prioridadeNumberTextBox = new NumberTextBox(false, false, 5, 5);
		qtdeVagasNumberTextBox = new NumberTextBox(false, false, 5, 5);
		ordemNumberTextBox = new NumberTextBox(false, false, 5, 5);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		
		casalConvidadoSuggestBox.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				casalConvidadoTelefoneLabel.setText(null);
				if(!casalConvidadoSuggestBox.getValue().equals("")){
					Casal casalConvidado = (Casal)ListUtil.getEntidadePorNome(casalConvidadoSuggest.getListaEntidades(), casalConvidadoSuggestBox.getValue());
					entidadeEditada.setCasalConvidado(casalConvidado);
					defineTelefone(casalConvidado);
				}
			}
		});
		
		tituloFormularioLabel.setText(getDisplayTitle());
		
		ListBoxUtil.populate(respostaListBox, true, TipoRespostaConviteEnum.values());
		ListBoxUtil.populate(tipoListBox, false, TipoFilaEnum.values());
		ListBoxUtil.populate(confirmacaoListBox, false, TipoConfirmacaoEnum.values());
		
		dataConviteDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		dataConviteDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		
		dataRespostaDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		dataRespostaDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
	}
	
	private void criaTabela() {
		encontroConviteFlexTable = new FlexTable();
		encontroConviteFlexTable.setStyleName("portal-formSmall");
		encontroConviteTableUtil.initialize(encontroConviteFlexTable);
		
		encontroConviteTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		encontroConviteTableUtil.addColumn("Fila", "80", HasHorizontalAlignment.ALIGN_LEFT);
		encontroConviteTableUtil.addColumn("#", "20", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.NUMBER, null);
		encontroConviteTableUtil.addColumn("Casal", "150", HasHorizontalAlignment.ALIGN_LEFT);
		encontroConviteTableUtil.addColumn("Casal convidado", "120", HasHorizontalAlignment.ALIGN_LEFT);
		encontroConviteTableUtil.addColumn("Casal responsável", "120", HasHorizontalAlignment.ALIGN_LEFT);
		encontroConviteTableUtil.addColumn("Observação", "250", HasHorizontalAlignment.ALIGN_LEFT);
		encontroConviteTableUtil.addColumn("Convite", "80", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
		encontroConviteTableUtil.addColumn("Resposta", "70", HasHorizontalAlignment.ALIGN_CENTER);
		encontroConviteTableUtil.addColumn("Data Resp.", "90", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
		encontroConviteTableUtil.addColumn("Confirmação", "70", HasHorizontalAlignment.ALIGN_CENTER);
//		encontroConviteTableUtil.addColumn("Env. Ficha", "80", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
//		encontroConviteTableUtil.addColumn("Rec. Ficha", "80", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
//		encontroConviteTableUtil.addColumn("Pre. Ficha", "80", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
	}
	
	private void criaTabelaFila() {
		filaFlexTable = new FlexTable();
		filaFlexTable.setStyleName("portal-formSmall");
		filaTableUtil.initialize(filaFlexTable);
		
		filaTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		filaTableUtil.addColumn("Ordem", "50", HasHorizontalAlignment.ALIGN_CENTER);
		filaTableUtil.addColumn("Nome", null, HasHorizontalAlignment.ALIGN_LEFT);
		filaTableUtil.addColumn("Vagas", "20", HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	@Override
	public void init(){
		centralPanel.setVisible(false);
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			centralPanel.setVisible(true);
		}
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
		EncontroFila fila = (EncontroFila) ListBoxUtil.getItemSelected(filaListBox, listaFilas);
		if(fila==null){
			Window.alert("Seleciona a fila");
			return;
		}
		TipoRespostaConviteEnum resposta = (TipoRespostaConviteEnum) ListBoxUtil.getItemSelected(respostaListBox, TipoRespostaConviteEnum.values());
		if(resposta!=null && casalConvidadoSuggestBox.getValue().equals("")){
			Window.alert("Defina o casal convidado");
			return;
		}
		
		entidadeEditada.setEncontro(presenter.getEncontroSelecionado());
		entidadeEditada.setEncontroFila(fila);
		entidadeEditada.setOrdem(null);
		if(prioridadeNumberTextBox.getNumber()!=null){
			entidadeEditada.setOrdem(prioridadeNumberTextBox.getNumber().intValue());
		}
		entidadeEditada.setCasal(null);
		if(!casalSuggestBox.getValue().equals("")){
			entidadeEditada.setCasal((Casal)ListUtil.getEntidadePorNome(casalSuggest.getListaEntidades(), casalSuggestBox.getValue()));
		}
		entidadeEditada.setCasalConvidado(null);
		if(!casalConvidadoSuggestBox.getValue().equals("")){
			entidadeEditada.setCasalConvidado((Casal)ListUtil.getEntidadePorNome(casalConvidadoSuggest.getListaEntidades(), casalConvidadoSuggestBox.getValue()));
		}
		entidadeEditada.setCasalResponsavel(null);
		EncontroConviteResponsavel responsavel = (EncontroConviteResponsavel) ListBoxUtil.getItemSelected(responsavelListBox, presenter.getListaResponsavel());
		if(responsavel!=null){
			entidadeEditada.setCasalResponsavel(responsavel.getCasal());
		}
		entidadeEditada.setDataConvite(dataConviteDateBox.getValue());
		entidadeEditada.setDataResposta(dataRespostaDateBox.getValue());
		entidadeEditada.setEsconderPlanoPagamento(esconderPagamentoCheckBox.getValue());
		entidadeEditada.setObservacao(observacaoTextArea.getValue());
		entidadeEditada.setTipoResposta(resposta);
		entidadeEditada.setTipoConfirmacao((TipoConfirmacaoEnum) ListBoxUtil.getItemSelected(confirmacaoListBox, TipoConfirmacaoEnum.values()));
		
		entidadeEditada.setMoverFinalFila(moverCheckBox.getValue());
		entidadeEditada.setGerarInscricao(gerarInscricaoCheckBox.getValue());
		presenter.salvar(entidadeEditada);
	}
	private void edita(EncontroConvite encontroConvite) {
		limpaCampos();
		if(encontroConvite == null){
			entidadeEditada = new EncontroConvite();
		} else {
			entidadeEditada = encontroConvite;
			defineCampos(encontroConvite);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		prioridadeNumberTextBox.setFocus(true);
	}
	
	@SuppressWarnings("deprecation")
	public void limpaCampos(){
		prioridadeNumberTextBox.setNumber(null);
		casalSuggestBox.setValue(null);
		responsavelListBox.setSelectedIndex(0);
		confirmacaoListBox.setSelectedIndex(0);
		casalConvidadoSuggestBox.setValue(null);
		observacaoTextArea.setValue(null);
		esconderPagamentoCheckBox.setValue(null);
		dataConviteDateBox.setValue(null);
		respostaListBox.setSelectedIndex(0);
		dataRespostaDateBox.setValue(null);
		casalConvidadoTelefoneLabel.setText(null);
		gerarInscricaoCheckBox.setValue(false);
		moverCheckBox.setValue(false);
		respostaHTMLPanel.setVisible(false);
		
		dataFichaEnviadaAfilhadoDateBox.setValue(null);
		dataFichaRecebidaAfilhadoDateBox.setValue(null);
		dataFichaAtualizadaAfilhadoDateBox.setValue(null);
		dataFichaEnviadaPadrinhoDateBox.setValue(null);
		dataFichaRecebidaPadrinhoDateBox.setValue(null);
		dataFichaAtualizadaPadrinhoDateBox.setValue(null);
		
		filaListBox.setEnabled(true);
		prioridadeNumberTextBox.setEnabled(true);
		casalSuggestBox.getTextBox().setEnabled(true);
		responsavelListBox.setEnabled(true);
		casalConvidadoSuggestBox.getTextBox().setEnabled(true);
		esconderPagamentoCheckBox.setEnabled(true);
	}

	@SuppressWarnings("deprecation")
	public void defineCampos(EncontroConvite encontroConvite){
		boolean convidador = false;
		for (EncontroConviteResponsavel responsavel : presenter.getListaResponsavel()) {
			if(responsavel.getCasal().getId().equals(presenter.getDadosLoginVO().getCasal().getId())){
				convidador = true;
				break;
			}
		}
		boolean podeEditar = false;
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			podeEditar = true;
		}
		if(convidador && !podeEditar){
			filaListBox.setEnabled(false);
			prioridadeNumberTextBox.setEnabled(false);
			casalSuggestBox.getTextBox().setEnabled(false);
			responsavelListBox.setEnabled(false);
			casalConvidadoSuggestBox.getTextBox().setEnabled(false);
			esconderPagamentoCheckBox.setEnabled(false);
		}
		
		
		entidadeEditada = encontroConvite;
		if(encontroConvite.getEncontroFila()!=null){
			ListBoxUtil.setItemSelected(filaListBox, encontroConvite.getEncontroFila().getNome());
		}
		prioridadeNumberTextBox.setNumber(encontroConvite.getOrdem());
		if(encontroConvite.getCasal()!=null){
			casalSuggestBox.setValue(encontroConvite.getCasal().toString());
			casalSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
			casalSuggest.getListaEntidades().add(encontroConvite.getCasal());
		}
		if(encontroConvite.getCasalResponsavel()!=null){
			ListBoxUtil.setItemSelected(responsavelListBox, encontroConvite.getCasalResponsavel().toString());
		}
		if(encontroConvite.getCasalConvidado()!=null){
			casalConvidadoSuggestBox.setValue(encontroConvite.getCasalConvidado().toString());
			casalConvidadoSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
			casalConvidadoSuggest.getListaEntidades().add(encontroConvite.getCasalConvidado());
			defineTelefone(encontroConvite.getCasalConvidado());
			
		}
		observacaoTextArea.setValue(encontroConvite.getObservacao());
		esconderPagamentoCheckBox.setValue(encontroConvite.getEsconderPlanoPagamento());
		dataConviteDateBox.setValue(encontroConvite.getDataConvite());
		if(encontroConvite.getTipoResposta()!=null){
			ListBoxUtil.setItemSelected(respostaListBox, encontroConvite.getTipoResposta().getNome());
		}
		if(encontroConvite.getTipoConfirmacao()!=null){
			ListBoxUtil.setItemSelected(confirmacaoListBox, encontroConvite.getTipoConfirmacao().getNome());
		}
		dataRespostaDateBox.setValue(encontroConvite.getDataResposta());
		setDadosFichaCasal();
	}
	
	public void setDadosFichaCasal(){
		/*
		//afilhado
		if(entidadeEditada.getCasalConvidado()!=null){
			if(entidadeEditada.getCasalConvidado().getDataFichaEnvio()!=null){
				dataFichaEnviadaAfilhadoDateBox.setValue(dfGlobal.format(entidadeEditada.getCasalConvidado().getDataFichaEnvio()));
			}
			if(entidadeEditada.getCasalConvidado().getDataFichaRecebimento()!=null){
				dataFichaRecebidaAfilhadoDateBox.setValue(dfGlobal.format(entidadeEditada.getCasalConvidado().getDataFichaRecebimento()));
			}
			if(entidadeEditada.getCasalConvidado().getAtualizacaoCadastro()!=null){
				dataFichaAtualizadaAfilhadoDateBox.setValue(dfGlobal.format(entidadeEditada.getCasalConvidado().getAtualizacaoCadastro()));
			}
		}
		
		//padrinho
		if(entidadeEditada.getCasal()!=null){
			if(entidadeEditada.getCasal().getDataFichaEnvio()!=null){
				dataFichaEnviadaPadrinhoDateBox.setValue(dfGlobal.format(entidadeEditada.getCasal().getDataFichaEnvio()));
			}
			if(entidadeEditada.getCasal().getDataFichaRecebimento()!=null){
				dataFichaRecebidaPadrinhoDateBox.setValue(dfGlobal.format(entidadeEditada.getCasal().getDataFichaRecebimento()));
			}
			if(entidadeEditada.getCasal().getAtualizacaoCadastro()!=null){
				dataFichaAtualizadaPadrinhoDateBox.setValue(dfGlobal.format(entidadeEditada.getCasal().getAtualizacaoCadastro()));
			}
		}
		respostaListBoxChangeHandler(null);
		*/
	}
	
	private void defineTelefone(Casal casalConvidado) {
		if(casalConvidado==null) return;
		String telefones = "";
		if(casalConvidado.getTelefone()!=null && !casalConvidado.getTelefone().equals("")){
			telefones += "Casal: " + casalConvidado.getTelefone();
		}
		if(casalConvidado.getEle().getTelefoneCelular()!=null && !casalConvidado.getEle().getTelefoneCelular().equals("")){
			telefones += " - Ele: " + casalConvidado.getEle().getTelefoneCelular();
		}
		if(casalConvidado.getEla().getTelefoneCelular()!=null && !casalConvidado.getEla().getTelefoneCelular().equals("")){
			telefones += " - Ela: " + casalConvidado.getEla().getTelefoneCelular();
		}
		casalConvidadoTelefoneLabel.setText(telefones);
		
	}

	@Override
	public String getDisplayTitle() {
		return "Convites ao encontro";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		encontroConviteTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<EncontroConvite> lista) {
		boolean podeEditar = false;
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			podeEditar = true;
		}
		boolean convidador = false;
		for (EncontroConviteResponsavel responsavel : presenter.getListaResponsavel()) {
			if(responsavel.getCasal().getId().equals(presenter.getDadosLoginVO().getCasal().getId())){
				convidador = true;
				break;
			}
		}
		
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "convite", "convites", "");
		encontroConviteTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		EncontroFila filaAnterior = null;
		int contaFila = 1, convidar = 0, recusados = 0, aceitos = 0, desistencia=0;
		boolean exibeEditar, exibeLinha;
		for (final EncontroConvite encontroConvite: lista) {
			exibeEditar = true;
			if(filaAnterior!=null && !filaAnterior.getId().equals(encontroConvite.getEncontroFila().getId())){
				contaFila = 1;
			}
			filaAnterior = encontroConvite.getEncontroFila();
			Object dados[] = new Object[11];
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);

			if(convidador){
				/*
				exibeEditar = false;
				if(presenter.getEncontroSelecionado().getQuantidadeAfilhados()!=null && 
				   convidar<=presenter.getEncontroSelecionado().getQuantidadeAfilhados()){
					if(filaAnterior.getQuantidadeVagas()==null || contaFila <= filaAnterior.getQuantidadeVagas()){
						exibeEditar = true;
					}
				}
				*/
			}
			
			if(podeEditar || convidador){
				if(exibeEditar){
					editar = new Image("images/edit.png");
					editar.setTitle("Editar as informações do convite");
					editar.setStyleName("portal-ImageCursor");
					editar.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent arg0) {
							edita(encontroConvite);
						}
					});
					hp.add(editar);
				}
				if(podeEditar){
					excluir = new Image("images/delete.png");
					excluir.setStyleName("portal-ImageCursor");
					excluir.setTitle("Excluir convite");
					excluir.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent arg0) {
							if(Window.confirm("Deseja excluir esta inscrição ?")){
								presenter.excluir(encontroConvite);
							}
						}
					});
					hp.add(excluir);
				}
			}
			
			dados[0] = hp;
			if(encontroConvite.getEncontroFila()!=null){
				dados[1] = encontroConvite.getEncontroFila().getNome();
			}
			if(encontroConvite.getTipoResposta()!=null && encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.RECUSADO)){
				dados[2] = "";
			} else {
				dados[2] = encontroConvite.getOrdem();
			}
			if(encontroConvite.getCasal()!=null){
				dados[3] = encontroConvite.getCasal().getApelidos("e");
			}
			if(encontroConvite.getCasalConvidado()!=null){
				dados[4] = encontroConvite.getCasalConvidado().getApelidos("e");
			}
			if(encontroConvite.getCasalResponsavel()!=null){
				dados[5] = encontroConvite.getCasalResponsavel().getApelidos("e");
			}
			dados[6] = encontroConvite.getObservacao();
			if(encontroConvite.getDataConvite()!=null){
				dados[7] = dfGlobal.format(encontroConvite.getDataConvite());
			}
			if(encontroConvite.getTipoResposta()!=null){
				dados[8] = encontroConvite.getTipoResposta().getNome();
			}
			if(encontroConvite.getDataResposta()!=null){
				dados[9] = dfGlobal.format(encontroConvite.getDataResposta());
			}
			if(encontroConvite.getTipoConfirmacao()!=null){
				dados[10] = encontroConvite.getTipoConfirmacao().getNome();
			} else {
				dados[10] = TipoConfirmacaoEnum.CONFIRMADO.getNome();
			}
			exibeLinha = true;
			if(encontroConvite.getTipoResposta()!=null && encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.RECUSADO) && !exibeRecusadosCheckBox.getValue()){
				exibeLinha = false;
			}
			if(encontroConvite.getTipoConfirmacao()!=null && encontroConvite.getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA) && !exibeDesistenciaCheckBox.getValue()){
				exibeLinha = false;
			}
			if(exibeLinha){
				encontroConviteTableUtil.addRow(dados,row+1);
			}
			if(encontroConvite.getTipoConfirmacao()!=null && encontroConvite.getTipoConfirmacao().equals(TipoConfirmacaoEnum.DESISTENCIA)){
				desistencia++;
				encontroConviteTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialNormalGrayLineThrough");
			} 
			if(encontroConvite.getTipoResposta()!=null && encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.RECUSADO)){
				recusados++;
				encontroConviteTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialNormalGrayLineThrough");
			} else { 
				if(filaAnterior.getQuantidadeVagas()!=null && contaFila > filaAnterior.getQuantidadeVagas()){
					encontroConviteTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialNormalGray");
				} else {
					convidar++;
				}
				if(encontroConvite.getTipoResposta()!=null && encontroConvite.getTipoResposta().equals(TipoRespostaConviteEnum.ACEITO)){
					encontroConviteTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialBkYellow");
					aceitos++;
				}
				contaFila++;
			}
			if(presenter.getEncontroSelecionado().getQuantidadeAfilhados()!=null && 
			   convidar<=presenter.getEncontroSelecionado().getQuantidadeAfilhados()){
				encontroConviteTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialNormalBlue");
			}
			row++;
		}
		encontroConviteTableUtil.applyDataRowStyles();
		if(aceitos>0){
			if(aceitos==1){
				itemTotal.setText(itemTotal.getText() +  " / " + aceitos + " convite aceito");
			} else {
				itemTotal.setText(itemTotal.getText() +  " / " + aceitos + " convites aceitos");
			}
		}
		if(recusados>0){
			if(recusados==1){
				itemTotal.setText(itemTotal.getText() +  " / " + recusados + " convite recusado");
			} else {
				itemTotal.setText(itemTotal.getText() +  " / " + recusados + " convites recusados");
			}
		}
		if(desistencia>0){
			if(desistencia==1){
				itemTotal.setText(itemTotal.getText() +  " / " + desistencia + " desistência");
			} else {
				itemTotal.setText(itemTotal.getText() +  " / " + desistencia + " desistências");
			}
		}
		showWaitMessage(false);
	}
	
	//fila
	@UiHandler("filasButton")
	public void filasButtonClickHandler(ClickEvent event){
		filasDialogBox.center();
		filasDialogBox.show();
	}
	@UiHandler("fecharFilasButton")
	public void fecharFilasButtonClickHandler(ClickEvent event){
		filasDialogBox.hide();
	}
	@UiHandler("fecharFilaButton")
	public void fecharFilaButtonClickHandler(ClickEvent event){
		editaFilaDialogBox.hide();
	}
	@UiHandler("novaFilaButton")
	public void novaFilaButtonClickHandler(ClickEvent event){
		editaFila(null);
	}
	@UiHandler("salvarFilaButton")
	public void salvarFilaButtonClickHandler(ClickEvent event){
		if(ordemNumberTextBox.getNumber()==null){
			Window.alert("Informe a ordem desta fila");
			return;
		}
		entidadeFilaEditada.setEncontro(presenter.getEncontroSelecionado());
		entidadeFilaEditada.setNome(nomeTextBox.getValue());
		entidadeFilaEditada.setQuantidadeVagas(null);
		if(qtdeVagasNumberTextBox.getNumber()!=null){
			entidadeFilaEditada.setQuantidadeVagas(qtdeVagasNumberTextBox.getNumber().intValue());
		}
		entidadeFilaEditada.setOrdem(null);
		if(ordemNumberTextBox.getNumber()!=null){
			entidadeFilaEditada.setOrdem(ordemNumberTextBox.getNumber().intValue());
		}
		entidadeFilaEditada.setTipoFila((TipoFilaEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoFilaEnum.values()));
		presenter.salvarFila(entidadeFilaEditada);
	}
	private void editaFila(EncontroFila encontroFila) {
		limpaCamposFila();
		if(encontroFila == null){
			entidadeFilaEditada = new EncontroFila();
		} else {
			entidadeFilaEditada = encontroFila;
			defineCamposFila(encontroFila);
		}
		editaFilaDialogBox.center();
		editaFilaDialogBox.show();
		nomeTextBox.setFocus(true);
	}
	
	public void limpaCamposFila(){
		nomeTextBox.setValue(null);
		ordemNumberTextBox.setNumber(1 + listaFilas.size());
		qtdeVagasNumberTextBox.setNumber(null);
	}

	public void defineCamposFila(EncontroFila encontroFila){
		entidadeFilaEditada = encontroFila;
		nomeTextBox.setValue(encontroFila.getNome());
		qtdeVagasNumberTextBox.setNumber(encontroFila.getQuantidadeVagas());
		ordemNumberTextBox.setNumber(encontroFila.getOrdem());
		if(encontroFila.getTipoFila()!=null){
			ListBoxUtil.setItemSelected(tipoListBox, encontroFila.getTipoFila().getNome());
		}
	}

	@Override
	public void populaFilas(List<EncontroFila> lista) {
		this.listaFilas = lista;
		ListBoxUtil.populate(filaListBox, false, lista);
		editaFilaDialogBox.hide();
		populaTabelaFilas(lista);
	}
	
	//casal
	@UiHandler("fecharCasalButton")
	public void fecharCasalButtonClickHandler(ClickEvent event){
		editaCasalDialogBox.hide();
	}
	@UiHandler("editaCasalImage")
	public void novaCasalButtonClickHandler(ClickEvent event){
		editaCasal(entidadeEditada.getCasalConvidado());
	}
	@UiHandler("salvarCasalButton")
	public void salvarCasalButtonClickHandler(ClickEvent event){
		entidadeEditada.getCasalConvidado().setTelefone(telefoneTextBox.getValue());
		entidadeEditada.getCasalConvidado().getEle().setNome(eleNomeTextBox.getValue());
		entidadeEditada.getCasalConvidado().getEle().setEmail(eleEmailTextBox.getValue());
		entidadeEditada.getCasalConvidado().getEle().setTelefoneCelular(eleTelefoneTextBox.getValue());

		entidadeEditada.getCasalConvidado().getEla().setNome(elaNomeTextBox.getValue());
		entidadeEditada.getCasalConvidado().getEla().setEmail(elaEmailTextBox.getValue());
		entidadeEditada.getCasalConvidado().getEla().setTelefoneCelular(elaTelefoneTextBox.getValue());
		
		presenter.salvarCasal(entidadeEditada.getCasalConvidado());
	}
	private void editaCasal(Casal casal) {
		limpaCamposCasal();
		if(casal==null){
			entidadeEditada.setCasalConvidado(new Casal());
			entidadeEditada.getCasalConvidado().setEle(new Pessoa());
			entidadeEditada.getCasalConvidado().setEla(new Pessoa());
		} else {
			defineCamposCasal(casal);
		}
		editaCasalDialogBox.center();
		editaCasalDialogBox.show();
		telefoneTextBox.setFocus(true);
	}
	
	public void limpaCamposCasal(){
		telefoneTextBox.setValue(null);
		eleNomeTextBox.setValue(null);
		eleEmailTextBox.setValue(null);
		eleTelefoneTextBox.setValue(null);
		elaNomeTextBox.setValue(null);
		elaEmailTextBox.setValue(null);
		elaTelefoneTextBox.setValue(null);
	}

	public void defineCamposCasal(Casal casal){
		if(casal!=null){
			telefoneTextBox.setValue(casal.getTelefone());
			eleNomeTextBox.setValue(casal.getEle().getNome());
			eleEmailTextBox.setValue(casal.getEle().getEmail());
			eleTelefoneTextBox.setValue(casal.getEle().getTelefoneCelular());
			elaNomeTextBox.setValue(casal.getEla().getNome());
			elaEmailTextBox.setValue(casal.getEla().getEmail());
			elaTelefoneTextBox.setValue(casal.getEla().getTelefoneCelular());
		}
	}
	// fim casal
	public void populaTabelaFilas(List<EncontroFila> lista) {
		LabelTotalUtil.setTotal(itemFilaTotal, lista.size(), "fila", "filas", "a");
		boolean podeEditar = false;
		if(presenter.getDadosLoginVO().getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			podeEditar = true;
		}
		
		filaTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroFila encontroFila: lista) {
			Object dados[] = new Object[4];
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);

			if(podeEditar){
				editar = new Image("images/edit.png");
				editar.setTitle("Editar as informações da inscrição");
				editar.setStyleName("portal-ImageCursor");
				editar.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						editaFila(encontroFila);
					}
				});
				hp.add(editar);
				
				excluir = new Image("images/delete.png");
				excluir.setStyleName("portal-ImageCursor");
				excluir.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						if(Window.confirm("Deseja excluir esta fila ?")){
							presenter.excluirFila(encontroFila);
						}
					}
				});
				hp.add(excluir);
			}
			
			dados[0] = hp;
			dados[1] = encontroFila.getOrdem();
			dados[2] = encontroFila.getNome();
			dados[3] = encontroFila.getQuantidadeVagas();
			filaTableUtil.addRow(dados,row+1);
			row++;
		}
		filaTableUtil.applyDataRowStyles();
	}

	@Override
	public void populaResponsaveis() {
		ListBoxUtil.populate(responsavelListBox, true, presenter.getListaResponsavel());
	}
	@UiHandler("respostaListBox")
	public void respostaListBoxChangeHandler(ChangeEvent event){
		respostaHTMLPanel.setVisible(false);
		fichaHTMLPanel.setVisible(false);
		gerarInscricaoCheckBox.setVisible(false);
		gerarInscricaoCheckBox.setValue(false);
		moverCheckBox.setVisible(false);
		moverCheckBox.setValue(false);
		TipoRespostaConviteEnum resposta = (TipoRespostaConviteEnum) ListBoxUtil.getItemSelected(respostaListBox, TipoRespostaConviteEnum.values());
		if(resposta!=null){
			if(resposta.equals(TipoRespostaConviteEnum.ACEITO)){
				respostaHTMLPanel.setVisible(true);
				fichaHTMLPanel.setVisible(true);
				gerarInscricaoCheckBox.setVisible(true);
				gerarInscricaoCheckBox.setValue(true);
			} else if(resposta.equals(TipoRespostaConviteEnum.RECUSADO)){
				respostaHTMLPanel.setVisible(true);
				moverCheckBox.setVisible(true);
				moverCheckBox.setValue(true);
			}
		}
		editaDialogBox.center();
	}
	@UiHandler("exibeRecusadosCheckBox")
	public void exibeRecusadosCheckBoxClickHandler(ClickEvent event){
		populaEntidades(presenter.getListaConvites());
	}
	@UiHandler("exibeDesistenciaCheckBox")
	public void exibeDesistenciaCheckBoxClickHandler(ClickEvent event){
		populaEntidades(presenter.getListaConvites());
	}

	@Override
	public void setCasal(Casal casal) {
		entidadeEditada.setCasal(casal);
		casalConvidadoSuggestBox.setValue(casal.toString());
		casalConvidadoSuggest.setListaEntidades(new ArrayList<_WebBaseEntity>());
		casalConvidadoSuggest.getListaEntidades().add(casal);
		defineTelefone(casal);
		editaCasalDialogBox.hide();
	}
}