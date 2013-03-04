package br.com.ecc.client.ui.sistema.encontro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.component.FlexTableHelper;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.EncontroOrganogramaArea;
import br.com.ecc.model.EncontroOrganogramaCoordenacao;
import br.com.ecc.model.OrganogramaArea;
import br.com.ecc.model.OrganogramaCoordenacao;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoAtividadeEnum;
import br.com.ecc.model.vo.EncontroOrganogramaVO;
import br.com.freller.tool.client.Print;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DistribuicaoOrganogramaView extends BaseView<DistribuicaoOrganogramaPresenter> implements DistribuicaoOrganogramaPresenter.Display {

	@UiTemplate("DistribuicaoOrganogramaView.ui.xml")
	interface DistribucaoViewUiBinder extends UiBinder<Widget, DistribuicaoOrganogramaView> {}
	private DistribucaoViewUiBinder uiBinder = GWT.create(DistribucaoViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField VerticalPanel centralPanel;
	@UiField FlexTable distribuicaoPanel;
	@UiField ListBox organogramaListBox;
	@UiField Label totalLabel;
	@UiField DialogBox editaDialogBox;
	@UiField(provided = true) SuggestBox inscricaoSuggestBox1;
	private final GenericEntitySuggestOracle inscricaoSuggest1 = new GenericEntitySuggestOracle();
	@UiField(provided = true) SuggestBox inscricaoSuggestBox2;
	private final GenericEntitySuggestOracle inscricaoSuggest2 = new GenericEntitySuggestOracle();
	@UiField Button salvarButton;
	@UiField Button salvarCoordenacaoButton;
	@UiField Button fecharCoordenacaoButton;
	@UiField Button printButton;
	@UiField(provided = true) RadioButton casalRadio;
	@UiField(provided = true) RadioButton pessoaRadio;

	private List<EncontroOrganograma> listaOrganograma;

	private boolean editaArea = false;
	private EncontroOrganogramaCoordenacao entidadeEditadaCoordenacao;
	private EncontroOrganogramaArea entidadeEditadaArea;

	protected VerticalPanel coordenacaoWidgetEditado;
	protected VerticalPanel coordenacaoAreaWidgetEditado;

	public DistribuicaoOrganogramaView() {
		inscricaoSuggest1.setMinimoCaracteres(2);
		inscricaoSuggest2.setMinimoCaracteres(2);

		inscricaoSuggestBox1 = new SuggestBox(inscricaoSuggest1);
		inscricaoSuggestBox2 = new SuggestBox(inscricaoSuggest2);

		casalRadio = new RadioButton("tipo", "Por Casal");
		pessoaRadio = new RadioButton("tipo", "Por Pessoa");

		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		presenter.salvar();
	}

	@UiHandler("printButton")
	public void printButtonClickHandler(ClickEvent event){
		//Print.it("","<link rel=styleSheet type=text/css media=paper href=/paperStyle.css>",distribuicaoPanel.getElement());
		Print.it("","<link rel=styleSheet type=text/css media=print href=/ECCWeb.css>",distribuicaoPanel.getElement());
	}

	@UiHandler("fecharCoordenacaoButton")
	public void fecharCoordenacaoButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}

	@UiHandler("casalRadio")
	public void casalRadioClickHandler(ClickEvent event){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest2.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
	}

	@UiHandler("pessoaRadio")
	public void pessoaRadioClickHandler(ClickEvent event){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest2.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
	}

	@UiHandler("salvarCoordenacaoButton")
	public void salvarCoordenacaoButtonClickHandler(ClickEvent event){
		if (editaArea){
			entidadeEditadaArea.setEncontroOrganograma(presenter.getEncontroOrganogramaSelecionado());

			entidadeEditadaArea.setEncontroInscricao1(null);
			entidadeEditadaArea.setEncontroInscricao2(null);

			if(!inscricaoSuggestBox1.getValue().equals("")){
				entidadeEditadaArea.setEncontroInscricao1((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue()));
			}
			if(!inscricaoSuggestBox2.getValue().equals("")){
				entidadeEditadaArea.setEncontroInscricao2((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest2.getListaEntidades(), inscricaoSuggestBox2.getValue()));
			}
			coordenacaoAreaWidgetEditado.clear();
			if (entidadeEditadaArea.getEncontroInscricao1()!= null){
				coordenacaoAreaWidgetEditado.add(new Label(entidadeEditadaArea.getEncontroInscricao1().toStringApelidos()));
				if (entidadeEditadaArea.getEncontroInscricao2()!= null){
					coordenacaoAreaWidgetEditado.add(new Label(entidadeEditadaArea.getEncontroInscricao2().toStringApelidos()));
				}
			}else{
				coordenacaoAreaWidgetEditado.add(new Label("VAGO"));
			}

		}else{
			entidadeEditadaCoordenacao.setEncontroOrganograma(presenter.getEncontroOrganogramaSelecionado());

			entidadeEditadaCoordenacao.setEncontroInscricao1(null);
			entidadeEditadaCoordenacao.setEncontroInscricao2(null);

			if(!inscricaoSuggestBox1.getValue().equals("")){
				entidadeEditadaCoordenacao.setEncontroInscricao1((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue()));
			}
			if(!inscricaoSuggestBox2.getValue().equals("")){
				entidadeEditadaCoordenacao.setEncontroInscricao2((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest2.getListaEntidades(), inscricaoSuggestBox2.getValue()));
			}
			coordenacaoWidgetEditado.clear();
			if (entidadeEditadaCoordenacao.getEncontroInscricao1()!= null){
				coordenacaoWidgetEditado.add(new Label(entidadeEditadaCoordenacao.getEncontroInscricao1().toStringApelidos()));
				if (entidadeEditadaCoordenacao.getEncontroInscricao2()!= null){
					coordenacaoWidgetEditado.add(new Label(entidadeEditadaCoordenacao.getEncontroInscricao2().toStringApelidos()));
				}
				coordenacaoWidgetEditado.setVisible(true);
			}else{
				coordenacaoWidgetEditado.add(new Label("VAGO"));
				coordenacaoWidgetEditado.setVisible(false);
			}
		}

		editaDialogBox.hide();

	}
	private void editaCoordenacao(EncontroOrganogramaCoordenacao encontroOrganogramaCoordenacao) {
		editaArea=false;
		limpaCamposCoordenacao();
		defineCamposCoordenacao(encontroOrganogramaCoordenacao);
		editaDialogBox.center();
		editaDialogBox.show();
		inscricaoSuggestBox1.setFocus(true);
	}

	private void editaArea(EncontroOrganogramaArea encontroOrganogramaArea) {
		editaArea=true;
		limpaCamposCoordenacao();
		defineCamposArea(encontroOrganogramaArea);
		editaDialogBox.center();
		editaDialogBox.show();
		inscricaoSuggestBox1.setFocus(true);
	}

	public void defineCamposCoordenacao(EncontroOrganogramaCoordenacao encontroOrganogramaCoordenacao){
		entidadeEditadaCoordenacao = encontroOrganogramaCoordenacao;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest2.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");

		if(encontroOrganogramaCoordenacao.getEncontroInscricao1() != null){
			inscricaoSuggestBox1.setValue(encontroOrganogramaCoordenacao.getEncontroInscricao1().toString());
			inscricaoSuggest1.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest1.getListaEntidades().add(encontroOrganogramaCoordenacao.getEncontroInscricao1());
		}
		if(encontroOrganogramaCoordenacao.getEncontroInscricao2() != null){
			inscricaoSuggestBox2.setValue(encontroOrganogramaCoordenacao.getEncontroInscricao2().toString());
			inscricaoSuggest2.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest2.getListaEntidades().add(encontroOrganogramaCoordenacao.getEncontroInscricao2());
		}

	}

	public void defineCamposArea(EncontroOrganogramaArea encontroOrganogramaArea){
		entidadeEditadaArea= encontroOrganogramaArea;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest2.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");

		if(encontroOrganogramaArea.getEncontroInscricao1() != null){
			inscricaoSuggestBox1.setValue(encontroOrganogramaArea.getEncontroInscricao1().toString());
			inscricaoSuggest1.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest1.getListaEntidades().add(encontroOrganogramaArea.getEncontroInscricao1());
		}
		if(encontroOrganogramaArea.getEncontroInscricao2() != null){
			inscricaoSuggestBox2.setValue(encontroOrganogramaArea.getEncontroInscricao2().toString());
			inscricaoSuggest2.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest2.getListaEntidades().add(encontroOrganogramaArea.getEncontroInscricao2());
		}

	}

	@Override
	public void populaOrganogramas(List<EncontroOrganograma> lista) {
		this.setListaOrganograma(lista);
		organogramaListBox.clear();
		for(EncontroOrganograma encontroOrganograma : lista) {
			organogramaListBox.addItem(encontroOrganograma.toString(), encontroOrganograma.getId().toString());
		}
		organogramaListBox.setSelectedIndex(0);
		EncontroOrganograma encontroOrganograma = getEncontroOrganogramaId(Integer.valueOf(organogramaListBox.getValue(organogramaListBox.getSelectedIndex())));
		if (encontroOrganograma != null){
			presenter.setEncontroOrganogramaSelecionado(encontroOrganograma);
			presenter.buscaVO();
		}
	}

	@UiHandler("organogramaListBox")
	public void HotelListBoxChangeHandler(ChangeEvent event) {
		EncontroOrganograma encontroOrganograma = getEncontroOrganogramaId(Integer.valueOf(organogramaListBox.getValue(organogramaListBox.getSelectedIndex())));
		if (encontroOrganograma != null){
			presenter.setEncontroOrganogramaSelecionado(encontroOrganograma);
			presenter.buscaVO();
		}
	}

	private EncontroOrganograma getEncontroOrganogramaId(Integer value) {
		for (EncontroOrganograma encontroOrganograma : getListaOrganograma()) {
			if(encontroOrganograma.getId().equals(value))
				return encontroOrganograma;
		}
		return null;
	}

	public void limpaCamposCoordenacao(){
		casalRadio.setValue(true);
		inscricaoSuggestBox1.setValue(null);
		inscricaoSuggestBox2.setValue(null);
	}

	@Override
	public String getDisplayTitle() {
		return "Distribuição do Organograma";
	}

	@Override
	public void reset() {
		distribuicaoPanel.clear();
	}

	@Override
	public void populaEntidades(EncontroOrganogramaVO vo) {
		distribuicaoPanel.clear(true);
		distribuicaoPanel.setCellSpacing(5);

		if (vo.getListaOrganogramaArea().size() > 0) {
			for (OrganogramaArea area : vo.getListaOrganogramaArea()) {
				VerticalPanel areaPanel = new VerticalPanel();
				areaPanel.setSize("100%", "100%");
				areaPanel.setStyleName("organograma");
				HorizontalPanel tituloAgrupamento = new HorizontalPanel();
				tituloAgrupamento.setSize("100%", "20px");
				tituloAgrupamento.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				tituloAgrupamento.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				tituloAgrupamento.setStyleName("organograma-AreaTitulo");
				tituloAgrupamento.add(new Label(area.getNome()));
				areaPanel.add(tituloAgrupamento);
				areaPanel.add(geraCoordenacoes(vo,area));
				if (area.getColunaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setColSpan(area.getLinha(), area.getColuna(),area.getColunaSpam());
				}
				if (area.getLinhaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setRowSpan(area.getLinha(), area.getColuna(),area.getLinhaSpam());
				}
				distribuicaoPanel.getFlexCellFormatter().setAlignment(area.getLinha(),area.getColuna(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_TOP);
				distribuicaoPanel.setWidget(area.getLinha(), area.getColuna(), areaPanel);
			}
			FlexTableHelper.fixRowSpan(distribuicaoPanel);
		}
		showWaitMessage(false);
	}

	private Widget geraCoordenacoes(EncontroOrganogramaVO vo, OrganogramaArea area) {
		ArrayList<OrganogramaCoordenacao> coordenacoes = new ArrayList<OrganogramaCoordenacao>();
		for (OrganogramaCoordenacao coordenacao : vo.getListaCoordenacao()) {
			if (coordenacao.getOrganogramaArea().equals(area))
				coordenacoes.add(coordenacao);
		}

		if (coordenacoes.size()>0){
			FlexTable coordenacoesPanel = new FlexTable();
			coordenacoesPanel.setCellSpacing(5);
			coordenacoesPanel.setSize("100%", "100%");
			final FocusPanel focusPanelArea = geraWigetCoordenacaoArea(vo,area);
			coordenacoesPanel.getFlexCellFormatter().setAlignment(area.getLinhaCoordenacao(),area.getColunaCoordenacao(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
			if (area.getColunaSpamCoordenacao()!=null){
				coordenacoesPanel.getFlexCellFormatter().setColSpan(area.getLinhaCoordenacao(), area.getColunaCoordenacao(),area.getColunaSpamCoordenacao());
			}
			if (area.getLinhaSpamCoordenacao()!=null){
				coordenacoesPanel.getFlexCellFormatter().setRowSpan(area.getLinhaCoordenacao(), area.getColunaCoordenacao(),area.getLinhaSpamCoordenacao());
			}
			coordenacoesPanel.setWidget(area.getLinhaCoordenacao(), area.getColunaCoordenacao(), focusPanelArea);
			for (OrganogramaCoordenacao  coordenacao : coordenacoes) {
					coordenacoesPanel.getFlexCellFormatter().setAlignment(coordenacao.getLinha(),coordenacao.getColuna(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
					final FocusPanel focusPanel = geraWigetCoordenacao(vo,coordenacao);
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
		return new Label("ORGANOGRAMA - " + area.getNome());
	}

	private FocusPanel geraWigetCoordenacao(EncontroOrganogramaVO vo,
			OrganogramaCoordenacao coordenacao) {
		final FocusPanel focusPanel = new FocusPanel();
		focusPanel.setSize("140px", "90px");
		VerticalPanel coordenacaoWidget = new VerticalPanel();
		focusPanel.add(coordenacaoWidget);
		final EncontroOrganogramaCoordenacao encontrocoordenacao = getEncontroOrganogramaCoordenacao(vo,coordenacao);
		coordenacaoWidget.setSize("140px", "90px");
		coordenacaoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		coordenacaoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		coordenacaoWidget.setStyleName("organograma-Coordenacao");

		HorizontalPanel tituloCoordenacaoWidget = new HorizontalPanel();
		tituloCoordenacaoWidget.setSize("140px", "100%");
		if (coordenacao.getOrganogramaArea().getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE))
			tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloBlue");
		else
			tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloRed");
		tituloCoordenacaoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		tituloCoordenacaoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		tituloCoordenacaoWidget.add(new Label(coordenacao.getDescricao()));

		final VerticalPanel coordenacaoNomeWidget = new VerticalPanel();
		coordenacaoNomeWidget.setSize("140px", "50px");
		coordenacaoNomeWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		coordenacaoNomeWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		if (encontrocoordenacao.getEncontroInscricao1()!= null){
			coordenacaoNomeWidget.setVisible(true);
			coordenacaoNomeWidget.add(new Label(encontrocoordenacao.getEncontroInscricao1().toStringApelidos()));
			if (encontrocoordenacao.getEncontroInscricao2()!= null){
				coordenacaoNomeWidget.add(new Label(encontrocoordenacao.getEncontroInscricao2().toStringApelidos()));
			}
		}else{
			coordenacaoNomeWidget.setVisible(false);
			coordenacaoNomeWidget.add(new Label("VAGO"));
		}
		coordenacaoWidget.add(coordenacaoNomeWidget);
		coordenacaoWidget.add(tituloCoordenacaoWidget);
		focusPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editaCoordenacao(encontrocoordenacao);
				coordenacaoWidgetEditado = (VerticalPanel) ((VerticalPanel) focusPanel.getWidget()).getWidget((((VerticalPanel) focusPanel.getWidget()).getWidgetIndex(coordenacaoNomeWidget)));
			}
		});
		return focusPanel;
	}

	private FocusPanel geraWigetCoordenacaoArea(EncontroOrganogramaVO vo, OrganogramaArea area) {
		final FocusPanel focusPanel = new FocusPanel();
		focusPanel.setSize("160px", "80px");
		VerticalPanel coordenacaoWidget = new VerticalPanel();
		focusPanel.add(coordenacaoWidget);
		final EncontroOrganogramaArea encontrocoarea = getEncontroOrganogramaArea(vo,area);
		coordenacaoWidget.setSize("160px", "80px");
		coordenacaoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);

		final VerticalPanel coordenacaoNomeWidget = new VerticalPanel();
		coordenacaoWidget.setStyleName("organograma-CoordenacaoArea");
		coordenacaoNomeWidget.setSize("160px", "100%");
		coordenacaoNomeWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		coordenacaoNomeWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		if (encontrocoarea.getEncontroInscricao1()!= null){
			coordenacaoNomeWidget.add(new Label(encontrocoarea.getEncontroInscricao1().toStringApelidos()));
			if (encontrocoarea.getEncontroInscricao2()!= null){
				coordenacaoNomeWidget.add(new Label(encontrocoarea.getEncontroInscricao2().toStringApelidos()));
			}
		}else
			coordenacaoNomeWidget.add(new Label("VAGO"));
		coordenacaoWidget.add(coordenacaoNomeWidget);
		focusPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editaArea(encontrocoarea);
				coordenacaoAreaWidgetEditado = (VerticalPanel) ((VerticalPanel) focusPanel.getWidget()).getWidget((((VerticalPanel) focusPanel.getWidget()).getWidgetIndex(coordenacaoNomeWidget)));
			}
		});
		return focusPanel;
	}

	private EncontroOrganogramaCoordenacao getEncontroOrganogramaCoordenacao(EncontroOrganogramaVO vo, OrganogramaCoordenacao coordenacao) {
		for (EncontroOrganogramaCoordenacao encontroOrganogramaCoordenacao : vo.getListaEncontroOrganogramaCoordenacao()) {
			if ( encontroOrganogramaCoordenacao.getOrganogramaCoordenacao().equals(coordenacao))
				return encontroOrganogramaCoordenacao;
		}
		EncontroOrganogramaCoordenacao encontroOrganogramaCoordenacao = new EncontroOrganogramaCoordenacao();
		encontroOrganogramaCoordenacao.setOrganogramaCoordenacao(coordenacao);
		vo.getListaEncontroOrganogramaCoordenacao().add(encontroOrganogramaCoordenacao);
		return encontroOrganogramaCoordenacao;
	}

	private EncontroOrganogramaArea getEncontroOrganogramaArea(EncontroOrganogramaVO vo, OrganogramaArea area) {
		for (EncontroOrganogramaArea encontroOrganogramaArea : vo.getListaEncontroOrganogramaArea()) {
			if ( encontroOrganogramaArea.getOrganogramaArea().equals(area))
				return encontroOrganogramaArea;
		}
		EncontroOrganogramaArea encontroOrganogramaArea = new EncontroOrganogramaArea();
		encontroOrganogramaArea.setOrganogramaArea(area);
		vo.getListaEncontroOrganogramaArea().add(encontroOrganogramaArea);
		return encontroOrganogramaArea;
	}

	public List<EncontroOrganograma> getListaOrganograma() {
		return listaOrganograma;
	}

	public void setListaOrganograma(List<EncontroOrganograma> lista) {
		this.listaOrganograma = lista;
	}

}