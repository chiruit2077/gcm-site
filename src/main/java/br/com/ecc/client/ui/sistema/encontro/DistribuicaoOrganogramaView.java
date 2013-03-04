package br.com.ecc.client.ui.sistema.encontro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.EncontroOrganogramaArea;
import br.com.ecc.model.EncontroOrganogramaCoordenacao;
import br.com.ecc.model.OrganogramaArea;
import br.com.ecc.model.OrganogramaCoordenacao;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.vo.EncontroOrganogramaVO;
import br.com.freller.tool.client.Print;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
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

	private List<EncontroOrganograma> listaOrganograma;

	private boolean editaArea = false;
	private EncontroOrganogramaCoordenacao entidadeEditadaCoordenacao;
	private EncontroOrganogramaArea entidadeEditadaArea;

	protected VerticalPanel coordenacaoWidgetEditado;

	public DistribuicaoOrganogramaView() {
		inscricaoSuggest1.setMinimoCaracteres(2);
		inscricaoSuggest2.setMinimoCaracteres(2);

		inscricaoSuggestBox1 = new SuggestBox(inscricaoSuggest1);
		inscricaoSuggestBox2 = new SuggestBox(inscricaoSuggest2);

		initWidget(uiBinder.createAndBindUi(this));

		inscricaoSuggestBox1.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox1.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue());
					entidadeEditadaCoordenacao.setEncontroInscricao1(encontroInscricao);
				}
			}
		});
		inscricaoSuggestBox2.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox2.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest2.getListaEntidades(), inscricaoSuggestBox2.getValue());
					entidadeEditadaCoordenacao.setEncontroInscricao2(encontroInscricao);
				}
			}
		});
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
		Print.it("","<link rel=styleSheet type=text/css media=paper href=/paperStyle.css>",distribuicaoPanel.getElement());
	}

	@UiHandler("fecharCoordenacaoButton")
	public void fecharCoordenacaoButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
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

		/*quartoWidgetEditado.clear();
		quartoWidgetEditado.setStyleName(entidadeEditadaCoordenacao.getTipo().getStyle());
		if (entidadeEditadaCoordenacao.getTipo().equals(TipoEncontroQuartoEnum.SOLTEIROS)){
			if (entidadeEditadaCoordenacao.getEncontroInscricao1()!= null){
				quartoWidgetEditado.add(new Label(entidadeEditadaCoordenacao.getEncontroInscricao1().getPessoa().getApelido()!=null?entidadeEditadaCoordenacao.getEncontroInscricao1().getPessoa().getApelido():entidadeEditadaCoordenacao.getEncontroInscricao1().getPessoa().getNome()));
			}
			if (entidadeEditadaCoordenacao.getEncontroInscricao2()!= null){
				quartoWidgetEditado.add(new Label(entidadeEditadaCoordenacao.getEncontroInscricao2().getPessoa().getApelido()!=null?entidadeEditadaCoordenacao.getEncontroInscricao2().getPessoa().getApelido():entidadeEditadaCoordenacao.getEncontroInscricao2().getPessoa().getNome()));
			}
			if (entidadeEditadaCoordenacao.getEncontroInscricao3()!= null){
				quartoWidgetEditado.add(new Label(entidadeEditadaCoordenacao.getEncontroInscricao3().getPessoa().getApelido()!=null?entidadeEditadaCoordenacao.getEncontroInscricao3().getPessoa().getApelido():entidadeEditadaCoordenacao.getEncontroInscricao3().getPessoa().getNome()));
			}
			if (entidadeEditadaCoordenacao.getEncontroInscricao4()!= null){
				quartoWidgetEditado.add(new Label(entidadeEditadaCoordenacao.getEncontroInscricao4().getPessoa().getApelido()!=null?entidadeEditadaCoordenacao.getEncontroInscricao4().getPessoa().getApelido():entidadeEditadaCoordenacao.getEncontroInscricao4().getPessoa().getNome()));
			}
		}else{
			if (entidadeEditadaCoordenacao.getEncontroInscricao1()!= null){
				quartoWidgetEditado.add(new Label(entidadeEditadaCoordenacao.getEncontroInscricao1().getCasal().getEle().getApelido()!=null?entidadeEditadaCoordenacao.getEncontroInscricao1().getCasal().getEle().getApelido():entidadeEditadaCoordenacao.getEncontroInscricao1().getCasal().getEle().getNome()));
				quartoWidgetEditado.add(new Label("e"));
				quartoWidgetEditado.add(new Label(entidadeEditadaCoordenacao.getEncontroInscricao1().getCasal().getEla().getApelido()!=null?entidadeEditadaCoordenacao.getEncontroInscricao1().getCasal().getEla().getApelido():entidadeEditadaCoordenacao.getEncontroInscricao1().getCasal().getEla().getNome()));
			}
		}*/
	}
	private void editaCoordenacao(EncontroOrganogramaCoordenacao encontroOrganogramaCoordenacao) {
		editaArea=false;
		limpaCamposCoordenacao();
		defineCamposCoordenacao(encontroOrganogramaCoordenacao);
		editaDialogBox.center();
		editaDialogBox.show();
	}

	private void editaArea(EncontroOrganogramaArea encontroOrganogramaArea) {
		editaArea=true;
		limpaCamposCoordenacao();
		defineCamposArea(encontroOrganogramaArea);
		editaDialogBox.center();
		editaDialogBox.show();
	}

	public void defineCamposCoordenacao(EncontroOrganogramaCoordenacao encontroOrganogramaCoordenacao){
		entidadeEditadaCoordenacao = encontroOrganogramaCoordenacao;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest2.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");

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
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");

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
		distribuicaoPanel.clear();
		distribuicaoPanel.setCellSpacing(5);
		distribuicaoPanel.setCellPadding(2);
/*		Collections.sort(vo.getListaOrganogramaArea(), new Comparator<OrganogramaArea>() {
			@Override
			public int compare(OrganogramaArea o1, OrganogramaArea o2) {
				return o1.getOrdem().compareTo(o2.getOrdem());
			}
		});*/

		if (vo.getListaOrganogramaArea().size() > 0) {
			for (OrganogramaArea area : vo.getListaOrganogramaArea()) {
				VerticalPanel areaPanel = new VerticalPanel();
				areaPanel.setSize("100%", "100%");
				areaPanel.setStyleName("agrupamento");
				HorizontalPanel tituloAgrupamento = new HorizontalPanel();
				tituloAgrupamento.setSize("100%", "20px");
				tituloAgrupamento.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				tituloAgrupamento.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				tituloAgrupamento.setStyleName("agrupamento-Titulo");
				tituloAgrupamento.add(new Label(area.getNome()));
				areaPanel.add(tituloAgrupamento);
				VerticalPanel coordenacaoPanel = new VerticalPanel();
				coordenacaoPanel.setSize("100%", "100%");
				HorizontalPanel coordenacoes = new HorizontalPanel();
				coordenacoes.setSize("100%", "80px");
				coordenacoes.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				coordenacoes.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				coordenacoes.setStyleName("agrupamento-Quartos");
				coordenacoes.add(geraCoordenacoes(vo,area));
				coordenacaoPanel.add(coordenacoes);
				areaPanel.add(coordenacaoPanel);
				if (area.getColunaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setColSpan(area.getLinha(), area.getColuna(),area.getColunaSpam());
				}
				if (area.getLinhaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setRowSpan(area.getLinha(), area.getColuna(),area.getLinhaSpam());
				}
				distribuicaoPanel.getFlexCellFormatter().setAlignment(area.getLinha(),area.getColuna(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_TOP);
				distribuicaoPanel.setWidget(area.getLinha(), area.getColuna(), areaPanel);
			}
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
			coordenacoesPanel.setCellPadding(2);
			coordenacoesPanel.setSize("100%", "100%");
			for (OrganogramaCoordenacao  coordenacao : coordenacoes) {
					final FocusPanel focusPanel = new FocusPanel();
					focusPanel.setSize("140px", "80px");
					VerticalPanel coordenacaoWidget = new VerticalPanel();
					focusPanel.add(coordenacaoWidget);
					final EncontroOrganogramaCoordenacao encontrocoordenacao = getEncontroOrganogramaCoordenacao(vo,coordenacao);
					coordenacaoWidget.setSize("140px", "80px");
					coordenacaoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					coordenacaoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);

					HorizontalPanel tituloCoordenacaoWidget = new HorizontalPanel();
					tituloCoordenacaoWidget.setSize("100%", "30px");
					tituloCoordenacaoWidget.setStyleName("organograma-CoordenacaoTitulo");
					tituloCoordenacaoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					tituloCoordenacaoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					tituloCoordenacaoWidget.add(new Label(coordenacao.getDescricao()));

					final VerticalPanel coordenacaoNomeWidget = new VerticalPanel();
					coordenacaoNomeWidget.setStyleName("organograma-Coordenacao");
					coordenacaoNomeWidget.setSize("100%", "50px");
					coordenacaoNomeWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					coordenacaoNomeWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					if (encontrocoordenacao.getEncontroInscricao1()!= null){
						coordenacaoNomeWidget.add(new Label(encontrocoordenacao.getEncontroInscricao1().toString()));
						if (encontrocoordenacao.getEncontroInscricao2()!= null){
							coordenacaoNomeWidget.add(new Label(encontrocoordenacao.getEncontroInscricao2().toString()));
						}
					}else
						coordenacaoNomeWidget.add(new Label("VAGO"));
					coordenacaoWidget.add(coordenacaoNomeWidget);
					coordenacaoWidget.add(coordenacaoNomeWidget);
					coordenacaoWidget.add(tituloCoordenacaoWidget);
					focusPanel.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							editaCoordenacao(encontrocoordenacao);
							coordenacaoWidgetEditado = (VerticalPanel) ((VerticalPanel) focusPanel.getWidget()).getWidget((((VerticalPanel) focusPanel.getWidget()).getWidgetIndex(coordenacaoNomeWidget)));
						}
					});
					if (coordenacao.getColunaSpam()!=null){
						coordenacoesPanel.getFlexCellFormatter().setColSpan(coordenacao.getLinha(), coordenacao.getColuna(),coordenacao.getColunaSpam());
					}
					if (coordenacao.getLinhaSpam()!=null){
						coordenacoesPanel.getFlexCellFormatter().setRowSpan(coordenacao.getLinha(), coordenacao.getColuna(),coordenacao.getLinhaSpam());
					}
					coordenacoesPanel.getFlexCellFormatter().setAlignment(coordenacao.getLinha(),coordenacao.getColuna(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_MIDDLE);
					coordenacoesPanel.setWidget(coordenacao.getLinha(),coordenacao.getColuna(),focusPanel);
			}
			return coordenacoesPanel;
		}
		return new Label("ORGANOGRAMA - " + area.getNome());
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

	public List<EncontroOrganograma> getListaOrganograma() {
		return listaOrganograma;
	}

	public void setListaOrganograma(List<EncontroOrganograma> lista) {
		this.listaOrganograma = lista;
	}

}