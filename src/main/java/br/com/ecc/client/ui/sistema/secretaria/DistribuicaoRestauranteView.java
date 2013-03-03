package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.Casal;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroRestauranteGarcon;
import br.com.ecc.model.Mesa;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.tipo.TipoMesaLadoEnum;
import br.com.ecc.model.tipo.TipoRestauranteEnum;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.model.vo.EncontroHotelVO;
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
import com.google.gwt.user.client.Random;
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

public class DistribuicaoRestauranteView extends BaseView<DistribuicaoRestaurantePresenter> implements DistribuicaoRestaurantePresenter.Display {

	@UiTemplate("DistribuicaoRestauranteView.ui.xml")
	interface DistribucaoRestauranteViewUiBinder extends UiBinder<Widget, DistribuicaoRestauranteView> {}
	private DistribucaoRestauranteViewUiBinder uiBinder = GWT.create(DistribucaoRestauranteViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField VerticalPanel centralPanel;
	@UiField HorizontalPanel distribuicaoPanel;
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
	@UiField Button printButton;
	@UiField Button gerarDistribuicaoButton;
	@UiField Button limpaDistribuicaoButton;

	private TipoRestauranteEnum restauranteSelecionado;
	private EncontroRestauranteGarcon entidadeEditada;
	private Label labelAfilhado1Editado;
	private Label labelAfilhado2Editado;
	private Label labelGarconEditado;
	private VerticalPanel tituloPanelEditado;
	private Mesa mesaEditada;

	private List<EncontroInscricao> listaGarcons;

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
		ListBoxUtil.populate(restauranteListBox, false, TipoRestauranteEnum.values());
		listaGarcons = new ArrayList<EncontroInscricao>();
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@UiHandler("printButton")
	public void printButtonClickHandler(ClickEvent event){
		Print.it("","<link rel=styleSheet type=text/css media=paper href=/paperStyle.css>",distribuicaoPanel.getElement());
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

		labelAfilhado1Editado.setText("VAGO");
		labelAfilhado2Editado.setText("VAGO");
		labelGarconEditado.setText("VAGO");

		if(!inscricaoSuggestBox1.getValue().equals("")){
			entidadeEditada.setEncontroAfilhado1((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue()));
			labelAfilhado1Editado.setText(inscricaoSuggestBox1.getValue());
		}
		if(!inscricaoSuggestBox2.getValue().equals("")){
			entidadeEditada.setEncontroAfilhado2((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest2.getListaEntidades(), inscricaoSuggestBox2.getValue()));
			labelAfilhado2Editado.setText(inscricaoSuggestBox2.getValue());
		}
		if(!inscricaoSuggestBox3.getValue().equals("")){
			entidadeEditada.setEncontroGarcon((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest3.getListaEntidades(), inscricaoSuggestBox3.getValue()));
			labelGarconEditado.setText(inscricaoSuggestBox3.getValue());
		}

		populaMesa(mesaEditada, tituloPanelEditado, entidadeEditada, labelAfilhado1Editado, labelAfilhado2Editado, labelGarconEditado);

		editaDialogBox.hide();

	}

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
		return "Distribuição dos Restaurantes";
	}

	@Override
	public void reset() {
		distribuicaoPanel.clear();
	}

