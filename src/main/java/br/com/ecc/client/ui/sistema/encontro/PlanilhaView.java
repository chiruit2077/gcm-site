package br.com.ecc.client.ui.sistema.encontro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.client.util.NavegadorUtil;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroOrganogramaArea;
import br.com.ecc.model.EncontroOrganogramaCoordenacao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.EncontroTotalizacaoAtividade;
import br.com.ecc.model.OrganogramaArea;
import br.com.ecc.model.Papel;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoAtividadeEnum;
import br.com.ecc.model.tipo.TipoExibicaoPlanilhaEnum;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoPreenchimentoAtividadeEnum;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.model.vo.EncontroOrganogramaVO;
import br.com.ecc.model.vo.EncontroTotalizacaoVO;
import br.com.ecc.model.vo.ParticipanteVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class PlanilhaView extends BaseView<PlanilhaPresenter> implements PlanilhaPresenter.Display {

	@UiTemplate("PlanilhaView.ui.xml")
	interface PlanilhaViewUiBinder extends UiBinder<Widget, PlanilhaView> {}
	private PlanilhaViewUiBinder uiBinder = GWT.create(PlanilhaViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemLabel;

	@UiField ListBox periodoListBox;
	@UiField ListBox planilhaListBox;
	@UiField ListBox planilhaAtividadeListBox;

	@UiField DialogBox selecaoInscricaoDialogBox;
	@UiField Button salvarSelecaoButton;
	@UiField Button desmarcaTodosButton;
	@UiField Button selecionaTodosButton;

	@UiField Button selecionaInscricaoButton;
	@UiField Label itemSelecionaTotal;
	@UiField(provided=true) FlexTable selecionInscricaoFlexTable;
	private FlexTableUtil selecionInscricaoTableUtil = new FlexTableUtil();


	@UiField DialogBox editaAtividadeDialogBox;
	@UiField ListBox tipoListBox;
	@UiField Label porcentagemPadraoLabel;
	@UiField Label mediaLabel;
	@UiField Label entidadeInfoLabel;
	@UiField ListBox atividadeListBox;
	@UiField ListBox atividadeEditaListBox;
	@UiField ListBox addAtividadeListBox;
	@UiField ListBox preenchimentoListBox;
	@UiField CheckBox revisadoCheckBox;
	@UiField CheckBox revisadoInscricaoCheckBox;
	@UiField CheckBox mostraCoordenacaoCheckBox;
	@UiField CheckBox mostraChoqueCheckBox;
	@UiField CheckBox naomostraPadrinhoCheckBox;
	@UiField CheckBox naomostraApoioCheckBox;
	@UiField CheckBox naomostraAtividadeCheckBox;
	@UiField ListBox filtroAtividadeListBox;
	@UiField DateBox inicioDateBox;
	@UiField DateBox fimDateBox;
	@UiField(provided=true) NumberTextBox qtdeNumberTextBox;
	@UiField(provided=true) NumberTextBox porcentagemNumberTextBox;
	@UiField(provided=true) NumberTextBox qtdeMaximaNumberTextBox;
	@UiField(provided=true) NumberTextBox qtdeMesmaNumberTextBox;
	@UiField Button excluirAtividadeButton;

	@UiField DialogBox editaInscricaoDialogBox;
	@UiField Label entidadeEditadaLabel;
	@UiField Label entidadeEditadaTituloLabel;
	@UiField(provided = true) SuggestBox inscricaoSuggestBox1;
	private final GenericEntitySuggestOracle inscricaoSuggest1 = new GenericEntitySuggestOracle();
	@UiField ListBox papelListBox;
	@UiField Button excluirInscricaoButton;
	@UiField Button salvarInscricaoButton;

	@UiField Button preencheAutomaticoInscricaoButton;
	@UiField Button adicionarInscricaoButton;
	@UiField Button excluirTodasInscricaoButton;

	@UiField Button limparPlanilhaButton;
	@UiField Button preencherAutomaticoButton;

	@UiField HTMLPanel atividadeHTMLPanel;
	@UiField HTMLPanel participanteHTMLPanel;
	@UiField HTMLPanel papelHTMLPanel;
	@UiField HTMLPanel opcoesHTMLPanel;

	@UiField ListBox addInscricaoListBox;
	@UiField FlowPanel participantesFlowPanel;
	@UiField Label itemTotal;
	@UiField(provided=true) FlexTable encontroInscricaoFlexTable;
	private FlexTableUtil encontroInscricaoTableUtil = new FlexTableUtil();
	@UiField(provided=true) FlexTable encontroInscricaoAtividadeFlexTable;
	private FlexTableUtil encontroInscricaoAtividadeTableUtil = new FlexTableUtil();

	@UiField(provided = true) RadioButton casalRadio;
	@UiField(provided = true) RadioButton pessoaRadio;

	@UiField HTMLPanel planilhaPanel;
	@UiField VerticalPanel centralPanel;
	@UiField HorizontalPanel opcoesPanel;

	private EncontroAtividade encontroAtividadeEditada;
	private EncontroInscricao encontroInscricaoEditada;
	private EncontroAtividadeInscricao encontroAtividadeInscricaoEditada;

	private List<EncontroAtividadeInscricao> listaParticipantesInscritos;
	private List<EncontroAtividadeInscricao> listaParticipantesInscritosOriginal;
	List<EncontroAtividade> listaEncontroAtividadeExibidas;
	private List<ParticipanteVO> listaParticipantesSugeridos;
	private List<Papel> listaPapel;

	DateTimeFormat dfDia = DateTimeFormat.getFormat("E");
	DateTimeFormat dfHora = DateTimeFormat.getFormat("HH:mm");
	NumberFormat dfPorcent = NumberFormat.getPercentFormat();

	public PlanilhaView() {
		criaTabela();
		criaTabelaSelecao();
		criaTabelaAtividade();
		qtdeNumberTextBox = new NumberTextBox(false, false, 5, 5);
		porcentagemNumberTextBox = new NumberTextBox(false, false, 3, 3);
		qtdeMaximaNumberTextBox = new NumberTextBox(false, false, 3, 3);
		qtdeMesmaNumberTextBox = new NumberTextBox(false, false, 3, 3);
		casalRadio = new RadioButton("tipo", "Por Casal");
		pessoaRadio = new RadioButton("tipo", "Por Pessoa");
		inscricaoSuggest1.setMinimoCaracteres(2);
		inscricaoSuggestBox1 = new SuggestBox(inscricaoSuggest1);

		initWidget(uiBinder.createAndBindUi(this));

		inscricaoSuggestBox1.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox1.getValue().equals("")){
					addInscricaoListBox.setSelectedIndex(0);
					addAtividadeListBox.setSelectedIndex(-1);
				}
			}
		});

		planilhaPanel.setWidth(this.getWindowWidth() - 30 +"px");
		planilhaPanel.setHeight(this.getWindowHeight() - 140 +"px");

		tituloFormularioLabel.setText(getDisplayTitle());

		inicioDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		inicioDateBox.getTextBox().setAlignment(TextAlignment.CENTER);

		fimDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		fimDateBox.getTextBox().setAlignment(TextAlignment.CENTER);

		ListBoxUtil.populate(tipoListBox, false, TipoAtividadeEnum.values());
		ListBoxUtil.populate(preenchimentoListBox, false, TipoPreenchimentoAtividadeEnum.values());

		ListBoxUtil.populate(planilhaListBox, true, TipoExibicaoPlanilhaEnum.values());
	}

	private void criaTabela() {
		encontroInscricaoFlexTable = new FlexTable();
		encontroInscricaoFlexTable.setStyleName("portal-formSmall");
		encontroInscricaoTableUtil.initialize(encontroInscricaoFlexTable);

		encontroInscricaoTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoTableUtil.addColumn("Participante", null, HasHorizontalAlignment.ALIGN_LEFT);
		encontroInscricaoTableUtil.addColumn("Papel", "200", HasHorizontalAlignment.ALIGN_LEFT);
		encontroInscricaoTableUtil.addColumn("Revisado", "50", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoTableUtil.addColumn("Info", "10", HasHorizontalAlignment.ALIGN_CENTER);
	}

	private void criaTabelaSelecao() {
		selecionInscricaoFlexTable = new FlexTable();
		selecionInscricaoFlexTable.setStyleName("portal-formSmall");
		selecionInscricaoTableUtil.initialize(selecionInscricaoFlexTable);

		selecionInscricaoTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		selecionInscricaoTableUtil.addColumn("Casal", null, HasHorizontalAlignment.ALIGN_LEFT);
		selecionInscricaoTableUtil.addColumn("Tipo", "100", HasHorizontalAlignment.ALIGN_LEFT);
		selecionInscricaoTableUtil.addColumn("Qtde", "50", HasHorizontalAlignment.ALIGN_RIGHT);
		selecionInscricaoTableUtil.addColumn("Qtde M.A.", "80", HasHorizontalAlignment.ALIGN_RIGHT);
	}

	private void criaTabelaAtividade() {
		encontroInscricaoAtividadeFlexTable = new FlexTable();
		encontroInscricaoAtividadeFlexTable.setStyleName("portal-formSmall");
		encontroInscricaoAtividadeTableUtil.initialize(encontroInscricaoAtividadeFlexTable);

		encontroInscricaoAtividadeTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoAtividadeTableUtil.addColumn("Dia", "40", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoAtividadeTableUtil.addColumn("Inicio", "50", HasHorizontalAlignment.ALIGN_CENTER,TipoColuna.DATE, "HH:mm");
		encontroInscricaoAtividadeTableUtil.addColumn("Fim", "50", HasHorizontalAlignment.ALIGN_CENTER,TipoColuna.DATE, "HH:mm");
		encontroInscricaoAtividadeTableUtil.addColumn("Tipo", "100", HasHorizontalAlignment.ALIGN_LEFT);
		encontroInscricaoAtividadeTableUtil.addColumn("Atividade", null, HasHorizontalAlignment.ALIGN_LEFT);
		encontroInscricaoAtividadeTableUtil.addColumn("Papel", "100", HasHorizontalAlignment.ALIGN_LEFT);
		encontroInscricaoAtividadeTableUtil.addColumn("Revisado", "50", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoAtividadeTableUtil.addColumn("Info", "10", HasHorizontalAlignment.ALIGN_CENTER);
	}

	@UiHandler("fecharAtividadeButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaAtividadeDialogBox.hide();
	}

	@UiHandler("fecharSelecaoButton")
	public void fecharSelecionaButtonClickHandler(ClickEvent event){
		selecaoInscricaoDialogBox.hide();
	}

	@UiHandler("excluirAtividadeButton")
	public void excluirButtonClickHandler(ClickEvent event){
		if(Window.confirm("Deseja excluir esta atividade ?")){
			presenter.excluirAtividade(encontroAtividadeEditada);
		}
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@UiHandler("preenchimentoListBox")
	public void preenchimentoListBox(ChangeEvent event){
		TipoPreenchimentoAtividadeEnum tipo = (TipoPreenchimentoAtividadeEnum) ListBoxUtil.getItemSelected(preenchimentoListBox, TipoPreenchimentoAtividadeEnum.values());
		if(tipo.equals(TipoPreenchimentoAtividadeEnum.VARIAVEL)){
			qtdeNumberTextBox.setEnabled(true);
			qtdeNumberTextBox.setFocus(true);
		}else{
			qtdeNumberTextBox.setValue(null);
			qtdeNumberTextBox.setEnabled(false);
		}
	}

	@UiHandler("salvarAtividadeButton")
	public void salvarAtividadeButtonClickHandler(ClickEvent event){
		Atividade atividade = (Atividade)ListBoxUtil.getItemSelected(atividadeListBox, presenter.getGrupoEncontroVO().getListaAtividade());
		encontroAtividadeEditada.setAtividade(atividade);
		encontroAtividadeEditada.setEncontro(presenter.getEncontroSelecionado());
		encontroAtividadeEditada.setFim(fimDateBox.getValue());
		encontroAtividadeEditada.setInicio(inicioDateBox.getValue());
		encontroAtividadeEditada.setQuantidadeDesejada(null);
		if(qtdeNumberTextBox.getNumber()!=null){
			encontroAtividadeEditada.setQuantidadeDesejada(qtdeNumberTextBox.getNumber().intValue());
		}
		encontroAtividadeEditada.setPorcentagem(null);
		if(porcentagemNumberTextBox.getNumber()!=null){
			encontroAtividadeEditada.setPorcentagem(porcentagemNumberTextBox.getNumber().intValue());
		}
		encontroAtividadeEditada.setTipoAtividade((TipoAtividadeEnum)ListBoxUtil.getItemSelected(tipoListBox, TipoAtividadeEnum.values()));
		encontroAtividadeEditada.setTipoPreenchimento((TipoPreenchimentoAtividadeEnum)ListBoxUtil.getItemSelected(preenchimentoListBox, TipoPreenchimentoAtividadeEnum.values()));
		encontroAtividadeEditada.setRevisado(revisadoCheckBox.getValue());
		presenter.salvarAtividade(encontroAtividadeEditada);
		editaAtividadeDialogBox.hide();
	}
	private void edita(EncontroAtividade encontroAtividade) {
		limpaCamposAtividade();
		if(encontroAtividade == null){
			encontroAtividadeEditada = new EncontroAtividade();
			if(inicioDateBox.getValue()==null){
				inicioDateBox.setValue(presenter.getEncontroSelecionado().getInicio());
				fimDateBox.setValue(presenter.getEncontroSelecionado().getInicio());
			}
		} else {
			encontroAtividadeEditada = encontroAtividade;
			defineCamposAtividade(encontroAtividade);
			excluirAtividadeButton.setVisible(true);
		}
		editaAtividadeDialogBox.center();
		editaAtividadeDialogBox.show();
		tipoListBox.setFocus(true);
	}

	public void limpaCamposAtividade(){
		excluirAtividadeButton.setVisible(false);
		revisadoCheckBox.setValue(false);
		qtdeNumberTextBox.setValue(null);
		porcentagemNumberTextBox.setValue(null);
	}

	public void defineCamposAtividade(EncontroAtividade encontroAtividade){
		encontroAtividadeEditada = encontroAtividade;
		if(encontroAtividade.getTipoAtividade()!=null){
			ListBoxUtil.setItemSelected(tipoListBox, encontroAtividade.getTipoAtividade().getNome());
			porcentagemPadraoLabel.setText("Porcentagem Padrão: " + encontroAtividade.getTipoAtividade().getPorcentagem());
		}
		if(encontroAtividade.getTipoPreenchimento()!=null){
			ListBoxUtil.setItemSelected(preenchimentoListBox, encontroAtividade.getTipoPreenchimento().getNome());
			if (encontroAtividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.VARIAVEL)){
				if (encontroAtividade.getQuantidadeDesejada()!=null)
					qtdeNumberTextBox.setValue(encontroAtividade.getQuantidadeDesejada().toString());
				qtdeNumberTextBox.setEnabled(true);
			}else{
				qtdeNumberTextBox.setValue(null);
				qtdeNumberTextBox.setEnabled(false);
			}
		}
		if (encontroAtividade.getPorcentagem()!=null)
			porcentagemNumberTextBox.setValue(encontroAtividade.getPorcentagem().toString());
		if(encontroAtividade.getAtividade()!=null){
			ListBoxUtil.setItemSelected(atividadeListBox, encontroAtividade.getAtividade().getNome());
		}

		revisadoCheckBox.setValue(encontroAtividade.getRevisado());
		inicioDateBox.setValue(encontroAtividade.getInicio());
		fimDateBox.setValue(encontroAtividade.getFim());
	}

	@Override
	public String getDisplayTitle() {
		return "Planilha de Atividades";
	}

	@Override
	public void reset() {
		editaAtividadeDialogBox.hide();
		editaInscricaoDialogBox.hide();
		selecaoInscricaoDialogBox.hide();
		planilhaPanel.clear();
	}

	@Override
	public void populaPlanilha() {
		listaParticipantesInscritos = null;
		listaParticipantesInscritosOriginal = null;
		boolean bCoordenador = presenter.isCoordenador();
		EncontroPeriodo encontroPeriodoSelecionado = getPeriodoSelecionado();
		List<EncontroPeriodo> listaPeriodos = presenter.getEncontroVO().getListaPeriodo();
		if (encontroPeriodoSelecionado!=null){
			listaPeriodos = new ArrayList<EncontroPeriodo>();
			listaPeriodos.add(encontroPeriodoSelecionado);
		}
		TipoExibicaoPlanilhaEnum tipoExibicao = getTipoExibicaoPlanilhaSelecionado();

		List<EncontroAtividade> listaEncontroAtividade = new ArrayList<EncontroAtividade>();
		listaEncontroAtividadeExibidas = new ArrayList<EncontroAtividade>();
		List<EncontroInscricao> listaEncontroInscricao = new ArrayList<EncontroInscricao>();
		List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao = presenter.getListaEncontroAtividadeInscricao();

		if(tipoExibicao.equals(TipoExibicaoPlanilhaEnum.COMPLETA)){
			listaEncontroInscricao = montaListaEncontroInscricaoSemExterno();
			listaEncontroAtividade = presenter.getEncontroVO().getListaEncontroAtividade();
		} else if(tipoExibicao.equals(TipoExibicaoPlanilhaEnum.COMPLETAEXTERNO)){
			listaEncontroInscricao = presenter.getEncontroVO().getListaInscricao();
			listaEncontroAtividade = presenter.getEncontroVO().getListaEncontroAtividade();
		} else if (tipoExibicao.equals(TipoExibicaoPlanilhaEnum.MINHA_ATIVIDADE_MINHA_COLUNA)){
			listaEncontroInscricao = montaListaEncontroInscricaoUsuarioAtual();
			listaEncontroAtividade = montaListaEncontroAtividadeUsuarioAtual();
		} else if (tipoExibicao.equals(TipoExibicaoPlanilhaEnum.MINHA_ATIVIDADE_TODAS_COLUNAS)){
			listaEncontroInscricao = presenter.getEncontroVO().getListaInscricao();
			listaEncontroAtividade = montaListaEncontroAtividadeUsuarioAtual();
		} else if (tipoExibicao.equals(TipoExibicaoPlanilhaEnum.TODAS_ATIVIDADES_MINHA_COLUNA)){
			listaEncontroInscricao = montaListaEncontroInscricaoUsuarioAtual();
			listaEncontroAtividade = presenter.getEncontroVO().getListaEncontroAtividade();
		}

		Collections.sort(listaEncontroAtividade, new Comparator<EncontroAtividade>() {
			@Override
			public int compare(EncontroAtividade o1, EncontroAtividade o2) {
				if(o1.getInicio().equals(o2.getInicio())){
					if(o1.getFim().equals(o2.getFim())){
						return o1.getAtividade().getNome().compareTo(o2.getAtividade().getNome());
					}
					return o1.getFim().compareTo(o2.getFim());
				}
				return o1.getInicio().compareTo(o2.getInicio());
			}
		});
		Collections.sort(listaEncontroInscricao, new Comparator<EncontroInscricao>() {
			@Override
			public int compare(EncontroInscricao o1, EncontroInscricao o2) {
				String n1 = o1.getCasal()==null?o1.getPessoa().getApelido():o1.getCasal().getApelidos(null);
				String n2 = o2.getCasal()==null?o2.getPessoa().getApelido():o2.getCasal().getApelidos(null);
				return n1.compareTo(n2.toString());
			}
		});

		Collections.sort(listaPeriodos, new Comparator<EncontroPeriodo>() {
			@Override
			public int compare(EncontroPeriodo o1, EncontroPeriodo o2) {
				return o1.getInicio().compareTo(o2.getInicio());
			}
		});

		List<ParticipanteVO> listaParticipantes = new ArrayList<ParticipanteVO>();

		planilhaPanel.clear();

		String parLinha="", parColuna="", tipoAtividade="", colunaPadrinho="", nome="";
		StringBuffer participante = new StringBuffer("");
		StringBuffer html = new StringBuffer(
			" <table cellpadding='0' cellspacing='0' border='0' align='left' height='100%'>" +
			" <tr><td><div><table cellpadding='0' cellspacing='0' border='0' style='font-size:10px;' align='left'>" +
				"<tr style='height:100px;background-color:#e3e3e3;'>" +
				"	<td class='portal-celulaPlanilhaDiaHead'>Dia</td>" +
				"	<td class='portal-celulaPlanilhaHoraHead'>Inicio</td>" +
				"	<td class='portal-celulaPlanilhaHoraHead'>Fim</td>");
		if(bCoordenador){
			html.append("	<td class='portal-celulaPlanilhaHead'>&nbsp;</td>");
		}
		html.append("	<td class='portal-celulaPlanilhaTipoHead'>Tipo</td>");
		if(bCoordenador){
			html.append("	<td class='portal-celulaPlanilhaAtividadeHead' id='colunaAtividade'></td>");
		} else {
			html.append("	<td class='portal-celulaPlanilhaAtividadeHead'>Atividade</td>");
		}
		html.append("	<td class='portal-celulaPlanilhaHead'>&nbsp;</td>" +
				"   <td class='portal-celulaPlanilhaHead'>I</td>" +
				"   <td class='portal-celulaPlanilhaHead'>Qt</td>");
		for (EncontroInscricao ei : listaEncontroInscricao) {
				ParticipanteVO p = new ParticipanteVO();
				p.setEncontroInscricao(ei);
				p.getEncontroAtividadeInscricaos().clear();

				listaParticipantes.add(p);
				if(ei.getCasal()!=null && ei.getCasal().getId().equals(presenter.getCasal().getId()) ||
				   ei.getPessoa()!=null && ei.getPessoa().getId().equals(presenter.getUsuario().getPessoa().getId())){
					colunaPadrinho = "style= 'background-color: #ffbf95;'";
				} else {
					if(ei.getTipo().equals(TipoInscricaoEnum.PADRINHO)){
						colunaPadrinho = "style= 'background-color: #fdf3b5;'";
					}else if(ei.getTipo().equals(TipoInscricaoEnum.EXTERNO)){
						colunaPadrinho = "style= 'background-color: #CD853F;'";
					}else {
						colunaPadrinho = "";
					}
				}
				html.append("<td id='editP_" + ei.getId() + "' class='portal-celulaPlanilhaInscritoHead' " + colunaPadrinho + " ></td>");
		}
		html.append("</tr></table></div></td></tr>");
		html.append("<tr><td><div><table cellpadding='0' cellspacing='0' border='0' style='font-size:10px;' align='left' height='100%'>");


		int colspan=6;
		if(bCoordenador)colspan=9;

		for (EncontroPeriodo encontroPeriodo : listaPeriodos) {
			List<String> lista = new ArrayList<String>();
			int linha=0;
			int coluna=0;

			for (final ParticipanteVO participanteVO : listaParticipantes) {
				participanteVO.setQtdeAtividades(0);
				participanteVO.getTags().clear();
			}


			List<EncontroAtividade> listaEA = montaListaAtividadesPorPeriodoSelecionado(listaEncontroAtividade,encontroPeriodo);
			if (listaEA.size()>0){
				listaEncontroAtividadeExibidas.addAll(listaEA);
				html.append("<tr style='background-color:#cdffbf;'>");
				html.append("<td colspan='" + (colspan+listaParticipantes.size()) + "' class='portal-celulaPlanilha' style='text-align:left;'> Atividades para \"" + encontroPeriodo.getNome() + "\"</td>");
				html.append("</tr>");
				for (EncontroAtividade ea : listaEA) {
					ea.getEncontroAtividadeInscricaos().clear();
					ea.setQuantidadeInscricoes(0);
					lista.clear();
					for (EncontroAtividadeInscricao eai : listaEncontroAtividadeInscricao) {
						if(eai.getEncontroAtividade().getId().equals(ea.getId())){
							ea.setQuantidadeInscricoes(ea.getQuantidadeInscricoes()+1);
							lista.add(eai.getEncontroInscricao().toStringApelidos() + " - " + eai.getPapel().toString());
						}
					}
					if(ea.getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE)){
						tipoAtividade = "color:blue;";
					} else {
						tipoAtividade = "";
					}
					if(linha % 2 == 0) {
						parLinha = "style='"+ tipoAtividade +"'";
					} else {
						parLinha = "style='background-color:#e3e3e3;"+ tipoAtividade +"'";
					}
					html.append("<tr " + parLinha + ">");
					html.append("<td class='portal-celulaPlanilhaDia'>" + dfDia.format(ea.getInicio()).toUpperCase() + "</td>");
					html.append("<td class='portal-celulaPlanilhaHora'>" + dfHora.format(ea.getInicio()) + "</td>");
					html.append("<td class='portal-celulaPlanilhaHora'>" + dfHora.format(ea.getFim()) + "</td>");
					if(bCoordenador){
						html.append("<td id='editA_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
					}
					html.append("<td class='portal-celulaPlanilhaTipo' style='text-align:left;'>" + ea.getTipoAtividade().getNome() + "</td>");
					html.append("<td class='portal-celulaPlanilhaAtividade' style='text-align:left;white-space: nowrap;'>" + ea.getAtividade().getNome() + "</td>");
					html.append("<td id='add_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
					html.append("<td id='info_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
					participante = new StringBuffer("");
					coluna = 0;
					for (ParticipanteVO participanteVO : listaParticipantes) {
						boolean achou = false;
						if(coluna% 2 == 0) {
							parColuna = "style='background-color:#f0f0f0;'";
						} else {
							parColuna = "";
						}
						if(participanteVO.getEncontroInscricao().getCasal()!=null && participanteVO.getEncontroInscricao().getCasal().getId().equals(presenter.getCasal().getId()) ||
								participanteVO.getEncontroInscricao().getPessoa()!=null && participanteVO.getEncontroInscricao().getPessoa().getId().equals(presenter.getUsuario().getPessoa().getId())){
							parColuna = "style= 'background-color: #ffbf95;'";
						} else {
							if(participanteVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.PADRINHO)){
								parColuna = "style= 'background-color: #fdf3b5;'";
							}else if(participanteVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.EXTERNO)){
								parColuna = "style= 'background-color: #CD853F;'";
							}else {
								parColuna = "";
							}
						}
						for (EncontroAtividadeInscricao eai : listaEncontroAtividadeInscricao) {
							if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
								if(eai.getEncontroAtividade().getId().equals(ea.getId())){
									ea.getEncontroAtividadeInscricaos().add(eai);
									participanteVO.getEncontroAtividadeInscricaos().add(eai);
									if(bCoordenador){
										participante.append("<td id='edit_" + eai.getId() + "' class='portal-celulaPlanilha' " + parColuna + "></td>");
									} else {
										participante.append("<td class='portal-celulaPlanilha' " + parColuna + ">" + eai.getPapel().getSigla() + "</td>");
									}
									participanteVO.setQtdeAtividades(participanteVO.getQtdeAtividades()+1);
									if (participanteVO.getTags()!=null)
										participanteVO.getTags().add(eai.getEncontroAtividade().toString() + " - " + eai.getPapel().toString());
									achou= true;
									break;
								}
							}
						}
						if(!achou){
							participante.append("<td class='portal-celulaPlanilha' " + parColuna + "/>");
						}
						coluna++;
					}
					html.append("<td class='portal-celulaPlanilha' title='Participantes:" + convertListToStringLinhas(lista) +"'>" + ea.getQuantidadeInscricoes() + "</td>");
					html.append(participante);
					html.append("</tr>");
					linha++;
				}
				html.append("<tr style='background-color:#cdffbf;'>");
				html.append("<td colspan='" + colspan + "' class='portal-celulaPlanilha' style='text-align:left;'> Total de atividades para \"" + encontroPeriodo.getNome() + "\":</td>");
				for (ParticipanteVO participanteVO : listaParticipantes) {
					html.append("<td class='portal-celulaPlanilha' title='" + participanteVO.getEncontroInscricao().toStringApelidos() + convertListToStringLinhas(participanteVO.getTags()) + "'>"  +
							participanteVO.getQtdeAtividades() + "</td>");
				}
				html.append("</tr><tr><td colspan='" + (colspan+listaParticipantes.size()) + "' class='portal-celulaPlanilha'>&nbsp;</td></tr>");
			}
		}
		if (listaPeriodos.size()==0){
			List<String> lista = new ArrayList<String>();
			int linha=0;
			int coluna=0;

			List<EncontroAtividade> listaEA = montaListaAtividadesPorPeriodoSelecionado(listaEncontroAtividade,null);
			listaEncontroAtividadeExibidas.addAll(listaEA);

			for (final ParticipanteVO participanteVO : listaParticipantes) {
				participanteVO.setQtdeAtividades(0);
				participanteVO.getTags().clear();
			}
			for (EncontroAtividade ea : listaEA) {
				ea.getEncontroAtividadeInscricaos().clear();
				ea.setQuantidadeInscricoes(0);
				lista.clear();
				for (EncontroAtividadeInscricao eai : listaEncontroAtividadeInscricao) {
					if(eai.getEncontroAtividade().getId().equals(ea.getId())){
						ea.setQuantidadeInscricoes(ea.getQuantidadeInscricoes()+1);
						lista.add(eai.getEncontroInscricao().toStringApelidos() + " - " + eai.getPapel().toString());
					}
				}
				if(ea.getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE)){
					tipoAtividade = "color:blue;";
				} else {
					tipoAtividade = "";
				}
				if(linha % 2 == 0) {
					parLinha = "style='"+ tipoAtividade +"'";
				} else {
					parLinha = "style='background-color:#e3e3e3;"+ tipoAtividade +"'";
				}
				html.append("<tr " + parLinha + ">");
				html.append("<td class='portal-celulaPlanilhaDia'>" + dfDia.format(ea.getInicio()).toUpperCase() + "</td>");
				html.append("<td class='portal-celulaPlanilhaHora'>" + dfHora.format(ea.getInicio()) + "</td>");
				html.append("<td class='portal-celulaPlanilhaHora'>" + dfHora.format(ea.getFim()) + "</td>");
				if(bCoordenador){
					html.append("<td id='editA_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
				}
				html.append("<td class='portal-celulaPlanilhaTipo' style='text-align:left;'>" + ea.getTipoAtividade().getNome() + "</td>");
				html.append("<td class='portal-celulaPlanilhaAtividade' style='text-align:left;white-space: nowrap;'>" + ea.getAtividade().getNome() + "</td>");
				html.append("<td id='add_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
				html.append("<td id='info_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
				participante = new StringBuffer("");
				coluna = 0;
				for (ParticipanteVO participanteVO : listaParticipantes) {
					boolean achou = false;
					if(coluna% 2 == 0) {
						parColuna = "style='background-color:#f0f0f0;'";
					} else {
						parColuna = "";
					}
					if(participanteVO.getEncontroInscricao().getCasal()!=null && participanteVO.getEncontroInscricao().getCasal().getId().equals(presenter.getCasal().getId()) ||
							participanteVO.getEncontroInscricao().getPessoa()!=null && participanteVO.getEncontroInscricao().getPessoa().getId().equals(presenter.getUsuario().getPessoa().getId())){
						parColuna = "style= 'background-color: #ffbf95;'";
					} else {
						if(participanteVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.PADRINHO)){
							parColuna = "style= 'background-color: #fdf3b5;'";
						}else if(participanteVO.getEncontroInscricao().getTipo().equals(TipoInscricaoEnum.EXTERNO)){
							parColuna = "style= 'background-color: #CD853F;'";
						}else {
							parColuna = "";
						}
					}
					for (EncontroAtividadeInscricao eai : listaEncontroAtividadeInscricao) {
						if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
							if(eai.getEncontroAtividade().getId().equals(ea.getId())){
								ea.getEncontroAtividadeInscricaos().add(eai);
								participanteVO.getEncontroAtividadeInscricaos().add(eai);
								if(bCoordenador){
									participante.append("<td id='edit_" + eai.getId() + "' class='portal-celulaPlanilha' " + parColuna + "></td>");
								} else {
									participante.append("<td class='portal-celulaPlanilha' " + parColuna + ">" + eai.getPapel().getSigla() + "</td>");
								}
								participanteVO.setQtdeAtividades(participanteVO.getQtdeAtividades()+1);
								if (participanteVO.getTags()!=null)
									participanteVO.getTags().add(eai.getEncontroAtividade().toString() + " - " + eai.getPapel().toString());
								achou= true;
								break;
							}
						}
					}
					if(!achou){
						participante.append("<td class='portal-celulaPlanilha' " + parColuna + "/>");
					}
					coluna++;
				}
				html.append("<td class='portal-celulaPlanilha' title='Participantes:" + convertListToStringLinhas(lista) +"'>" + ea.getQuantidadeInscricoes() + "</td>");
				html.append(participante);
				html.append("</tr>");
				linha++;
			}
		}

		//totalizadores
		if(bCoordenador){
			html.append("<tr><td colspan='99' class='portal-celulaPlanilha'>&nbsp;</td></tr>");
			for (EncontroTotalizacaoVO totalizador : presenter.getEncontroVO().getListaTotalizacao()) {
				html.append("<tr>");
				html.append("<td colspan='" + colspan + "' class='portal-celulaPlanilha' style='text-align:left;'>" + totalizador.getEncontroTotalizacao().getNome() + "</td>");
				for (ParticipanteVO participanteVO : listaParticipantes) {
					participanteVO.setQtdeAtividades(0);
					for (EncontroAtividadeInscricao eai : listaEncontroAtividadeInscricao) {
						if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
							for (EncontroTotalizacaoAtividade atividade : totalizador.getListaAtividade()) {
								if(atividade.getAtividade()==null || eai.getEncontroAtividade().getAtividade().getId().equals(atividade.getAtividade().getId())){
									if(atividade.getTipoAtividade()==null || atividade.getTipoAtividade().equals(eai.getEncontroAtividade().getTipoAtividade())){
										participanteVO.setQtdeAtividades(participanteVO.getQtdeAtividades()+1);
									}
								}
							}
						}
					}
					html.append("<td class='portal-celulaPlanilha' title='" + participanteVO.getEncontroInscricao().toStringApelidos()+"'>" + participanteVO.getQtdeAtividades() + "</td>");
				}
				html.append("</tr>");
			}
		}

		html.append("</table></div></td></tr></table>");
		HTMLPanel htmlPanel = new HTMLPanel(new String(html));

		int qtdeok = 0;
		int qtdeerro = 0;
		int qtdeatencao = 0;
		int qtdeatividade=0;

		for (final ParticipanteVO participanteVO : listaParticipantes) {
			final EncontroInscricao ei = participanteVO.getEncontroInscricao();
			if(ei.getCasal()!=null){
				nome = ei.getCasal().getApelidos(" e ");
			} else {
				nome = ei.getPessoa().getApelido();
			}
			HTML label = new HTML(nome);
			if (NavegadorUtil.navegador.equals("ie"))
				label.setStyleName("portal-celulaPlanilhaVerticalIE");
			else
				label.setStyleName("portal-celulaPlanilhaVertical");
			if(bCoordenador){
				label.setTitle("Editar/Adicionar atividades para este participante");
			} else {
				label.setTitle("Visualizar as atividades para este participante");
			}
			label.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					listaParticipantesInscritos = new ArrayList<EncontroAtividadeInscricao>();
					listaParticipantesInscritosOriginal = new ArrayList<EncontroAtividadeInscricao>();
					listaParticipantesInscritos.addAll(participanteVO.getEncontroAtividadeInscricaos());
					listaParticipantesInscritosOriginal.addAll(participanteVO.getEncontroAtividadeInscricaos());
					editaInscricao(ei);
				}
			});
			htmlPanel.add(label, "editP_"+ei.getId().toString());
		}

		Image addImage, editImage, infoImage;
		if(bCoordenador){
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(3);
			hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			addImage = new Image("images/add.png");
			addImage.setStyleName("portal-ImageCursor");
			addImage.setTitle("Adicionar uma nova atividade");
			addImage.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(null);
				}
			});
			hp.add(addImage);
			hp.add(new Label("Atividade"));
			htmlPanel.add(hp, "colunaAtividade");
		}

		for (final EncontroAtividade ea : listaEncontroAtividadeExibidas) {
			verificaInconsistenciasAtividade(ea);
			List<EncontroAtividadeInscricao> inscricoes = ea.getEncontroAtividadeInscricaos();

			qtdeatividade++;
			addImage = new Image();
			addImage.setStyleName("portal-ImageCursor");
			if(bCoordenador){
				addImage.setTitle("Adicionar/Editar participantes da atividade");
				addImage.setUrl("images/add.png");
			} else {
				addImage.setTitle("Visualizar participantes desta atividade");
				addImage.setUrl("images/edit.png");
			}
			addImage.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					listaParticipantesInscritos = new ArrayList<EncontroAtividadeInscricao>();
					listaParticipantesInscritosOriginal = new ArrayList<EncontroAtividadeInscricao>();
					listaParticipantesInscritos.addAll(ea.getEncontroAtividadeInscricaos());
					listaParticipantesInscritosOriginal.addAll(ea.getEncontroAtividadeInscricaos());
					editaAtividade(ea);
				}
			});
			htmlPanel.add(addImage, "add_"+ea.getId().toString());

			infoImage = new Image();
			infoImage.setSize("16px", "16px");
			infoImage.setStyleName("portal-ImageCursor");
			if(ea.getRevisado() && ea.getInfoErro().size() == 0 && ea.getInfoAtencao().size() == 0 ){
				infoImage.setUrl("images/inforevisado.png");
				infoImage.setTitle("Preenchimento Revisado");
				qtdeok++;
			}else if(ea.getInfoErro().size() == 0 && ea.getInfoAtencao().size() == 0){
				infoImage.setUrl("images/infook.png");
				infoImage.setTitle("Preenchimento Ok");
				qtdeok++;
			}else if(ea.getInfoErro().size() > 0 ){
				infoImage.setUrl("images/infoerror.png");
				infoImage.setTitle("Preenchimento com Erros - " +convertListToStringLinhas(ea.getInfoErro()));
				ea.getInfoErro().toString();
				qtdeerro++;
			}else if(ea.getInfoAtencao().size() > 0 ){
				infoImage.setUrl("images/infowarning.png");
				infoImage.setTitle("Preenchimento com Atenção - " + convertListToStringLinhas(ea.getInfoAtencao()));
				qtdeatencao++;
			}
			htmlPanel.add(infoImage, "info_"+ea.getId().toString());

			if(bCoordenador){
				editImage = new Image("images/edit.png");
				editImage.setStyleName("portal-ImageCursor");
				editImage.setTitle("Editar dados da atividade");
				editImage.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						edita(ea);
					}
				});
				htmlPanel.add(editImage, "editA_"+ea.getId().toString());
				for (final EncontroAtividadeInscricao eai : inscricoes) {
					if(eai.getEncontroAtividade().getId().equals(ea.getId())){
						HTML label = new HTML(eai.getPapel().getSigla());
						label.setTitle(eai.getEncontroInscricao().toStringApelidos());
						if(eai.getInfoErro().size()>0){
							label.setStyleName("portal-ImageCursorErro");
							label.setTitle(label.getTitle() + "\rErros - " + convertListToStringLinhas(eai.getInfoErro()) + convertListToStringLinhas(eai.getInfoAtencao()));
						}
						else if(eai.getInfoErro().size()>0){
							label.setStyleName("portal-ImageCursorAtencao");
							label.setTitle(label.getTitle() + "\rErros - " + convertListToStringLinhas(eai.getInfoAtencao()));
						}
						else
							label.setStyleName("portal-ImageCursor");
						label.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent arg0) {
								editaAtividadeInscricao(eai);
							}
						});
						htmlPanel.add(label, "edit_"+eai.getId().toString());
					}
				}
			}
		}

		String totais="Resumo: " + qtdeatividade + " atividades";
		if(qtdeok == 1 || qtdeok == 0){
			totais += " / " + qtdeok + " ok " + dfPorcent.format(qtdeatividade == 0 ? 0 : (double)qtdeok/(double)qtdeatividade);
		} else {
			totais += " / " + qtdeok + " ok " + dfPorcent.format(qtdeatividade == 0 ? 0 : (double)qtdeok/(double)qtdeatividade);
		}
		if(qtdeerro == 1 || qtdeerro == 0){
			totais += " / " + qtdeerro + " erro " + dfPorcent.format(qtdeatividade == 0 ? 0 : (double)qtdeerro/(double)qtdeatividade);
		} else {
			totais += " / " + qtdeerro + " erros " + dfPorcent.format(qtdeatividade == 0 ? 0 : (double)qtdeerro/(double)qtdeatividade);
		}
		if(qtdeok == 1 || qtdeok == 0){
			totais += " / " + qtdeatencao + " atenção " + dfPorcent.format(qtdeatividade == 0 ? 0 : (double)qtdeatencao/(double)qtdeatividade);
		} else {
			totais += " / " + qtdeatencao + " atenção " + dfPorcent.format(qtdeatividade == 0 ? 0 : (double)qtdeatencao/(double)qtdeatividade);
		}
		itemLabel.setText(totais);

		planilhaPanel.add(htmlPanel);
	}

	private boolean getChoqueHorarios(EncontroAtividadeInscricao eai) {
		return getChoqueHorarios(eai, eai.getEncontroAtividade(), eai.getEncontroInscricao(), eai.getPapel());
	}

	private boolean getChoqueHorariosSeguidos(EncontroAtividadeInscricao eai, EncontroAtividade ea, EncontroInscricao ei, Papel papel) {
		List<EncontroAtividade> listaEncontroAtividade = montaListaAtividadesPorPeriodoSelecionado(presenter.getEncontroVO().getListaEncontroAtividade(),getPeriodoSelecionado());
		List<EncontroAtividade> listaSeguidas = new ArrayList<EncontroAtividade>();
		int qtde=0;
		for (EncontroAtividade encontroAtividade : listaEncontroAtividade) {
			if (!encontroAtividade.equals(ea)) {
				if((encontroAtividade.getFim().after(new Date(ea.getInicio().getTime()-(1000*60*60))) &&
						(encontroAtividade.getFim().before(ea.getInicio())))||
						((encontroAtividade.getInicio().before(new Date(ea.getFim().getTime()+(1000*60*60)))) &&
								(encontroAtividade.getInicio().after(ea.getFim())))){
					listaSeguidas.add(encontroAtividade);
				}
			}
		}
		for (EncontroAtividade encontroAtividade : listaSeguidas) {
			EncontroAtividadeInscricao inscricao = getEncontroAtividadeInscricaoAtivdade(encontroAtividade, ei);
			if (inscricao!=null && !inscricao.getRevisado() && !inscricao.getPapel().getChocaPlanilha()){
				if(eai != null){
					eai.getInfoAtencao().add(encontroAtividade.toString());
				}
				else{
					qtde++;
				}
			}
		}
		if (qtde>3) return true;
		return false;
	}

	private boolean getChoqueHorarios(EncontroAtividadeInscricao eai, EncontroAtividade ea, EncontroInscricao ei, Papel papel) {
		if (papel.getChocaPlanilha()) return false;
		if (eai != null){
			eai.getInfoErro().clear();
			eai.getInfoAtencao().clear();
			if (eai.getRevisado()) return false;
		}
		List<EncontroAtividade> listaEncontroAtividade = presenter.getEncontroVO().getListaEncontroAtividade();
		List<EncontroAtividade> listaChocam = new ArrayList<EncontroAtividade>();
		for (EncontroAtividade encontroAtividade : listaEncontroAtividade) {
			if (!encontroAtividade.equals(ea)) {
				boolean choca=false;
				boolean chocainicio=false;
				boolean chocafim=false;
				boolean chocaseguido=false;
				/*if (encontroAtividade.getId().equals(491) && ea.getId().equals(484) && ei.getId().equals(217))
					System.out.println("TESTE");*/

				if(encontroAtividade.getInicio().equals(ea.getInicio()) ||
						(encontroAtividade.getInicio().after(ea.getInicio()) && encontroAtividade.getInicio().before(ea.getFim()))){
					chocainicio=true;
				}
				if( encontroAtividade.getFim().equals(ea.getFim()) ||
						(encontroAtividade.getFim().after(ea.getInicio()) && encontroAtividade.getFim().before(ea.getFim()))){
					chocafim=true;
				}
				if( encontroAtividade.getPorcentagemReal().equals(100) &&
						ea.getPorcentagemReal().equals(100) ){
						if ( encontroAtividade.getInicio().equals(ea.getFim()))
							chocaseguido = true;
						if ( encontroAtividade.getFim().equals(ea.getInicio()))
							chocaseguido = true;
				}
				if (chocainicio || chocafim){
					int porcentagem1 = encontroAtividade.getPorcentagemReal();
					int porcentagem2 = ea.getPorcentagemReal();
					long duracaominima1 = (long) (encontroAtividade.getDuracao()*porcentagem1/100);
					long duracaominima2 = (long) (ea.getDuracao()*porcentagem2/100);
					long duracaochoque = 0;
					if (chocainicio && chocafim)  {
						Date inicio = null;
						Date fim = null;

						if (encontroAtividade.getFim().before(ea.getFim()) || encontroAtividade.getFim().equals(ea.getFim()))
							fim = encontroAtividade.getFim();
						else
							fim = ea.getFim();
						if (encontroAtividade.getInicio().after(ea.getInicio()) || encontroAtividade.getInicio().equals(ea.getInicio()))
							inicio = encontroAtividade.getInicio();
						else
							inicio = ea.getInicio();
						duracaochoque = (fim.getTime() - inicio.getTime()) / 1000 / 60;
					}
					else if (chocainicio) duracaochoque = (ea.getFim().getTime() - encontroAtividade.getInicio().getTime()) / 1000 / 60;
					else if (chocafim) duracaochoque = (encontroAtividade.getFim().getTime() - ea.getInicio().getTime()) / 1000 / 60;
					long duracaomaximachoque1 = (long) (encontroAtividade.getDuracao()-duracaominima1);
					long duracaomaximachoque2 = (long) (ea.getDuracao()-duracaominima2);

					if (duracaomaximachoque1 < duracaochoque && duracaomaximachoque2 < duracaochoque)
						choca=true;
				}
				if (chocaseguido)
					choca=true;
				if (choca){
					listaChocam.add(encontroAtividade);
				}
			}
		}
		for (EncontroAtividade encontroAtividade : listaChocam) {
			EncontroAtividadeInscricao inscricao = getEncontroAtividadeInscricaoFull(encontroAtividade, ei);
			if (inscricao!=null && !inscricao.getRevisado() && !inscricao.getPapel().getChocaPlanilha()){
				if(eai != null){
					eai.getInfoErro().add(encontroAtividade.toString());
				}
				else
					return true;
			}
		}
		if (eai != null && ( eai.getInfoErro().size()>0 || getChoqueHorariosSeguidos(eai, ea, ei, papel))) return true;
		return false;
	}

	private List<EncontroAtividade> montaListaEncontroAtividadeUsuarioAtual() {
		List<EncontroAtividade> listaAtividades = new ArrayList<EncontroAtividade>();
		for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
			if(presenter.getCasal()!=null){
				if(eai.getEncontroInscricao().getCasal()!=null && presenter.getCasal().getId().equals(eai.getEncontroInscricao().getCasal().getId())){
					listaAtividades.add(eai.getEncontroAtividade());
				}
			} else {
				if(eai.getEncontroInscricao().getPessoa()!=null && presenter.getUsuario().getPessoa().getId().equals(eai.getEncontroInscricao().getPessoa().getId())){
					listaAtividades.add(eai.getEncontroAtividade());
				}
			}
		}
		return listaAtividades;
	}

	private List<EncontroInscricao> montaListaEncontroInscricaoUsuarioAtual() {
		List<EncontroInscricao> listaEncontroInscricao = new ArrayList<EncontroInscricao>();
		for (EncontroInscricao encontroInscricao : presenter.getEncontroVO().getListaInscricao()) {
			if(encontroInscricao.getCasal()!=null){
				if(encontroInscricao.getCasal().getId().equals(presenter.getCasal().getId())){
					listaEncontroInscricao.add(encontroInscricao);
					break;
				}
			} else {
				if(encontroInscricao.getPessoa().getId().equals(presenter.getUsuario().getPessoa().getId())){
					listaEncontroInscricao.add(encontroInscricao);
					break;
				}
			}
		}
		return listaEncontroInscricao;
	}

	private List<EncontroInscricao> montaListaEncontroInscricaoSemExterno() {
		List<EncontroInscricao> listaEncontroInscricao = new ArrayList<EncontroInscricao>();
		for (EncontroInscricao encontroInscricao : presenter.getEncontroVO().getListaInscricao()) {
			if(!encontroInscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO)){
				listaEncontroInscricao.add(encontroInscricao);
			}
		}
		return listaEncontroInscricao;
	}

	@Override
	public void populaPeriodos(){
		periodoListBox.clear();
		periodoListBox.addItem("TODOS");
		for(EncontroPeriodo periodo : presenter.getEncontroVO().getListaPeriodo()) {
			periodoListBox.addItem(periodo.toString());
		}
	}
	@UiHandler(value={"planilhaListBox","periodoListBox","planilhaAtividadeListBox"})
	public void planilhaListBoxChangeHandler(ChangeEvent event) {
		planilhaPanel.clear();
		if(getTipoExibicaoPlanilhaSelecionado()!=null){
			presenter.buscaDadosPlanilha();
		}
	}

	@UiHandler("tipoListBox")
	public void tipoListBoxChangeHandler(ChangeEvent event) {
		porcentagemPadraoLabel.setText("Porcentagem Padrão: " + ListBoxUtil.getItemSelected(tipoListBox, TipoAtividadeEnum.values()));
	}

	@UiHandler("addInscricaoListBox")
	public void addInscricaoListBoxChangeHandler(ChangeEvent event) {
		if(addInscricaoListBox.getSelectedIndex()>0){
			inscricaoSuggestBox1.setValue(null);
			ListBoxUtil.setItemSelected(addAtividadeListBox, listaEncontroAtividadeExibidas.get(0).toString());
		}else{
			addAtividadeListBox.setSelectedIndex(-1);
		}
	}

	@UiHandler("fecharInscricaoButton")
	public void fecharInscricaoButtonClickHandler(ClickEvent event){
		editaInscricaoDialogBox.hide();
	}

	@UiHandler("excluirInscricaoButton")
	public void excluirInscricaoButtonClickHandler(ClickEvent event){
		if(Window.confirm("Deseja excluir esta participação ?")){
			presenter.excluirEncontroAtividadeInscricao(encontroAtividadeInscricaoEditada);
			editaInscricaoDialogBox.hide();
		}
	}

	@UiHandler("salvarInscricaoButton")
	public void salvarInscricaoButtonClickHandler(ClickEvent event){
		if (encontroAtividadeInscricaoEditada!=null){
			encontroAtividadeInscricaoEditada.setPapel((Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel()));
			encontroAtividadeInscricaoEditada.setRevisado(revisadoInscricaoCheckBox.getValue());
			presenter.salvarInscricao(encontroAtividadeInscricaoEditada);
		}else{
			if(listaParticipantesInscritos.size()==0 && !Window.confirm("Deseja salvar sem nenhuma Atividadde?")){
				return;
			}
			presenter.salvarInscricoes(listaParticipantesInscritos, listaParticipantesInscritosOriginal);
		}
		editaInscricaoDialogBox.hide();
	}

	private void editaInscricao(EncontroInscricao encontroInscricao) {
		limpaCamposInscricao();
		ListBoxUtil.populate(atividadeEditaListBox, false, listaEncontroAtividadeExibidas);
		ListBoxUtil.populate(addAtividadeListBox, false, presenter.getEncontroVO().getListaEncontroAtividade());
		encontroInscricaoEditada = encontroInscricao;
		adicionarInscricaoButton.setVisible(true);
		excluirTodasInscricaoButton.setVisible(true);
		preencheAutomaticoInscricaoButton.setVisible(false);
		selecionaInscricaoButton.setVisible(false);
		atividadeHTMLPanel.setVisible(true);
		atividadeEditaListBox.setEnabled(true);
		papelHTMLPanel.setVisible(true);
		opcoesHTMLPanel.setVisible(true);
		participantesFlowPanel.setVisible(true);
		entidadeEditadaTituloLabel.setText("Participante:");
		if(encontroInscricao.getCasal()!=null){
			entidadeEditadaLabel.setText(encontroInscricao.getCasal().getApelidos("e"));
		} else {
			entidadeEditadaLabel.setText(encontroInscricao.getPessoa().getApelido());
		}
		addInscricaoListBox.addItem("Esta atividade", "1");
		encontroInscricaoFlexTable.setVisible(false);
		encontroInscricaoAtividadeFlexTable.setVisible(true);
		populaAtividadesPorParticipante();

		addAtividadeListBox.setSelectedIndex(-1);
		addAtividadeListBox.setVisible(false);
		revisadoInscricaoCheckBox.setVisible(false);

		boolean bCoordenador = presenter.isCoordenador();
		if(!bCoordenador){
			participanteHTMLPanel.setVisible(false);
			atividadeHTMLPanel.setVisible(false);
			papelHTMLPanel.setVisible(false);
			opcoesHTMLPanel.setVisible(false);
			excluirInscricaoButton.setVisible(false);
			salvarInscricaoButton.setVisible(false);
			preencheAutomaticoInscricaoButton.setVisible(false);
			excluirTodasInscricaoButton.setVisible(false);
			revisadoInscricaoCheckBox.setVisible(false);
			selecionaInscricaoButton.setVisible(false);

		}
		editaInscricaoDialogBox.center();
		editaInscricaoDialogBox.show();
		inscricaoSuggestBox1.setFocus(true);
	}

	private void editaAtividadeInscricao(EncontroAtividadeInscricao encontroAtividadeInscricao) {
		showWaitMessage(true);
		limpaCamposInscricao();
		ListBoxUtil.populate(atividadeEditaListBox, false, listaEncontroAtividadeExibidas);
		ListBoxUtil.populate(addAtividadeListBox, false, presenter.getEncontroVO().getListaEncontroAtividade());
		encontroAtividadeInscricaoEditada = encontroAtividadeInscricao;
		adicionarInscricaoButton.setVisible(false);
		excluirTodasInscricaoButton.setVisible(false);
		preencheAutomaticoInscricaoButton.setVisible(false);
		selecionaInscricaoButton.setVisible(false);
		atividadeHTMLPanel.setVisible(true);
		atividadeEditaListBox.setEnabled(false);
		papelHTMLPanel.setVisible(true);
		opcoesHTMLPanel.setVisible(false);
		participantesFlowPanel.setVisible(false);
		entidadeEditadaTituloLabel.setText("Participante:");
		if(encontroAtividadeInscricao.getEncontroInscricao().getCasal()!=null){
			entidadeEditadaLabel.setText(encontroAtividadeInscricao.getEncontroInscricao().getCasal().getApelidos("e"));
		} else {
			entidadeEditadaLabel.setText(encontroAtividadeInscricao.getEncontroInscricao().getPessoa().getApelido());
		}
		addInscricaoListBox.addItem("Esta atividade", "1");
		encontroInscricaoFlexTable.setVisible(false);
		encontroInscricaoAtividadeFlexTable.setVisible(false);
		addAtividadeListBox.setSelectedIndex(-1);
		addAtividadeListBox.setVisible(false);
		revisadoInscricaoCheckBox.setVisible(true);
		excluirInscricaoButton.setVisible(true);
		salvarInscricaoButton.setVisible(true);

		boolean bCoordenador = presenter.isCoordenador();
		if(!bCoordenador){
			participanteHTMLPanel.setVisible(false);
			atividadeListBox.setEnabled(false);
			papelListBox.setEnabled(false);
			opcoesHTMLPanel.setVisible(false);
			excluirInscricaoButton.setVisible(false);
			salvarInscricaoButton.setVisible(false);
			preencheAutomaticoInscricaoButton.setVisible(false);
			excluirTodasInscricaoButton.setVisible(false);
			revisadoInscricaoCheckBox.setVisible(false);
			selecionaInscricaoButton.setVisible(false);

		}
		ListBoxUtil.setItemSelected(papelListBox, encontroAtividadeInscricao.getPapel().getNome());
		if (encontroAtividadeInscricao.getEncontroInscricao().getCasal()!=null)
			casalRadio.setValue(true);
		else
			pessoaRadio.setValue(true);
		inscricaoSuggestBox1.setValue(encontroAtividadeInscricao.getEncontroInscricao().toString());
		inscricaoSuggest1.setListaEntidades(new ArrayList<_WebBaseEntity>());
		inscricaoSuggest1.getListaEntidades().add(encontroAtividadeInscricao.getEncontroInscricao());

		editaInscricaoDialogBox.center();
		editaInscricaoDialogBox.show();
		papelListBox.setFocus(true);
		showWaitMessage(false);
	}

	private void editaAtividade(EncontroAtividade encontroAtividade) {
		showWaitMessage(true);
		limpaCamposInscricao();
		ListBoxUtil.populate(atividadeEditaListBox, false, listaEncontroAtividadeExibidas);
		ListBoxUtil.populate(addAtividadeListBox, false, presenter.getEncontroVO().getListaEncontroAtividade());
		encontroAtividadeEditada = encontroAtividade;
		participanteHTMLPanel.setVisible(true);
		opcoesHTMLPanel.setVisible(true);
		atividadeHTMLPanel.setVisible(true);
		atividadeEditaListBox.setEnabled(false);
		papelHTMLPanel.setVisible(true);
		entidadeEditadaTituloLabel.setText("Atividade:");
		entidadeEditadaLabel.setText(encontroAtividade.toString());
		Integer quantidade = encontroAtividade.getQuantidadeDesejada();
		if (encontroAtividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOS))
			quantidade = presenter.getEncontroVO().getQuantidadeInscricao();
		else if (encontroAtividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOSEXTERNO))
			quantidade = presenter.getEncontroVO().getQuantidadeInscricaoTotal();
		else if (encontroAtividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.EXTERNO))
			quantidade = presenter.getEncontroVO().getQuantidadeInscricaoExterno();
		else if (encontroAtividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.PADRINHOS))
			quantidade = presenter.getEncontroVO().getEncontro().getQuantidadeAfilhados();
		else if (encontroAtividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.METADE))
			quantidade = presenter.getEncontroVO().getQuantidadeInscricao()/2;
		entidadeInfoLabel.setText("Tipo Preenchimento: " + encontroAtividade.getTipoPreenchimento().getNome() + (quantidade==null?"":" Quantidade: " + quantidade));
		entidadeInfoLabel.setTitle(convertListToStringLinhas(encontroAtividade.getInfoErro()) + convertListToStringLinhas(encontroAtividade.getInfoAtencao()));

		preencheAutomaticoInscricaoButton.setVisible(true);
		excluirTodasInscricaoButton.setVisible(true);
		adicionarInscricaoButton.setVisible(true);
		excluirTodasInscricaoButton.setVisible(true);
		selecionaInscricaoButton.setVisible(true);
		addInscricaoListBox.addItem("Este participante", "1");
		addInscricaoListBox.addItem("Todos os participantes", "2");
		addInscricaoListBox.addItem("Todos os participantes + Externo", "3");
		addInscricaoListBox.addItem("Padrinhos", "4");
		addInscricaoListBox.addItem("Apoios", "5");
		addInscricaoListBox.addItem("Estão na atividade:", "6");
		addInscricaoListBox.addItem("Não estão na atividade:", "7");
		addInscricaoListBox.addItem("Não choca horario", "8");
		for (AgrupamentoVO agrupamentoVO : presenter.getEncontroVO().getListaAgrupamentoVOEncontro()) {
			if ( agrupamentoVO.getAgrupamento().getTipo().equals(TipoInscricaoCasalEnum.ENCONTRISTA))
				addInscricaoListBox.addItem(agrupamentoVO.getAgrupamento().getNome(), agrupamentoVO.getAgrupamento().getNome());
		}
		participantesFlowPanel.setVisible(true);
		ListBoxUtil.setItemSelected(addAtividadeListBox, encontroAtividadeEditada.toString());
		addAtividadeListBox.setVisible(true);
		revisadoInscricaoCheckBox.setVisible(false);

		encontroInscricaoFlexTable.setVisible(true);
		encontroInscricaoAtividadeFlexTable.setVisible(false);
		populaParticipantesPorAtividade();

		boolean bCoordenador = presenter.isCoordenador();
		if(!bCoordenador){
			participanteHTMLPanel.setVisible(false);
			atividadeHTMLPanel.setVisible(false);
			papelHTMLPanel.setVisible(false);
			opcoesHTMLPanel.setVisible(false);
			excluirInscricaoButton.setVisible(false);
			salvarInscricaoButton.setVisible(false);
			preencheAutomaticoInscricaoButton.setVisible(false);
			excluirTodasInscricaoButton.setVisible(false);
			revisadoInscricaoCheckBox.setVisible(false);
			selecionaInscricaoButton.setVisible(false);

		}
		editaInscricaoDialogBox.center();
		editaInscricaoDialogBox.show();
		inscricaoSuggestBox1.setFocus(true);
		showWaitMessage(false);
	}

	public void limpaCamposInscricao(){
		encontroAtividadeEditada = null;
		encontroAtividadeInscricaoEditada = null;
		encontroInscricaoEditada = null;
		addInscricaoListBox.clear();
		atividadeHTMLPanel.setVisible(false);
		opcoesHTMLPanel.setVisible(false);
		participanteHTMLPanel.setVisible(false);
		excluirInscricaoButton.setVisible(false);
		participantesFlowPanel.setVisible(false);
		preencheAutomaticoInscricaoButton.setVisible(false);
		papelHTMLPanel.setVisible(false);
		salvarInscricaoButton.setVisible(true);
		encontroInscricaoTableUtil.clearData();
		revisadoInscricaoCheckBox.setVisible(false);
		inscricaoSuggestBox1.setValue(null);
		itemTotal.setText(null);
		entidadeEditadaLabel.setText(null);
		entidadeEditadaTituloLabel.setText(null);
		entidadeInfoLabel.setText(null);
		entidadeInfoLabel.setTitle(null);
		inscricaoSuggestBox1.setEnabled(true);
		casalRadio.setEnabled(true);
		pessoaRadio.setEnabled(true);
		casalRadio.setValue(true);
		ListBoxUtil.setItemSelected(papelListBox, presenter.getPapelPadrao().toString());
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
	}

	@Override
	public void populaPapel(List<Papel> listaPapel) {
		setListaPapel(listaPapel);
		ListBoxUtil.populate(papelListBox, false, listaPapel);
	}

	@UiHandler("preencheAutomaticoInscricaoButton")
	public void preencheAutomaticoInscricaoButtonClickHandler(ClickEvent event){
		if (presenter.getPapelPadrao() == null){
			Window.alert("Falta definir o Papel Padrão!");
			return;
		}
		if (presenter.getPapelPadrinho() == null){
			Window.alert("Falta definir o Papel de Padrinho!");
			return;
		}
		preencheAtividade(encontroAtividadeEditada, presenter.getPapelPadrao(), presenter.getPapelPadrinho());
		verificaInconsistenciasAtividade(encontroAtividadeEditada);
		populaParticipantesPorAtividade();
	}

	@UiHandler("adicionarInscricaoButton")
	public void adicionarInscricaoButtonClickHandler(ClickEvent event){
		String opcao = addInscricaoListBox.getValue(addInscricaoListBox.getSelectedIndex());
		Papel papel = (Papel )ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel());
		if(encontroAtividadeEditada != null){
			if(opcao.equals("1")){
				EncontroInscricao ei = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue());
				if (ei != null)
					adicionaParticipante(papel, ei, encontroAtividadeEditada, true);
			} else if(opcao.equals("2")){
				for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
					if (!ei.getTipo().equals(TipoInscricaoEnum.EXTERNO))
						adicionaParticipante(papel, ei, encontroAtividadeEditada, false);
				}
			} else if(opcao.equals("3")){
				for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
					adicionaParticipante(papel, ei, encontroAtividadeEditada, false);
				}
			} else if(opcao.equals("4")){
				for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
					if(ei.getTipo().equals(TipoInscricaoEnum.PADRINHO)){
						adicionaParticipante(papel, ei, encontroAtividadeEditada, false);
					}
				}
			} else if(opcao.equals("5")){
				for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
					if(ei.getTipo().equals(TipoInscricaoEnum.APOIO)){
						adicionaParticipante(papel, ei, encontroAtividadeEditada, false);
					}
				}
			} else if(opcao.equals("6")){
				EncontroAtividade atividade = (EncontroAtividade) ListBoxUtil.getItemSelected(addAtividadeListBox, presenter.getEncontroVO().getListaEncontroAtividade());
				if (atividade != null ) {
					for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricaoFull()) {
						if(eai.getEncontroAtividade().getId().equals(atividade.getId())){
							adicionaParticipante(eai.getPapel(),eai.getEncontroInscricao(), encontroAtividadeEditada,false);
						}
					}
				}
			} else if(opcao.equals("7")){
				EncontroAtividade atividade = (EncontroAtividade) ListBoxUtil.getItemSelected(addAtividadeListBox, presenter.getEncontroVO().getListaEncontroAtividade());
				if (atividade != null ) {
					List<EncontroInscricao> lista = new ArrayList<EncontroInscricao>();
					for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
						if(eai.getEncontroAtividade().getId().equals(atividade.getId())){
							lista.add(eai.getEncontroInscricao());
						}
					}
					for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
						if(!ei.getTipo().equals(TipoInscricaoEnum.EXTERNO) && !lista.contains(ei)){
							adicionaParticipante(papel, ei, encontroAtividadeEditada, false);
						}
					}
				}
			} else if(opcao.equals("8")){
				for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
					if(!ei.getTipo().equals(TipoInscricaoEnum.EXTERNO)){
						if(!getChoqueHorarios(null,encontroAtividadeEditada,ei,(Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel())))
							adicionaParticipante(papel, ei, encontroAtividadeEditada, false);
					}
				}
			} else {
				for (AgrupamentoVO agrupamentoVO : presenter.getEncontroVO().getListaAgrupamentoVOEncontro()) {
					if(agrupamentoVO.getAgrupamento().getNome().equals(opcao)){
						for (AgrupamentoMembro membro : agrupamentoVO.getListaMembros()) {
							EncontroInscricao encontroInscricao = getEncontroInscricaoPorMembro(membro);
							if (encontroInscricao!=null){
								papel = membro.getPapel();

								if (papel == null)
									papel = (Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel());
								adicionaParticipante(papel,encontroInscricao, encontroAtividadeEditada,false);
							}
						}
						break;
					}
				}
			}
			populaParticipantesPorAtividade();
		} else if (encontroInscricaoEditada!=null){
			if(opcao.equals("1")){
				adicionaParticipante(papel, encontroInscricaoEditada, (EncontroAtividade)ListBoxUtil.getItemSelected(atividadeEditaListBox, presenter.getEncontroVO().getListaEncontroAtividade()),true);
			}
			populaAtividadesPorParticipante();
		}
	}

	@UiHandler("excluirTodasInscricaoButton")
	public void excluirTodasInscricaoButtonButtonClickHandler(ClickEvent event){
		listaParticipantesInscritos.clear();
		if (encontroAtividadeEditada!=null){
			populaParticipantesPorAtividade();
		}
		else if (encontroInscricaoEditada!=null)
			populaAtividadesPorParticipante();
	}

	private void adicionaParticipante(Papel papel, EncontroInscricao ei, EncontroAtividade atividade, boolean mensagem){
		EncontroAtividadeInscricao eaiEncontrada = null;
		if(encontroAtividadeEditada!=null){
			for (EncontroAtividadeInscricao eai : listaParticipantesInscritos) {
				if(eai.getEncontroInscricao().equals(ei)){
					eaiEncontrada = eai;
					if (mensagem) Window.alert("Participante já adicionado para esta atividade\n" + eai.getEncontroInscricao().toString());
					break;
				}
			}
		} else if(encontroInscricaoEditada!=null){
			for (EncontroAtividadeInscricao eai : listaParticipantesInscritos) {
				if(eai.getEncontroAtividade().getId().equals(atividade.getId())){
					eaiEncontrada = eai;
					if (mensagem) Window.alert("Atividade já adicionada para este participante\n" + eai.getEncontroAtividade().getAtividade().toString());
					break;
				}
			}
		}
		if(eaiEncontrada==null){
			eaiEncontrada = new EncontroAtividadeInscricao();
			eaiEncontrada.setEncontroAtividade(atividade);
			eaiEncontrada.setEncontroInscricao(ei);
			eaiEncontrada.setRevisado(false);
			listaParticipantesInscritos.add(eaiEncontrada);
		}
		eaiEncontrada.setPapel(papel);
	}

	public void populaParticipantesPorAtividade() {
		verificaInconsistenciasAtividadeEditada();
		Integer quantidade = encontroAtividadeEditada.getQuantidadeDesejada();
		if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOS))
			quantidade = presenter.getEncontroVO().getQuantidadeInscricao();
		else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOSEXTERNO))
			quantidade = presenter.getEncontroVO().getQuantidadeInscricaoTotal();
		else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.EXTERNO))
			quantidade = presenter.getEncontroVO().getQuantidadeInscricaoExterno();
		else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.PADRINHOS))
			quantidade = presenter.getEncontroVO().getEncontro().getQuantidadeAfilhados();
		else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.METADE))
			quantidade = presenter.getEncontroVO().getQuantidadeInscricao()/2;
		entidadeInfoLabel.setText("Tipo Preenchimento: " + encontroAtividadeEditada.getTipoPreenchimento().getNome() + (quantidade==null?"":" Quantidade: " + quantidade));
		entidadeInfoLabel.setTitle(convertListToStringLinhas(encontroAtividadeEditada.getInfoErro()) + convertListToStringLinhas(encontroAtividadeEditada.getInfoAtencao()));
		LabelTotalUtil.setTotal(itemTotal, listaParticipantesInscritos.size(), "participante", "participantes", "");
		encontroInscricaoTableUtil.clearData();
		int row = 0;
		if (listaParticipantesInscritos.size() > 0){
			for (final EncontroAtividadeInscricao atividadeParticipante: listaParticipantesInscritos) {
				Object dados[] = new Object[5];

				final Image excluirParticipante = new Image("images/delete.png");
				excluirParticipante.setStyleName("portal-ImageCursor");
				excluirParticipante.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						listaParticipantesInscritos.remove(atividadeParticipante);
						if (atividadeParticipante.getEncontroInscricao().getCasal()!=null)
							casalRadio.setValue(true);
						else
							pessoaRadio.setValue(true);
						inscricaoSuggestBox1.setValue(atividadeParticipante.getEncontroInscricao().toString());
						inscricaoSuggest1.setListaEntidades(new ArrayList<_WebBaseEntity>());
						inscricaoSuggest1.getListaEntidades().add(atividadeParticipante.getEncontroInscricao());
						ListBoxUtil.setItemSelected(papelListBox, atividadeParticipante.getPapel().toString());
						verificaInconsistenciasAtividade(encontroAtividadeEditada);
						populaParticipantesPorAtividade();
					}
				});

				dados[0] = excluirParticipante;
				dados[1] = atividadeParticipante.getEncontroInscricao().toStringApelidos();
				final ListBox listBox = new ListBox();
				ListBoxUtil.populate(listBox, false, getListaPapel());
				if (atividadeParticipante.getPapel()!=null)
					ListBoxUtil.setItemSelected(listBox, atividadeParticipante.getPapel().toString());
				listBox.addChangeHandler(new ChangeHandler() {
					public void onChange(ChangeEvent event) {
						atividadeParticipante.setPapel((Papel) ListBoxUtil.getItemSelected(listBox,getListaPapel()));
					}
				});
				dados[2] = listBox;
				final CheckBox checkBox = new CheckBox();
				checkBox.setValue(atividadeParticipante.getRevisado());
				checkBox.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						atividadeParticipante.setRevisado(checkBox.getValue());
					}
				});
				dados[3] = checkBox;
				Label label = new Label();
				if (getChoqueHorarios(atividadeParticipante)){
					label.setText("Erros");
					label.setStyleName("portal-ImageCursorErro");
					label.setTitle("\rErros - " + convertListToStringLinhas(atividadeParticipante.getInfoErro()));
				}
				dados[4] = label;
				if (!presenter.isCoordenador()){
					checkBox.setEnabled(false);
					listBox.setEnabled(false);
					label.setVisible(false);
				}

				encontroInscricaoTableUtil.addRow(dados,row+1);
				row++;
			}
			encontroInscricaoTableUtil.applyDataRowStyles();
		}
	}
	public void populaAtividadesPorParticipante() {
		LabelTotalUtil.setTotal(itemTotal, listaParticipantesInscritos.size(), "atividade", "atividades", "");
		encontroInscricaoAtividadeTableUtil.clearData();
		if (listaParticipantesInscritos.size() > 0){
			int row = 0;
			for (final EncontroAtividadeInscricao atividadeInscricao: listaParticipantesInscritos) {
				Object dados[] = new Object[9];

				final Image excluirAtividade = new Image("images/delete.png");
				excluirAtividade.setStyleName("portal-ImageCursor");
				excluirAtividade.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						listaParticipantesInscritos.remove(atividadeInscricao);
						populaAtividadesPorParticipante();
					}
				});

				dados[0] = excluirAtividade;
				dados[1] = dfDia.format(atividadeInscricao.getEncontroAtividade().getInicio());
				dados[2] = dfHora.format(atividadeInscricao.getEncontroAtividade().getInicio());
				dados[3] = dfHora.format(atividadeInscricao.getEncontroAtividade().getFim());
				dados[4] = atividadeInscricao.getEncontroAtividade().getTipoAtividade().getNome();
				dados[5] = atividadeInscricao.getEncontroAtividade().getAtividade().getNome();
				final ListBox listBox = new ListBox();
				ListBoxUtil.populate(listBox, false, getListaPapel());
				if (atividadeInscricao.getPapel()!=null)
					ListBoxUtil.setItemSelected(listBox, atividadeInscricao.getPapel().toString());
				listBox.addChangeHandler(new ChangeHandler() {
					public void onChange(ChangeEvent event) {
						atividadeInscricao.setPapel((Papel) ListBoxUtil.getItemSelected(listBox,getListaPapel()));
					}
				});
				dados[6] = listBox;
				final CheckBox checkBox = new CheckBox();
				checkBox.setValue(atividadeInscricao.getRevisado());
				checkBox.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						atividadeInscricao.setRevisado(checkBox.getValue());
					}
				});
				dados[7] = checkBox;
				Label label = new Label();
				if (getChoqueHorarios(atividadeInscricao)){
					label.setText("Erros");
					label.setStyleName("portal-ImageCursorErro");
					label.setTitle("\rErros - " + convertListToStringLinhas(atividadeInscricao.getInfoErro()));
				}
				dados[8] = label;
				if (!presenter.isCoordenador()){
					checkBox.setEnabled(false);
					listBox.setEnabled(false);
					label.setVisible(false);
				}
				encontroInscricaoAtividadeTableUtil.addRow(dados,row+1);
				row++;
			}
			encontroInscricaoAtividadeTableUtil.applyDataRowStyles();
		}
	}

	@Override
	public EncontroPeriodo getPeriodoSelecionado() {
		return (EncontroPeriodo) ListBoxUtil.getItemSelected(periodoListBox, presenter.getEncontroVO().getListaPeriodo());
	}

	@Override
	public TipoExibicaoPlanilhaEnum getTipoExibicaoPlanilhaSelecionado() {
		return (TipoExibicaoPlanilhaEnum) ListBoxUtil.getItemSelected(planilhaListBox, TipoExibicaoPlanilhaEnum.values());
	}

	@UiHandler("imprimirButton")
	public void imprimirButtonClickHandler(ClickEvent event){
		if(getTipoExibicaoPlanilhaSelecionado()==null){
			Window.alert("Selecione a planilha que deseja imprimir");
			return;
		}
		presenter.imprimirPlanilha(false);
	}

	@UiHandler("baixarButton")
	public void baixarButtonClickHandler(ClickEvent event){
		if(getTipoExibicaoPlanilhaSelecionado()==null){
			Window.alert("Selecione a planilha que deseja baixar");
			return;
		}
		presenter.imprimirPlanilha(true);
	}

	@UiHandler("limparPlanilhaButton")
	public void limparPlanilhaButtonClickHandler(ClickEvent event){
		if (Window.confirm("Deseja Limpar Tudo?") == false)
			return;
		if (Window.confirm("Deseja realmente Limpar Tudo?") == false)
			return;
		if (Window.confirm("Tem certeza?") == false)
			return;
		if (Window.confirm("Olha?") == false)
			return;
		listaParticipantesInscritos = new ArrayList<EncontroAtividadeInscricao>();
		listaParticipantesInscritosOriginal = new ArrayList<EncontroAtividadeInscricao>();
		listaParticipantesInscritosOriginal.addAll(presenter.getListaEncontroAtividadeInscricao());
		presenter.salvarInscricoes(listaParticipantesInscritos, listaParticipantesInscritosOriginal);
	}

	@UiHandler("preencherAutomaticoButton")
	public void preencherAutomaticoButtonButtonClickHandler(ClickEvent event){
		Papel papelPadrao = null;
		Papel papelPadrinho = null;
		for (Papel papel:presenter.getGrupoEncontroVO().getListaPapel()){
			if (papel.getPadrao()){
				papelPadrao = papel;
			}
			if (papel.getPadrinho()){
				papelPadrinho = papel;
			}
		}
		if (papelPadrao == null){
			Window.alert("Falta definir o Papel Padrão!");
			return;
		}
		if (papelPadrinho == null){
			Window.alert("Falta definir o Papel de Padrinho!");
			return;
		}

		listaParticipantesInscritos = new ArrayList<EncontroAtividadeInscricao>();
		listaParticipantesInscritosOriginal = new ArrayList<EncontroAtividadeInscricao>();
		listaParticipantesInscritos.addAll(presenter.getListaEncontroAtividadeInscricao());
		listaParticipantesInscritosOriginal.addAll(presenter.getListaEncontroAtividadeInscricao());

		for (EncontroAtividade atividade : montaListaAtividadesPorPeriodoSelecionado(presenter.getEncontroVO().getListaEncontroAtividade(),getPeriodoSelecionado())) {
			preencheAtividade(atividade, papelPadrao, papelPadrinho);
		}
		presenter.salvarInscricoes(listaParticipantesInscritos,listaParticipantesInscritosOriginal);
	}

	private void preencheAtividade(EncontroAtividade atividade,
			Papel papelPadrao, Papel papelPadrinho) {
		if (!atividade.getRevisado()){
			if (atividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOS)){
				List<EncontroInscricao> listaInscricao = presenter.getEncontroVO().getListaInscricao();
				for (EncontroInscricao inscricao : listaInscricao) {
					if (!inscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO)) {
						EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,inscricao);
						if (atividadeInscricao==null){
							atividadeInscricao = new EncontroAtividadeInscricao();
							atividadeInscricao.setEncontroAtividade(atividade);
							listaParticipantesInscritos.add(atividadeInscricao);
						}
						atividadeInscricao.setPapel(papelPadrao);
						atividadeInscricao.setEncontroInscricao(inscricao);
					}
				}
			}
			else if (atividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.EXTERNO)){
				List<EncontroInscricao> listaInscricao = presenter.getEncontroVO().getListaInscricao();
				for (EncontroInscricao inscricao : listaInscricao) {
					if (inscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO)) {
						EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,inscricao);
						if (atividadeInscricao==null){
							atividadeInscricao = new EncontroAtividadeInscricao();
							atividadeInscricao.setEncontroAtividade(atividade);
							listaParticipantesInscritos.add(atividadeInscricao);
						}
						atividadeInscricao.setPapel(papelPadrao);
						atividadeInscricao.setEncontroInscricao(inscricao);
					}
				}
			}
			else if (atividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOSEXTERNO)){
				List<EncontroInscricao> listaInscricao = presenter.getEncontroVO().getListaInscricao();
				for (EncontroInscricao inscricao : listaInscricao) {
						EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,inscricao);
						if (atividadeInscricao==null){
							atividadeInscricao = new EncontroAtividadeInscricao();
							atividadeInscricao.setEncontroAtividade(atividade);
							listaParticipantesInscritos.add(atividadeInscricao);
						}
						atividadeInscricao.setPapel(papelPadrao);
						atividadeInscricao.setEncontroInscricao(inscricao);
				}
			}
			if (atividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.METADE)){
				List<EncontroInscricao> listaInscricao = presenter.getEncontroVO().getListaInscricao();
				int total = listaInscricao.size();
				int metade = listaInscricao.size()/2+1;
				while (listaParticipantesInscritos.size() <= metade){
					int sorteio = Random.nextInt(total-1);
					EncontroInscricao inscricao = listaInscricao.get(sorteio);
					if (!getChoqueHorarios(null, atividade, inscricao, papelPadrao)){
						EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,inscricao);
						if (atividadeInscricao==null){
							atividadeInscricao = new EncontroAtividadeInscricao();
							atividadeInscricao.setEncontroAtividade(atividade);
							listaParticipantesInscritos.add(atividadeInscricao);
						}
						atividadeInscricao.setPapel(papelPadrao);
						atividadeInscricao.setEncontroInscricao(inscricao);
					}
				}
				for (EncontroInscricao inscricao : listaInscricao) {
					if (!inscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO)) {
						EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,inscricao);
						if (atividadeInscricao==null){
							atividadeInscricao = new EncontroAtividadeInscricao();
							atividadeInscricao.setEncontroAtividade(atividade);
							listaParticipantesInscritos.add(atividadeInscricao);
						}
						atividadeInscricao.setPapel(papelPadrao);
						atividadeInscricao.setEncontroInscricao(inscricao);
					}
				}
			}
			else if (atividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.PADRINHOS)){
				List<EncontroInscricao> listaInscricao = presenter.getEncontroVO().getListaInscricao();
				for (EncontroInscricao inscricao : listaInscricao) {
					if (inscricao.getTipo().equals(TipoInscricaoEnum.PADRINHO)) {
						EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,inscricao);
						if (atividadeInscricao==null){
							atividadeInscricao = new EncontroAtividadeInscricao();
							atividadeInscricao.setEncontroAtividade(atividade);
							listaParticipantesInscritos.add(atividadeInscricao);
						}
						atividadeInscricao.setPapel(papelPadrinho);
						atividadeInscricao.setEncontroInscricao(inscricao);
					}
				}
			}
			else if (atividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.VARIAVEL)){
				List<AgrupamentoVO> agrupamentos = getListaAgrupamentosAtividade(atividade);
				for (AgrupamentoVO agrupamentoVO : agrupamentos) {
					for (AgrupamentoMembro menbro : agrupamentoVO.getListaMembros()) {
						EncontroInscricao inscricao = getEncontroInscricaoPorMembro(menbro);
						EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,inscricao);
						if (atividadeInscricao==null){
							atividadeInscricao = new EncontroAtividadeInscricao();
							atividadeInscricao.setEncontroAtividade(atividade);
							listaParticipantesInscritos.add(atividadeInscricao);
						}
						if (menbro.getPapel()!=null)
							atividadeInscricao.setPapel(menbro.getPapel());
						else
							atividadeInscricao.setPapel(papelPadrao);
						atividadeInscricao.setEncontroInscricao(inscricao);
					}
				}
			}
			if (true){
				List<EncontroOrganogramaVO> listaOrganogramaEncontroVO = presenter.getEncontroVO().getListaOrganogramaEncontroVO();
				for (EncontroOrganogramaVO encontroOrganogramaVO : listaOrganogramaEncontroVO) {
					List<EncontroOrganogramaCoordenacao> listaEncontroOrganogramaCoordenacao = encontroOrganogramaVO.getListaEncontroOrganogramaCoordenacao();
					for (EncontroOrganogramaCoordenacao encontroOrganogramaCoordenacao : listaEncontroOrganogramaCoordenacao) {
						if (encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getAtividade() != null &&
								encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getAtividade().equals(atividade.getAtividade())){
							if (encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getTipoAtividade()==null ||
									( encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getTipoAtividade() != null &&
									encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getTipoAtividade().equals(atividade.getTipoAtividade()))){
								if (encontroOrganogramaCoordenacao.getEncontroInscricao1()!=null){
									EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,encontroOrganogramaCoordenacao.getEncontroInscricao1());
									if (atividadeInscricao==null){
										atividadeInscricao = new EncontroAtividadeInscricao();
										atividadeInscricao.setEncontroAtividade(atividade);
										listaParticipantesInscritos.add(atividadeInscricao);
									}
									if (encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getPapel()!=null)
										atividadeInscricao.setPapel(encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getPapel());
									else{
										atividadeInscricao.setPapel(papelPadrao);
									}
									atividadeInscricao.setEncontroInscricao(encontroOrganogramaCoordenacao.getEncontroInscricao1());
								}
								if (encontroOrganogramaCoordenacao.getEncontroInscricao2()!=null){
									EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,encontroOrganogramaCoordenacao.getEncontroInscricao2());
									if (atividadeInscricao==null){
										atividadeInscricao = new EncontroAtividadeInscricao();
										atividadeInscricao.setEncontroAtividade(atividade);
										listaParticipantesInscritos.add(atividadeInscricao);
									}
									if (encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getPapel()!=null)
										atividadeInscricao.setPapel(encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getPapel());
									else{
										atividadeInscricao.setPapel(papelPadrao);
									}
									atividadeInscricao.setEncontroInscricao(encontroOrganogramaCoordenacao.getEncontroInscricao2());
								}
								if (true){
									OrganogramaArea area = encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().getOrganogramaArea();
									List<EncontroOrganogramaArea> listaEncontroOrganogramaArea = encontroOrganogramaVO.getListaEncontroOrganogramaArea();
									for (EncontroOrganogramaArea encontroOrganogramaArea : listaEncontroOrganogramaArea) {
										if (encontroOrganogramaArea.getOrganogramaArea().equals(area)){
											if (encontroOrganogramaArea.getOrganogramaArea().getTipoAtividade()==null ||
													( encontroOrganogramaArea.getOrganogramaArea().getTipoAtividade() != null &&
													encontroOrganogramaArea.getOrganogramaArea().getTipoAtividade().equals(atividade.getTipoAtividade()))){
												if (encontroOrganogramaArea.getEncontroInscricao1()!=null && encontroOrganogramaArea.getOrganogramaArea().getPapel()!=null &&
														encontroOrganogramaArea.getOrganogramaArea().getPapel().getAparecePlanilha()){
													EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,encontroOrganogramaArea.getEncontroInscricao1());
													if (atividadeInscricao==null){
														atividadeInscricao = new EncontroAtividadeInscricao();
														atividadeInscricao.setEncontroAtividade(atividade);
														listaParticipantesInscritos.add(atividadeInscricao);
													}
													atividadeInscricao.setPapel(encontroOrganogramaArea.getOrganogramaArea().getPapel());
													atividadeInscricao.setEncontroInscricao(encontroOrganogramaArea.getEncontroInscricao1());
												}
												if (encontroOrganogramaArea.getEncontroInscricao2()!=null && encontroOrganogramaArea.getOrganogramaArea().getPapel()!=null &&
														encontroOrganogramaArea.getOrganogramaArea().getPapel().getAparecePlanilha()){
													EncontroAtividadeInscricao atividadeInscricao = getEncontroAtividadeInscricao(atividade,encontroOrganogramaArea.getEncontroInscricao2());
													if (atividadeInscricao==null){
														atividadeInscricao = new EncontroAtividadeInscricao();
														atividadeInscricao.setEncontroAtividade(atividade);
														listaParticipantesInscritos.add(atividadeInscricao);
													}
													atividadeInscricao.setPapel(encontroOrganogramaArea.getOrganogramaArea().getPapel());
													atividadeInscricao.setEncontroInscricao(encontroOrganogramaArea.getEncontroInscricao2());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private EncontroInscricao getEncontroInscricaoPorMembro(AgrupamentoMembro menbro) {
		List<EncontroInscricao> listaInscricao = presenter.getEncontroVO().getListaInscricao();
		for (EncontroInscricao encontroInscricao : listaInscricao) {
			if (menbro.getCasal() != null && encontroInscricao.getCasal()!= null && encontroInscricao.getCasal().equals(menbro.getCasal()))
				return encontroInscricao;
			if (menbro.getPessoa() != null && encontroInscricao.getPessoa()!= null && encontroInscricao.getPessoa().equals(menbro.getPessoa()))
				return encontroInscricao;
		}
		return null;
	}

	private List<AgrupamentoVO> getListaAgrupamentosAtividade(EncontroAtividade atividade) {
		ArrayList<AgrupamentoVO> list = new ArrayList<AgrupamentoVO>();
		List<AgrupamentoVO> listaAgrupamentoVOEncontro = presenter.getEncontroVO().getListaAgrupamentoVOEncontro();
		for (AgrupamentoVO agrupamentoVO : listaAgrupamentoVOEncontro) {
			if (agrupamentoVO.getAgrupamento().getTipo() != null && agrupamentoVO.getAgrupamento().getTipo().equals(TipoInscricaoCasalEnum.ENCONTRISTA)){
				if (agrupamentoVO.getAgrupamento().getAtividade()!=null && agrupamentoVO.getAgrupamento().getAtividade().equals(atividade.getAtividade())){
					if (agrupamentoVO.getAgrupamento().getTipoAtividade()==null || ( agrupamentoVO.getAgrupamento().getTipoAtividade() != null &&
							agrupamentoVO.getAgrupamento().getTipoAtividade().equals(atividade.getTipoAtividade() ))){
						list.add(agrupamentoVO);
					}
				}else if (agrupamentoVO.getAgrupamento().getAtividade()==null && agrupamentoVO.getAgrupamento().getTipoAtividade() != null
						&& agrupamentoVO.getAgrupamento().getTipoAtividade().equals(atividade.getTipoAtividade())){
					list.add(agrupamentoVO);
				}
			}
		}
		return list;
	}

	private EncontroAtividadeInscricao getEncontroAtividadeInscricao(EncontroAtividade atividade, EncontroInscricao inscricao){
		for (EncontroAtividadeInscricao encontroAtividadeInscricao : listaParticipantesInscritos) {
			if (encontroAtividadeInscricao.getEncontroAtividade().equals(atividade) && encontroAtividadeInscricao.getEncontroInscricao().equals(inscricao)){
				return encontroAtividadeInscricao;
			}
		}
		return null;
	}

	private EncontroAtividadeInscricao getEncontroAtividadeInscricaoFull(EncontroAtividade atividade, EncontroInscricao inscricao){
		for (EncontroAtividadeInscricao encontroAtividadeInscricao : presenter.getListaEncontroAtividadeInscricao()) {
			if (encontroAtividadeInscricao.getEncontroAtividade().equals(atividade) && encontroAtividadeInscricao.getEncontroInscricao().equals(inscricao)){
				return encontroAtividadeInscricao;
			}
		}
		return null;
	}

	private EncontroAtividadeInscricao getEncontroAtividadeInscricaoAtivdade(EncontroAtividade atividade, EncontroInscricao inscricao){
		for (EncontroAtividadeInscricao encontroAtividadeInscricao : atividade.getEncontroAtividadeInscricaos()) {
			if (encontroAtividadeInscricao.getEncontroAtividade().equals(atividade) && encontroAtividadeInscricao.getEncontroInscricao().equals(inscricao)){
				return encontroAtividadeInscricao;
			}
		}
		return null;
	}

	private void verificaInconsistenciasAtividade(EncontroAtividade ea) {
		ea.getInfoAtencao().clear();
		ea.getInfoErro().clear();

		if (!ea.getRevisado()){
			int quantidade = ea.getQuantidadeInscricoes();
			if (quantidade==0){
				ea.getInfoErro().add("Sem participantes");
			}else{
				if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.VARIAVEL)){
					if (ea.getQuantidadeDesejada() != null && ea.getQuantidadeDesejada() > quantidade ){
						ea.getInfoAtencao().add("Faltam " + (ea.getQuantidadeDesejada()-quantidade) + " participantes");
					}
					else if (ea.getQuantidadeDesejada() != null && ea.getQuantidadeDesejada() < quantidade ){
						ea.getInfoAtencao().add("Esta passando " + (quantidade - ea.getQuantidadeDesejada()) + " participantes");
					}
				}else if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOS)){
					if (presenter.getEncontroVO().getQuantidadeInscricao() > quantidade ){
						ea.getInfoErro().add("Faltam " + (presenter.getEncontroVO().getQuantidadeInscricao() - quantidade) + " participantes");
					}else if (presenter.getEncontroVO().getQuantidadeInscricao() < quantidade ){
						ea.getInfoErro().add("Esta passando " + (quantidade-presenter.getEncontroVO().getQuantidadeInscricao()) + " participantes");
					}
				}else if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOSEXTERNO)){
					if (presenter.getEncontroVO().getQuantidadeInscricaoTotal() > quantidade ){
						ea.getInfoErro().add("Faltam " + (presenter.getEncontroVO().getQuantidadeInscricaoTotal() - quantidade) + " participantes");
					}else if (presenter.getEncontroVO().getQuantidadeInscricaoTotal() < quantidade ){
						ea.getInfoErro().add("Esta passando " + (quantidade-presenter.getEncontroVO().getQuantidadeInscricaoTotal()) + " participantes");
					}
				}else if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.EXTERNO)){
					if (presenter.getEncontroVO().getQuantidadeInscricaoExterno() > quantidade ){
						ea.getInfoErro().add("Faltam " + (presenter.getEncontroVO().getQuantidadeInscricaoExterno() - quantidade) + " participantes");
					}else if (presenter.getEncontroVO().getQuantidadeInscricaoExterno() < quantidade ){
						ea.getInfoErro().add("Esta passando " + (quantidade-presenter.getEncontroVO().getQuantidadeInscricaoExterno()) + " participantes");
					}
				}else if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.METADE)){
					if ((presenter.getEncontroVO().getQuantidadeInscricao()/2) > quantidade ){
						ea.getInfoAtencao().add("Faltam " + ((presenter.getEncontroVO().getQuantidadeInscricao()/2) - quantidade) + " participantes");
					}
					else if ((presenter.getEncontroVO().getQuantidadeInscricao()/2+1) < quantidade ){
						ea.getInfoAtencao().add("Esta passando " + (quantidade - (presenter.getEncontroVO().getQuantidadeInscricao()/2+1)) + " participantes");
					}
				}else if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.PADRINHOS)){
					if (ea.getEncontro().getQuantidadeAfilhados() > quantidade ){
						ea.getInfoErro().add("Faltam " + (ea.getEncontro().getQuantidadeAfilhados() - quantidade) + " participantes");
					}
					if (ea.getEncontro().getQuantidadeAfilhados() < quantidade ){
						ea.getInfoErro().add("Esta passando " + (quantidade-ea.getEncontro().getQuantidadeAfilhados()) + " participantes");
					}
				}
			}
		}
		for (EncontroAtividadeInscricao eai : ea.getEncontroAtividadeInscricaos()) {
			if (getChoqueHorarios(eai)){
				ea.getInfoAtencao().add(eai.getEncontroInscricao().toString() + convertListToStringLinhas(eai.getInfoErro()) + convertListToStringLinhas(eai.getInfoAtencao()));
			}
		}
	}

	private void verificaInconsistenciasAtividadeEditada() {
		encontroAtividadeEditada.getInfoAtencao().clear();
		encontroAtividadeEditada.getInfoErro().clear();

		if (!encontroAtividadeEditada.getRevisado()){
			int quantidade = listaParticipantesInscritos.size();
			if (quantidade==0){
				encontroAtividadeEditada.getInfoErro().add("Sem participantes");
			}else{
				if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.VARIAVEL)){
					if (encontroAtividadeEditada.getQuantidadeDesejada() != null && encontroAtividadeEditada.getQuantidadeDesejada() > quantidade ){
						encontroAtividadeEditada.getInfoAtencao().add("Faltam " + (encontroAtividadeEditada.getQuantidadeDesejada()-quantidade) + " participantes");
					}
					if (encontroAtividadeEditada.getQuantidadeDesejada() != null && encontroAtividadeEditada.getQuantidadeDesejada() < quantidade ){
						encontroAtividadeEditada.getInfoAtencao().add("Esta passando " + (quantidade - encontroAtividadeEditada.getQuantidadeDesejada()) + " participantes");
					}
				}else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOS)){
					if (presenter.getEncontroVO().getQuantidadeInscricao() > quantidade ){
						encontroAtividadeEditada.getInfoErro().add("Faltam " + (presenter.getEncontroVO().getQuantidadeInscricao() - quantidade) + " participantes");
					}
				}else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOSEXTERNO)){
					if (presenter.getEncontroVO().getQuantidadeInscricaoTotal() > quantidade ){
						encontroAtividadeEditada.getInfoErro().add("Faltam " + (presenter.getEncontroVO().getQuantidadeInscricaoTotal() - quantidade) + " participantes");
					}else if (presenter.getEncontroVO().getQuantidadeInscricaoTotal() < quantidade ){
						encontroAtividadeEditada.getInfoErro().add("Esta passando " + (quantidade-presenter.getEncontroVO().getQuantidadeInscricaoTotal()) + " participantes");
					}
				}else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.EXTERNO)){
					if (presenter.getEncontroVO().getQuantidadeInscricaoExterno() > quantidade ){
						encontroAtividadeEditada.getInfoErro().add("Faltam " + (presenter.getEncontroVO().getQuantidadeInscricaoExterno() - quantidade) + " participantes");
					}else if (presenter.getEncontroVO().getQuantidadeInscricaoExterno() < quantidade ){
						encontroAtividadeEditada.getInfoErro().add("Esta passando " + (quantidade-presenter.getEncontroVO().getQuantidadeInscricaoExterno()) + " participantes");
					}
				}else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.METADE)){
					if ((presenter.getEncontroVO().getQuantidadeInscricao()/2) > quantidade ){
						encontroAtividadeEditada.getInfoAtencao().add("Faltam " + ((presenter.getEncontroVO().getQuantidadeInscricao()/2) - quantidade) + " participantes");
					}
					else if ((presenter.getEncontroVO().getQuantidadeInscricao()/2+1) < quantidade ){
						encontroAtividadeEditada.getInfoAtencao().add("Esta passando " + (quantidade - (presenter.getEncontroVO().getQuantidadeInscricao()/2+1)) + " participantes");
					}
				}else if (encontroAtividadeEditada.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.PADRINHOS)){
					if (encontroAtividadeEditada.getEncontro().getQuantidadeAfilhados() > quantidade ){
						encontroAtividadeEditada.getInfoErro().add("Faltam " + (encontroAtividadeEditada.getEncontro().getQuantidadeAfilhados() - quantidade) + " participantes");
					}
					if (encontroAtividadeEditada.getEncontro().getQuantidadeAfilhados() < quantidade ){
						encontroAtividadeEditada.getInfoErro().add("Esta passando " + (quantidade-encontroAtividadeEditada.getEncontro().getQuantidadeAfilhados()) + " participantes");
					}
				}
			}
		}
		for (EncontroAtividadeInscricao eai : listaParticipantesInscritos) {
			if(getChoqueHorarios(eai))
				encontroAtividadeEditada.getInfoAtencao().add(eai.getEncontroInscricao().toString() + convertListToStringLinhas(eai.getInfoErro()));
		}
	}

	@UiHandler("casalRadio")
	public void casalRadioClickHandler(ClickEvent event){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
	}

	@UiHandler("pessoaRadio")
	public void pessoaRadioClickHandler(ClickEvent event){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
	}

	public List<Papel> getListaPapel() {
		return listaPapel;
	}

	public void setListaPapel(List<Papel> listaPapel) {
		this.listaPapel = listaPapel;
	}

	private String convertListToStringLinhas(List<String> objects){
		if (objects==null || objects.size()==0) return "";
		StringBuffer retorno = new StringBuffer();
		for (Object object : objects) {
			retorno.append("\r"+object.toString());
		}
		return new String(retorno);
	}

	@UiHandler("salvarSelecaoButton")
	public void salvarSelecaoButtonClickHandler(ClickEvent event){
		boolean achou = false;
		for (final ParticipanteVO participante: listaParticipantesSugeridos) {
			if(participante.getSelecionado()){
				adicionaParticipante((Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel()),
						participante.getEncontroInscricao(), encontroAtividadeEditada,true);
				achou=true;
			}
		}
		if (achou){
			populaParticipantesPorAtividade();
			selecaoInscricaoDialogBox.hide();
		}else{
			Window.alert("Nenhum dado Selecionado");
		}
	}

	@UiHandler("selecionaTodosButton")
	public void selecionaTodosButtonButtonClickHandler(ClickEvent event){
		int total = selecionInscricaoFlexTable.getRowCount();
		int i = 0;
		while (i < total) {
			Widget widget = selecionInscricaoFlexTable.getWidget(i, 0);
			if (widget instanceof CheckBox){
				((CheckBox)widget).setValue(true);
				((CheckBox)widget).fireEvent( new GwtEvent<ClickHandler>() {
			        @Override
			        public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
			        return ClickEvent.getType();
			        }
			        @Override
			        protected void dispatch(ClickHandler handler) {
			            handler.onClick(null);
			        }
			   });
			}
			i++;
		}
	}

	@UiHandler("desmarcaTodosButton")
	public void desmarcaTodosButtonButtonClickHandler(ClickEvent event){
		int total = selecionInscricaoFlexTable.getRowCount();
		int i = 0;
		while (i < total) {
			Widget widget = selecionInscricaoFlexTable.getWidget(i, 0);
			if (widget instanceof CheckBox){
				((CheckBox)widget).setValue(false);
				((CheckBox)widget).fireEvent( new GwtEvent<ClickHandler>() {
			        @Override
			        public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
			        return ClickEvent.getType();
			        }
			        @Override
			        protected void dispatch(ClickHandler handler) {
			            handler.onClick(null);
			        }
			   });
			}
			i++;
		}
	}

	@UiHandler("selecionaInscricaoButton")
	public void selecionaInscricaoButtonClickHandler(ClickEvent event){
		limpaCamposSelecao();
		defineCamposSelecao();
		selecaoInscricaoDialogBox.center();
		selecaoInscricaoDialogBox.show();
	}


	@UiHandler(value={"mostraChoqueCheckBox", "mostraCoordenacaoCheckBox","naomostraPadrinhoCheckBox","naomostraApoioCheckBox","naomostraAtividadeCheckBox"})
	public void filtroClickHandler(ClickEvent event){
		defineCamposSelecao();
	}

	@UiHandler(value={"qtdeMaximaNumberTextBox","qtdeMesmaNumberTextBox","filtroAtividadeListBox"})
	public void filtroChangeHandler(ChangeEvent event){
		defineCamposSelecao();
	}

	@SuppressWarnings("deprecation")
	public List<EncontroAtividade> montaListaAtividadesPorPeriodoSelecionado(List<EncontroAtividade> listaEncontroAtividade, EncontroPeriodo periodo){
		List<EncontroAtividade> listaEncontroAtividadePeriodo = new ArrayList<EncontroAtividade>();
		Atividade atividade = (Atividade) ListBoxUtil.getItemSelected(planilhaAtividadeListBox, presenter.getGrupoEncontroVO().getListaAtividade());
		if(periodo!=null){
			Date inicio = null, fim = new Date(3000,1,1);
			boolean achou = false;
			inicio = periodo.getInicio();
			List<EncontroPeriodo> listaPeriodo = presenter.getEncontroVO().getListaPeriodo();
			Collections.sort(listaPeriodo, new Comparator<EncontroPeriodo>() {
				@Override
				public int compare(EncontroPeriodo o1, EncontroPeriodo o2) {
					return o1.getInicio().compareTo(o2.getInicio());
				}
			});
			for (EncontroPeriodo ep : listaPeriodo) {
				if(ep.getId().equals(periodo.getId())){
					achou = true;
				}
				if(achou && ep.getInicio().after(inicio)){
					fim = ep.getInicio();
					break;
				}
			}

			for (EncontroAtividade ea : listaEncontroAtividade) {
				if (ea.getInicio().compareTo(inicio)>=0 && ea.getInicio().compareTo(fim)<0){
					if (atividade == null || atividade.getId().equals(ea.getAtividade().getId()))
						listaEncontroAtividadePeriodo.add(ea);
				}
			}
			return listaEncontroAtividadePeriodo;

		}else{
			for (EncontroAtividade ea : listaEncontroAtividade) {
				if (atividade == null || atividade.getId().equals(ea.getAtividade().getId()))
						listaEncontroAtividadePeriodo.add(ea);
			}
			return listaEncontroAtividadePeriodo;
		}
	}

	private void defineCamposSelecao() {
		showWaitMessage(true);
		List<EncontroAtividade> listaEncontroAtividade = montaListaAtividadesPorPeriodoSelecionado(presenter.getEncontroVO().getListaEncontroAtividade(),getPeriodoSelecionado());
		List<EncontroInscricao> listaEncontroInscricao = presenter.getEncontroVO().getListaInscricao();
		List<ParticipanteVO> listaParticipantes = new ArrayList<ParticipanteVO>();
		List<ParticipanteVO> listaExcluida = new ArrayList<ParticipanteVO>();
		listaParticipantesSugeridos = new ArrayList<ParticipanteVO>();

		Papel papel = (Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel());
		EncontroAtividade atividade = (EncontroAtividade)ListBoxUtil.getItemSelected(filtroAtividadeListBox, presenter.getEncontroVO().getListaEncontroAtividade());

		boolean ok;

		for (EncontroInscricao ei : listaEncontroInscricao) {
			if(getEncontroAtividadeInscricao(encontroAtividadeEditada, ei) == null && !ei.getTipo().equals(TipoInscricaoEnum.EXTERNO)){
				ok=true;
				if ( !mostraChoqueCheckBox.getValue() && getChoqueHorarios(null, encontroAtividadeEditada, ei, papel)){
					ok=false;
				}
				if ( !mostraCoordenacaoCheckBox.getValue() && getCoordenacao(ei)){
					ok=false;
				}
				if ( naomostraPadrinhoCheckBox.getValue() && ei.getTipo().equals(TipoInscricaoEnum.PADRINHO)){
					ok=false;
				}
				if ( naomostraPadrinhoCheckBox.getValue() && ei.getTipo().equals(TipoInscricaoEnum.APOIO)){
					ok=false;
				}
				if ( naomostraAtividadeCheckBox.getValue() && atividade != null && getEncontroAtividadeInscricaoFull(atividade, ei) != null){
					ok=false;
				}
				if(ok){
					ParticipanteVO p = new ParticipanteVO();
					p.setEncontroInscricao(ei);
					p.setQtdeAtividades(0);
					p.setQtdeMesmaAtividades(0);
					p.getTags().clear();
					listaParticipantes.add(p);
				}else{
					ParticipanteVO p = new ParticipanteVO();
					p.setEncontroInscricao(ei);
					p.setQtdeAtividades(0);
					p.setQtdeMesmaAtividades(0);
					p.getTags().clear();
					listaExcluida.add(p);
				}
			}
		}

		int qtdeOcorrencias=0;

		for (EncontroAtividade ea : presenter.getEncontroVO().getListaEncontroAtividade()) {
			if(ea.getAtividade().getId().equals(encontroAtividadeEditada.getAtividade().getId())){
				qtdeOcorrencias++;
			}
		}

		int qtdeAtividade=0;
		int qtdeAtividadeParticipacoes=0;

		if (listaParticipantes.size()>0){
			for (EncontroAtividade ea : listaEncontroAtividade) {
				if(!ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOS) && !ea.getId().equals(encontroAtividadeEditada.getId())){
					qtdeAtividade++;
					for (ParticipanteVO participanteVO : listaParticipantes) {
						for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
							if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
								if(eai.getEncontroAtividade().getId().equals(ea.getId())){
									participanteVO.setQtdeAtividades(participanteVO.getQtdeAtividades()+1);
									qtdeAtividadeParticipacoes++;
									break;
								}
							}
						}
					}
					for (ParticipanteVO participanteVO : listaExcluida) {
						for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
							if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
								if(eai.getEncontroAtividade().getId().equals(ea.getId())){
									participanteVO.setQtdeAtividades(participanteVO.getQtdeAtividades()+1);
									qtdeAtividadeParticipacoes++;
									break;
								}
							}
						}
					}
				}
			}
			for (ParticipanteVO participanteVO : listaParticipantes) {
				for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricaoFull()) {
					if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
						if(!eai.getEncontroAtividade().getId().equals(encontroAtividadeEditada.getId()) &&
								eai.getEncontroAtividade().getAtividade().getId().equals(encontroAtividadeEditada.getAtividade().getId())){
							participanteVO.setQtdeMesmaAtividades(participanteVO.getQtdeMesmaAtividades()+1);
						}
					}
				}
			}


			Integer qtdeMax = null;
			if (qtdeMaximaNumberTextBox.getNumber()!=null) qtdeMax = new Integer(qtdeMaximaNumberTextBox.getNumber().intValue());
			Integer qtdeMesma = null;
			if (qtdeMesmaNumberTextBox.getNumber()!=null) qtdeMesma = new Integer(qtdeMesmaNumberTextBox.getNumber().intValue());
			Collections.sort(listaParticipantes, new Comparator<ParticipanteVO>() {
				@Override
				public int compare(ParticipanteVO o1, ParticipanteVO o2) {
					return o1.getQtdeAtividades().compareTo(o2.getQtdeAtividades());
				}
			});

			int row = 0;
			for (ParticipanteVO participante: listaParticipantes) {
				boolean add = true;
				if ((qtdeMax != null && row > qtdeMax)){
					add = false;
				}
				if ((qtdeMesma != null && participante.getQtdeMesmaAtividades() > qtdeMesma)){
					add = false;
				}

				if (add){
					listaParticipantesSugeridos.add(participante);
					row++;
				}else
					listaExcluida.add(participante);
			}
		}

		int media = 0;
		if (qtdeAtividade > 0) media = qtdeAtividadeParticipacoes / qtdeAtividade;
		mediaLabel.setText("Média de Atividades: " + media + " Qtde M.A.: " + qtdeOcorrencias );
		LabelTotalUtil.setTotal(itemSelecionaTotal, listaParticipantes.size(), "sugestão", "sugestões", "");
		selecionInscricaoTableUtil.clearData();
		int row = 0;
		for (final ParticipanteVO participante: listaParticipantesSugeridos) {
				Object dados[] = new Object[5];
				final CheckBox checkBox = new CheckBox();
				checkBox.setValue(false);
				checkBox.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						participante.setSelecionado(checkBox.getValue());
					}
				});

				dados[0] = checkBox;
				dados[1] = participante.getEncontroInscricao().toStringApelidos();
				dados[2] = participante.getEncontroInscricao().getTipo().getNome();
				dados[3] = participante.getQtdeAtividades().toString();
				if (participante.getQtdeAtividades() <= media )
					selecionInscricaoTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialNormalBlue");
				else
					selecionInscricaoTableUtil.setRowSpecialStyle(row+1, "FlexTable-RowSpecialNormalRed");
				dados[4] = participante.getQtdeMesmaAtividades().toString();
				selecionInscricaoTableUtil.addRow(dados,row+1);
				row++;
		}
		LabelTotalUtil.setTotal(itemSelecionaTotal, row, "sugestão", "sugestões", "");
		selecionInscricaoTableUtil.applyDataRowStyles();
		showWaitMessage(false);
	}

	private boolean getCoordenacao(EncontroInscricao ei) {
		List<EncontroOrganogramaVO> organograma = presenter.getEncontroVO().getListaOrganogramaEncontroVO();
		for (EncontroOrganogramaVO encontroOrganogramaVO : organograma) {
			List<EncontroOrganogramaArea> area = encontroOrganogramaVO.getListaEncontroOrganogramaArea();
			for (EncontroOrganogramaArea aux : area) {
				if (aux.getEncontroInscricao1()!=null && aux.getEncontroInscricao1().getId().equals(ei.getId()))
					return true;
				if (aux.getEncontroInscricao2()!=null && aux.getEncontroInscricao2().getId().equals(ei.getId()))
					return true;
			}
			List<EncontroOrganogramaCoordenacao> coordenacao = encontroOrganogramaVO.getListaEncontroOrganogramaCoordenacao();
			for (EncontroOrganogramaCoordenacao aux : coordenacao) {
				if (aux.getEncontroInscricao1()!=null && aux.getEncontroInscricao1().getId().equals(ei.getId()))
					return true;
				if (aux.getEncontroInscricao2()!=null && aux.getEncontroInscricao2().getId().equals(ei.getId()))
					return true;
			}
		}
		return false;
	}

	private void limpaCamposSelecao() {
		selecionInscricaoTableUtil.clearData();
		mostraChoqueCheckBox.setValue(false);
		mostraCoordenacaoCheckBox.setValue(true);
		naomostraPadrinhoCheckBox.setValue(false);
		naomostraApoioCheckBox.setValue(false);
		naomostraAtividadeCheckBox.setValue(false);
		qtdeMaximaNumberTextBox.setValue(null);
		qtdeMesmaNumberTextBox.setValue(null);
		ListBoxUtil.populate(filtroAtividadeListBox,false,presenter.getEncontroVO().getListaEncontroAtividade());
		ListBoxUtil.setItemSelected(filtroAtividadeListBox, encontroAtividadeEditada.toString());
	}

	@Override
	public void populaAtividades(List<Atividade> listaAtividades) {
		ListBoxUtil.populate(atividadeListBox, false, listaAtividades);
		ListBoxUtil.populate(planilhaAtividadeListBox, true, listaAtividades);
	}

}