package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroRestauranteGarcon;
import br.com.ecc.model.Mesa;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoMesaLadoEnum;
import br.com.ecc.model.tipo.TipoRestauranteEnum;
import br.com.ecc.model.vo.EncontroHotelVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DistribuicaoRestauranteView extends BaseView<DistribuicaoRestaurantePresenter> implements DistribuicaoRestaurantePresenter.Display {

	@UiTemplate("DistribuicaoRestauranteView.ui.xml")
	interface DistribucaoViewUiBinder extends UiBinder<Widget, DistribuicaoRestauranteView> {}
	private DistribucaoViewUiBinder uiBinder = GWT.create(DistribucaoViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField VerticalPanel centralPanel;
	@UiField HorizontalPanel distribuicaoPanel;
	@UiField VerticalPanel legendaPanel;
	@UiField ListBox restauranteListBox;
	@UiField Label totalLabel;
	@UiField DialogBox editaDialogBox;
	@UiField Label labelInscricao1;
	@UiField Label labelInscricao2;
	@UiField Label labelInscricao3;
	@UiField(provided = true) SuggestBox inscricaoSuggestBox1;
	private final GenericEntitySuggestOracle inscricaoSuggest1 = new GenericEntitySuggestOracle();
	@UiField(provided = true) SuggestBox inscricaoSuggestBox2;
	private final GenericEntitySuggestOracle inscricaoSuggest2 = new GenericEntitySuggestOracle();
	@UiField(provided = true) SuggestBox inscricaoSuggestBox3;
	private final GenericEntitySuggestOracle inscricaoSuggest3 = new GenericEntitySuggestOracle();
	@UiField Label mesaNumberLabel;
	@UiField Button salvarButton;
	@UiField Button salvarMesaButton;
	@UiField Button fecharMesaButton;

	private TipoRestauranteEnum restauranteSelecionado;
	private EncontroRestauranteGarcon entidadeEditada;

	protected VerticalPanel mesaWidgetEditado;

	public DistribuicaoRestauranteView() {
		inscricaoSuggest1.setMinimoCaracteres(2);
		inscricaoSuggest2.setMinimoCaracteres(2);
		inscricaoSuggest3.setMinimoCaracteres(2);

		inscricaoSuggestBox1 = new SuggestBox(inscricaoSuggest1);
		inscricaoSuggestBox2 = new SuggestBox(inscricaoSuggest2);
		inscricaoSuggestBox3 = new SuggestBox(inscricaoSuggest3);

		initWidget(uiBinder.createAndBindUi(this));

		inscricaoSuggestBox1.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox1.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue());
					entidadeEditada.setEncontroAfilhado1(encontroInscricao);
				}
			}
		});
		inscricaoSuggestBox2.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox2.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest2.getListaEntidades(), inscricaoSuggestBox2.getValue());
					entidadeEditada.setEncontroAfilhado2(encontroInscricao);
				}
			}
		});
		inscricaoSuggestBox3.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox3.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest3.getListaEntidades(), inscricaoSuggestBox3.getValue());
					entidadeEditada.setEncontroAfilhado1(encontroInscricao);
				}
			}
		});
		tituloFormularioLabel.setText(getDisplayTitle());
		ListBoxUtil.populate(restauranteListBox, true, TipoRestauranteEnum.values());
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		presenter.salvar();
	}

	@UiHandler("fecharMesaButton")
	public void fecharQuartoButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}

	@UiHandler("salvarMesaButton")
	public void salvarMesaButtonClickHandler(ClickEvent event){
		entidadeEditada.setEncontroHotel(presenter.getEncontroHotelSelecionado());
		entidadeEditada.setTipo(getRestauranteSelecionado());

		entidadeEditada.setEncontroAfilhado1(null);
		entidadeEditada.setEncontroAfilhado2(null);
		entidadeEditada.setEncontroGarcon(null);

		if(!inscricaoSuggestBox1.getValue().equals("")){
			entidadeEditada.setEncontroAfilhado1((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue()));
		}
		if(!inscricaoSuggestBox2.getValue().equals("")){
			entidadeEditada.setEncontroAfilhado2((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest2.getListaEntidades(), inscricaoSuggestBox2.getValue()));
		}
		if(!inscricaoSuggestBox3.getValue().equals("")){
			entidadeEditada.setEncontroGarcon((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest3.getListaEntidades(), inscricaoSuggestBox3.getValue()));
		}

		editaDialogBox.hide();

		mesaWidgetEditado.clear();
		if (entidadeEditada.getEncontroAfilhado1()!= null){
			mesaWidgetEditado.add(new Label(entidadeEditada.getEncontroAfilhado1().getPessoa().getApelido()!=null?entidadeEditada.getEncontroAfilhado1().getPessoa().getApelido():entidadeEditada.getEncontroAfilhado1().getPessoa().getNome()));
		}
		if (entidadeEditada.getEncontroAfilhado2()!= null){
			mesaWidgetEditado.add(new Label(entidadeEditada.getEncontroAfilhado2().getPessoa().getApelido()!=null?entidadeEditada.getEncontroAfilhado2().getPessoa().getApelido():entidadeEditada.getEncontroAfilhado2().getPessoa().getNome()));
		}
		if (entidadeEditada.getEncontroGarcon()!= null){
			mesaWidgetEditado.add(new Label(entidadeEditada.getEncontroGarcon().getPessoa().getApelido()!=null?entidadeEditada.getEncontroGarcon().getPessoa().getApelido():entidadeEditada.getEncontroGarcon().getPessoa().getNome()));
		}
	}

	@SuppressWarnings("unused")
	private void edita(EncontroRestauranteGarcon encontroRestauranteGarcon) {
		limpaCampos();
		defineCampos(encontroRestauranteGarcon);
		editaDialogBox.center();
		editaDialogBox.show();
		inscricaoSuggestBox1.setFocus(true);
	}

	@UiHandler("restauranteListBox")
	public void restauranteListBoxListBoxChangeHandler(ChangeEvent event) {
		TipoRestauranteEnum tipoRestaurante = (TipoRestauranteEnum) ListBoxUtil.getItemSelected(restauranteListBox, TipoRestauranteEnum.values());
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
		inscricaoSuggest3.setSuggestQuery("encontroInscricao.porEncontroCasalNomeLike");
		HashMap<String, Object> paramsAfilhaod = new HashMap<String, Object>();
		paramsAfilhaod.put("encontro", presenter.getEncontroSelecionado());
		paramsAfilhaod.put("tipo", TipoInscricaoEnum.AFILHADO);
		inscricaoSuggest1.setQueryParams(paramsAfilhaod);
		inscricaoSuggest2.setQueryParams(paramsAfilhaod);
		HashMap<String, Object> paramGarcon = new HashMap<String, Object>();
		paramGarcon.put("encontro", presenter.getEncontroSelecionado());
		paramGarcon.put("tipo", TipoInscricaoEnum.APOIO);
		inscricaoSuggest3.setQueryParams(paramGarcon);
		presenter.setTipoRestauranteSelecionado(tipoRestaurante);
		setRestauranteSelecionado(tipoRestaurante);
	}

	public void defineCampos(EncontroRestauranteGarcon encontroRestauranteGarcon){
		entidadeEditada = encontroRestauranteGarcon;
		mesaNumberLabel.setText(encontroRestauranteGarcon.getMesa().getNumero());
		if(encontroRestauranteGarcon.getEncontroAfilhado1() != null){
			inscricaoSuggestBox1.setValue(encontroRestauranteGarcon.getEncontroAfilhado1().toString());
			inscricaoSuggest1.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest1.getListaEntidades().add(encontroRestauranteGarcon.getEncontroAfilhado1());
		}
		if(encontroRestauranteGarcon.getEncontroAfilhado2() != null){
			inscricaoSuggestBox2.setValue(encontroRestauranteGarcon.getEncontroAfilhado2().toString());
			inscricaoSuggest2.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest2.getListaEntidades().add(encontroRestauranteGarcon.getEncontroAfilhado2());
		}
		if(encontroRestauranteGarcon.getEncontroGarcon() != null){
			inscricaoSuggestBox3.setValue(encontroRestauranteGarcon.getEncontroGarcon().toString());
			inscricaoSuggest3.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest3.getListaEntidades().add(encontroRestauranteGarcon.getEncontroGarcon());
		}
	}

	public void limpaCampos(){
		mesaNumberLabel.setText(null);
		inscricaoSuggestBox1.setValue(null);
		inscricaoSuggestBox2.setValue(null);
		inscricaoSuggestBox3.setValue(null);
	}

	@Override
	public String getDisplayTitle() {
		return "Distribuição de Restaurante";
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

		/*TipoEncontroQuartoEnum[] values = TipoEncontroQuartoEnum.values();
		for (TipoEncontroQuartoEnum tipoEncontroQuartoEnum : values) {
			VerticalPanel legenda = new VerticalPanel();
			legenda.setStyleName(tipoEncontroQuartoEnum.getStyle());
			legenda.setSize("80px", "30px");
			legenda.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
			legenda.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
			legenda.add(new Label(tipoEncontroQuartoEnum.getNome()));
			legendaPanel.add(legenda);
			HorizontalPanel separador = new HorizontalPanel();
			separador.setSize("100%", "20px");
			legendaPanel.add(separador);
		}*/

		//otalLabel.setText(geraTotaisQuartos(vo, null));

		distribuicaoPanel.clear();

		VerticalPanel panelEsquerdo = new VerticalPanel();
		panelEsquerdo.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		panelEsquerdo.setSize("420px", "100%");
		panelEsquerdo.setStyleName("mesa-Panel");
		VerticalPanel tituloLado = new VerticalPanel();
		tituloLado.setSize("100%", "30px");
		tituloLado.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		tituloLado.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		tituloLado.setStyleName("mesa-Titulo");
		tituloLado.add(new Label("LADO ESQUERTO"));
		panelEsquerdo.add(tituloLado);
		HorizontalPanel separador = new HorizontalPanel();
		separador.setSize("100%", "20px");
		panelEsquerdo.add(separador);

		VerticalPanel panelDireito = new VerticalPanel();
		panelDireito.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		panelDireito.setSize("420px", "100%");
		panelDireito.setStyleName("mesa-Panel");
		tituloLado = new VerticalPanel();
		tituloLado.setSize("100%", "30px");
		tituloLado.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		tituloLado.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		tituloLado.setStyleName("mesa-Titulo");
		tituloLado.add(new Label("LADO DIREITO"));
		panelDireito.add(tituloLado);
		separador = new HorizontalPanel();
		separador.setSize("100%", "20px");
		panelDireito.add(separador);

		distribuicaoPanel.add(panelEsquerdo);
		separador = new HorizontalPanel();
		separador.setSize("20px", "100%");
		distribuicaoPanel.add(separador);
		distribuicaoPanel.add(panelDireito);

		if (vo.getListaMesas().size()>0){
			for (Mesa mesa : vo.getListaMesas()) {
				HorizontalPanel mesaPanel = new HorizontalPanel();
				mesaPanel.setSize("380px", "92px");
				VerticalPanel tituloMesa = new VerticalPanel();
				tituloMesa.setSize("80px", "92px");
				tituloMesa.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				tituloMesa.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				tituloMesa.setStyleName("mesa-Titulo");
				tituloMesa.add(new Label("MESA"));
				tituloMesa.add(new Label(mesa.getNumero()));
				mesaPanel.add(tituloMesa);

				final EncontroRestauranteGarcon encontroRestauranteGarcon = getEncontroRestauranteGarcon(vo,mesa,getRestauranteSelecionado());

				VerticalPanel nomesMesa = new VerticalPanel();
				nomesMesa.setSize("300px", "90px");
				nomesMesa.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
				nomesMesa.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				nomesMesa.setStyleName("mesa-Nomes");
				Label afilhado1 = new Label("VAGO");
				afilhado1.setSize("300px", "28px");
				afilhado1.setStyleName("mesa-Afilhado");
				Label afilhado2 = new Label("VAGO");
				afilhado2.setSize("300px", "28px");
				afilhado2.setStyleName("mesa-Afilhado");
				Label garcon = new Label("VAGO");
				garcon.setSize("300px", "28px");
				garcon.setStyleName("mesa-Garcon");

				if (encontroRestauranteGarcon.getEncontroAfilhado1() != null)
					afilhado1.setText(encontroRestauranteGarcon.getEncontroAfilhado1().toString());
				if (encontroRestauranteGarcon.getEncontroAfilhado2() != null)
					afilhado2.setText(encontroRestauranteGarcon.getEncontroAfilhado2().toString());
				if (encontroRestauranteGarcon.getEncontroGarcon() != null)
					garcon.setText(encontroRestauranteGarcon.getEncontroGarcon().toString());
				nomesMesa.add(afilhado1);
				nomesMesa.add(afilhado2);
				nomesMesa.add(garcon);
				mesaPanel.add(nomesMesa);

				if (mesa.getLado().equals(TipoMesaLadoEnum.ESQUERDO)) {
					panelEsquerdo.add(mesaPanel);

					separador = new HorizontalPanel();
					separador.setSize("100%", "20px");
					panelEsquerdo.add(separador);
				}
				else if (mesa.getLado().equals(TipoMesaLadoEnum.DIREITO)) {
					panelDireito.add(mesaPanel);

					separador = new HorizontalPanel();
					separador.setSize("100%", "20px");
					panelDireito.add(separador);
				}
			}

		}
		showWaitMessage(false);
	}

	private EncontroRestauranteGarcon getEncontroRestauranteGarcon(EncontroHotelVO vo, Mesa mesa, TipoRestauranteEnum tipoRestaurante) {
		for (EncontroRestauranteGarcon encontroRestauranteGarcon : vo.getListaEncontroRestauranteGarcon()) {
			if ( encontroRestauranteGarcon.getMesa().equals(mesa) && encontroRestauranteGarcon.getTipo().equals(tipoRestaurante) )
				return encontroRestauranteGarcon;
		}
		EncontroRestauranteGarcon encontroRestauranteGarcon = new EncontroRestauranteGarcon();
		encontroRestauranteGarcon.setMesa(mesa);
		encontroRestauranteGarcon.setTipo(tipoRestaurante);
		vo.getListaEncontroRestauranteGarcon().add(encontroRestauranteGarcon);
		return encontroRestauranteGarcon;
	}

	public TipoRestauranteEnum getRestauranteSelecionado() {
		return restauranteSelecionado;
	}

	public void setRestauranteSelecionado(TipoRestauranteEnum restauranteSelecionado) {
		this.restauranteSelecionado = restauranteSelecionado;
		ListBoxUtil.setItemSelected(restauranteListBox, restauranteSelecionado.getNome());
		populaEntidades(presenter.getEncontroHotelVO());
	}
}