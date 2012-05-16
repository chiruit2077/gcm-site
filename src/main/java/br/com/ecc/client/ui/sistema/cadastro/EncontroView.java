package br.com.ecc.client.ui.sistema.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.ui.component.textbox.NumberTextBox.Formato;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroConviteResponsavel;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.EncontroTotalizacao;
import br.com.ecc.model.EncontroTotalizacaoAtividade;
import br.com.ecc.model.tipo.TipoAtividadeEnum;
import br.com.ecc.model.vo.EncontroTotalizacaoVO;
import br.com.ecc.model.vo.EncontroVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class EncontroView extends BaseView<EncontroPresenter> implements EncontroPresenter.Display {

	@UiTemplate("EncontroView.ui.xml")
	interface EncontroViewUiBinder extends UiBinder<Widget, EncontroView> {}
	private EncontroViewUiBinder uiBinder = GWT.create(EncontroViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	
	@UiField DateBox inicioDateBox;
	@UiField DateBox fimDateBox;
	@UiField(provided=true) NumberTextBox afilhadosNumberTextBox;
	
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	@UiField(provided=true) FlexTable periodoFlexTable;
	private FlexTableUtil periodoTableUtil = new FlexTableUtil();
	@UiField Label itemPeriodoTotal;
	
	@UiField(provided=true) FlexTable totalizacaoFlexTable;
	private FlexTableUtil totalizacaoTableUtil = new FlexTableUtil();
	@UiField Label itemTotalizacaoTotal;
	
	@UiField DialogBox editaPeriodoDialogBox;
	@UiField TextBox nomePeriodoTextBox;
	@UiField DateBox inicioPeriodoDateBox;
	@UiField Button salvarPeriodoButton;
	@UiField Button fecharPeriodoButton;
	
	@UiField(provided=true) FlexTable totalizacaoAtividadeFlexTable;
	private FlexTableUtil totalizacaoAtividadeTableUtil = new FlexTableUtil();
	@UiField Label totalizacaoAtividadeTotal;
	
	@UiField ListBox tipoAtividadeListBox;
	
	@UiField ListBox atividadeListBox;
	
	@UiField DialogBox editaTotalizacaoDialogBox;
	@UiField TextBox nomeTotalizacaoTextBox;
	@UiField Button salvarTotalizacaoButton;
	@UiField Button fecharTotalizacaoButton;
	
	@UiField(provided=true) FlexTable encontroFlexTable;
	private FlexTableUtil encontroTableUtil = new FlexTableUtil();
	
	//responsavel convite
	@UiField(provided=true) FlexTable responsavelFlexTable;
	private FlexTableUtil responsavelTableUtil = new FlexTableUtil();
	@UiField Label itemResponsavelTotal;
	
	@UiField(provided = true) SuggestBox casalSuggestBox;
	private final GenericEntitySuggestOracle casalSuggest = new GenericEntitySuggestOracle();
	
	//financeiro
	@UiField(provided=true) NumberTextBox valorAfilhadoNumberTextBox;
	@UiField(provided=true) NumberTextBox valorPadrinhoNumberTextBox;
	@UiField(provided=true) NumberTextBox valorApoioNumberTextBox;
	@UiField(provided=true) NumberTextBox valorInscricaoNumberTextBox;
	@UiField DateBox dataVencimentoInscricaoDateBox;
	@UiField DateBox dataVencimentoDateBox;

	DateTimeFormat dfGlobal = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
	DateTimeFormat dfGlobalTempo = DateTimeFormat.getFormat("dd-MM-yyyy HH:mm");
	
	private EncontroVO entidadeEditada;
	private EncontroPeriodo entidadePeriodoEditado;
	private EncontroTotalizacaoVO entidadeTotalizacaoVOEditado;

	private List<Atividade> listaAtividade;
	
	public EncontroView() {
		criaTabela();
		criaTabelaResponsavel();
		criaTabelaPeriodo();
		criaTabelaTotalizacao();
		criaTabelaTotalizacaoAtividade();
		
		casalSuggest.setMinimoCaracteres(2);
		casalSuggest.setSuggestQuery("casal.porNomeLike");
		casalSuggestBox = new SuggestBox(casalSuggest);
		
		afilhadosNumberTextBox = new NumberTextBox(false, false, 5, 5);
		valorAfilhadoNumberTextBox = new NumberTextBox(true, false, 16, 16, Formato.MOEDA);
		valorPadrinhoNumberTextBox = new NumberTextBox(true, false, 16, 16, Formato.MOEDA);
		valorApoioNumberTextBox = new NumberTextBox(true, false, 16, 16, Formato.MOEDA);
		valorInscricaoNumberTextBox = new NumberTextBox(true, false, 16, 16, Formato.MOEDA);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		tituloFormularioLabel.setText(getDisplayTitle());
		
		inicioDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		inicioDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		
		fimDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		fimDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		
		dataVencimentoInscricaoDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		dataVencimentoInscricaoDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		
		dataVencimentoDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM)));
		dataVencimentoDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		
		inicioPeriodoDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		inicioPeriodoDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
		
		ListBoxUtil.populate(tipoAtividadeListBox, true, TipoAtividadeEnum.values());
	}
	
	private void criaTabela() {
		encontroFlexTable = new FlexTable();
		encontroFlexTable.setStyleName("portal-formSmall");
		encontroTableUtil.initialize(encontroFlexTable);
		
		encontroTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		encontroTableUtil.addColumn("Nome", "200", HasHorizontalAlignment.ALIGN_LEFT);
		encontroTableUtil.addColumn("Inicio", "80", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy");
		encontroTableUtil.addColumn("Fim", "80", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy");
	}
	
	private void criaTabelaResponsavel() {
		responsavelFlexTable = new FlexTable();
		responsavelFlexTable.setStyleName("portal-formSmall");
		responsavelTableUtil.initialize(responsavelFlexTable);
		
		responsavelTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		responsavelTableUtil.addColumn("Nome", "250", HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	private void criaTabelaPeriodo() {
		periodoFlexTable = new FlexTable();
		periodoFlexTable.setStyleName("portal-formSmall");
		periodoTableUtil.initialize(periodoFlexTable);
		
		periodoTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		periodoTableUtil.addColumn("Nome", "150", HasHorizontalAlignment.ALIGN_LEFT);
		periodoTableUtil.addColumn("Inicio", "120", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
	}
	
	private void criaTabelaTotalizacao() {
		totalizacaoFlexTable = new FlexTable();
		totalizacaoFlexTable.setStyleName("portal-formSmall");
		totalizacaoTableUtil.initialize(totalizacaoFlexTable);
		
		totalizacaoTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		totalizacaoTableUtil.addColumn("Nome", null, HasHorizontalAlignment.ALIGN_LEFT);
		totalizacaoTableUtil.addColumn("Atividades", "50", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.NUMBER, null);
	}
	
	private void criaTabelaTotalizacaoAtividade() {
		totalizacaoAtividadeFlexTable = new FlexTable();
		totalizacaoAtividadeFlexTable.setStyleName("portal-formSmall");
		totalizacaoAtividadeTableUtil.initialize(totalizacaoAtividadeFlexTable);
		
		totalizacaoAtividadeTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		totalizacaoAtividadeTableUtil.addColumn("Tipo", "150", HasHorizontalAlignment.ALIGN_LEFT);
		totalizacaoAtividadeTableUtil.addColumn("Atividade", null, HasHorizontalAlignment.ALIGN_LEFT);
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
		entidadeEditada.getEncontro().setInicio(inicioDateBox.getValue());
		entidadeEditada.getEncontro().setFim(fimDateBox.getValue());
		entidadeEditada.getEncontro().setQuantidadeAfilhados(null);
		if(afilhadosNumberTextBox.getNumber()!=null){
			entidadeEditada.getEncontro().setQuantidadeAfilhados(afilhadosNumberTextBox.getNumber().intValue());	
		}
		entidadeEditada.getEncontro().setGrupo(presenter.getGrupoSelecionado());
		
		if(valorAfilhadoNumberTextBox.getNumber()!=null){
			entidadeEditada.getEncontro().setValorAfilhado(new BigDecimal(valorAfilhadoNumberTextBox.getNumber().doubleValue()));	
		}
		if(valorPadrinhoNumberTextBox.getNumber()!=null){
			entidadeEditada.getEncontro().setValorPadrinho(new BigDecimal(valorPadrinhoNumberTextBox.getNumber().doubleValue()));	
		}
		if(valorApoioNumberTextBox.getNumber()!=null){
			entidadeEditada.getEncontro().setValorApoio(new BigDecimal(valorApoioNumberTextBox.getNumber().doubleValue()));	
		}
		if(valorInscricaoNumberTextBox.getNumber()!=null){
			entidadeEditada.getEncontro().setValorInscricao(new BigDecimal(valorInscricaoNumberTextBox.getNumber().doubleValue()));	
		}
		entidadeEditada.getEncontro().setDataMaximaPagamento(dataVencimentoDateBox.getValue());
		entidadeEditada.getEncontro().setDataPagamentoInscricao(dataVencimentoInscricaoDateBox.getValue());
		
		presenter.salvar(entidadeEditada);
	}
	private void edita(Encontro encontro) {
		limpaCampos();
		if(encontro == null){
			entidadeEditada = new EncontroVO();
			entidadeEditada.setEncontro(new Encontro());
			entidadeEditada.setListaCoordenadores(new ArrayList<Casal>());
			entidadeEditada.setListaEncontroAtividade(new ArrayList<EncontroAtividade>());
			entidadeEditada.setListaInscricao(new ArrayList<EncontroInscricao>());
			entidadeEditada.setListaPeriodo(new ArrayList<EncontroPeriodo>());
			entidadeEditada.setListaTotalizacao(new ArrayList<EncontroTotalizacaoVO>());
			editaDialogBox.center();
			editaDialogBox.show();
			inicioDateBox.setFocus(true);
		} else {
			presenter.getVO(encontro);
		}
	}
	
	public void limpaCampos(){
		afilhadosNumberTextBox.setNumber(null);
		inicioDateBox.setValue(null);
		fimDateBox.setValue(null);
		periodoTableUtil.clearData();
		totalizacaoTableUtil.clearData();
		itemTotalizacaoTotal.setText(null);
		itemPeriodoTotal.setText(null);
		itemResponsavelTotal.setText(null);
		responsavelTableUtil.clearData();
		casalSuggestBox.setValue(null);
		
		valorAfilhadoNumberTextBox.setNumber(null);
		valorPadrinhoNumberTextBox.setNumber(null);
		valorApoioNumberTextBox.setNumber(null);
		valorInscricaoNumberTextBox.setNumber(null);
		dataVencimentoInscricaoDateBox.setValue(null);
		dataVencimentoDateBox.setValue(null);
	}

	public void defineCampos(EncontroVO encontroVO){
		inicioDateBox.setValue(encontroVO.getEncontro().getInicio());
		fimDateBox.setValue(encontroVO.getEncontro().getFim());
		afilhadosNumberTextBox.setNumber(encontroVO.getEncontro().getQuantidadeAfilhados());
		
		if(encontroVO.getEncontro().getValorAfilhado()!=null){
			valorAfilhadoNumberTextBox.setNumber(encontroVO.getEncontro().getValorAfilhado());
		}
		if(encontroVO.getEncontro().getValorPadrinho()!=null){
			valorPadrinhoNumberTextBox.setNumber(encontroVO.getEncontro().getValorPadrinho());
		}
		if(encontroVO.getEncontro().getValorApoio()!=null){
			valorApoioNumberTextBox.setNumber(encontroVO.getEncontro().getValorApoio());
		}
		if(encontroVO.getEncontro().getValorInscricao()!=null){
			valorInscricaoNumberTextBox.setNumber(encontroVO.getEncontro().getValorInscricao());
		}
		dataVencimentoInscricaoDateBox.setValue(encontroVO.getEncontro().getDataPagamentoInscricao());
		dataVencimentoDateBox.setValue(encontroVO.getEncontro().getDataMaximaPagamento());
		
		populaPeriodos();
		populaTotalizacoes();
		populaResponsaveis();
	}
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de Encontros";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		encontroTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<Encontro> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "encontro", "encontros", "");
		encontroTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Encontro encontro: lista) {
			Object dados[] = new Object[4];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(encontro);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este encontro ?")){
						presenter.excluir(encontro);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			
			dados[1] = encontro.toString();
			dados[2] = encontro.getInicio()==null?"":dfGlobal.format(encontro.getInicio());
			dados[3] = encontro.getFim()==null?"":dfGlobal.format(encontro.getFim());
			encontroTableUtil.addRow(dados,row+1);
			row++;
		}
		encontroTableUtil.applyDataRowStyles();
	}
	
	@Override
	public void setVO(EncontroVO encontroVO) {
		entidadeEditada = encontroVO;
		defineCampos(encontroVO);
		editaDialogBox.center();
		editaDialogBox.show();
	}
	
	public void populaPeriodos() {
		LabelTotalUtil.setTotal(itemPeriodoTotal, entidadeEditada.getListaPeriodo().size(), "periodo", "periodos", "");
		periodoTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroPeriodo encontroPeriodo: entidadeEditada.getListaPeriodo()) {
			Object dados[] = new Object[3];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					editaPeriodo(encontroPeriodo);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este periodo ?")){
						entidadeEditada.getListaPeriodo().remove(encontroPeriodo);
						populaPeriodos();
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			dados[1] = encontroPeriodo.getNome();
			dados[2] = encontroPeriodo.getInicio()==null?"":dfGlobalTempo.format(encontroPeriodo.getInicio());
			periodoTableUtil.addRow(dados,row+1);
			row++;
		}
		periodoTableUtil.applyDataRowStyles();
	}
	
	//periodos
	private void editaPeriodo(EncontroPeriodo encontroPeriodo) {
		limpaCamposPeriodo();
		if(encontroPeriodo == null){
			entidadePeriodoEditado = new EncontroPeriodo();
		} else {
			entidadePeriodoEditado = encontroPeriodo;
			defineCamposPeriodo(encontroPeriodo);
		}
		editaPeriodoDialogBox.center();
		editaPeriodoDialogBox.show();
		nomePeriodoTextBox.setFocus(true);
	}
	public void defineCamposPeriodo(EncontroPeriodo encontroPeriodo){
		nomePeriodoTextBox.setValue(encontroPeriodo.getNome());
		inicioPeriodoDateBox.setValue(encontroPeriodo.getInicio());
	}
	public void limpaCamposPeriodo(){
		nomePeriodoTextBox.setValue(null);
		inicioPeriodoDateBox.setValue(null);
	}
	@UiHandler("salvarPeriodoButton")
	public void salvarPeriodoButtonClickHandler(ClickEvent event){
		entidadeEditada.getListaPeriodo().remove(entidadePeriodoEditado);
		entidadePeriodoEditado.setNome(nomePeriodoTextBox.getValue());
		entidadePeriodoEditado.setInicio(inicioPeriodoDateBox.getValue());
		entidadeEditada.getListaPeriodo().add(entidadePeriodoEditado);
		editaPeriodoDialogBox.hide();
		populaPeriodos();
	}
	@UiHandler("fecharPeriodoButton")
	public void fecharPeriodoButtonClickHandler(ClickEvent event){
		editaPeriodoDialogBox.hide();
	}
	@UiHandler("addPeriodoImage")
	public void addPeriodoImageClickHandler(ClickEvent event){
		editaPeriodo(null);
	}
	
	//totalizacoes
	private void editaTotalizacao(EncontroTotalizacaoVO encontroTotalizacaoVO) {
		limpaCamposTotalizacao();
		if(encontroTotalizacaoVO == null){
			entidadeTotalizacaoVOEditado = new EncontroTotalizacaoVO();
			entidadeTotalizacaoVOEditado.setEncontroTotalizacao(new EncontroTotalizacao());
		} else {
			entidadeTotalizacaoVOEditado = encontroTotalizacaoVO;
			defineCamposTotalizacao(encontroTotalizacaoVO);
		}
		editaTotalizacaoDialogBox.center();
		editaTotalizacaoDialogBox.show();
		nomeTotalizacaoTextBox.setFocus(true);
	}
	public void defineCamposTotalizacao(EncontroTotalizacaoVO encontroTotalizacaoVO){
		nomeTotalizacaoTextBox.setValue(encontroTotalizacaoVO.getEncontroTotalizacao().getNome());
		populaTotalizacaoAtividades();
	}
	public void limpaCamposTotalizacao(){
		totalizacaoAtividadeTotal.setText(null);
		nomeTotalizacaoTextBox.setValue(null);
		totalizacaoAtividadeTableUtil.clearData();
	}
	@UiHandler("salvarTotalizacaoButton")
	public void salvarTotalizacaoButtonClickHandler(ClickEvent event){
		if(nomeTotalizacaoTextBox.getValue().equals("")){
			Window.alert("Informe o nome da totalização");
			nomeTotalizacaoTextBox.setFocus(true);
			return;
		}
		entidadeEditada.getListaTotalizacao().remove(entidadeTotalizacaoVOEditado);
		entidadeTotalizacaoVOEditado.getEncontroTotalizacao().setNome(nomeTotalizacaoTextBox.getValue());
		entidadeEditada.getListaTotalizacao().add(entidadeTotalizacaoVOEditado);
		editaTotalizacaoDialogBox.hide();
		populaTotalizacoes();
	}
	@UiHandler("fecharTotalizacaoButton")
	public void fecharTotalizacaoButtonClickHandler(ClickEvent event){
		editaTotalizacaoDialogBox.hide();
	}
	@UiHandler("addTotalizacaoImage")
	public void addTotalizacaoImageClickHandler(ClickEvent event){
		editaTotalizacao(null);
	}
	
	public void populaTotalizacoes() {
		LabelTotalUtil.setTotal(itemTotalizacaoTotal, entidadeEditada.getListaTotalizacao().size(), "totalização", "totalizações", "a");
		totalizacaoTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroTotalizacaoVO encontroTotalizacao: entidadeEditada.getListaTotalizacao()) {
			Object dados[] = new Object[3];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					editaTotalizacao(encontroTotalizacao);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este agrupamento ?")){
						entidadeEditada.getListaTotalizacao().remove(encontroTotalizacao);
						populaTotalizacoes();
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			dados[1] = encontroTotalizacao.getEncontroTotalizacao().getNome();
			dados[2] = encontroTotalizacao.getListaAtividade()==null?"":encontroTotalizacao.getListaAtividade().size();
			totalizacaoTableUtil.addRow(dados,row+1);
			row++;
		}
		totalizacaoTableUtil.applyDataRowStyles();
	}
	
	public void populaTotalizacaoAtividades() {
		LabelTotalUtil.setTotal(totalizacaoAtividadeTotal, entidadeTotalizacaoVOEditado.getListaAtividade(), "atividade", "atividades", "a");
		totalizacaoAtividadeTableUtil.clearData();
		int row = 0;
		Image excluir;
		for (final EncontroTotalizacaoAtividade atividade: entidadeTotalizacaoVOEditado.getListaAtividade()) {
			Object dados[] = new Object[3];
			
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este participante ?")){
						entidadeTotalizacaoVOEditado.getListaAtividade().remove(atividade);
						populaTotalizacaoAtividades();
					}
				}
			});
			
			dados[0] = excluir;
			dados[1] = atividade.getTipoAtividade()==null?"Todos":atividade.getTipoAtividade().getNome();
			dados[2] = atividade.getAtividade()==null?"Todas":atividade.getAtividade().getNome();
			totalizacaoAtividadeTableUtil.addRow(dados,row+1);
			row++;
		}
		totalizacaoAtividadeTableUtil.applyDataRowStyles();
	}

	@UiHandler("addTotalizacaoAtividadeImage")
	public void addTotalizacaoAtividadeImageClickHandler(ClickEvent event){
		TipoAtividadeEnum tipoAtividade = (TipoAtividadeEnum) ListBoxUtil.getItemSelected(tipoAtividadeListBox, TipoAtividadeEnum.values());
		Atividade atividade = (Atividade) ListBoxUtil.getItemSelected(atividadeListBox, listaAtividade);
		if(entidadeTotalizacaoVOEditado.getListaAtividade()==null){
			entidadeTotalizacaoVOEditado.setListaAtividade(new ArrayList<EncontroTotalizacaoAtividade>());
		}
		for (EncontroTotalizacaoAtividade a : entidadeTotalizacaoVOEditado.getListaAtividade()) {
			if(a.getAtividade()!=null && a.getAtividade().equals(atividade) && 
			   a.getTipoAtividade()!=null && a.getTipoAtividade().equals(tipoAtividade)){
				Window.alert("Esta atividade já foi adicionada a esta totalização");
				return;
			}
		}
		EncontroTotalizacaoAtividade eai = new EncontroTotalizacaoAtividade();
		eai.setEncontroTotalizacao(entidadeTotalizacaoVOEditado.getEncontroTotalizacao());
		eai.setAtividade(atividade);
		eai.setTipoAtividade(tipoAtividade);
		entidadeTotalizacaoVOEditado.getListaAtividade().add(eai);
		populaTotalizacaoAtividades();
	}

	@Override
	public void populaAtividades(List<Atividade> listaAtividade) {
		ListBoxUtil.populate(atividadeListBox, true, listaAtividade);
		this.listaAtividade = listaAtividade;
	}
	
	public void populaResponsaveis() {
		LabelTotalUtil.setTotal(itemResponsavelTotal, entidadeEditada.getListaResponsavelConvite().size(), "responsável", "responsáveis", "");
		responsavelTableUtil.clearData();
		int row = 0;
		Image excluir;
		HorizontalPanel hp;
		for (final EncontroConviteResponsavel responsavel: entidadeEditada.getListaResponsavelConvite()) {
			Object dados[] = new Object[2];
			
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este responsável ?")){
						entidadeEditada.getListaResponsavelConvite().remove(responsavel);
						populaResponsaveis();
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(excluir);
			
			dados[0] = hp;
			dados[1] = responsavel.getCasal().getApelidos("e");
			responsavelTableUtil.addRow(dados,row+1);
			row++;
		}
		responsavelTableUtil.applyDataRowStyles();
	}
	
	@UiHandler("adicionaResponsavelButton")
	public void adicionaResponsavelButtonClickHandler(ClickEvent event){
		Casal casal = (Casal)ListUtil.getEntidadePorNome(casalSuggest.getListaEntidades(), casalSuggestBox.getValue());
		if(casal!=null){
			EncontroConviteResponsavel responsavel = new EncontroConviteResponsavel();
			responsavel.setCasal(casal);
			responsavel.setEncontro(entidadeEditada.getEncontro());
			entidadeEditada.getListaResponsavelConvite().add(responsavel);
			populaResponsaveis();
		}
		casalSuggestBox.setValue(null);
		casalSuggestBox.setFocus(true);
	}
}