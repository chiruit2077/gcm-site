package br.com.ecc.client.ui.sistema.hotelaria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroHotelQuarto;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.HotelAgrupamento;
import br.com.ecc.model.Quarto;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoAgrupamentoLadoEnum;
import br.com.ecc.model.tipo.TipoEncontroQuartoEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoQuartoEnum;
import br.com.ecc.model.vo.EncontroHotelVO;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DistribuicaoQuartosView extends BaseView<DistribuicaoQuartosPresenter> implements DistribuicaoQuartosPresenter.Display {

	@UiTemplate("DistribuicaoQuartosView.ui.xml")
	interface DistribucaoViewUiBinder extends UiBinder<Widget, DistribuicaoQuartosView> {}
	private DistribucaoViewUiBinder uiBinder = GWT.create(DistribucaoViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField VerticalPanel centralPanel;
	@UiField VerticalPanel distribuicaoPanel;
	@UiField VerticalPanel legendaPanel;
	@UiField ListBox hotelListBox;
	@UiField Label totalLabel;
	@UiField DialogBox editaDialogBox;
	@UiField Label labelInscricao1;
	@UiField Label labelInscricao2;
	@UiField Label labelInscricao3;
	@UiField Label labelInscricao4;
	@UiField(provided = true) SuggestBox inscricaoSuggestBox1;
	private final GenericEntitySuggestOracle inscricaoSuggest1 = new GenericEntitySuggestOracle();
	@UiField(provided = true) SuggestBox inscricaoSuggestBox2;
	private final GenericEntitySuggestOracle inscricaoSuggest2 = new GenericEntitySuggestOracle();
	@UiField(provided = true) SuggestBox inscricaoSuggestBox3;
	private final GenericEntitySuggestOracle inscricaoSuggest3 = new GenericEntitySuggestOracle();
	@UiField(provided = true) SuggestBox inscricaoSuggestBox4;
	private final GenericEntitySuggestOracle inscricaoSuggest4 = new GenericEntitySuggestOracle();
	@UiField ListBox tipoEncontroQuartoListBox;
	@UiField Label quartoNumberLabel;
	@UiField Button salvarQuartoButton;
	@UiField Button fecharQuartoButton;
	@UiField Button printButton;

	private List<EncontroHotel> listaHotel;

	private EncontroHotelQuarto entidadeEditada;

	protected VerticalPanel quartoWidgetEditado;

	public DistribuicaoQuartosView() {
		inscricaoSuggest1.setMinimoCaracteres(2);
		inscricaoSuggest2.setMinimoCaracteres(2);
		inscricaoSuggest3.setMinimoCaracteres(2);
		inscricaoSuggest4.setMinimoCaracteres(2);

		inscricaoSuggestBox1 = new SuggestBox(inscricaoSuggest1);
		inscricaoSuggestBox2 = new SuggestBox(inscricaoSuggest2);
		inscricaoSuggestBox3 = new SuggestBox(inscricaoSuggest3);
		inscricaoSuggestBox4 = new SuggestBox(inscricaoSuggest4);

		initWidget(uiBinder.createAndBindUi(this));

		inscricaoSuggestBox1.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox1.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue());
					entidadeEditada.setEncontroInscricao1(encontroInscricao);
				}
			}
		});
		inscricaoSuggestBox2.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox2.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest2.getListaEntidades(), inscricaoSuggestBox2.getValue());
					entidadeEditada.setEncontroInscricao2(encontroInscricao);
				}
			}
		});
		inscricaoSuggestBox3.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox3.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest3.getListaEntidades(), inscricaoSuggestBox3.getValue());
					entidadeEditada.setEncontroInscricao3(encontroInscricao);
				}
			}
		});
		inscricaoSuggestBox4.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox4.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest4.getListaEntidades(), inscricaoSuggestBox4.getValue());
					entidadeEditada.setEncontroInscricao4(encontroInscricao);
				}
			}
		});
		tituloFormularioLabel.setText(getDisplayTitle());
		ListBoxUtil.populate(tipoEncontroQuartoListBox, false, TipoEncontroQuartoEnum.values());
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@UiHandler("printButton")
	public void printButtonClickHandler(ClickEvent event){
		printWidget(distribuicaoPanel);

	}

	@UiHandler("fecharQuartoButton")
	public void fecharQuartoButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}

	@UiHandler("salvarQuartoButton")
	public void salvarQuartoButtonClickHandler(ClickEvent event){
		TipoEncontroQuartoEnum tipoQuarto = (TipoEncontroQuartoEnum) ListBoxUtil.getItemSelected(tipoEncontroQuartoListBox, TipoEncontroQuartoEnum.values());
		if(tipoQuarto==null){
			Window.alert("Seleciona a Tipo Reserva");
			return;
		}
		entidadeEditada.setEncontroHotel(presenter.getEncontroHotelSelecionado());
		entidadeEditada.setTipo(tipoQuarto);

		entidadeEditada.setEncontroInscricao1(null);
		entidadeEditada.setEncontroInscricao2(null);
		entidadeEditada.setEncontroInscricao3(null);
		entidadeEditada.setEncontroInscricao4(null);

		if(!inscricaoSuggestBox1.getValue().equals("")){
			entidadeEditada.setEncontroInscricao1((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue()));
		}
		if (tipoQuarto.equals(TipoEncontroQuartoEnum.SOLTEIROS)){
			if(!inscricaoSuggestBox2.getValue().equals("")){
				entidadeEditada.setEncontroInscricao2((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest2.getListaEntidades(), inscricaoSuggestBox2.getValue()));
			}
			if(!inscricaoSuggestBox3.getValue().equals("")){
				entidadeEditada.setEncontroInscricao3((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest3.getListaEntidades(), inscricaoSuggestBox3.getValue()));
			}
			if(!inscricaoSuggestBox4.getValue().equals("")){
				entidadeEditada.setEncontroInscricao4((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest4.getListaEntidades(), inscricaoSuggestBox4.getValue()));
			}
		}

		editaDialogBox.hide();
		presenter.salvar();
	}
	private void edita(EncontroHotelQuarto encontroHotelQuarto) {
		limpaCampos();
		defineCampos(encontroHotelQuarto);
		editaDialogBox.center();
		editaDialogBox.show();
		tipoEncontroQuartoListBox.setFocus(true);
	}

	@UiHandler("tipoEncontroQuartoListBox")
	public void tipoEncontroQuartoListBoxChangeHandler(ChangeEvent event) {
		TipoEncontroQuartoEnum tipoQuarto = (TipoEncontroQuartoEnum) ListBoxUtil.getItemSelected(tipoEncontroQuartoListBox, TipoEncontroQuartoEnum.values());
		if (tipoQuarto != null){
			HashMap<String, Object> params = new HashMap<String, Object>();
			if (tipoQuarto.equals(TipoEncontroQuartoEnum.SOLTEIROS)){
				inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
				inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
				inscricaoSuggest3.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
				inscricaoSuggest4.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
				params.put("encontro", presenter.getEncontroSelecionado());
				inscricaoSuggest1.setQueryParams(params);
				inscricaoSuggest2.setQueryParams(params);
				inscricaoSuggest3.setQueryParams(params);
				inscricaoSuggest4.setQueryParams(params);
				labelInscricao1.setText("Pessoa 1:");
				labelInscricao1.setVisible(true);
				labelInscricao2.setVisible(true);
				labelInscricao3.setVisible(true);
				labelInscricao4.setVisible(true);
				inscricaoSuggestBox1.setVisible(true);
				inscricaoSuggestBox2.setVisible(true);
				inscricaoSuggestBox3.setVisible(true);
				inscricaoSuggestBox4.setVisible(true);
				entidadeEditada.setEncontroInscricao1(null);
				entidadeEditada.setEncontroInscricao2(null);
				entidadeEditada.setEncontroInscricao3(null);
				entidadeEditada.setEncontroInscricao4(null);
				inscricaoSuggestBox1.setValue(null);
				inscricaoSuggestBox2.setValue(null);
				inscricaoSuggestBox3.setValue(null);
				inscricaoSuggestBox4.setValue(null);
			}else if (tipoQuarto.equals(TipoEncontroQuartoEnum.RESERVADO)) {
				labelInscricao1.setText("Casal:");
				labelInscricao1.setVisible(false);
				labelInscricao2.setVisible(false);
				labelInscricao3.setVisible(false);
				labelInscricao4.setVisible(false);
				inscricaoSuggestBox1.setVisible(false);
				inscricaoSuggestBox2.setVisible(false);
				inscricaoSuggestBox3.setVisible(false);
				inscricaoSuggestBox4.setVisible(false);
				entidadeEditada.setEncontroInscricao1(null);
				entidadeEditada.setEncontroInscricao2(null);
				entidadeEditada.setEncontroInscricao3(null);
				entidadeEditada.setEncontroInscricao4(null);
				inscricaoSuggestBox1.setValue(null);
				inscricaoSuggestBox2.setValue(null);
				inscricaoSuggestBox3.setValue(null);
				inscricaoSuggestBox4.setValue(null);

			}else{
				inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
				inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
				inscricaoSuggest3.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
				inscricaoSuggest4.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
				params.put("encontro", presenter.getEncontroSelecionado());
				params.put("tipo", TipoInscricaoEnum.getPorEncontroQuarto(tipoQuarto));
				inscricaoSuggest1.setQueryParams(params);
				inscricaoSuggest2.setQueryParams(params);
				inscricaoSuggest3.setQueryParams(params);
				inscricaoSuggest4.setQueryParams(params);
				labelInscricao1.setText("Casal:");
				labelInscricao1.setVisible(true);
				labelInscricao2.setVisible(false);
				labelInscricao3.setVisible(false);
				labelInscricao4.setVisible(false);
				inscricaoSuggestBox1.setVisible(true);
				inscricaoSuggestBox2.setVisible(false);
				inscricaoSuggestBox3.setVisible(false);
				inscricaoSuggestBox4.setVisible(false);
				entidadeEditada.setEncontroInscricao1(null);
				entidadeEditada.setEncontroInscricao2(null);
				entidadeEditada.setEncontroInscricao3(null);
				entidadeEditada.setEncontroInscricao4(null);
				inscricaoSuggestBox1.setValue(null);
				inscricaoSuggestBox2.setValue(null);
				inscricaoSuggestBox3.setValue(null);
				inscricaoSuggestBox4.setValue(null);
			}
		}
	}

	public void defineCampos(EncontroHotelQuarto encontroHotelQuarto){
		entidadeEditada = encontroHotelQuarto;
		quartoNumberLabel.setText(encontroHotelQuarto.getQuarto().getNumeroQuarto());
		if(encontroHotelQuarto.getTipo() !=null ){
			ListBoxUtil.setItemSelected(tipoEncontroQuartoListBox, encontroHotelQuarto.getTipo().getNome());
			HashMap<String, Object> params = new HashMap<String, Object>();
			if (encontroHotelQuarto.getTipo().equals(TipoEncontroQuartoEnum.SOLTEIROS)){
				labelInscricao1.setText("Pessoa 1:");
				labelInscricao1.setVisible(true);
				labelInscricao2.setVisible(true);
				labelInscricao3.setVisible(true);
				labelInscricao4.setVisible(true);
				inscricaoSuggestBox1.setVisible(true);
				inscricaoSuggestBox2.setVisible(true);
				inscricaoSuggestBox3.setVisible(true);
				inscricaoSuggestBox4.setVisible(true);
				inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
				inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
				inscricaoSuggest3.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
				inscricaoSuggest4.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
				params.put("encontro", presenter.getEncontroSelecionado());
				inscricaoSuggest1.setQueryParams(params);
				inscricaoSuggest2.setQueryParams(params);
				inscricaoSuggest3.setQueryParams(params);
				inscricaoSuggest4.setQueryParams(params);
			}else if (encontroHotelQuarto.getTipo().equals(TipoEncontroQuartoEnum.RESERVADO)){
				labelInscricao1.setVisible(false);
				labelInscricao2.setVisible(false);
				labelInscricao3.setVisible(false);
				labelInscricao4.setVisible(false);
				inscricaoSuggestBox1.setVisible(false);
				inscricaoSuggestBox2.setVisible(false);
				inscricaoSuggestBox3.setVisible(false);
				inscricaoSuggestBox4.setVisible(false);
			}else{
				labelInscricao1.setText("Casal:");
				labelInscricao1.setVisible(true);
				labelInscricao2.setVisible(false);
				labelInscricao3.setVisible(false);
				labelInscricao4.setVisible(false);
				inscricaoSuggestBox1.setVisible(true);
				inscricaoSuggestBox2.setVisible(false);
				inscricaoSuggestBox3.setVisible(false);
				inscricaoSuggestBox4.setVisible(false);
				inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
				inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
				inscricaoSuggest3.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
				inscricaoSuggest4.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLikeTipo");
				params.put("encontro", presenter.getEncontroSelecionado());
				params.put("tipo", TipoInscricaoEnum.getPorEncontroQuarto(entidadeEditada.getTipo()));
				inscricaoSuggest1.setQueryParams(params);
				inscricaoSuggest2.setQueryParams(params);
				inscricaoSuggest3.setQueryParams(params);
				inscricaoSuggest4.setQueryParams(params);
			}

			if(encontroHotelQuarto.getEncontroInscricao1() != null){
				inscricaoSuggestBox1.setValue(encontroHotelQuarto.getEncontroInscricao1().toString());
				inscricaoSuggest1.setListaEntidades(new ArrayList<_WebBaseEntity>());
				inscricaoSuggest1.getListaEntidades().add(encontroHotelQuarto.getEncontroInscricao1());
			}
			if (encontroHotelQuarto.getTipo().equals(TipoEncontroQuartoEnum.SOLTEIROS)){
				if(encontroHotelQuarto.getEncontroInscricao2() != null){
					inscricaoSuggestBox2.setValue(encontroHotelQuarto.getEncontroInscricao2().toString());
					inscricaoSuggest2.setListaEntidades(new ArrayList<_WebBaseEntity>());
					inscricaoSuggest2.getListaEntidades().add(encontroHotelQuarto.getEncontroInscricao2());
				}
				if(encontroHotelQuarto.getEncontroInscricao3() != null){
					inscricaoSuggestBox3.setValue(encontroHotelQuarto.getEncontroInscricao3().toString());
					inscricaoSuggest3.setListaEntidades(new ArrayList<_WebBaseEntity>());
					inscricaoSuggest3.getListaEntidades().add(encontroHotelQuarto.getEncontroInscricao3());
				}
				if(encontroHotelQuarto.getEncontroInscricao4() != null){
					inscricaoSuggestBox4.setValue(encontroHotelQuarto.getEncontroInscricao4().toString());
					inscricaoSuggest4.setListaEntidades(new ArrayList<_WebBaseEntity>());
					inscricaoSuggest4.getListaEntidades().add(encontroHotelQuarto.getEncontroInscricao4());
				}
			}
		}
	}

	@Override
	public void populaHoteis(List<EncontroHotel> lista) {
		this.setListaHotel(lista);
		hotelListBox.clear();
		for(EncontroHotel encontroHotel : lista) {
			hotelListBox.addItem(encontroHotel.toString(), encontroHotel.getId().toString());
		}
		hotelListBox.setSelectedIndex(0);
		EncontroHotel encontroHotel = getEncontroHotelId(Integer.valueOf(hotelListBox.getValue(hotelListBox.getSelectedIndex())));
		if (encontroHotel != null){
			presenter.setEncontroHotelSelecionado(encontroHotel);
			presenter.buscaVO();
		}
	}

	@UiHandler("hotelListBox")
	public void HotelListBoxChangeHandler(ChangeEvent event) {
		EncontroHotel encontroHotel = getEncontroHotelId(Integer.valueOf(hotelListBox.getValue(hotelListBox.getSelectedIndex())));
		if (encontroHotel != null){
			presenter.setEncontroHotelSelecionado(encontroHotel);
			presenter.buscaVO();
		}
	}

	private EncontroHotel getEncontroHotelId(Integer value) {
		for (EncontroHotel encontroHotel : getListaHotel()) {
			if(encontroHotel.getId().equals(value))
				return encontroHotel;
		}
		return null;
	}

	public void limpaCampos(){
		quartoNumberLabel.setText(null);
		inscricaoSuggestBox1.setValue(null);
		inscricaoSuggestBox2.setValue(null);
		inscricaoSuggestBox3.setValue(null);
		inscricaoSuggestBox4.setValue(null);
		labelInscricao1.setText("Casal:");
		labelInscricao1.setVisible(false);
		labelInscricao2.setVisible(false);
		labelInscricao3.setVisible(false);
		labelInscricao4.setVisible(false);
		inscricaoSuggestBox1.setVisible(false);
		inscricaoSuggestBox2.setVisible(false);
		inscricaoSuggestBox3.setVisible(false);
		inscricaoSuggestBox4.setVisible(false);
		tipoEncontroQuartoListBox.setSelectedIndex(0);
	}

	@Override
	public String getDisplayTitle() {
		return "Distribuição de Quartos";
	}

	@Override
	public void reset() {
		legendaPanel.clear();
		distribuicaoPanel.clear();
	}

	@Override
	public void populaEntidades(EncontroHotelVO vo) {
		legendaPanel.clear();

		HorizontalPanel titulo = new HorizontalPanel();
		titulo.setSize("100%", "30px");
		titulo.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		titulo.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		titulo.setStyleName("agrupamento-Titulo");
		titulo.add(new Label("Legenda dos Quartos"));
		legendaPanel.add(titulo);
		HorizontalPanel separador = new HorizontalPanel();
		separador.setSize("100%", "20px");
		legendaPanel.add(separador);

		TipoEncontroQuartoEnum[] values = TipoEncontroQuartoEnum.values();
		for (TipoEncontroQuartoEnum tipoEncontroQuartoEnum : values) {
			VerticalPanel legenda = new VerticalPanel();
			legenda.setStyleName(tipoEncontroQuartoEnum.getStyle());
			legenda.setSize("80px", "30px");
			legenda.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
			legenda.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
			legenda.add(new Label(tipoEncontroQuartoEnum.getNome()));
			legendaPanel.add(legenda);
			separador = new HorizontalPanel();
			separador.setSize("100%", "20px");
			legendaPanel.add(separador);
		}
		totalLabel.setText(geraTotaisQuartos(vo, null));

		distribuicaoPanel.clear();
		Collections.sort(vo.getListaHotelAgrupamentos(), new Comparator<HotelAgrupamento>() {
			@Override
			public int compare(HotelAgrupamento o1, HotelAgrupamento o2) {
				return o1.getOrdem().compareTo(o2.getOrdem());
			}
		});

		if (vo.getListaHotelAgrupamentos().size() > 0) {
			VerticalPanel panel = new VerticalPanel();
			panel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
			panel.setSize("100%", "100%");
			for (HotelAgrupamento agrupamento : vo.getListaHotelAgrupamentos()) {
				VerticalPanel agrupamentoPanel = new VerticalPanel();
				agrupamentoPanel.setSize("100%", "100%");
				agrupamentoPanel.setStyleName("agrupamento");
				HorizontalPanel tituloAgrupamento = new HorizontalPanel();
				tituloAgrupamento.setSize("100%", "20px");
				tituloAgrupamento.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
				tituloAgrupamento.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				tituloAgrupamento.setStyleName("agrupamento-Titulo");
				tituloAgrupamento.add(new Label(agrupamento.getNome()));
				Label totalAgrupamento = new Label();
				totalAgrupamento.setStyleName(".total");
				totalAgrupamento.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
				totalAgrupamento.setText(geraTotaisQuartos(vo,agrupamento));
				tituloAgrupamento.add(totalAgrupamento);
				agrupamentoPanel.add(tituloAgrupamento);
				if ( agrupamento.getQuantidadeLados() == 2 ){
					//DIREITO
					VerticalPanel ladodireito = new VerticalPanel();
					ladodireito.setSize("100%", "100%");
					HorizontalPanel ladoTitulo = new HorizontalPanel();
					ladoTitulo.setSize("100%", "20px");
					ladoTitulo.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					ladoTitulo.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					ladoTitulo.setStyleName("agrupamento-Lado");
					ladoTitulo.add(new Label(TipoAgrupamentoLadoEnum.DIREITO.getNome()));
					ladodireito.add(ladoTitulo);
					HorizontalPanel quartos = new HorizontalPanel();
					quartos.setSize("100%", "80px");
					quartos.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					quartos.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					quartos.setStyleName("agrupamento-Quartos");
					quartos.add(geraQuartos(vo,agrupamento,TipoAgrupamentoLadoEnum.DIREITO));
					ladodireito.add(quartos);
					agrupamentoPanel.add(ladodireito);

					separador = new HorizontalPanel();
					separador.setSize("100%", "20px");
					agrupamentoPanel.add(separador);

					//ESQUERDO
					VerticalPanel ladoesquerdo = new VerticalPanel();
					ladoesquerdo.setSize("100%", "100%");
					quartos = new HorizontalPanel();
					quartos.setSize("100%", "80px");
					quartos.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					quartos.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					quartos.setStyleName("agrupamento-Quartos");
					quartos.add(geraQuartos(vo,agrupamento,TipoAgrupamentoLadoEnum.ESQUERDO));
					ladoesquerdo.add(quartos);
					ladoTitulo = new HorizontalPanel();
					ladoTitulo.setSize("100%", "20px");
					ladoTitulo.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					ladoTitulo.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					ladoTitulo.setStyleName("agrupamento-Lado");
					ladoTitulo.add(new Label(TipoAgrupamentoLadoEnum.ESQUERDO.getNome()));
					panel.add(ladoTitulo);
					ladoesquerdo.add(ladoTitulo);
					agrupamentoPanel.add(ladoesquerdo);
				}else{
					VerticalPanel ladounico = new VerticalPanel();
					ladounico.setSize("100%", "100%");
					HorizontalPanel quartos = new HorizontalPanel();
					quartos.setSize("100%", "80px");
					quartos.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					quartos.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					quartos.setStyleName("agrupamento-Quartos");
					quartos.add(geraQuartos(vo,agrupamento,TipoAgrupamentoLadoEnum.UNICO));
					ladounico.add(quartos);
					agrupamentoPanel.add(ladounico);
				}
				panel.add(agrupamentoPanel);

				separador = new HorizontalPanel();
				separador.setSize("100%", "20px");
				panel.add(separador);
			}
			distribuicaoPanel.add(panel);
		}
		showWaitMessage(false);
	}

	private String geraTotaisQuartos(EncontroHotelVO vo, HotelAgrupamento agrupamento ) {
		String totais = "";
		int qtde = 0;
		int qtdeapoio = 0;
		int qtdedisponiveis = 0;
		int qtdepadrinho = 0;
		int qtdeafilhado = 0;
		int qtdereservado = 0;
		for (Quarto quarto : vo.getListaQuartos()) {
			if (!quarto.getTipoQuarto().equals(TipoQuartoEnum.SPAM)) {
				if (agrupamento == null || quarto.getHotelAgrupamento().equals(agrupamento)){
					qtde++;
					EncontroHotelQuarto encontroHotelQuarto = getEncontroHotelQuarto(vo,quarto);
					if (encontroHotelQuarto != null){
						if(encontroHotelQuarto.getTipo().equals(TipoEncontroQuartoEnum.AFILHADO)){
							qtdeafilhado++;
							qtdedisponiveis++;
						}
						else if(encontroHotelQuarto.getTipo().equals(TipoEncontroQuartoEnum.PADRINHO)){
							qtdepadrinho++;
							qtdedisponiveis++;
						}
						else if(encontroHotelQuarto.getTipo().equals(TipoEncontroQuartoEnum.RESERVADO)){
							qtdereservado++;
						}
						else{
							qtdeapoio++;
							qtdedisponiveis++;
						}
					}
					else{
						qtdeapoio++;
						qtdedisponiveis++;
					}
				}
			}

		}

		if(qtdedisponiveis > 0){
			totais += qtdedisponiveis + " disponíveis";
		}
		if(qtdeafilhado > 0){
			if (totais.length() > 0) totais += " / ";
			totais += qtdeafilhado + " afilhados";
		}
		if(qtdepadrinho > 0){
			if (totais.length() > 0) totais += " / ";
			totais += qtdepadrinho + " padrinhos";
		}
		if(qtdeapoio > 0){
			if (totais.length() > 0) totais += " / ";
			totais += qtdeapoio + " apoios";
		}
		if(qtdereservado > 0){
			if (totais.length() > 0) totais += " / ";
			totais += qtdereservado + " reservados";
		}
		if(qtde > 0){
			if (totais.length() > 0) totais += " / ";
			totais += qtde + " quartos listados";
		}
		return totais;
	}

	private Widget geraQuartos(EncontroHotelVO vo, HotelAgrupamento agrupamento, TipoAgrupamentoLadoEnum lado) {
		ArrayList<Quarto> quartos = new ArrayList<Quarto>();
		for (Quarto quarto : vo.getListaQuartos()) {
			if (quarto.getHotelAgrupamento().equals(agrupamento) && quarto.getLado().equals(lado))
				quartos.add(quarto);
		}

		Collections.sort(quartos, new Comparator<Quarto>() {
			@Override
			public int compare(Quarto o1, Quarto o2) {
				return o1.getOrdem().compareTo(o2.getOrdem());
			}
		});

		if (quartos.size()>0){
			HorizontalPanel quartosPanel = new HorizontalPanel();
			quartosPanel.setSize("100%", "100%");
			for (Quarto  quarto : quartos) {
				HorizontalPanel separador = new HorizontalPanel();
				separador.setSize("5px", "100%");
				quartosPanel.add(separador);
				if (quarto.getTipoQuarto().equals(TipoQuartoEnum.SPAM)) {
					VerticalPanel quartoWidget = new VerticalPanel();
					quartoWidget.setSize("80px", "100px");
					quartoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					quartoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
					quartosPanel.add(quartoWidget);
				}else{
					final FocusPanel focusPanel = new FocusPanel();
					focusPanel.setSize("80px", "100px");
					VerticalPanel quartoWidget = new VerticalPanel();
					focusPanel.add(quartoWidget);
					final EncontroHotelQuarto encontroHotelQuarto = getEncontroHotelQuarto(vo,quarto);
					quartoWidget.setSize("80px", "100px");
					quartoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					quartoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);

					HorizontalPanel numquartoWidget = new HorizontalPanel();
					numquartoWidget.setSize("100%", "20px");
					numquartoWidget.setStyleName("agrupamento-QuartoNumero");
					numquartoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					numquartoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					numquartoWidget.add(new Label(quarto.getNumeroQuarto()));

					final VerticalPanel quartoNomeWidget = new VerticalPanel();
					quartoNomeWidget.setStyleName(encontroHotelQuarto.getTipo().getStyle());
					quartoNomeWidget.setSize("100%", "80px");
					quartoNomeWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					quartoNomeWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					if (encontroHotelQuarto.getTipo().equals(TipoEncontroQuartoEnum.SOLTEIROS)){
						if (encontroHotelQuarto.getEncontroInscricao1()!= null){
							quartoNomeWidget.add(new Label(encontroHotelQuarto.getEncontroInscricao1().getPessoa().getApelido()!=null?encontroHotelQuarto.getEncontroInscricao1().getPessoa().getApelido():encontroHotelQuarto.getEncontroInscricao1().getPessoa().getNome()));
						}
						if (encontroHotelQuarto.getEncontroInscricao2()!= null){
							quartoNomeWidget.add(new Label(encontroHotelQuarto.getEncontroInscricao2().getPessoa().getApelido()!=null?encontroHotelQuarto.getEncontroInscricao2().getPessoa().getApelido():encontroHotelQuarto.getEncontroInscricao2().getPessoa().getNome()));
						}
						if (encontroHotelQuarto.getEncontroInscricao3()!= null){
							quartoNomeWidget.add(new Label(encontroHotelQuarto.getEncontroInscricao3().getPessoa().getApelido()!=null?encontroHotelQuarto.getEncontroInscricao3().getPessoa().getApelido():encontroHotelQuarto.getEncontroInscricao3().getPessoa().getNome()));
						}
						if (encontroHotelQuarto.getEncontroInscricao4()!= null){
							quartoNomeWidget.add(new Label(encontroHotelQuarto.getEncontroInscricao4().getPessoa().getApelido()!=null?encontroHotelQuarto.getEncontroInscricao4().getPessoa().getApelido():encontroHotelQuarto.getEncontroInscricao4().getPessoa().getNome()));
						}
						if (encontroHotelQuarto.getEncontroInscricao1()== null && encontroHotelQuarto.getEncontroInscricao2()== null && encontroHotelQuarto.getEncontroInscricao3()== null && encontroHotelQuarto.getEncontroInscricao4()== null ){
							quartoNomeWidget.add(new Label("VAGO"));
						}
					}else{
						if (encontroHotelQuarto.getEncontroInscricao1()!= null){
							quartoNomeWidget.add(new Label(encontroHotelQuarto.getEncontroInscricao1().getCasal().getEle().getApelido()!=null?encontroHotelQuarto.getEncontroInscricao1().getCasal().getEle().getApelido():encontroHotelQuarto.getEncontroInscricao1().getCasal().getEle().getNome()));
							quartoNomeWidget.add(new Label("e"));
							quartoNomeWidget.add(new Label(encontroHotelQuarto.getEncontroInscricao1().getCasal().getEla().getApelido()!=null?encontroHotelQuarto.getEncontroInscricao1().getCasal().getEla().getApelido():encontroHotelQuarto.getEncontroInscricao1().getCasal().getEla().getNome()));
						}else{
							quartoNomeWidget.add(new Label("VAGO"));
						}
					}
					quartoWidget.add(quartoNomeWidget);
					if (quarto.getLado().equals(TipoAgrupamentoLadoEnum.DIREITO)){
						quartoWidget.add(quartoNomeWidget);
						quartoWidget.add(numquartoWidget);
						quartoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
					}else{
						quartoWidget.add(numquartoWidget);
						quartoWidget.add(quartoNomeWidget);
					}
					if (presenter.getEncontroSelecionado().getDataPublicacaoHotelaria()==null){
						focusPanel.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								edita(encontroHotelQuarto);
								quartoWidgetEditado = (VerticalPanel) ((VerticalPanel) focusPanel.getWidget()).getWidget((((VerticalPanel) focusPanel.getWidget()).getWidgetIndex(quartoNomeWidget)));
							}
						});
					}
					quartosPanel.add(focusPanel);
				}
			}
			HorizontalPanel separador = new HorizontalPanel();
			separador.setSize("5px", "100%");
			quartosPanel.add(separador);

			return quartosPanel;
		}
		return new Label("QUARTOS - " + lado.getNome());
	}



	private EncontroHotelQuarto getEncontroHotelQuarto(EncontroHotelVO vo, Quarto quarto) {
		for (EncontroHotelQuarto encontroHotelQuarto : vo.getListaEncontroQuartos()) {
			if ( encontroHotelQuarto.getQuarto().equals(quarto))
				return encontroHotelQuarto;
		}
		EncontroHotelQuarto encontroHotelQuarto = new EncontroHotelQuarto();
		encontroHotelQuarto.setQuarto(quarto);
		encontroHotelQuarto.setTipo(TipoEncontroQuartoEnum.APOIO);
		vo.getListaEncontroQuartos().add(encontroHotelQuarto);
		return encontroHotelQuarto;
	}

	public List<EncontroHotel> getListaHotel() {
		return listaHotel;
	}

	public void setListaHotel(List<EncontroHotel> listaHotel) {
		this.listaHotel = listaHotel;
	}

}