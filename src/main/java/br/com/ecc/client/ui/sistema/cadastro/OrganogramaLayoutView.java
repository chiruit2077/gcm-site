package br.com.ecc.client.ui.sistema.cadastro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableHelper;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.Organograma;
import br.com.ecc.model.OrganogramaArea;
import br.com.ecc.model.OrganogramaCoordenacao;
import br.com.ecc.model.Papel;
import br.com.ecc.model.tipo.TipoAtividadeEnum;
import br.com.ecc.model.vo.OrganogramaVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OrganogramaLayoutView extends BaseView<OrganogramaLayoutPresenter> implements OrganogramaLayoutPresenter.Display {

	@UiTemplate("OrganogramaLayoutView.ui.xml")
	interface OrganogramaLayoutViewUiBinder extends UiBinder<Widget, OrganogramaLayoutView> {}
	private OrganogramaLayoutViewUiBinder uiBinder = GWT.create(OrganogramaLayoutViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField FlexTable distribuicaoPanel;
	@UiField ListBox organogramaListBox;
	@UiField DialogBox editaDialogBox;
	@UiField DialogBox editaGruposDialogBox;
	@UiField DialogBox editaGrupoDialogBox;
	@UiField Label valorLabel;
	@UiField TextBox valorTextBox;
	@UiField NumberTextBox linhaTextBox;
	@UiField NumberTextBox linhaSpamTextBox;
	@UiField NumberTextBox colunaTextBox;
	@UiField NumberTextBox colunaSpamTextBox;
	@UiField NumberTextBox linhaCoordenacaoTextBox;
	@UiField NumberTextBox linhaSpamCoordenacaoTextBox;
	@UiField NumberTextBox colunaCoordenacaoTextBox;
	@UiField NumberTextBox colunaSpamCoordenacaoTextBox;
	@UiField Button salvarButton;
	@UiField Button addAreaButton;
	@UiField Button salvarAreaButton;
	@UiField Button fecharAreaButton;
	@UiField ListBox atividadeListBox;
	@UiField ListBox tipoAtividadeListBox;
	@UiField ListBox papelListBox;
	@UiField HTMLPanel areaDadosPanel;
	@UiField HTMLPanel atividadeDadosPanel;

	private Organograma organogramaSelecionado;
	private OrganogramaArea entidadeAreaEditada;
	private OrganogramaArea organogramaAreaSelecionado;
	private OrganogramaCoordenacao entidadeCoordenacaoEditada;

	private List<Organograma> listaOrganogramas;

	protected HorizontalPanel areaPanelEditado;
	protected VerticalPanel coordenacaoAreaWidgetEditado;
	protected VerticalPanel coordenacaoWidgetEditado;

	private List<Papel> listaPapeis;
	private List<Atividade> listaAtividades;

	public OrganogramaLayoutView() {
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		ListBoxUtil.populate(tipoAtividadeListBox, true, TipoAtividadeEnum.values());
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		presenter.salvar();
	}

	@UiHandler("fecharAreaButton")
	public void fecharAreaButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}

	@UiHandler("excluirAreaButton")
	public void excluirAreaButtonClickHandler(ClickEvent event){
		if (entidadeAreaEditada!=null ){
			presenter.getVo().getListaAreas().remove(entidadeAreaEditada);
		}else if (entidadeCoordenacaoEditada!=null ){
			presenter.getVo().getListaCoordenacoes().remove(entidadeCoordenacaoEditada);
		}
		populaEntidades(presenter.getVo());
		editaDialogBox.hide();
	}

	@UiHandler("addAreaButton")
	public void addAreaButtonClickHandler(ClickEvent event){
		limpaCampos();
		areaPanelEditado = null;
		defineCampos(new OrganogramaArea());
		editaDialogBox.center();
		editaDialogBox.show();
	}

	@UiHandler("salvarAreaButton")
	public void salvarAreaButtonClickHandler(ClickEvent event){
		if (entidadeAreaEditada!=null){
			if(valorTextBox.getValue()==null || valorTextBox.getValue().equals("")){
				Window.alert("Informe o Nome da Area");
			}
			if(linhaCoordenacaoTextBox.getValue()==null || linhaCoordenacaoTextBox.getValue().equals("")){
				Window.alert("Informe a linha Coordenação");
				return;
			}
			if(colunaCoordenacaoTextBox.getValue()==null || colunaCoordenacaoTextBox.getValue().equals("")){
				Window.alert("Informe a coluna Coordenação");
				return;
			}
		}
		if (entidadeCoordenacaoEditada!=null){
			if(valorTextBox.getValue()==null || valorTextBox.getValue().equals("")){
				Window.alert("Informe o Descrição da Coordenação");
			}
		}
		if(linhaTextBox.getValue()==null || linhaTextBox.getValue().equals("")){
			Window.alert("Informe a linha");
			return;
		}
		if(colunaTextBox.getValue()==null || colunaTextBox.getValue().equals("")){
			Window.alert("Informe a coluna");
			return;
		}
		if (entidadeAreaEditada!=null){
			entidadeAreaEditada.setOrganograma(getOrganogramaSelecionado());
			entidadeAreaEditada.setNome(valorTextBox.getValue());
			entidadeAreaEditada.setLinha(Integer.valueOf(linhaTextBox.getValue()));
			entidadeAreaEditada.setColuna(Integer.valueOf(colunaTextBox.getValue()));
			entidadeAreaEditada.setLinhaSpam(null);
			if (linhaSpamTextBox.getValue()!= null && !linhaSpamTextBox.getValue().equals("")){
				entidadeAreaEditada.setLinhaSpam(Integer.valueOf(linhaSpamTextBox.getValue()));
			}
			entidadeAreaEditada.setColunaSpam(null);
			if (colunaSpamTextBox.getValue()!= null && !colunaSpamTextBox.getValue().equals("")){
				entidadeAreaEditada.setColunaSpam(Integer.valueOf(colunaSpamTextBox.getValue()));
			}
			entidadeAreaEditada.setLinhaCoordenacao(Integer.valueOf(linhaCoordenacaoTextBox.getValue()));
			entidadeAreaEditada.setColunaCoordenacao(Integer.valueOf(colunaCoordenacaoTextBox.getValue()));
			entidadeAreaEditada.setLinhaSpamCoordenacao(null);
			if (linhaSpamCoordenacaoTextBox.getValue()!= null && !linhaSpamCoordenacaoTextBox.getValue().equals("")){
				entidadeAreaEditada.setLinhaSpamCoordenacao(Integer.valueOf(linhaSpamCoordenacaoTextBox.getValue()));
			}
			entidadeAreaEditada.setColunaSpamCoordenacao(null);
			if (colunaSpamCoordenacaoTextBox.getValue()!= null && !colunaSpamCoordenacaoTextBox.getValue().equals("")){
				entidadeAreaEditada.setColunaSpamCoordenacao(Integer.valueOf(colunaSpamCoordenacaoTextBox.getValue()));
			}
			entidadeAreaEditada.setPapel((Papel) ListBoxUtil.getItemSelected(papelListBox, getListaPapeis()));
			entidadeAreaEditada.setTipoAtividade((TipoAtividadeEnum) ListBoxUtil.getItemSelected(tipoAtividadeListBox, TipoAtividadeEnum.values()));
			if (areaPanelEditado == null){
				presenter.getVo().getListaAreas().add(entidadeAreaEditada);
			}
			populaEntidades(presenter.getVo());
			editaDialogBox.hide();
		}else if (entidadeCoordenacaoEditada!=null){
			entidadeCoordenacaoEditada.setOrganogramaArea(getOrganogramaAreaSelecionado());
			entidadeCoordenacaoEditada.setDescricao(valorTextBox.getValue());
			entidadeCoordenacaoEditada.setLinha(Integer.valueOf(linhaTextBox.getValue()));
			entidadeCoordenacaoEditada.setColuna(Integer.valueOf(colunaTextBox.getValue()));
			entidadeCoordenacaoEditada.setLinhaSpam(null);
			if (linhaSpamTextBox.getValue()!= null && !linhaSpamTextBox.getValue().equals("")){
				entidadeCoordenacaoEditada.setLinhaSpam(Integer.valueOf(linhaSpamTextBox.getValue()));
			}
			entidadeCoordenacaoEditada.setColunaSpam(null);
			if (colunaSpamTextBox.getValue()!= null && !colunaSpamTextBox.getValue().equals("")){
				entidadeCoordenacaoEditada.setColunaSpam(Integer.valueOf(colunaSpamTextBox.getValue()));
			}
			entidadeCoordenacaoEditada.setPapel((Papel) ListBoxUtil.getItemSelected(papelListBox, getListaPapeis()));
			entidadeCoordenacaoEditada.setTipoAtividade((TipoAtividadeEnum) ListBoxUtil.getItemSelected(tipoAtividadeListBox, TipoAtividadeEnum.values()));
			entidadeCoordenacaoEditada.setAtividade((Atividade) ListBoxUtil.getItemSelected(atividadeListBox, getListaAtividades()));
			if (areaPanelEditado == null){
				presenter.getVo().getListaCoordenacoes().add(entidadeCoordenacaoEditada);
			}
			populaEntidades(presenter.getVo());
			editaDialogBox.hide();
		}

	}

	private void edita(OrganogramaArea organogramaArea) {
		limpaCampos();
		defineCampos(organogramaArea);
		editaDialogBox.setText("Dados da Coordenação Área");
		editaDialogBox.center();
		editaDialogBox.show();
	}

	private void edita(OrganogramaCoordenacao organogramaCoordenacao) {
		limpaCampos();
		defineCampos(organogramaCoordenacao);
		editaDialogBox.setText("Dados da Coordenação");
		editaDialogBox.center();
		editaDialogBox.show();
	}

	@UiHandler("organogramaListBox")
	public void OrganogramaListBoxListBoxChangeHandler(ChangeEvent event) {
		Organograma Organograma = (Organograma) ListBoxUtil.getItemSelected(organogramaListBox, getListaOrganogramas());
		setOrganogramaSelecionado(Organograma);
		presenter.setOrganogramaSelecionado(Organograma);
		presenter.buscaVO();
	}

	public void defineCampos(OrganogramaArea organogramaArea){
		areaDadosPanel.setVisible(true);
		atividadeDadosPanel.setVisible(false);
		valorLabel.setText("Nome:");
		entidadeAreaEditada = organogramaArea;
		valorTextBox.setText(organogramaArea.getNome());
		if (organogramaArea.getLinha() != null)
			linhaTextBox.setText(organogramaArea.getLinha().toString());
		if (organogramaArea.getLinhaSpam() != null)
			linhaSpamTextBox.setText(organogramaArea.getLinhaSpam().toString());
		if (organogramaArea.getColuna() != null)
			colunaTextBox.setText(organogramaArea.getColuna().toString());
		if (organogramaArea.getColunaSpam() != null )
			colunaSpamTextBox.setText(organogramaArea.getColunaSpam().toString());
		if (organogramaArea.getLinhaCoordenacao()!= null)
			linhaCoordenacaoTextBox.setText(organogramaArea.getLinhaCoordenacao().toString());
		if (organogramaArea.getLinhaSpamCoordenacao() != null)
			linhaSpamCoordenacaoTextBox.setText(organogramaArea.getLinhaSpamCoordenacao().toString());
		if (organogramaArea.getColunaCoordenacao() != null)
			colunaCoordenacaoTextBox.setText(organogramaArea.getColunaCoordenacao().toString());
		if (organogramaArea.getColunaSpamCoordenacao() != null )
			colunaSpamCoordenacaoTextBox.setText(organogramaArea.getColunaSpamCoordenacao().toString());
		if (organogramaArea.getPapel() != null )
			ListBoxUtil.setItemSelected(papelListBox, organogramaArea.getPapel().toString());
		if (organogramaArea.getTipoAtividade() != null )
			ListBoxUtil.setItemSelected(tipoAtividadeListBox, organogramaArea.getTipoAtividade().getNome());
	}

	public void defineCampos(OrganogramaCoordenacao organogramaCoordenacao){
		areaDadosPanel.setVisible(false);
		atividadeDadosPanel.setVisible(true);
		valorLabel.setText("Descrição:");
		entidadeCoordenacaoEditada = organogramaCoordenacao;
		valorTextBox.setText(organogramaCoordenacao.getDescricao());
		if (organogramaCoordenacao.getLinha() != null)
			linhaTextBox.setText(organogramaCoordenacao.getLinha().toString());
		if (organogramaCoordenacao.getLinhaSpam() != null)
			linhaSpamTextBox.setText(organogramaCoordenacao.getLinhaSpam().toString());
		if (organogramaCoordenacao.getColuna() != null)
			colunaTextBox.setText(organogramaCoordenacao.getColuna().toString());
		if (organogramaCoordenacao.getColunaSpam() != null )
			colunaSpamTextBox.setText(organogramaCoordenacao.getColunaSpam().toString());
		if (organogramaCoordenacao.getPapel() != null )
			ListBoxUtil.setItemSelected(papelListBox, organogramaCoordenacao.getPapel().toString());
		if (organogramaCoordenacao.getAtividade() != null )
			ListBoxUtil.setItemSelected(atividadeListBox, organogramaCoordenacao.getAtividade().toString());
		if (organogramaCoordenacao.getTipoAtividade() != null )
			ListBoxUtil.setItemSelected(tipoAtividadeListBox, organogramaCoordenacao.getTipoAtividade().getNome());
	}

	public void limpaCampos(){
		entidadeAreaEditada = null;
		entidadeCoordenacaoEditada = null;
		areaDadosPanel.setVisible(false);
		atividadeDadosPanel.setVisible(false);
		valorLabel.setText(null);
		valorTextBox.setText(null);
		linhaTextBox.setText(null);
		linhaSpamTextBox.setText(null);
		colunaTextBox.setText(null);
		colunaSpamTextBox.setText(null);
		linhaCoordenacaoTextBox.setText(null);
		linhaSpamCoordenacaoTextBox.setText(null);
		colunaCoordenacaoTextBox.setText(null);
		colunaSpamCoordenacaoTextBox.setText(null);
		papelListBox.setSelectedIndex(-1);
		tipoAtividadeListBox.setSelectedIndex(-1);
		atividadeListBox.setSelectedIndex(-1);
	}

	@Override
	public String getDisplayTitle() {
		return "Layout do Organograma";
	}

	@Override
	public void reset() {
		distribuicaoPanel.removeAllRows();
		distribuicaoPanel.clear();
	}

	@Override
	public void populaEntidades(OrganogramaVO vo) {
		distribuicaoPanel.removeAllRows();
		distribuicaoPanel.clear();
		distribuicaoPanel.clear(true);
		distribuicaoPanel.setCellSpacing(10);
		distribuicaoPanel.setStyleName("organograma");

		if (vo.getListaAreas().size()>0){
			for (final OrganogramaArea area : vo.getListaAreas()) {
				VerticalPanel areaPanel = new VerticalPanel();
				areaPanel.setSize("100%", "100%");
				areaPanel.setStyleName("organograma");
				HorizontalPanel tituloAgrupamento = new HorizontalPanel();
				tituloAgrupamento.setSize("100%", "20px");
				tituloAgrupamento.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				tituloAgrupamento.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				tituloAgrupamento.setStyleName("organograma-AreaTitulo");
				Label label = new Label(area.getNome());
				label.setWidth("100%");
				tituloAgrupamento.add(label);
				Button addCoordenacao = new Button();
				addCoordenacao.setText("Adiciona Coordenação");
				addCoordenacao.setWidth("200px");
				addCoordenacao.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						setOrganogramaAreaSelecionado(area);
						limpaCampos();
						areaPanelEditado = null;
						defineCampos(new OrganogramaCoordenacao());
						editaDialogBox.center();
						editaDialogBox.show();
					}
				});
				tituloAgrupamento.add(addCoordenacao);
				areaPanel.add(tituloAgrupamento);
				areaPanel.add(geraCoordenacoes(tituloAgrupamento,area));

				if (area.getColunaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setColSpan(area.getLinha(), area.getColuna(),area.getColunaSpam());
				}
				if (area.getLinhaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setRowSpan(area.getLinha(), area.getColuna(),area.getLinhaSpam());
					distribuicaoPanel.getFlexCellFormatter().setHeight(area.getLinha(), area.getColuna(), "100%");
				}
				distribuicaoPanel.getFlexCellFormatter().setAlignment(area.getLinha(),area.getColuna(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
				distribuicaoPanel.setWidget(area.getLinha(), area.getColuna(), areaPanel);
			}

		}

		FlexTableHelper.fixRowSpan(distribuicaoPanel);
		showWaitMessage(false);
	}



	private Widget geraCoordenacoes(HorizontalPanel areaPanel, OrganogramaArea area) {
		ArrayList<OrganogramaCoordenacao> coordenacoes = new ArrayList<OrganogramaCoordenacao>();
		for (OrganogramaCoordenacao coordenacao : presenter.getVo().getListaCoordenacoes()) {
			if (coordenacao.getOrganogramaArea().equals(area))
				coordenacoes.add(coordenacao);
		}

		FlexTable coordenacoesPanel = new FlexTable();
		coordenacoesPanel.setCellSpacing(5);
		coordenacoesPanel.setSize("100%", "100%");
		final FocusPanel focusPanelArea = geraWigetCoordenacaoArea(areaPanel, area);
		coordenacoesPanel.getFlexCellFormatter().setAlignment(area.getLinhaCoordenacao(),area.getColunaCoordenacao(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
		coordenacoesPanel.setWidget(area.getLinhaCoordenacao(), area.getColunaCoordenacao(), focusPanelArea);
		if (area.getColunaSpamCoordenacao()!=null){
			coordenacoesPanel.getFlexCellFormatter().setColSpan(area.getLinhaCoordenacao(), area.getColunaCoordenacao(),area.getColunaSpamCoordenacao());
		}
		if (area.getLinhaSpamCoordenacao()!=null){
			coordenacoesPanel.getFlexCellFormatter().setRowSpan(area.getLinhaCoordenacao(), area.getColunaCoordenacao(),area.getLinhaSpamCoordenacao());
		}
		coordenacoesPanel.setWidget(area.getLinhaCoordenacao(), area.getColunaCoordenacao(), focusPanelArea);
		for (OrganogramaCoordenacao  coordenacao : coordenacoes) {
			coordenacoesPanel.getFlexCellFormatter().setAlignment(coordenacao.getLinha(),coordenacao.getColuna(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
			final FocusPanel focusPanel = geraWigetCoordenacao(coordenacao,area);
			if (coordenacao.getColunaSpam()!=null){
				coordenacoesPanel.getFlexCellFormatter().setColSpan(coordenacao.getLinha(), coordenacao.getColuna(), coordenacao.getColunaSpam());
			}
			if (coordenacao.getLinhaSpam()!=null){
				coordenacoesPanel.getFlexCellFormatter().setRowSpan(coordenacao.getLinha(), coordenacao.getColuna(), coordenacao.getLinhaSpam());
			}
			coordenacoesPanel.setWidget(coordenacao.getLinha(), coordenacao.getColuna(), focusPanel);
		}
		FlexTableHelper.fixRowSpan(coordenacoesPanel);
		return coordenacoesPanel;
	}

	private FocusPanel geraWigetCoordenacao(final OrganogramaCoordenacao coordenacao, final OrganogramaArea area) {
		final FocusPanel focusPanel = new FocusPanel();
		focusPanel.setSize("140px", "90px");
		VerticalPanel coordenacaoWidget = new VerticalPanel();
		focusPanel.add(coordenacaoWidget);
		coordenacaoWidget.setSize("140px", "90px");
		coordenacaoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		coordenacaoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		coordenacaoWidget.setStyleName("organograma-Coordenacao");

		HorizontalPanel tituloCoordenacaoWidget = new HorizontalPanel();
		tituloCoordenacaoWidget.setSize("140px", "20px");
		if(coordenacao.getTipoAtividade()!=null){
			if (coordenacao.getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE))
				tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloBlueLayout");
			else
				tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloRedLayout");
		}else
			tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloBlackLayout");
		tituloCoordenacaoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		tituloCoordenacaoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		tituloCoordenacaoWidget.add(new Label(coordenacao.getDescricao()));

		final VerticalPanel coordenacaoNomeWidget = new VerticalPanel();
		coordenacaoNomeWidget.setSize("140px", "70px");
		coordenacaoNomeWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		coordenacaoNomeWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		/*if (coordenacao.getAtividade()!=null)
			coordenacaoNomeWidget.add(new Label("Atividade: " + coordenacao.getAtividade().getNome()));*/
		if (coordenacao.getPapel()!=null)
			coordenacaoNomeWidget.add(new Label("Papel: " + coordenacao.getPapel().getNome()));
		if (coordenacao.getTipoAtividade()!=null)
			coordenacaoNomeWidget.add(new Label("Tipo: " + coordenacao.getTipoAtividade().getNome()));
		coordenacaoWidget.add(tituloCoordenacaoWidget);
		coordenacaoWidget.add(coordenacaoNomeWidget);
		focusPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				organogramaAreaSelecionado = area;
				edita(coordenacao);
				coordenacaoWidgetEditado = (VerticalPanel) ((VerticalPanel) focusPanel.getWidget()).getWidget((((VerticalPanel) focusPanel.getWidget()).getWidgetIndex(coordenacaoNomeWidget)));
			}
		});
		return focusPanel;
	}

	private FocusPanel geraWigetCoordenacaoArea(final HorizontalPanel areaPanel, final OrganogramaArea area) {
		final FocusPanel focusPanel = new FocusPanel();
		focusPanel.setSize("160px", "80px");
		VerticalPanel coordenacaoWidget = new VerticalPanel();
		focusPanel.add(coordenacaoWidget);
		coordenacaoWidget.setSize("160px", "80px");
		coordenacaoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

		final VerticalPanel coordenacaoNomeWidget = new VerticalPanel();
		coordenacaoWidget.setStyleName("organograma-CoordenacaoArea");
		coordenacaoNomeWidget.setSize("160px", "100%");
		coordenacaoNomeWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		coordenacaoNomeWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		if (area.getPapel()!=null)
			coordenacaoNomeWidget.add(new Label(area.getPapel().toString()));
		if (area.getTipoAtividade()!=null)
			coordenacaoNomeWidget.add(new Label("Tipo: " + area.getTipoAtividade().getNome()));
		coordenacaoWidget.add(coordenacaoNomeWidget);
		focusPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				edita(area);
				areaPanelEditado = areaPanel;
				coordenacaoAreaWidgetEditado = (VerticalPanel) ((VerticalPanel) focusPanel.getWidget()).getWidget((((VerticalPanel) focusPanel.getWidget()).getWidgetIndex(coordenacaoNomeWidget)));
			}
		});
		return focusPanel;
	}

	public Organograma getOrganogramaSelecionado() {
		return organogramaSelecionado;
	}

	@Override
	public void setOrganogramaSelecionado(Organograma organogramaSelecionado) {
		this.organogramaSelecionado = organogramaSelecionado;
		ListBoxUtil.setItemSelected(organogramaListBox, organogramaSelecionado.toString());

	}

	@Override
	public void setListaOrganogramas(List<Organograma> lista) {
		this.listaOrganogramas = lista;
		ListBoxUtil.populate(organogramaListBox, false, lista );
	}

	public List<Organograma> getListaOrganogramas() {
		return listaOrganogramas;
	}

	@Override
	public void populaPapeis(List<Papel> lista) {
		setListaPapeis(lista);
		ListBoxUtil.populate(papelListBox, true, lista);
	}

	public List<Papel> getListaPapeis() {
		return listaPapeis;
	}

	public void setListaPapeis(List<Papel> listaPapeis) {
		this.listaPapeis = listaPapeis;
	}

	public List<Atividade> getListaAtividades() {
		return listaAtividades;
	}

	@Override
	public void populaAtividades(List<Atividade> lista) {
		setListaAtividades(lista);
		ListBoxUtil.populate(atividadeListBox, true, lista);
	}


	public void setListaAtividades(List<Atividade> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}

	public OrganogramaArea getOrganogramaAreaSelecionado() {
		return organogramaAreaSelecionado;
	}

	public void setOrganogramaAreaSelecionado(OrganogramaArea organogramaAreaSelecionado) {
		this.organogramaAreaSelecionado = organogramaAreaSelecionado;
	}

}