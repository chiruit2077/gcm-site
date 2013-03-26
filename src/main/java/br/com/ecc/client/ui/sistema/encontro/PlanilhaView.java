package br.com.ecc.client.ui.sistema.encontro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
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
import br.com.ecc.model.tipo.TipoAtividadeEnum;
import br.com.ecc.model.tipo.TipoEncontroAtividadeProgramaEnum;
import br.com.ecc.model.tipo.TipoExibicaoPlanilhaEnum;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoOcorrenciaAtividadeEnum;
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
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
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

	@UiField DialogBox editaAtividadeDialogBox;
	@UiField ListBox tipoListBox;
	@UiField ListBox ocorrenciaListBox;
	@UiField ListBox programaListBox;
	@UiField ListBox atividadeListBox;
	@UiField ListBox atividadeEditaListBox;
	@UiField ListBox preenchimentoListBox;
	@UiField CheckBox revisadoCheckBox;
	@UiField DateBox inicioDateBox;
	@UiField DateBox fimDateBox;
	@UiField(provided=true) NumberTextBox qtdeNumberTextBox;
	@UiField Button excluirAtividadeButton;

	@UiField DialogBox editaInscricaoDialogBox;
	@UiField Label entidadeEditadaLabel;
	@UiField Label entidadeEditadaTituloLabel;
	@UiField ListBox inscricaoListBox;
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

	@UiField ListBox addInscricaoListBox;
	@UiField FlowPanel participantesFlowPanel;
	@UiField Label itemTotal;
	@UiField(provided=true) FlexTable encontroInscricaoFlexTable;
	private FlexTableUtil encontroInscricaoTableUtil = new FlexTableUtil();
	@UiField(provided=true) FlexTable encontroInscricaoAtividadeFlexTable;
	private FlexTableUtil encontroInscricaoAtividadeTableUtil = new FlexTableUtil();

	@UiField FlowPanel planilhaFlowPanel;
	@UiField FlowPanel planilhaHeadFlowPanel;
	@UiField VerticalPanel centralPanel;
	@UiField HorizontalPanel opcoesPanel;

	private EncontroAtividade encontroAtividadeEditada;
	private EncontroInscricao encontroInscricaoEditada;
	private EncontroAtividadeInscricao encontroAtividadeInscricaoEditada;

	private List<EncontroAtividadeInscricao> listaParticipantesInscritos;
	private List<Papel> listaPapel;

	DateTimeFormat dfDia = DateTimeFormat.getFormat("E");
	DateTimeFormat dfHora = DateTimeFormat.getFormat("HH:mm");
	NumberFormat dfPorcent = NumberFormat.getPercentFormat();

	public PlanilhaView() {
		criaTabela();
		criaTabelaAtividade();
		qtdeNumberTextBox = new NumberTextBox(false, false, 5, 5);
		initWidget(uiBinder.createAndBindUi(this));

		planilhaFlowPanel.setWidth(this.getWindowWidth() - 30 +"px");
		planilhaFlowPanel.setHeight(this.getWindowHeight() - 140 +"px");

		tituloFormularioLabel.setText(getDisplayTitle());

		inicioDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		inicioDateBox.getTextBox().setAlignment(TextAlignment.CENTER);

		fimDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy HH:mm")));
		fimDateBox.getTextBox().setAlignment(TextAlignment.CENTER);

		ListBoxUtil.populate(tipoListBox, false, TipoAtividadeEnum.values());
		ListBoxUtil.populate(ocorrenciaListBox, false, TipoOcorrenciaAtividadeEnum.values());
		ListBoxUtil.populate(programaListBox, false, TipoEncontroAtividadeProgramaEnum.values());
		ListBoxUtil.populate(preenchimentoListBox, false, TipoPreenchimentoAtividadeEnum.values());

		ListBoxUtil.populate(planilhaListBox, true, TipoExibicaoPlanilhaEnum.values());
	}

	@Override
	public void adjustWindowSize() {
		planilhaFlowPanel.setHeight((centralPanel.getOffsetHeight() - (opcoesPanel.getOffsetHeight() + planilhaHeadFlowPanel.getAbsoluteTop()) + 4 )+"px");
	}

	private void criaTabela() {
		encontroInscricaoFlexTable = new FlexTable();
		encontroInscricaoFlexTable.setStyleName("portal-formSmall");
		encontroInscricaoTableUtil.initialize(encontroInscricaoFlexTable);

		encontroInscricaoTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		encontroInscricaoTableUtil.addColumn("Participante", null, HasHorizontalAlignment.ALIGN_LEFT);
		encontroInscricaoTableUtil.addColumn("Papel", "150", HasHorizontalAlignment.ALIGN_LEFT);
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
		encontroInscricaoAtividadeTableUtil.addColumn("Papel", "150", HasHorizontalAlignment.ALIGN_LEFT);
	}

	@UiHandler("fecharAtividadeButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaAtividadeDialogBox.hide();
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
			qtdeNumberTextBox.setValue(null);
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
		encontroAtividadeEditada.setTipoAtividade((TipoAtividadeEnum)ListBoxUtil.getItemSelected(tipoListBox, TipoAtividadeEnum.values()));
		encontroAtividadeEditada.setTipoOcorrencia((TipoOcorrenciaAtividadeEnum)ListBoxUtil.getItemSelected(ocorrenciaListBox, TipoOcorrenciaAtividadeEnum.values()));
		encontroAtividadeEditada.setTipoPrograma((TipoEncontroAtividadeProgramaEnum)ListBoxUtil.getItemSelected(programaListBox, TipoEncontroAtividadeProgramaEnum.values()));
		encontroAtividadeEditada.setTipoPreenchimento((TipoPreenchimentoAtividadeEnum)ListBoxUtil.getItemSelected(preenchimentoListBox, TipoPreenchimentoAtividadeEnum.values()));
		encontroAtividadeEditada.setRevisado(revisadoCheckBox.getValue());
		presenter.salvarAtividade(encontroAtividadeEditada);
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
	}

	public void defineCamposAtividade(EncontroAtividade encontroAtividade){
		encontroAtividadeEditada = encontroAtividade;
		if(encontroAtividade.getTipoAtividade()!=null){
			ListBoxUtil.setItemSelected(tipoListBox, encontroAtividade.getTipoAtividade().getNome());
		}
		if(encontroAtividade.getTipoOcorrencia()!=null){
			ListBoxUtil.setItemSelected(ocorrenciaListBox, encontroAtividade.getTipoOcorrencia().getNome());
		}
		if(encontroAtividade.getTipoPrograma()!=null){
			ListBoxUtil.setItemSelected(programaListBox, encontroAtividade.getTipoPrograma().getNome());
		}
		if(encontroAtividade.getTipoPreenchimento()!=null){
			ListBoxUtil.setItemSelected(preenchimentoListBox, encontroAtividade.getTipoPreenchimento().getNome());
			if (!encontroAtividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.VARIAVEL)){
				if (encontroAtividade.getQuantidadeDesejada()!=null)
					qtdeNumberTextBox.setValue(encontroAtividade.getQuantidadeDesejada().toString());
				qtdeNumberTextBox.setEnabled(true);
			}else{
				qtdeNumberTextBox.setValue(null);
				qtdeNumberTextBox.setEnabled(false);
			}
		}
		if(encontroAtividade.getAtividade()!=null){
			ListBoxUtil.setItemSelected(atividadeListBox, encontroAtividade.getAtividade().getNome());
		}
		revisadoCheckBox.setValue(encontroAtividade.getRevisado());
		inicioDateBox.setValue(encontroAtividade.getInicio());
		fimDateBox.setValue(encontroAtividade.getFim());
	}

	@Override
	public String getDisplayTitle() {
		return "Planilha";
	}

	@Override
	public void reset() {
		editaAtividadeDialogBox.hide();
		editaInscricaoDialogBox.hide();
		planilhaFlowPanel.clear();
		planilhaHeadFlowPanel.clear();
		if(presenter.isCoordenador()){
			limparPlanilhaButton.setVisible(true);
			preencherAutomaticoButton.setVisible(true);
		}
	}

	private boolean participanteAdicionado(List<ParticipanteVO> listaParticipantes, Integer id){
		for (ParticipanteVO p : listaParticipantes) {
			if(p.getEncontroInscricao().getId().equals(id)){
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void populaPlanilha() {
		boolean bCoordenador = presenter.isCoordenador();
		EncontroPeriodo encontroPeriodoSelecionado = getPeriodoSelecionado();
		TipoExibicaoPlanilhaEnum tipoExibicao = getTipoExibicaoPlanilhaSelecionado();

		Date inicio = null, fim = new Date(3000,1,1);
		if(encontroPeriodoSelecionado!=null){
			boolean achou = false;
			inicio = encontroPeriodoSelecionado.getInicio();
			for (EncontroPeriodo ep : presenter.getEncontroVO().getListaPeriodo()) {
				if(ep.getId().equals(encontroPeriodoSelecionado.getId())){
					achou = true;
				}
				if(achou && ep.getInicio().after(inicio)){
					fim = ep.getInicio();
					break;
				}
			}
		}

		List<EncontroAtividade> listaEncontroAtividade = new ArrayList<EncontroAtividade>();
		List<EncontroInscricao> listaEncontroInscricao = new ArrayList<EncontroInscricao>();

		if(tipoExibicao.equals(TipoExibicaoPlanilhaEnum.COMPLETA)){
			listaEncontroInscricao = presenter.getEncontroVO().getListaInscricao();
			listaEncontroAtividade = presenter.getEncontroVO().getListaEncontroAtividade();
		} else if (tipoExibicao.equals(TipoExibicaoPlanilhaEnum.MINHA_ATIVIDADE_MINHA_COLUNA)){
			listaEncontroInscricao = montaListaEncontroInscricaoUsuarioAtual();
			listaEncontroAtividade = montaListaEncontroAtividadeusuarioAtual();
		} else if (tipoExibicao.equals(TipoExibicaoPlanilhaEnum.MINHA_ATIVIDADE_TODAS_COLUNAS)){
			listaEncontroInscricao = presenter.getEncontroVO().getListaInscricao();
			listaEncontroAtividade = montaListaEncontroAtividadeusuarioAtual();
		} else if (tipoExibicao.equals(TipoExibicaoPlanilhaEnum.TODAS_ATIVIDADES_MINHA_COLUNA)){
			listaEncontroInscricao = montaListaEncontroInscricaoUsuarioAtual();
			listaEncontroAtividade = presenter.getEncontroVO().getListaEncontroAtividade();
		}

		Collections.sort(presenter.getListaEncontroAtividadeInscricao(), new Comparator<EncontroAtividadeInscricao>() {
			@Override
			public int compare(EncontroAtividadeInscricao o1, EncontroAtividadeInscricao o2) {
				String s1, s2;
				if(o1.getEncontroInscricao().getCasal()!=null) s1 = o1.getEncontroInscricao().getCasal().getApelidos(null);
				else s1 = o1.getEncontroInscricao().getPessoa().getApelido();

				if(o2.getEncontroInscricao().getCasal()!=null) s2 = o2.getEncontroInscricao().getCasal().getApelidos(null);
				else s2 = o2.getEncontroInscricao().getPessoa().getApelido();
				return s1.compareTo(s2);
			}
		});
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

		editaAtividadeDialogBox.hide();
		editaInscricaoDialogBox.hide();

		EncontroPeriodo periodo = null, periodoAnterior = null;
		List<EncontroPeriodo> listaEncontroPeriodo = presenter.getEncontroVO().getListaPeriodo();
		if(listaEncontroPeriodo.size()>0){
			periodo = listaEncontroPeriodo.get(0);
			periodoAnterior = periodo;
		}

		List<ParticipanteVO> listaParticipantes = new ArrayList<ParticipanteVO>();

		planilhaHeadFlowPanel.clear();
		planilhaFlowPanel.clear();
		String parLinha="", parColuna="", tipoAtividade="", colunaPadrinho="", nome="";
		StringBuffer participante = new StringBuffer("");
		StringBuffer html = new StringBuffer(
			" <table cellpadding='0' cellspacing='0' border='0' style='font-size:10px;' align='left'>" +
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
			html.append("	<td lass='portal-celulaPlanilhaAtividadeHead'>Atividade</td>");
		}
		html.append("	<td class='portal-celulaPlanilhaHead'>&nbsp;</td>" +
				"   <td class='portal-celulaPlanilhaHead'>I</td>" +
				"   <td class='portal-celulaPlanilhaHead'>Qt</td>");
		for (EncontroInscricao ei : listaEncontroInscricao) {
			if(!participanteAdicionado(listaParticipantes, ei.getId())){
				ParticipanteVO p = new ParticipanteVO();
				p.setEncontroInscricao(ei);
				p.setQtdeAtividades(0);
				listaParticipantes.add(p);
				if(ei.getCasal()!=null && ei.getCasal().getId().equals(presenter.getCasal().getId()) ||
				   ei.getPessoa()!=null && ei.getPessoa().getId().equals(presenter.getUsuario().getPessoa().getId())){
					colunaPadrinho = "style= 'background-color: #ffbf95;'";
				} else {
					if(ei.getTipo().equals(TipoInscricaoEnum.PADRINHO)){
						colunaPadrinho = "style= 'background-color: #fdf3b5;'";
					}else if(ei.getTipo().equals(TipoInscricaoEnum.EXTERNO)){
						colunaPadrinho = "style= 'background-color: #FF6347;'";
					}else {
						colunaPadrinho = "";
					}
				}
				html.append("<td id='editP_" + ei.getId() + "' class='portal-celulaPlanilhaInscritoHead' " + colunaPadrinho + " ></td>");
			}
		}
		html.append("</tr></table>");
		HTMLPanel htmlPanelHead = new HTMLPanel(new String(html));
		for (final ParticipanteVO participanteVO : listaParticipantes) {
			for (final EncontroInscricao ei : listaEncontroInscricao) {
				if(participanteVO.getEncontroInscricao().getId().equals(ei.getId())){
					if(ei.getCasal()!=null){
						nome = ei.getCasal().getApelidos(" e ");
					} else {
						nome = ei.getPessoa().getApelido();
					}
					HTML label = new HTML(nome);
					label.setStyleName("portal-celulaPlanilhaVertical");
					if(bCoordenador){
						label.setTitle("Editar/Adicionar atividades para este participante");
					} else {
						label.setTitle("Visualizar as atividades para este participante");
					}
					label.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent arg0) {
							/*boolean achou = false;
							listaParticipantesInscritos = new ArrayList<EncontroAtividadeInscricao>();
							for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
								if(!achou && eai.getEncontroInscricao().getId().equals(ei.getId())){
									editaInscricao(null, eai);
									achou=true;
								}
								if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
									listaParticipantesInscritos.add(eai);
								}
							}
							populaAtividadesPorParticipante();*/
						}
					});
					htmlPanelHead.add(label, "editP_"+ei.getId().toString());
					break;
				}
			}
		}
		planilhaHeadFlowPanel.add(htmlPanelHead);

		StringBuffer htmlBody = new StringBuffer("<table cellpadding='0' cellspacing='0' border='0' style='font-size:10px;' align='left'>");
		int qtdeParticipantes = 0;
		int linha=0, coluna=0, colspan=9;
		if(!bCoordenador)colspan=6;
		boolean achou, ok;
		for (EncontroAtividade ea : listaEncontroAtividade) {
			ea.setQuantidade(0);
			ok = true;
			if(encontroPeriodoSelecionado!=null){
				ok = false;
				if(ea.getInicio().compareTo(inicio)>=0 && ea.getInicio().compareTo(fim)<0){
					ok = true;
				}
			}
			if(ok){
				if(periodo!=null){
					for (EncontroPeriodo p : listaEncontroPeriodo) {
						if(ea.getInicio().compareTo(p.getInicio())>=0){
							periodo = p;
						}
					}
					if(encontroPeriodoSelecionado==null && periodoAnterior!=null && !periodo.getId().equals(periodoAnterior.getId())){
						htmlBody.append("<tr style='background-color:#cdffbf;'>");
						htmlBody.append("<td colspan='" + colspan + "' class='portal-celulaPlanilha'> Total de atividades para \"" + periodoAnterior.getNome() + "\":</td>");
						for (final ParticipanteVO participanteVO : listaParticipantes) {
							htmlBody.append("<td align='center' class='portal-celulaPlanilha'>"+ participanteVO.getQtdeAtividades() + "</td>");
							participanteVO.setQtdeAtividades(0);
						}
						htmlBody.append("</tr>");
						periodoAnterior = periodo;
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
				htmlBody.append("<tr " + parLinha + ">");
				htmlBody.append("<td class='portal-celulaPlanilhaDia'>" + dfDia.format(ea.getInicio()).toUpperCase() + "</td>");
				htmlBody.append("<td class='portal-celulaPlanilhaHora'>" + dfHora.format(ea.getInicio()) + "</td>");
				htmlBody.append("<td class='portal-celulaPlanilhaHora'>" + dfHora.format(ea.getFim()) + "</td>");
				if(bCoordenador){
					htmlBody.append("<td id='editA_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
				}
				htmlBody.append("<td class='portal-celulaPlanilhaTipo' style='text-align:left;'>" + ea.getTipoAtividade().getNome() + "</td>");
				htmlBody.append("<td class='portal-celulaPlanilhaAtividade' style='text-align:left;white-space: nowrap;'>" + ea.getAtividade().getNome() + "</td>");
				htmlBody.append("<td id='add_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
				htmlBody.append("<td id='info_" + ea.getId() + "' class='portal-celulaPlanilha'></td>");
				participante = new StringBuffer("");
				qtdeParticipantes = 0;
				coluna = 0;
				for (ParticipanteVO participanteVO : listaParticipantes) {
					achou = false;
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
							parColuna = "style= 'background-color: #FF6347;'";
						}else {
							parColuna = "";
						}
					}
					for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
						if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
							if(eai.getEncontroAtividade().getId().equals(ea.getId())){
								if(bCoordenador){
									participante.append("<td id='edit_" + eai.getId() + "' class='portal-celulaPlanilha' " + parColuna + "></td>");
								} else {
									participante.append("<td class='portal-celulaPlanilha' " + parColuna + ">" + eai.getPapel().getSigla() + "</td>");
								}
								qtdeParticipantes++;
								participanteVO.setQtdeAtividades(participanteVO.getQtdeAtividades()+1);
								achou= true;
								break;
							}
						}
					}
					if(!achou){
						participante.append("<td class='portal-celulaPlanilha' " + parColuna + ">");
					}
					coluna++;
				}
				htmlBody.append("<td class='portal-celulaPlanilha'>" + qtdeParticipantes + "</td>");
				ea.setQuantidade(qtdeParticipantes);
				htmlBody.append(participante);
				htmlBody.append("</tr>");
				linha++;
			}
		}
		if(periodo!=null){
			htmlBody.append("<tr style='background-color:#cdffbf;'>");
			htmlBody.append("<td colspan='" + colspan + "' class='portal-celulaPlanilha' style='text-align:left;'> Total de atividades para \"" + periodo.getNome() + "\":</td>");
			for (ParticipanteVO participanteVO : listaParticipantes) {
				htmlBody.append("<td align='center' class='portal-celulaPlanilha'>"+ participanteVO.getQtdeAtividades() + "</td>");
				participanteVO.setQtdeAtividades(0);
			}
			htmlBody.append("</tr>");
		}

		//totalizadores
		if(bCoordenador){
			htmlBody.append("<tr><td colspan='99' class='portal-celulaPlanilha'>&nbsp;</td></tr>");
			for (EncontroTotalizacaoVO totalizador : presenter.getEncontroVO().getListaTotalizacao()) {
				htmlBody.append("<tr>");
				htmlBody.append("<td colspan='" + colspan + "' class='portal-celulaPlanilha' style='text-align:left;'>" + totalizador.getEncontroTotalizacao().getNome() + "</td>");
				for (ParticipanteVO participanteVO : listaParticipantes) {
					participanteVO.setQtdeAtividades(0);
					achou = false;
					for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
						if(participanteVO.getEncontroInscricao().getId().equals(eai.getEncontroInscricao().getId())){
							for (EncontroTotalizacaoAtividade atividade : totalizador.getListaAtividade()) {
								if(atividade.getAtividade()==null || eai.getEncontroAtividade().getAtividade().getId().equals(atividade.getAtividade().getId())){
									if(atividade.getTipoAtividade()==null || atividade.getTipoAtividade().equals(eai.getEncontroAtividade().getTipoAtividade())){
										participanteVO.setQtdeAtividades(participanteVO.getQtdeAtividades()+1);
										achou = true;
									}
								}
							}
						}
					}
					if(achou){
						htmlBody.append("<td class='portal-celulaPlanilha'>" + participanteVO.getQtdeAtividades() + "</td>");
					} else {
						htmlBody.append("<td class='portal-celulaPlanilha'>0</td>");
					}
				}
				htmlBody.append("</tr>");
			}
		}

		htmlBody.append("</table>");
		HTMLPanel htmlPanel = new HTMLPanel(new String(htmlBody));

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

		int qtdeok = 0;
		int qtdeerro = 0;
		int qtdeatencao = 0;
		int qtdeatividade=0;

		for (final EncontroAtividade ea : listaEncontroAtividade) {
			ok = true;
			if(encontroPeriodoSelecionado!=null){
				ok = false;
				if(ea.getInicio().compareTo(inicio)>=0 && ea.getInicio().compareTo(fim)<0){
					ok = true;
				}
			}

			verificaInconsistenciasAtividade(ea);

			if(ok){
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
						for (EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
							if(eai.getEncontroAtividade().getId().equals(ea.getId())){
								listaParticipantesInscritos.add(eai);
							}
						}
						editaInscricao(ea, null);
					}
				});
				htmlPanel.add(addImage, "add_"+ea.getId().toString());

				infoImage = new Image();
				infoImage.setSize("16px", "16px");
				infoImage.setStyleName("portal-ImageCursor");
				if(ea.getRevisado() && ea.getInfoErro().size() == 0 && ea.getInfoAtencao().size() == 0 ){
					infoImage.setUrl("images/inforevisado.png");
					infoImage.setTitle("Preenchimento Revisado Manualmente");
					qtdeok++;
				}else if(ea.getInfoErro().size() == 0 && ea.getInfoAtencao().size() == 0){
					infoImage.setUrl("images/infook.png");
					infoImage.setTitle("Preenchimento Ok");
					qtdeok++;
				}else if(ea.getInfoErro().size() > 0 ){
					infoImage.setUrl("images/infoerror.png");
					infoImage.setTitle("Preenchimento com Erros - " + ea.getInfoErro().toString());
					ea.getInfoErro().toString();
					qtdeerro++;
				}else if(ea.getInfoAtencao().size() > 0 ){
					infoImage.setUrl("images/infowarning.png");
					infoImage.setTitle("Preenchimento com Atenção - " + ea.getInfoAtencao().toString());
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
					for (final EncontroAtividadeInscricao eai : presenter.getListaEncontroAtividadeInscricao()) {
						if(eai.getEncontroAtividade().getId().equals(ea.getId())){
							HTML label = new HTML(eai.getPapel().getSigla());
							label.setStyleName("portal-ImageCursor");
							label.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent arg0) {
									editaInscricao(ea, eai);
								}
							});
							htmlPanel.add(label, "edit_"+eai.getId().toString());
						}
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

		planilhaFlowPanel.add(htmlPanel);
	}

	private List<EncontroAtividade> montaListaEncontroAtividadeusuarioAtual() {
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

	@Override
	public void populaPeriodos(){
		periodoListBox.clear();
		periodoListBox.addItem("TODOS");
		for(EncontroPeriodo periodo : presenter.getEncontroVO().getListaPeriodo()) {
			periodoListBox.addItem(periodo.toString());
		}
//		ListBoxUtil.populate(periodoListBox, true, presenter.getEncontroVO().getListaPeriodo());
	}
	@UiHandler("planilhaListBox")
	public void planilhaListBoxChangeHandler(ChangeEvent event) {
		planilhaHeadFlowPanel.clear();
		planilhaFlowPanel.clear();
		if(getTipoExibicaoPlanilhaSelecionado()!=null){
			presenter.buscaDadosPlanilha();
		}
	}
	@UiHandler("periodoListBox")
	public void periodoListBoxChangeHandler(ChangeEvent event) {
		planilhaListBoxChangeHandler(null);
	}

	@UiHandler("addInscricaoListBox")
	public void addInscricaoListBoxChangeHandler(ChangeEvent event) {
		if(addInscricaoListBox.getSelectedIndex()>0){
			inscricaoListBox.setSelectedIndex(-1);
		}
	}
	@UiHandler("inscricaoListBox")
	public void inscricaoListBoxListBoxChangeHandler(ChangeEvent event) {
		if(inscricaoListBox.getSelectedIndex()>=0){
			addInscricaoListBox.setSelectedIndex(0);
		}
	}

	@Override
	public void populaAtividades(List<Atividade> listaAtividades) {
		ListBoxUtil.populate(atividadeListBox, false, listaAtividades);
	}

	@Override
	public void populaInscricao() {
		ListBoxUtil.populate(inscricaoListBox, false, presenter.getEncontroVO().getListaInscricao());
		ListBoxUtil.populate(atividadeEditaListBox, false, presenter.getEncontroVO().getListaEncontroAtividade());
	}

	@UiHandler("fecharInscricaoButton")
	public void fecharInscricaoButtonClickHandler(ClickEvent event){
		editaInscricaoDialogBox.hide();
	}

	@UiHandler("excluirInscricaoButton")
	public void excluirInscricaoButtonClickHandler(ClickEvent event){
		if(Window.confirm("Deseja excluir esta participação ?")){
			presenter.excluirEncontroAtividadeInscricao(encontroAtividadeInscricaoEditada);
		}
	}

	@UiHandler("salvarInscricaoButton")
	public void salvarInscricaoButtonClickHandler(ClickEvent event){
		if(participantesFlowPanel.isVisible()){
			if(listaParticipantesInscritos.size()>0){
				presenter.salvarInscricoes(encontroAtividadeEditada, encontroInscricaoEditada, listaParticipantesInscritos);
			} else {
				if(Window.confirm("Deseja excluir todos os participantes para esta atividade?")){
					presenter.salvarInscricoes(encontroAtividadeEditada, encontroInscricaoEditada, listaParticipantesInscritos);
				} else {
					editaInscricaoDialogBox.hide();
				}
			}
		} else {
			encontroAtividadeInscricaoEditada.setPapel((Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel()));
			encontroAtividadeInscricaoEditada.setEncontroInscricao((EncontroInscricao)ListBoxUtil.getItemSelected(inscricaoListBox, presenter.getEncontroVO().getListaInscricao()));
			presenter.salvarInscricao(encontroAtividadeInscricaoEditada);
		}
	}
	private void editaInscricao(EncontroAtividade encontroAtividade, EncontroAtividadeInscricao encontroAtividadeInscricao) {
		limpaCamposInscricao();
		encontroAtividadeEditada = encontroAtividade;
		if(encontroAtividadeInscricao!=null){
			encontroInscricaoEditada = encontroAtividadeInscricao.getEncontroInscricao();
		}
		if(encontroAtividade!=null){
			participanteHTMLPanel.setVisible(true);
			entidadeEditadaTituloLabel.setText("Atividade:");
			entidadeEditadaLabel.setText(encontroAtividade.getAtividade().getNome());
			if(encontroAtividadeInscricao != null){
				adicionarInscricaoButton.setVisible(false);
				excluirTodasInscricaoButton.setVisible(false);
				encontroAtividadeInscricaoEditada = new EncontroAtividadeInscricao();
				encontroAtividadeInscricaoEditada.setEncontroAtividade(encontroAtividade);
				participantesFlowPanel.setVisible(false);
				addInscricaoListBox.addItem("Este participante", "1");
				excluirInscricaoButton.setVisible(true);
				preencheAutomaticoInscricaoButton.setVisible(false);
				excluirTodasInscricaoButton.setVisible(false);
			} else {
				preencheAutomaticoInscricaoButton.setVisible(true);
				excluirTodasInscricaoButton.setVisible(true);
				adicionarInscricaoButton.setVisible(true);
				excluirTodasInscricaoButton.setVisible(true);
				encontroAtividadeInscricaoEditada = encontroAtividadeInscricao;
				addInscricaoListBox.addItem("Este participante", "1");
				addInscricaoListBox.addItem("Todos os participantes", "2");
				addInscricaoListBox.addItem("Padrinhos", "3");
				addInscricaoListBox.addItem("Apoios", "4");
				for (AgrupamentoVO agrupamentoVO : presenter.getEncontroVO().getListaAgrupamentoVOEncontro()) {
					if ( agrupamentoVO.getAgrupamento().getTipo().equals(TipoInscricaoCasalEnum.ENCONTRISTA))
						addInscricaoListBox.addItem(agrupamentoVO.getAgrupamento().getNome(), agrupamentoVO.getAgrupamento().getNome());
				}
				participantesFlowPanel.setVisible(true);
			}
			defineCamposInscricao(encontroAtividadeInscricao);
			encontroInscricaoFlexTable.setVisible(true);
			encontroInscricaoAtividadeFlexTable.setVisible(false);
		} else {
			adicionarInscricaoButton.setVisible(true);
			excluirTodasInscricaoButton.setVisible(false);
			preencheAutomaticoInscricaoButton.setVisible(false);
			atividadeHTMLPanel.setVisible(true);
			participantesFlowPanel.setVisible(true);
			entidadeEditadaTituloLabel.setText("Participante:");
			if(encontroAtividadeInscricao.getEncontroInscricao().getCasal()!=null){
				entidadeEditadaLabel.setText(encontroAtividadeInscricao.getEncontroInscricao().getCasal().getApelidos("e"));
			} else {
				entidadeEditadaLabel.setText(encontroAtividadeInscricao.getEncontroInscricao().getPessoa().getApelido());
			}
			addInscricaoListBox.addItem("Esta atividade", "1");
			encontroInscricaoFlexTable.setVisible(false);
			encontroInscricaoAtividadeFlexTable.setVisible(true);
		}
		boolean bCoordenador = presenter.isCoordenador();
		if(!bCoordenador){
			participanteHTMLPanel.setVisible(false);
			atividadeHTMLPanel.setVisible(false);
			papelHTMLPanel.setVisible(false);
			excluirInscricaoButton.setVisible(false);
			salvarInscricaoButton.setVisible(false);
			preencheAutomaticoInscricaoButton.setVisible(false);
			excluirTodasInscricaoButton.setVisible(false);

		}
		editaInscricaoDialogBox.center();
		editaInscricaoDialogBox.show();
		inscricaoListBox.setFocus(true);
	}

	public void limpaCamposInscricao(){
		addInscricaoListBox.clear();
		atividadeHTMLPanel.setVisible(false);
		participanteHTMLPanel.setVisible(false);
		excluirInscricaoButton.setVisible(false);
		participantesFlowPanel.setVisible(false);
		preencheAutomaticoInscricaoButton.setVisible(false);
		papelHTMLPanel.setVisible(true);
		salvarInscricaoButton.setVisible(true);
		encontroInscricaoTableUtil.clearData();
		itemTotal.setText(null);
	}

	public void defineCamposInscricao(EncontroAtividadeInscricao encontroAtividadeInscricao){
		encontroAtividadeInscricaoEditada = encontroAtividadeInscricao;
		if(encontroAtividadeInscricao!=null){
			if(encontroAtividadeInscricao!=null && encontroAtividadeInscricao.getPapel()!=null){
				ListBoxUtil.setItemSelected(papelListBox, encontroAtividadeInscricao.getPapel().getNome());
			}
			if(encontroAtividadeInscricao!=null && encontroAtividadeInscricao.getEncontroInscricao()!=null){
				ListBoxUtil.setItemSelected(inscricaoListBox, encontroAtividadeInscricao.getEncontroInscricao().toString());
			}
			populaAtividadesPorParticipante();
		}else{
			populaParticipantesPorAtividade();
		}
	}

	@Override
	public void populaPapel(List<Papel> listaPapel) {
		setListaPapel(listaPapel);
		ListBoxUtil.populate(papelListBox, false, listaPapel);
	}

	@UiHandler("preencheAutomaticoInscricaoButton")
	public void preencheAutomaticoInscricaoButtonClickHandler(ClickEvent event){
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
		preencheAtividade(encontroAtividadeEditada, papelPadrao, papelPadrinho);
		populaParticipantesPorAtividade();
	}

	@UiHandler("adicionarInscricaoButton")
	public void adicionarInscricaoButtonClickHandler(ClickEvent event){
		String opcao = addInscricaoListBox.getValue(addInscricaoListBox.getSelectedIndex());
		if(encontroInscricaoFlexTable.isVisible()){
			if(opcao.equals("1")){
				adicionaParticipante((Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel()),
							(EncontroInscricao)ListBoxUtil.getItemSelected(inscricaoListBox, presenter.getEncontroVO().getListaInscricao()),
							 encontroAtividadeEditada,true);
			} else if(opcao.equals("2")){
				for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
					adicionaParticipante((Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel()),
							ei, encontroAtividadeEditada,false);
				}
			} else if(opcao.equals("3")){
				for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
					if(ei.getTipo().equals(TipoInscricaoEnum.PADRINHO)){
						adicionaParticipante((Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel()),
								ei, encontroAtividadeEditada,false);
					}
				}
			} else if(opcao.equals("4")){
				for (EncontroInscricao ei : presenter.getEncontroVO().getListaInscricao()) {
					if(ei.getTipo().equals(TipoInscricaoEnum.APOIO)){
						adicionaParticipante((Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel()),
								ei, encontroAtividadeEditada,false);
					}
				}
			} else {
				for (AgrupamentoVO agrupamentoVO : presenter.getEncontroVO().getListaAgrupamentoVOEncontro()) {
					if(agrupamentoVO.getAgrupamento().getNome().equals(opcao)){
						for (AgrupamentoMembro membro : agrupamentoVO.getListaMembros()) {
							EncontroInscricao encontroInscricao = getEncontroInscricao(membro);
							if (encontroInscricao!=null){
								Papel papel = membro.getPapel();

								if (papel == null)
									papel = (Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel());
								adicionaParticipante(papel,getEncontroInscricao(membro), encontroAtividadeEditada,false);
							}
						}
						break;
					}
				}
			}
			populaParticipantesPorAtividade();
		} else {
			if(opcao.equals("1")){
				adicionaParticipante((Papel)ListBoxUtil.getItemSelected(papelListBox, presenter.getGrupoEncontroVO().getListaPapel()),
							         encontroInscricaoEditada,
							         (EncontroAtividade)ListBoxUtil.getItemSelected(atividadeEditaListBox, presenter.getEncontroVO().getListaEncontroAtividade()),true);
			}
			populaAtividadesPorParticipante();
		}
	}

	@UiHandler("excluirTodasInscricaoButton")
	public void excluirTodasInscricaoButtonButtonClickHandler(ClickEvent event){
		listaParticipantesInscritos = new ArrayList<EncontroAtividadeInscricao>();
		populaParticipantesPorAtividade();
	}

	private void adicionaParticipante(Papel papel, EncontroInscricao ei, EncontroAtividade atividade, boolean mensagem){
		boolean achou = false;
		EncontroAtividadeInscricao eaiEncontrada = null;
		for (EncontroAtividadeInscricao eai : listaParticipantesInscritos) {
			if(encontroInscricaoFlexTable.isVisible()){
				if(eai.getEncontroInscricao().equals(ei)){
					achou = true;
					eaiEncontrada = eai;
					if (mensagem) Window.alert("Participante já adicionado para esta atividade\n" + eai.getEncontroInscricao().toString());
					break;
				}
			} else {
				if(eai.getEncontroAtividade().getId().equals(atividade.getId())){
					if (mensagem) Window.alert("Atividade já adicionada para este participante\n" + eai.getEncontroAtividade().getAtividade().toString());
					achou = true;
					eaiEncontrada = eai;
					break;
				}
			}
		}
		if(!achou){
			encontroAtividadeInscricaoEditada = new EncontroAtividadeInscricao();
			encontroAtividadeInscricaoEditada.setEncontroAtividade(atividade);
			encontroAtividadeInscricaoEditada.setPapel(papel);
			encontroAtividadeInscricaoEditada.setEncontroInscricao(ei);
			listaParticipantesInscritos.add(encontroAtividadeInscricaoEditada);
		}else{
			eaiEncontrada.setPapel(papel);
		}
	}

	public void populaParticipantesPorAtividade() {
		LabelTotalUtil.setTotal(itemTotal, listaParticipantesInscritos.size(), "participante", "participantes", "");
		encontroInscricaoTableUtil.clearData();
		int row = 0;
		for (final EncontroAtividadeInscricao atividadeParticipante: listaParticipantesInscritos) {
			Object dados[] = new Object[3];

			final Image excluirParticipante = new Image("images/delete.png");
			excluirParticipante.setStyleName("portal-ImageCursor");
			excluirParticipante.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					listaParticipantesInscritos.remove(atividadeParticipante);
					ListBoxUtil.setItemSelected(inscricaoListBox, atividadeParticipante.getEncontroInscricao().toString());
					ListBoxUtil.setItemSelected(papelListBox, atividadeParticipante.getPapel().toString());
					populaParticipantesPorAtividade();
				}
			});

			dados[0] = excluirParticipante;
			dados[1] = atividadeParticipante.getEncontroInscricao().toString();
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
			dados[2] = atividadeParticipante.getPapel().getNome();
			encontroInscricaoTableUtil.addRow(dados,row+1);
			row++;
		}
		encontroInscricaoTableUtil.applyDataRowStyles();
	}
	public void populaAtividadesPorParticipante() {
		LabelTotalUtil.setTotal(itemTotal, listaParticipantesInscritos.size(), "atividade", "atividades", "a");
		encontroInscricaoAtividadeTableUtil.clearData();
		int row = 0;
		for (final EncontroAtividadeInscricao ativdadeInscricao: listaParticipantesInscritos) {
			Object dados[] = new Object[7];

			final Image excluirAtividade = new Image("images/delete.png");
			excluirAtividade.setStyleName("portal-ImageCursor");
			excluirAtividade.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					listaParticipantesInscritos.remove(ativdadeInscricao);
					ListBoxUtil.setItemSelected(inscricaoListBox, ativdadeInscricao.getEncontroInscricao().toString());
					ListBoxUtil.setItemSelected(papelListBox, ativdadeInscricao.getPapel().toString());
					populaAtividadesPorParticipante();
				}
			});

			dados[0] = excluirAtividade;
			dados[1] = dfDia.format(ativdadeInscricao.getEncontroAtividade().getInicio());
			dados[2] = dfHora.format(ativdadeInscricao.getEncontroAtividade().getInicio());
			dados[3] = dfHora.format(ativdadeInscricao.getEncontroAtividade().getFim());
			dados[4] = ativdadeInscricao.getEncontroAtividade().getTipoAtividade().getNome();
			dados[5] = ativdadeInscricao.getEncontroAtividade().getAtividade().getNome();
			dados[6] = ativdadeInscricao.getPapel().getNome();
			encontroInscricaoAtividadeTableUtil.addRow(dados,row+1);
			row++;
		}
		encontroInscricaoAtividadeTableUtil.applyDataRowStyles();
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
		listaParticipantesInscritos = new ArrayList<EncontroAtividadeInscricao>();
		presenter.salvarInscricoes(null, null, listaParticipantesInscritos);
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
		listaParticipantesInscritos.addAll(presenter.getListaEncontroAtividadeInscricao());

		for (EncontroAtividade atividade : presenter.getEncontroVO().getListaEncontroAtividade()) {
			preencheAtividade(atividade, papelPadrao, papelPadrinho);
		}
		presenter.salvarInscricoes(null, null, listaParticipantesInscritos);
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
			if (atividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.PADRINHOS)){
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
			if (atividade.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.VARIAVEL)){
				List<AgrupamentoVO> agrupamentos = getListaAgrupamentosAtividade(atividade);
				for (AgrupamentoVO agrupamentoVO : agrupamentos) {
					for (AgrupamentoMembro menbro : agrupamentoVO.getListaMembros()) {
						EncontroInscricao inscricao = getEncontroInscricao(menbro);
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

	private EncontroInscricao getEncontroInscricao(AgrupamentoMembro menbro) {
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
		List<EncontroAtividadeInscricao> atividadeInscricao = listaParticipantesInscritos;
		for (EncontroAtividadeInscricao encontroAtividadeInscricao : atividadeInscricao) {
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
			if (ea.getQuantidade()==0){
				ea.getInfoErro().add("Sem participantes");
			}else{
				if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.VARIAVEL)){
					if (ea.getQuantidadeDesejada() != null && ea.getQuantidadeDesejada() > ea.getQuantidade() ){
						ea.getInfoAtencao().add("Faltam " + (ea.getQuantidadeDesejada()-ea.getQuantidade()) + " participantes");
					}
					if (ea.getQuantidadeDesejada() != null && ea.getQuantidadeDesejada() < ea.getQuantidade() ){
						ea.getInfoAtencao().add("Esta passando " + (ea.getQuantidade() - ea.getQuantidadeDesejada()) + " participantes");
					}
				}else if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.TODOS)){
					if (presenter.getEncontroVO().getQuantidadeInscricao() > ea.getQuantidade() ){
						ea.getInfoErro().add("Faltam " + (presenter.getEncontroVO().getQuantidadeInscricao() - ea.getQuantidade()) + " participantes");
					}
				}else if (ea.getTipoPreenchimento().equals(TipoPreenchimentoAtividadeEnum.PADRINHOS)){
					if (ea.getEncontro().getQuantidadeAfilhados() > ea.getQuantidade() ){
						ea.getInfoErro().add("Faltam " + (ea.getEncontro().getQuantidadeAfilhados() - ea.getQuantidade()) + " participantes");
					}
					if (ea.getEncontro().getQuantidadeAfilhados() < ea.getQuantidade() ){
						ea.getInfoAtencao().add("Esta passando " + (ea.getQuantidade()-ea.getEncontro().getQuantidadeAfilhados()) + " participantes");
					}
				}
			}
		}
	}

	public List<Papel> getListaPapel() {
		return listaPapel;
	}

	public void setListaPapel(List<Papel> listaPapel) {
		this.listaPapel = listaPapel;
	}

}