	@Override
	public void populaEntidades(EncontroHotelVO vo) {

		distribuicaoPanel.clear();

		VerticalPanel panelEsquerdo = new VerticalPanel();
		panelEsquerdo.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		panelEsquerdo.setSize("600px", "100%");
		panelEsquerdo.setStyleName("mesa-Panel");
		VerticalPanel tituloLado = new VerticalPanel();
		tituloLado.setSize("100%", "30px");
		tituloLado.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		tituloLado.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		tituloLado.setStyleName("mesa-Titulo");
		tituloLado.add(new Label("LADO ESQUERTO"));
		panelEsquerdo.add(tituloLado);
		HorizontalPanel separador = new HorizontalPanel();
		separador.setSize("100%", "20px");
		panelEsquerdo.add(separador);

		VerticalPanel panelDireito = new VerticalPanel();
		panelDireito.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		panelDireito.setSize("600px", "100%");
		panelDireito.setStyleName("mesa-Panel");
		tituloLado = new VerticalPanel();
		tituloLado.setSize("100%", "30px");
		tituloLado.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		tituloLado.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
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
			for (final Mesa mesa : vo.getListaMesas()) {
				HorizontalPanel mesaPanel = new HorizontalPanel();
				mesaPanel.setSize("580px", "92px");
				final FocusPanel focusPanel = new FocusPanel();
				final VerticalPanel tituloMesa = new VerticalPanel();
				tituloMesa.setSize("80px", "92px");
				tituloMesa.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
				tituloMesa.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
				tituloMesa.add(new Label("MESA"));
				tituloMesa.add(new Label(mesa.getNumero()));
				focusPanel.add(tituloMesa);
				mesaPanel.add(focusPanel);

				final EncontroRestauranteGarcon encontroRestauranteGarcon = getEncontroRestauranteGarcon(vo,mesa,getRestauranteSelecionado());

				VerticalPanel nomesMesaPanel = new VerticalPanel();
				nomesMesaPanel.setStyleName("mesa-Nomes");
				nomesMesaPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
				nomesMesaPanel.setSize("500px", "90px");
				final Label afilhado1 = new Label("VAGO");
				afilhado1.setStyleName("mesa-Afilhado");
				afilhado1.setSize("100%", "28px");
				final Label afilhado2 = new Label("VAGO");
				afilhado2.setStyleName("mesa-Afilhado");
				afilhado2.setSize("100%", "28px");
				final Label garcon = new Label("VAGO");
				garcon.setStyleName("mesa-Garcon");
				garcon.setSize("100%", "28px");

				populaMesa(mesa, tituloMesa, encontroRestauranteGarcon, afilhado1, afilhado2, garcon);

				nomesMesaPanel.add(afilhado1);
				nomesMesaPanel.add(afilhado2);
				nomesMesaPanel.add(garcon);
				mesaPanel.add(nomesMesaPanel);

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

				focusPanel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						labelAfilhado1Editado = afilhado1;
						labelAfilhado2Editado = afilhado2;
						labelGarconEditado = garcon;
						tituloPanelEditado = tituloMesa;
						mesaEditada = mesa;
						edita(encontroRestauranteGarcon);
					}
				});
			}

		}
		showWaitMessage(false);
	}

	private void populaMesa(final Mesa mesa, final VerticalPanel tituloMesa, final EncontroRestauranteGarcon encontroRestauranteGarcon, final Label afilhado1, final Label afilhado2, final Label garcon) {
		String erros = "";
		if (encontroRestauranteGarcon.getEncontroAfilhado1() != null){
			afilhado1.setText(encontroRestauranteGarcon.getEncontroAfilhado1().toString());
			if (isSelecionadoRestaurante(encontroRestauranteGarcon.getEncontroAfilhado1(), getRestauranteSelecionado(), mesa, encontroRestauranteGarcon.getEncontroAfilhado2()))
				erros+="Afilhado1 já selecionado neste Restaurante\n";
			if (isSelecionadoOutroAfilhado(encontroRestauranteGarcon.getEncontroAfilhado1(), getRestauranteSelecionado(), encontroRestauranteGarcon.getEncontroAfilhado2()))
				erros+="Afilhado1 já selecionado com Afilhado 2 em outro Restaurante\n";
			if (isSelecionadoMesa(encontroRestauranteGarcon.getEncontroAfilhado1(), getRestauranteSelecionado(), mesa))
				erros+="Afilhado1 já selecionado nesta mesa em outro restaurante\n";
		}
		if (encontroRestauranteGarcon.getEncontroAfilhado2() != null){
			afilhado2.setText(encontroRestauranteGarcon.getEncontroAfilhado2().toString());
			if (isSelecionadoRestaurante(encontroRestauranteGarcon.getEncontroAfilhado2(), getRestauranteSelecionado(), mesa, encontroRestauranteGarcon.getEncontroAfilhado1()))
				erros+="Afilhado2 já selecionado neste Restaurante\n";
			if (isSelecionadoOutroAfilhado(encontroRestauranteGarcon.getEncontroAfilhado2(), getRestauranteSelecionado(), encontroRestauranteGarcon.getEncontroAfilhado1()))
				erros+="Afilhado2 já selecionado com Afilhado 1 em outro Restaurante\n";
			if (isSelecionadoMesa(encontroRestauranteGarcon.getEncontroAfilhado2(), getRestauranteSelecionado(), mesa))
				erros+="Afilhado2 já selecionado nesta mesa em outro Restaurante\n";
		}
		if (encontroRestauranteGarcon.getEncontroGarcon() != null){
			garcon.setText(encontroRestauranteGarcon.getEncontroGarcon().toString());
			if (isSelecionadoRestaurante(encontroRestauranteGarcon.getEncontroGarcon(), getRestauranteSelecionado(), mesa, null))
				erros+="Garçon já selecionado neste Restaurante\n";
			if (isSelecionadoAfilhado(encontroRestauranteGarcon.getEncontroGarcon(), getRestauranteSelecionado(), encontroRestauranteGarcon.getEncontroAfilhado1(),encontroRestauranteGarcon.getEncontroAfilhado2()))
				erros+="Garçon selecionado com Afilhado\n";
			if (isSelecionadoCasal(encontroRestauranteGarcon.getEncontroGarcon(), getRestauranteSelecionado(), encontroRestauranteGarcon.getEncontroAfilhado1(),encontroRestauranteGarcon.getEncontroAfilhado2()))
				erros+="Garçon já selecionado para alguns dos Afilhados em outro Restaurante\n";
		}
		if (erros.length()>0){
			tituloMesa.setStyleName("mesa-TituloErro");
			tituloMesa.setTitle(erros);
		}else
			tituloMesa.setStyleName("mesa-Titulo");
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

		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeAfilhadoLike");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeAfilhadoLike");
		inscricaoSuggest3.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest2.setQueryParams(params);
		inscricaoSuggest3.setQueryParams(params);

		listaGarcons.clear();
		for (AgrupamentoVO agrupamentoVO : presenter.getEncontroHotelVO().getListaAgrupamentosVO()) {
			TipoRestauranteEnum tipoRestaurante = agrupamentoVO.getAgrupamento().getTipoRestaurante();
			if (tipoRestaurante != null && tipoRestaurante.equals(restauranteSelecionado)){
				for (AgrupamentoMembro menbro : agrupamentoVO.getListaMembros()) {
					EncontroInscricao inscricao = getInscricaoCasal(menbro.getCasal());
					if (inscricao!=null)
						listaGarcons.add(inscricao);
				}
			}
		}

		populaEntidades(presenter.getEncontroHotelVO());

	}

	private EncontroInscricao getInscricaoCasal(Casal casal) {
		List<EncontroInscricao> listaEncontristas = presenter.getEncontroHotelVO().getListaEncontristas();
		for (EncontroInscricao encontroInscricao : listaEncontristas) {
			if (encontroInscricao.getCasal()!= null && encontroInscricao.getCasal().equals(casal))
				return encontroInscricao;
		}
		return null;
	}

	@UiHandler("limpaDistribuicaoButton")
	public void limpaDistribuicaoButtonClickHandler(ClickEvent event){
		if ( !Window.confirm("Este procedimento ira limpar todos dados, desja continur ?"))
			return;
		if (presenter.getEncontroHotelVO().getListaMesas().size()>0){
			List<EncontroRestauranteGarcon> restauranteGarcon = presenter.getEncontroHotelVO().getListaEncontroRestauranteGarcon();
			for (EncontroRestauranteGarcon encontroRestauranteGarcon : restauranteGarcon) {
				encontroRestauranteGarcon.setEncontroAfilhado1(null);
				encontroRestauranteGarcon.setEncontroAfilhado2(null);
				encontroRestauranteGarcon.setEncontroGarcon(null);
			}
			presenter.salvar();
		}
	}

	@UiHandler("gerarDistribuicaoButton")
	public void gerarDistribuicaoButtonClickHandler(ClickEvent event){
		if (presenter.getEncontroHotelVO().getListaMesas().size()>0){
			boolean achou = false;
			for (Mesa mesa : presenter.getEncontroHotelVO().getListaMesas()) {
				EncontroRestauranteGarcon encontroRestauranteGarcon = getEncontroRestauranteGarcon(presenter.getEncontroHotelVO(),mesa,getRestauranteSelecionado());
				if (encontroRestauranteGarcon.getEncontroGarcon() != null || encontroRestauranteGarcon.getEncontroAfilhado1() != null ||
						encontroRestauranteGarcon.getEncontroAfilhado2() != null){
					achou = true;
				}
			}
			if(achou){
					if ( !Window.confirm("Há distribuição já lançada, deseja continur, os dados serão apagados ?"))
						return;
					for (Mesa mesa : presenter.getEncontroHotelVO().getListaMesas()) {
						EncontroRestauranteGarcon encontroRestauranteGarcon = getEncontroRestauranteGarcon(presenter.getEncontroHotelVO(),mesa,getRestauranteSelecionado());
						encontroRestauranteGarcon.setEncontroAfilhado1(null);
						encontroRestauranteGarcon.setEncontroAfilhado2(null);
						encontroRestauranteGarcon.setEncontroGarcon(null);
					}

			}

			int tentativa=0;
			do{
				tentativa++;
			}while(!geraDistribuicao() && tentativa < 1500);

			showWaitMessage(true);
			if (tentativa==1500){
				Window.alert("Não foi possivel gerar a distribuição!");
				for (Mesa mesa : presenter.getEncontroHotelVO().getListaMesas()) {
					EncontroRestauranteGarcon encontroRestauranteGarcon = getEncontroRestauranteGarcon(presenter.getEncontroHotelVO(),mesa,getRestauranteSelecionado());
					encontroRestauranteGarcon.setEncontroAfilhado1(null);
					encontroRestauranteGarcon.setEncontroAfilhado2(null);
					encontroRestauranteGarcon.setEncontroGarcon(null);
				}
			}
			showWaitMessage(false);
			populaEntidades(presenter.getEncontroHotelVO());
		}
	}

	public boolean geraDistribuicao(){
		if (presenter.getEncontroHotelVO().getListaMesas().size()>0){
			for (Mesa mesa : presenter.getEncontroHotelVO().getListaMesas()) {
				EncontroRestauranteGarcon encontroRestauranteGarcon = getEncontroRestauranteGarcon(presenter.getEncontroHotelVO(),mesa,getRestauranteSelecionado());
				EncontroInscricao afilhado1 = getAfilhalhadoSorteio(null,mesa,getRestauranteSelecionado());
				EncontroInscricao afilhado2 = getAfilhalhadoSorteio(afilhado1,mesa,getRestauranteSelecionado());
				EncontroInscricao garcon = getGarconSorteio(afilhado1,afilhado2,mesa,getRestauranteSelecionado());
				if (afilhado1==null || afilhado2 == null)
					return false;
				encontroRestauranteGarcon.setEncontroAfilhado1(afilhado1);
				encontroRestauranteGarcon.setEncontroAfilhado2(afilhado2);
				encontroRestauranteGarcon.setEncontroGarcon(garcon);
			}
			return true;
		}
		return false;
	}

	private EncontroInscricao getAfilhalhadoSorteio(EncontroInscricao afilhado1, Mesa mesa, TipoRestauranteEnum restauranteSelecionado) {
		EncontroInscricao encontroInscricao=null;
		int tentativa=0;
		do{
			int randon = Random.nextInt(presenter.getEncontroSelecionado().getQuantidadeAfilhados());
			encontroInscricao = presenter.getEncontroHotelVO().getListaAfilhados().get(randon);
			if (isSelecionadoRestaurante(encontroInscricao,restauranteSelecionado,mesa,afilhado1))
				encontroInscricao = null;
			if (isSelecionadoMesa(encontroInscricao,restauranteSelecionado,mesa))
				encontroInscricao = null;
			if (isSelecionadoOutroAfilhado(encontroInscricao,restauranteSelecionado,afilhado1))
				encontroInscricao = null;
			tentativa++;
		}while(encontroInscricao == null && tentativa < 100);
		return encontroInscricao;
	}

	private boolean isSelecionadoOutroAfilhado( EncontroInscricao encontroInscricao, TipoRestauranteEnum restaurante, EncontroInscricao outroAfilhado) {
		if( encontroInscricao==null || outroAfilhado == null) return false;
		for (EncontroRestauranteGarcon encontroRestaurante : presenter.getEncontroHotelVO().getListaEncontroRestauranteGarcon()) {
			if (!encontroRestaurante.getTipo().equals(restaurante)){
				if (encontroRestaurante.getEncontroAfilhado1()!=null && encontroRestaurante.getEncontroAfilhado1().equals(encontroInscricao)){
					if (encontroRestaurante.getEncontroAfilhado2()!=null && encontroRestaurante.getEncontroAfilhado2().equals(outroAfilhado)){
						return true;
					}
				}
				if (encontroRestaurante.getEncontroAfilhado2()!=null && encontroRestaurante.getEncontroAfilhado2().equals(encontroInscricao)){
					if (encontroRestaurante.getEncontroAfilhado1()!=null && encontroRestaurante.getEncontroAfilhado1().equals(outroAfilhado)){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isSelecionadoMesa(EncontroInscricao encontroInscricao, TipoRestauranteEnum restaurante, Mesa mesa) {
		if( encontroInscricao==null) return false;
		for (EncontroRestauranteGarcon encontroRestaurante : presenter.getEncontroHotelVO().getListaEncontroRestauranteGarcon()) {
			if (!encontroRestaurante.getTipo().equals(restaurante)){
				if (encontroInscricao.getTipo().equals(TipoInscricaoEnum.AFILHADO)){
					if (encontroRestaurante.getEncontroAfilhado1()!=null && encontroRestaurante.getEncontroAfilhado1().equals(encontroInscricao)){
						if (encontroRestaurante.getMesa().equals(mesa)){
							return true;
						}
					}
					if (encontroRestaurante.getEncontroAfilhado2()!=null && encontroRestaurante.getEncontroAfilhado2().equals(encontroInscricao)){
						if (encontroRestaurante.getMesa().equals(mesa)){
							return true;
						}
					}
				}else {
					if (encontroRestaurante.getEncontroGarcon()!=null && encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
						if (encontroRestaurante.getMesa().equals(mesa)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isSelecionadoCasal(EncontroInscricao encontroInscricao, TipoRestauranteEnum restaurante, EncontroInscricao afilhado1, EncontroInscricao afilhado2) {
		if( encontroInscricao==null) return false;
		for (EncontroRestauranteGarcon encontroRestaurante : presenter.getEncontroHotelVO().getListaEncontroRestauranteGarcon()) {
			if (!encontroRestaurante.getTipo().equals(restaurante)){
				if (encontroRestaurante.getEncontroGarcon()!=null){
					if (encontroRestaurante.getEncontroAfilhado1()!=null && encontroRestaurante.getEncontroAfilhado1().equals(afilhado1) &&
							encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
						return true;
					}
					if (encontroRestaurante.getEncontroAfilhado2()!=null && encontroRestaurante.getEncontroAfilhado2().equals(afilhado2) &&
							encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isSelecionadoRestaurante( EncontroInscricao encontroInscricao, TipoRestauranteEnum restaurante, Mesa mesa, EncontroInscricao outroAfilhado) {
		if( encontroInscricao==null) return false;
		for (EncontroRestauranteGarcon encontroRestaurante : presenter.getEncontroHotelVO().getListaEncontroRestauranteGarcon()) {
			if (encontroRestaurante.getTipo().equals(restaurante)){
				if (encontroInscricao.getTipo().equals(TipoInscricaoEnum.AFILHADO)){
					if (outroAfilhado != null && encontroInscricao.equals(outroAfilhado)) return true;
					if (!encontroRestaurante.getMesa().equals(mesa)){
						if (encontroRestaurante.getEncontroAfilhado1()!=null && encontroRestaurante.getEncontroAfilhado1().equals(encontroInscricao)){
							return true;
						}
						if (encontroRestaurante.getEncontroAfilhado2()!=null && encontroRestaurante.getEncontroAfilhado2().equals(encontroInscricao)){
							return true;
						}
					}
				}
				else {
					if (!encontroRestaurante.getMesa().equals(mesa)){
						if (encontroRestaurante.getEncontroGarcon()!=null && encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private EncontroInscricao getGarconSorteio(EncontroInscricao afilhado1, EncontroInscricao afilhado2, Mesa mesa, TipoRestauranteEnum restauranteSelecionado) {
		EncontroInscricao encontroInscricao=null;
		int tentativa=0;
		do{
			int randon = Random.nextInt(listaGarcons.size());
			encontroInscricao = listaGarcons.get(randon);
			if (isSelecionadoRestaurante(encontroInscricao,restauranteSelecionado,mesa,afilhado1))
				encontroInscricao = null;
			if (isSelecionadoCasal(encontroInscricao,restauranteSelecionado,afilhado1,afilhado2))
				encontroInscricao = null;
			if (isSelecionadoAfilhado(encontroInscricao,restauranteSelecionado,afilhado1,afilhado2))
				encontroInscricao = null;
			tentativa++;
		}while(encontroInscricao == null && tentativa < 1000);
		return encontroInscricao;
	}

	private boolean isSelecionadoAfilhado(EncontroInscricao encontroInscricao, TipoRestauranteEnum restauranteSelecionado2, EncontroInscricao afilhado1, EncontroInscricao afilhado2) {
		if( encontroInscricao==null) return false;
		if(afilhado1 != null && afilhado1.getCasal().getCasalPadrinho().equals(encontroInscricao.getCasal()))
			return true;
		if(afilhado2 != null && afilhado2.getCasal().getCasalPadrinho().equals(encontroInscricao.getCasal()))
			return true;
		return false;
	}

}