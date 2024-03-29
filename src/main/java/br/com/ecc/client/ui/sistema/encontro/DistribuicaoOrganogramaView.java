package br.com.ecc.client.ui.sistema.encontro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.util.FlexTableHelper;
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

	@UiHandler("printButton")
	public void printButtonClickHandler(ClickEvent event){
		printWidget(distribuicaoPanel);
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
		}

		editaDialogBox.hide();
		presenter.salvar();

	}
	private void editaCoordenacao(EncontroOrganogramaCoordenacao encontroOrganogramaCoordenacao) {
		editaArea=false;
		limpaCamposCoordenacao();
		defineCamposCoordenacao(encontroOrganogramaCoordenacao);
		editaDialogBox.center();
		editaDialogBox.show();
		casalRadio.setValue(true);
		inscricaoSuggestBox1.setFocus(true);
	}

	private void editaArea(EncontroOrganogramaArea encontroOrganogramaArea) {
		editaArea=true;
		limpaCamposCoordenacao();
		defineCamposArea(encontroOrganogramaArea);
		editaDialogBox.center();
		editaDialogBox.show();
		casalRadio.setValue(true);
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
		if(coordenacao.getTipoAtividade()!=null){
			if (coordenacao.getTipoAtividade().equals(TipoAtividadeEnum.ATIVIDADE) ||
					coordenacao.getTipoAtividade().equals(TipoAtividadeEnum.CONDUCAO )){
				tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloBlueLayout");
			}else if (coordenacao.getTipoAtividade().equals(TipoAtividadeEnum.DESMONTAGEM) ||
					coordenacao.getTipoAtividade().equals(TipoAtividadeEnum.PREPARO)){
				tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloRedLayout");
			}else
				tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloBlackLayout");
		}else
			tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTituloBlackLayout");
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
		if (presenter.getEncontroSelecionado().getDataPublicacaoOrganograma()==null){
			focusPanel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					editaCoordenacao(encontrocoordenacao);
					coordenacaoWidgetEditado = (VerticalPanel) ((VerticalPanel) focusPanel.getWidget()).getWidget((((VerticalPanel) focusPanel.getWidget()).getWidgetIndex(coordenacaoNomeWidget)));
				}
			});
		}
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
		if (presenter.getEncontroSelecionado().getDataPublicacaoOrganograma()==null){
			focusPanel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					editaArea(encontrocoarea);
					coordenacaoAreaWidgetEditado = (VerticalPanel) ((VerticalPanel) focusPanel.getWidget()).getWidget((((VerticalPanel) focusPanel.getWidget()).getWidgetIndex(coordenacaoNomeWidget)));
				}
			});
		}
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