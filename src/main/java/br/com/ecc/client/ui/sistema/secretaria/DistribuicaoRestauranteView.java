package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.util.FlexTableHelper;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.client.util.NavegadorUtil;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.Casal;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.EncontroRestauranteMesa;
import br.com.ecc.model.Mesa;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.RestauranteGrupo;
import br.com.ecc.model.RestauranteTitulo;
import br.com.ecc.model._WebBaseEntity;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.model.vo.EncontroRestauranteVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DistribuicaoRestauranteView extends BaseView<DistribuicaoRestaurantePresenter> implements DistribuicaoRestaurantePresenter.Display {

	@UiTemplate("DistribuicaoRestauranteView.ui.xml")
	interface DistribucaoRestauranteViewUiBinder extends UiBinder<Widget, DistribuicaoRestauranteView> {}
	private DistribucaoRestauranteViewUiBinder uiBinder = GWT.create(DistribucaoRestauranteViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField VerticalPanel centralPanel;
	@UiField FlexTable distribuicaoPanel;
	@UiField ListBox restauranteListBox;
	@UiField DialogBox editaDialogBox;
	@UiField(provided = true) SuggestBox inscricaoSuggestBox1;
	private final GenericEntitySuggestOracle inscricaoSuggest1 = new GenericEntitySuggestOracle();
	@UiField(provided = true) SuggestBox inscricaoSuggestBox2;
	private final GenericEntitySuggestOracle inscricaoSuggest2 = new GenericEntitySuggestOracle();
	@UiField Label labelInscricao1;
	@UiField Label labelInscricao2;
	@UiField Label labelInscricao3;
	@UiField(provided = true) SuggestBox inscricaoSuggestBox3;
	private final GenericEntitySuggestOracle inscricaoSuggest3 = new GenericEntitySuggestOracle();
	@UiField Label mesaNumberLabel;
	@UiField Button salvarMesaButton;
	@UiField Button fecharMesaButton;
	@UiField Button printButton;
	@UiField Button gerarDistribuicaoButton;
	@UiField Button limpaDistribuicaoButton;
	@UiField Button encontroMaitreButton;
	@UiField Label labelMaitre;
	@UiField Label labelMesaNumberLabel;

	private EncontroRestauranteMesa entidadeEditada;
	private List<EncontroInscricao> listaGarcons;
	private List<EncontroRestaurante> listaRestaurantes;

	private EncontroInscricao encontroMaitre;

	public DistribuicaoRestauranteView() {
		inscricaoSuggest1.setMinimoCaracteres(2);
		inscricaoSuggest2.setMinimoCaracteres(2);
		inscricaoSuggest3.setMinimoCaracteres(2);

		inscricaoSuggestBox1 = new SuggestBox(inscricaoSuggest1);
		inscricaoSuggestBox2 = new SuggestBox(inscricaoSuggest2);
		inscricaoSuggestBox3 = new SuggestBox(inscricaoSuggest3);

		initWidget(uiBinder.createAndBindUi(this));

		/*inscricaoSuggestBox1.addSelectionHandler(new SelectionHandler<GenericEntitySuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(!inscricaoSuggestBox1.getValue().equals("")){
					EncontroInscricao encontroInscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue());
					if (entidadeEditada != null)
						entidadeEditada.setEncontroAfilhado1(encontroInscricao);
					else
						restauranteSelecionado.setEncontroMaitre(encontroInscricao);
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
		});*/
		tituloFormularioLabel.setText(getDisplayTitle());
		listaGarcons = new ArrayList<EncontroInscricao>();
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@UiHandler("printButton")
	public void printButtonClickHandler(ClickEvent event){
		printWidget(distribuicaoPanel);
	}


	@UiHandler("fecharMesaButton")
	public void fecharQuartoButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}

	@UiHandler("salvarMesaButton")
	public void salvarMesaButtonClickHandler(ClickEvent event){
		if (entidadeEditada != null){
			entidadeEditada.setEncontroRestaurante(getRestauranteSelecionado());

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
			presenter.salvar();
		}else{
			getRestauranteSelecionado().setEncontroMaitre((EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue()));
			editaDialogBox.hide();
			presenter.salvar();
		}
	}

	private void edita(EncontroRestauranteMesa mesa) {
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeAfilhadoLike");
		inscricaoSuggest2.setSuggestQuery("encontroInscricao.porEncontroCasalNomeAfilhadoLike");
		inscricaoSuggest3.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest2.setQueryParams(params);
		inscricaoSuggest3.setQueryParams(params);
		limpaCampos();
		defineCampos(mesa);
		if (mesa.getMesa().getQuantidadeCasais()==2){
			inscricaoSuggestBox2.setVisible(true);
			labelInscricao2.setVisible(true);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		inscricaoSuggestBox1.setFocus(true);
	}

	private void edita(EncontroRestaurante encontroRestaurante) {
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		limpaCampos();
		defineCampos(encontroRestaurante);
		editaDialogBox.center();
		editaDialogBox.show();
		inscricaoSuggestBox1.setFocus(true);
	}

	@UiHandler("restauranteListBox")
	public void restauranteListBoxListBoxChangeHandler(ChangeEvent event) {
		EncontroRestaurante restaurante = (EncontroRestaurante) ListBoxUtil.getItemSelected(restauranteListBox, getListaRestaurantes());
		presenter.setEncontroRestauranteSelecionado(restaurante);
		presenter.buscaVO();
	}

	public void defineCampos(EncontroRestaurante encontroRestaurante){
		mesaNumberLabel.setVisible(false);
		labelMesaNumberLabel.setVisible(false);
		labelInscricao1.setText("Maitre:");
		labelInscricao2.setVisible(false);
		labelInscricao3.setVisible(false);
		inscricaoSuggestBox2.setVisible(false);
		inscricaoSuggestBox3.setVisible(false);
		if(encontroRestaurante.getEncontroMaitre() != null){
			inscricaoSuggestBox1.setValue(encontroRestaurante.getEncontroMaitre().toString());
			inscricaoSuggest1.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest1.getListaEntidades().add(encontroRestaurante.getEncontroMaitre());
		}
	}

	public void defineCampos(EncontroRestauranteMesa encontroRestauranteMesa){
		entidadeEditada = encontroRestauranteMesa;
		mesaNumberLabel.setText(encontroRestauranteMesa.getMesa().getNumero());
		if(encontroRestauranteMesa.getEncontroAfilhado1() != null){
			inscricaoSuggestBox1.setValue(encontroRestauranteMesa.getEncontroAfilhado1().toString());
			inscricaoSuggest1.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest1.getListaEntidades().add(encontroRestauranteMesa.getEncontroAfilhado1());
		}
		if(encontroRestauranteMesa.getEncontroAfilhado2() != null){
			inscricaoSuggestBox2.setValue(encontroRestauranteMesa.getEncontroAfilhado2().toString());
			inscricaoSuggest2.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest2.getListaEntidades().add(encontroRestauranteMesa.getEncontroAfilhado2());
		}
		if(encontroRestauranteMesa.getEncontroGarcon() != null){
			inscricaoSuggestBox3.setValue(encontroRestauranteMesa.getEncontroGarcon().toString());
			inscricaoSuggest3.setListaEntidades(new ArrayList<_WebBaseEntity>());
			inscricaoSuggest3.getListaEntidades().add(encontroRestauranteMesa.getEncontroGarcon());
		}
	}

	public void limpaCampos(){
		entidadeEditada = null;
		mesaNumberLabel.setText(null);
		mesaNumberLabel.setVisible(true);
		labelMesaNumberLabel.setVisible(true);
		inscricaoSuggestBox2.setVisible(false);
		labelInscricao2.setVisible(false);
		inscricaoSuggestBox1.setValue(null);
		inscricaoSuggestBox2.setValue(null);
		inscricaoSuggestBox3.setValue(null);
		labelInscricao1.setText("Afilhado 1:");
		labelInscricao3.setVisible(true);
		inscricaoSuggestBox3.setVisible(true);
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
	public void populaEntidades(EncontroRestauranteVO vo) {
		distribuicaoPanel.removeAllRows();
		distribuicaoPanel.clear();
		distribuicaoPanel.clear(true);
		distribuicaoPanel.setCellSpacing(10);
		distribuicaoPanel.setStyleName("restaurante");
		encontroMaitre = null;
		listaGarcons.clear();
		for (AgrupamentoVO agrupamentoVO : presenter.getVo().getListaAgrupamentosVO()) {
			if ((agrupamentoVO.getAgrupamento().getAtividade() != null) &&
					agrupamentoVO.getAgrupamento().getAtividade().equals(getRestauranteSelecionado().getRestaurante().getAtividade())){
				for (AgrupamentoMembro menbro : agrupamentoVO.getListaMembros()) {
					EncontroInscricao inscricao = getInscricaoCasal(menbro.getCasal());
					if ((inscricao!=null) && menbro.getPapel().getGarcon())
						listaGarcons.add(inscricao);
					else if ((inscricao!=null) && menbro.getPapel().getMaitre()){
						encontroMaitre = inscricao;
					}
				}
			}
		}

		if (vo.getListaTitulos().size()>0){
			for (final RestauranteTitulo titulo : vo.getListaTitulos()) {
				Widget widget = geraTituloWidget(vo, titulo);
				if (titulo.getColunaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setColSpan(titulo.getLinha(), titulo.getColuna(),titulo.getColunaSpam());
				}
				if (titulo.getLinhaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setRowSpan(titulo.getLinha(), titulo.getColuna(),titulo.getLinhaSpam());
				}
				distribuicaoPanel.getFlexCellFormatter().setAlignment(titulo.getLinha(),titulo.getColuna(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_TOP);
				distribuicaoPanel.setWidget(titulo.getLinha(), titulo.getColuna(), widget);
			}

		}

		if (vo.getListaMesas().size()>0){
			for (final Mesa mesa : vo.getListaMesas()) {
				Widget widget = geraMesaWidget(vo, mesa);
				if (mesa.getColunaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setColSpan(mesa.getLinha(), mesa.getColuna(),mesa.getColunaSpam());
				}
				if (mesa.getLinhaSpam()!=null){
					distribuicaoPanel.getFlexCellFormatter().setRowSpan(mesa.getLinha(), mesa.getColuna(),mesa.getLinhaSpam());
				}
				distribuicaoPanel.getFlexCellFormatter().setAlignment(mesa.getLinha(),mesa.getColuna(), HorizontalPanel.ALIGN_CENTER, VerticalPanel.ALIGN_TOP);
				distribuicaoPanel.setWidget(mesa.getLinha(), mesa.getColuna(), widget);
			}

		}
		FlexTableHelper.fixRowSpan(distribuicaoPanel);

		if (vo.getEncontroRestaurante().getEncontroMaitre() != null){
			labelMaitre.setText("Maitre: " + vo.getEncontroRestaurante().getEncontroMaitre().toStringApelidos());
		}else if (encontroMaitre != null){
			vo.getEncontroRestaurante().setEncontroMaitre(encontroMaitre);
			labelMaitre.setText("Maitre: " + vo.getEncontroRestaurante().getEncontroMaitre().toStringApelidos());
			encontroMaitreButton.click();
		}
		showWaitMessage(false);
	}

	private Widget geraTituloWidget(EncontroRestauranteVO vo, final RestauranteTitulo titulo) {
		final VerticalPanel mesaPanel = new VerticalPanel();
		mesaPanel.setSize("200px", "50px");
		mesaPanel.setStyleName("restaurante-Panel");
		mesaPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		mesaPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		final Label tituloLabel = new Label(titulo.getTitulo());
		mesaPanel.add(tituloLabel);
		return mesaPanel;
	}

	private Widget geraMesaWidget(EncontroRestauranteVO vo, final Mesa mesa) {
		if ( mesa.getRestaurante().getQuantidadeCasaisPorMesa() == 2 ){
			final VerticalPanel mesaPanel = new VerticalPanel();
			mesaPanel.setSize("200px", "100px");

			HorizontalPanel afilhadosFundoPanel = new HorizontalPanel();
			afilhadosFundoPanel.setSize("200px", "20px");
			afilhadosFundoPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			afilhadosFundoPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
			final Label afilhadoEle1 = new Label("VAGO");
			afilhadoEle1.setStyleName("restaurante-Afilhado");
			afilhadosFundoPanel.add(afilhadoEle1);
			final Label afilhadoEla2 = new Label("VAGO");
			afilhadoEla2.setStyleName("restaurante-Afilhado");
			afilhadoEla2.setSize("100px", "20px");
			if (mesa.getQuantidadeCasais()>1) {
				afilhadoEle1.setSize("100px", "20px");
				afilhadosFundoPanel.add(afilhadoEla2);
			}else{
				afilhadoEle1.setSize("200px", "20px");
			}


			mesaPanel.add(afilhadosFundoPanel);

			final FocusPanel focusPanel = new FocusPanel();
			focusPanel.setSize("200px", "60px");
			final VerticalPanel tituloMesa = new VerticalPanel();
			tituloMesa.setSize("200px", "60px");
			tituloMesa.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			tituloMesa.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
			tituloMesa.add(new Label("MESA " + mesa.getNumero()));
			final Label garcon = new Label("VAGO");
			tituloMesa.add(garcon);
			if (mesa.getGrupo() != null)
				tituloMesa.add(new Label(mesa.getGrupo().toString()));
			focusPanel.add(tituloMesa);
			mesaPanel.add(focusPanel);

			HorizontalPanel afilhadosFrentePanel = new HorizontalPanel();
			afilhadosFrentePanel.setSize("200px", "20px");
			afilhadosFrentePanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			afilhadosFrentePanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
			final Label afilhadoEla1 = new Label("VAGO");
			afilhadoEla1.setStyleName("restaurante-Afilhado");
			afilhadosFrentePanel.add(afilhadoEla1);
			final Label afilhadoEle2 = new Label("VAGO");
			afilhadoEle2.setStyleName("restaurante-Afilhado");
			afilhadoEle2.setSize("100px", "20px");
			if (mesa.getQuantidadeCasais()>1){
				afilhadoEla1.setSize("100px", "20px");
				afilhadosFrentePanel.add(afilhadoEle2);
			}else{
				afilhadoEla1.setSize("200px", "20px");
			}

			mesaPanel.add(afilhadosFrentePanel);

			final EncontroRestauranteMesa encontroRestauranteMesa = getEncontroRestauranteMesa(vo,mesa,getRestauranteSelecionado());

			populaMesa(mesa, mesaPanel, encontroRestauranteMesa, afilhadoEle1, afilhadoEla1, afilhadoEle2, afilhadoEla2, garcon);
			if (presenter.getEncontroSelecionado().getDataPublicacaoRestaurante()==null){
				focusPanel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						edita(encontroRestauranteMesa);
					}
				});
			}
			return mesaPanel;
		}else if (mesa.getRestaurante().getQuantidadeCasaisPorMesa()==1){
			final VerticalPanel mesaPanel = new VerticalPanel();
			mesaPanel.setSize("200px", "100px");

			final FocusPanel focusPanel = new FocusPanel();
			focusPanel.setSize("200px", "80px");
			final VerticalPanel tituloMesa = new VerticalPanel();
			tituloMesa.setSize("200px", "80px");
			tituloMesa.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			tituloMesa.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
			tituloMesa.add(new Label("MESA " + mesa.getNumero()));
			final Label garcon = new Label("VAGO");
			tituloMesa.add(garcon);
			if (mesa.getGrupo() != null)
				tituloMesa.add(new Label(mesa.getGrupo().toString()));
			focusPanel.add(tituloMesa);
			mesaPanel.add(focusPanel);

			HorizontalPanel afilhadosFrentePanel = new HorizontalPanel();
			afilhadosFrentePanel.setSize("200px", "20px");
			afilhadosFrentePanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			afilhadosFrentePanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
			final Label afilhadoEla1 = new Label("VAGO");
			afilhadoEla1.setStyleName("restaurante-Afilhado");
			afilhadoEla1.setSize("100px", "20px");
			afilhadosFrentePanel.add(afilhadoEla1);
			final Label afilhadoEle1 = new Label("VAGO");
			afilhadoEle1.setStyleName("restaurante-Afilhado");
			afilhadoEle1.setSize("100px", "20px");
			afilhadosFrentePanel.add(afilhadoEle1);

			mesaPanel.add(afilhadosFrentePanel);

			final EncontroRestauranteMesa encontroRestauranteMesa = getEncontroRestauranteMesa(vo,mesa,getRestauranteSelecionado());

			populaMesa(mesa, mesaPanel, encontroRestauranteMesa, afilhadoEle1, afilhadoEla1, null, null, garcon);

			if (presenter.getEncontroSelecionado().getDataPublicacaoRestaurante()==null){
				focusPanel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						edita(encontroRestauranteMesa);
					}
				});
			}
			return mesaPanel;

		}else {
			final Label mesaLabel = new Label("MESA " + mesa.getNumero() + " NÃO CONFIGURADA");
			mesaLabel.setStyleName("restaurante-Garcon");
			mesaLabel.setSize("140px", "80px");
			return mesaLabel;
		}
	}

	private void populaMesa(final Mesa mesa, VerticalPanel tituloMesa, EncontroRestauranteMesa encontroRestauranteMesa,
			final Label afilhadoEle1, Label afilhadoEla1, Label afilhadoEle2, Label afilhadoEla2, Label garcon) {
		String erros = "";
		boolean atencao = false;
		if (encontroRestauranteMesa.getEncontroAfilhado1() != null){
			afilhadoEle1.getElement().setInnerHTML(getNomeEspecial(encontroRestauranteMesa.getEncontroAfilhado1().getCasal().getEle()));
			afilhadoEla1.getElement().setInnerHTML(getNomeEspecial(encontroRestauranteMesa.getEncontroAfilhado1().getCasal().getEla()));
			if (isSelecionadoRestaurante(encontroRestauranteMesa.getEncontroAfilhado1(), getRestauranteSelecionado(), mesa, encontroRestauranteMesa.getEncontroAfilhado2()))
				erros+="Afilhados " + encontroRestauranteMesa.getEncontroAfilhado1().toStringApelidos() + " já selecionado neste Restaurante\n";
			if (isSelecionadoOutroAfilhado(encontroRestauranteMesa.getEncontroAfilhado1(), getRestauranteSelecionado(), encontroRestauranteMesa.getEncontroAfilhado2()))
				erros+="Afilhados " + encontroRestauranteMesa.getEncontroAfilhado1().toStringApelidos() + " já selecionado com os " + encontroRestauranteMesa.getEncontroAfilhado2().toStringApelidos() + " em outro Restaurante\n";
			if (isSelecionadoMesa(encontroRestauranteMesa.getEncontroAfilhado1(), getRestauranteSelecionado(), mesa))
				erros+="Afilhados " + encontroRestauranteMesa.getEncontroAfilhado1().toStringApelidos() + " já selecionado nesta mesa em outro restaurante\n";
		}else atencao = true;
		if (mesa.getQuantidadeCasais()>1){
			if (encontroRestauranteMesa.getEncontroAfilhado2() != null){
				afilhadoEle2.getElement().setInnerHTML(getNomeEspecial(encontroRestauranteMesa.getEncontroAfilhado2().getCasal().getEle()));
				afilhadoEla2.getElement().setInnerHTML(getNomeEspecial(encontroRestauranteMesa.getEncontroAfilhado2().getCasal().getEla()));
				if (isSelecionadoRestaurante(encontroRestauranteMesa.getEncontroAfilhado2(), getRestauranteSelecionado(), mesa, encontroRestauranteMesa.getEncontroAfilhado1()))
					erros+="Afilhados " + encontroRestauranteMesa.getEncontroAfilhado2().toStringApelidos() + " já selecionado neste Restaurante\n";
				if (isSelecionadoOutroAfilhado(encontroRestauranteMesa.getEncontroAfilhado2(), getRestauranteSelecionado(), encontroRestauranteMesa.getEncontroAfilhado1()))
					erros+="Afilhados " + encontroRestauranteMesa.getEncontroAfilhado2().toStringApelidos() + " já selecionado com os " + encontroRestauranteMesa.getEncontroAfilhado1().toStringApelidos() + " em outro Restaurante\n";
				if (isSelecionadoMesa(encontroRestauranteMesa.getEncontroAfilhado2(), getRestauranteSelecionado(), mesa))
					erros+="Afilhados " + encontroRestauranteMesa.getEncontroAfilhado2().toStringApelidos() + " já selecionado nesta mesa em outro restaurante\n";
			}else atencao = true;
		}
		if (encontroRestauranteMesa.getEncontroGarcon() != null){
			garcon.setText(encontroRestauranteMesa.getEncontroGarcon().toStringApelidos());
			if ( mesa.getGrupo() == null ) {
				if (isSelecionadoRestaurante(encontroRestauranteMesa.getEncontroGarcon(), getRestauranteSelecionado(),mesa,null))
					erros+="Garçon já selecionado neste Restaurante\n";
			}else{
				if (isSelecionadoRestauranteGrupo(encontroRestauranteMesa.getEncontroGarcon(), getRestauranteSelecionado(),mesa))
					erros+="Garçon já selecionado neste Restaurante em Outro Grupo\n";
			}
			if (isSelecionadoAfilhado(encontroRestauranteMesa.getEncontroGarcon(), getRestauranteSelecionado(), encontroRestauranteMesa.getEncontroAfilhado1(),encontroRestauranteMesa.getEncontroAfilhado2()))
				erros+="Garçon selecionado é Padrinho de alguns dos Afilhados\n";
			if (isSelecionadoCasal(encontroRestauranteMesa.getEncontroGarcon(), getRestauranteSelecionado(), encontroRestauranteMesa.getEncontroAfilhado1(),encontroRestauranteMesa.getEncontroAfilhado2()))
				erros+="Garçon já selecionado para alguns dos Afilhados em outro Restaurante\n";
		}else atencao = true;
		if (erros.length()>0){
			tituloMesa.setStyleName("restaurante-PanelErro");
			tituloMesa.setTitle(erros);
		}else{
			if (atencao)
				tituloMesa.setStyleName("restaurante-PanelAtencao");
			else
				tituloMesa.setStyleName("restaurante-Panel");
		}
	}

	private String getNomeEspecial(Pessoa p) {
		if (p!=null){
			String apelido = "<span>" + p.getApelido() + "</span>";
			if (p.getDiabetico()){
				apelido+="<input type='image' src='" + NavegadorUtil.makeUrlResource("images/ballred32.png") + "' width='15' height='15'/>";
			}
			if (p.getVegetariano()){
				apelido+="<input type='image' src='" + NavegadorUtil.makeUrlResource("images/ballgreen32.png") + "' width='15' height='15'/>";
			}
			return apelido;
		}
		return "<span>VAGO</span>";
	}

	private EncontroRestauranteMesa getEncontroRestauranteMesa(EncontroRestauranteVO vo, Mesa mesa, EncontroRestaurante encontroRestaurante) {
		for (EncontroRestauranteMesa encontroRestauranteMesa : vo.getListaEncontroRestauranteMesa()) {
			if ( encontroRestauranteMesa.getMesa().equals(mesa))
				return encontroRestauranteMesa;
		}
		EncontroRestauranteMesa encontroRestauranteMesa = new EncontroRestauranteMesa();
		encontroRestauranteMesa.setMesa(mesa);
		encontroRestauranteMesa.setEncontroRestaurante(encontroRestaurante);
		vo.getListaEncontroRestauranteMesa().add(encontroRestauranteMesa);
		return encontroRestauranteMesa;
	}

	public EncontroRestaurante getRestauranteSelecionado() {
		return presenter.getEncontroRestauranteSelecionado();
	}

	@Override
	public void setRestauranteSelecionado(EncontroRestaurante restauranteSelecionado) {
		ListBoxUtil.setItemSelected(restauranteListBox, restauranteSelecionado.toString());

	}

	private EncontroInscricao getInscricaoCasal(Casal casal) {
		List<EncontroInscricao> listaEncontristas = presenter.getVo().getListaEncontristas();
		for (EncontroInscricao encontroInscricao : listaEncontristas) {
			if ((encontroInscricao.getCasal()!= null) && encontroInscricao.getCasal().equals(casal))
				return encontroInscricao;
		}
		return null;
	}

	@UiHandler("limpaDistribuicaoButton")
	public void limpaDistribuicaoButtonClickHandler(ClickEvent event){
		if ( !Window.confirm("Este procedimento irá apagar a distribuição atual," +
				"deseja continur ?"))
			return;
		if (presenter.getVo().getListaMesas().size()>0){
			List<EncontroRestauranteMesa> restauranteMesa = presenter.getVo().getListaEncontroRestauranteMesa();
			for (EncontroRestauranteMesa mesa : restauranteMesa) {
				mesa.setEncontroAfilhado1(null);
				mesa.setEncontroAfilhado2(null);
				mesa.setEncontroGarcon(null);
			}
			presenter.salvar();
		}
	}

	@UiHandler("encontroMaitreButton")
	public void encontroMaitreButtonButtonClickHandler(ClickEvent event){
		edita(getRestauranteSelecionado());
	}

	@UiHandler("gerarDistribuicaoButton")
	public void gerarDistribuicaoButtonClickHandler(ClickEvent event){
		if (presenter.getVo().getListaMesas().size()>0){
			boolean achou = false;
			for (Mesa mesa : presenter.getVo().getListaMesas()) {
				EncontroRestauranteMesa encontroRestauranteMesa = getEncontroRestauranteMesa(presenter.getVo(),mesa,getRestauranteSelecionado());
				if ((encontroRestauranteMesa.getEncontroGarcon() != null) || (encontroRestauranteMesa.getEncontroAfilhado1() != null) ||
						(encontroRestauranteMesa.getEncontroAfilhado2() != null)){
					achou = true;
				}
			}
			if(achou){
				if ( !Window.confirm("Há distribuição já lançada, deseja continur, os dados serão apagados ?"))
					return;
				for (Mesa mesa : presenter.getVo().getListaMesas()) {
					EncontroRestauranteMesa encontroRestauranteMesa = getEncontroRestauranteMesa(presenter.getVo(),mesa,getRestauranteSelecionado());
					encontroRestauranteMesa.setEncontroAfilhado1(null);
					encontroRestauranteMesa.setEncontroAfilhado2(null);
					encontroRestauranteMesa.setEncontroGarcon(null);
				}

			}

			int tentativa=0;
			do{
				tentativa++;
			}while(!geraDistribuicao() && (tentativa < 1500));

			showWaitMessage(true);
			if (tentativa==1500){
				Window.alert("Não foi possivel gerar a distribuição!");
				for (Mesa mesa : presenter.getVo().getListaMesas()) {
					EncontroRestauranteMesa encontroRestauranteMesa = getEncontroRestauranteMesa(presenter.getVo(),mesa,getRestauranteSelecionado());
					encontroRestauranteMesa.setEncontroAfilhado1(null);
					encontroRestauranteMesa.setEncontroAfilhado2(null);
					encontroRestauranteMesa.setEncontroGarcon(null);
				}
			}
			showWaitMessage(false);
			presenter.salvar();
		}
	}

	public boolean geraDistribuicao(){
		if (presenter.getVo().getListaMesas().size()>0){
			for (Mesa mesa : presenter.getVo().getListaMesas()) {
				EncontroRestauranteMesa encontroRestauranteGarcon = getEncontroRestauranteMesa(presenter.getVo(),mesa,getRestauranteSelecionado());
				EncontroInscricao afilhado1 = getAfilhalhadoSorteio(null,mesa,getRestauranteSelecionado(),1);
				EncontroInscricao afilhado2 = null;
				EncontroInscricao garcon = getGarconSorteio(afilhado1,afilhado2,mesa,getRestauranteSelecionado());
				if (mesa.getQuantidadeCasais()==2){
					afilhado2 = getAfilhalhadoSorteio(afilhado1,mesa,getRestauranteSelecionado(),2);
					if ((afilhado1==null) || (afilhado2 == null))
						return false;
				}else{
					if (afilhado1==null)
						return false;
				}
				encontroRestauranteGarcon.setEncontroAfilhado1(afilhado1);
				encontroRestauranteGarcon.setEncontroAfilhado2(afilhado2);
				encontroRestauranteGarcon.setEncontroGarcon(garcon);
			}
			return true;
		}
		return false;
	}

	private EncontroInscricao getAfilhalhadoSorteio(EncontroInscricao afilhado1, Mesa mesa, EncontroRestaurante restauranteSelecionado,int posicao) {
		EncontroInscricao encontroInscricao=null;
		int tentativa=0;
		do{
			int randon = Random.nextInt(presenter.getEncontroSelecionado().getQuantidadeAfilhados());
			encontroInscricao = presenter.getVo().getListaAfilhados().get(randon);
			if (isSelecionadoRestaurante(encontroInscricao,restauranteSelecionado,mesa,afilhado1))
				encontroInscricao = null;
			if (isSelecionadoMesa(encontroInscricao,restauranteSelecionado,mesa))
				encontroInscricao = null;
			if (isSelecionadoOutroAfilhado(encontroInscricao,restauranteSelecionado,afilhado1))
				encontroInscricao = null;
			if (isSelecionadoMesaMesmaPosicao(encontroInscricao,restauranteSelecionado,posicao))
				encontroInscricao = null;
			tentativa++;
		}while((encontroInscricao == null) && (tentativa < 100));
		return encontroInscricao;
	}

	private boolean isSelecionadoOutroAfilhado( EncontroInscricao encontroInscricao, EncontroRestaurante restaurante, EncontroInscricao outroAfilhado) {
		if( (encontroInscricao==null) || (outroAfilhado == null)) return false;
		for (EncontroRestauranteMesa encontroRestaurante : presenter.getVo().getListaEncontroRestauranteMesaOutros()) {
			if ((encontroRestaurante.getEncontroAfilhado1()!=null) && encontroRestaurante.getEncontroAfilhado1().equals(encontroInscricao)){
				if ((encontroRestaurante.getEncontroAfilhado2()!=null) && encontroRestaurante.getEncontroAfilhado2().equals(outroAfilhado)){
					return true;
				}
			}
			if ((encontroRestaurante.getEncontroAfilhado2()!=null) && encontroRestaurante.getEncontroAfilhado2().equals(encontroInscricao)){
				if ((encontroRestaurante.getEncontroAfilhado1()!=null) && encontroRestaurante.getEncontroAfilhado1().equals(outroAfilhado)){
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSelecionadoMesa(EncontroInscricao encontroInscricao, EncontroRestaurante restaurante, Mesa mesa) {
		if( encontroInscricao==null) return false;
		if (!mesa.getRestaurante().isCheckMesa()) return false;
		for (EncontroRestauranteMesa encontroRestaurante : presenter.getVo().getListaEncontroRestauranteMesaOutros()) {
			if (encontroRestaurante.getMesa().getRestaurante().isCheckMesa()){
				if (encontroInscricao.getTipo().equals(TipoInscricaoEnum.AFILHADO)){
					if ((encontroRestaurante.getEncontroAfilhado1()!=null) && encontroRestaurante.getEncontroAfilhado1().equals(encontroInscricao)){
						if (encontroRestaurante.getMesa().equals(mesa)){
							return true;
						}
					}
					if ((encontroRestaurante.getEncontroAfilhado2()!=null) && encontroRestaurante.getEncontroAfilhado2().equals(encontroInscricao)){
						if (encontroRestaurante.getMesa().equals(mesa)){
							return true;
						}
					}
				}else {
					if ((encontroRestaurante.getEncontroGarcon()!=null) && encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
						if (encontroRestaurante.getMesa().equals(mesa)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isSelecionadoMesaMesmaPosicao(EncontroInscricao encontroInscricao, EncontroRestaurante restaurante, int posicao) {
		if ( getListaRestaurantes().size() == 1 ) return false;
		if (!restaurante.getRestaurante().isCheckMesa()) return false;
		if( encontroInscricao==null) return false;
		int qtde=0;
		for (EncontroRestauranteMesa encontroRestaurante : presenter.getVo().getListaEncontroRestauranteMesaOutros()) {
			if (encontroRestaurante.getMesa().getRestaurante().isCheckMesa()){
				if ((posicao == 1) && (encontroRestaurante.getEncontroAfilhado1()!=null) && encontroRestaurante.getEncontroAfilhado1().equals(encontroInscricao)){
					qtde++;
				}
				if ((posicao == 2) && (encontroRestaurante.getEncontroAfilhado2()!=null) && encontroRestaurante.getEncontroAfilhado2().equals(encontroInscricao)){
					qtde++;
				}
			}
		}
		int qtdemax = getListaRestaurantes().size() / 2;
		if( (getListaRestaurantes().size() % 2) == 1) qtdemax++;
		if (qtde > qtdemax )
			return true;
		return false;
	}

	private boolean isSelecionadoCasal(EncontroInscricao encontroInscricao, EncontroRestaurante restaurante, EncontroInscricao afilhado1, EncontroInscricao afilhado2) {
		if( encontroInscricao==null) return false;
		for (EncontroRestauranteMesa encontroRestaurante : presenter.getVo().getListaEncontroRestauranteMesaOutros()) {
			if (encontroRestaurante.getEncontroGarcon()!=null){
				if ((encontroRestaurante.getEncontroAfilhado1()!=null) && encontroRestaurante.getEncontroAfilhado1().equals(afilhado1) &&
						encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
					return true;
				}
				if ((encontroRestaurante.getEncontroAfilhado2()!=null) && encontroRestaurante.getEncontroAfilhado2().equals(afilhado2) &&
						encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
					return true;
				}
			}
		}
		return false;
	}

	private boolean isSelecionadoRestaurante( EncontroInscricao encontroInscricao, EncontroRestaurante restaurante, Mesa mesa, EncontroInscricao outroAfilhado) {
		if( encontroInscricao==null) return false;
		for (EncontroRestauranteMesa encontroRestaurante : presenter.getVo().getListaEncontroRestauranteMesa()) {
			if (encontroRestaurante.getEncontroRestaurante().equals(restaurante)){
				if (encontroInscricao.getTipo().equals(TipoInscricaoEnum.AFILHADO)){
					if ((outroAfilhado != null) && encontroInscricao.equals(outroAfilhado)) return true;
					if (!encontroRestaurante.getMesa().equals(mesa)){
						if ((encontroRestaurante.getEncontroAfilhado1()!=null) && encontroRestaurante.getEncontroAfilhado1().equals(encontroInscricao)){
							return true;
						}
						if ((encontroRestaurante.getEncontroAfilhado2()!=null) && encontroRestaurante.getEncontroAfilhado2().equals(encontroInscricao)){
							return true;
						}
					}
				}
				else {
					if (!encontroRestaurante.getMesa().equals(mesa)){
						if ((encontroRestaurante.getEncontroGarcon()!=null) && encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isSelecionadoRestauranteGrupo( EncontroInscricao encontroInscricao, EncontroRestaurante restaurante, Mesa mesa) {
		if( encontroInscricao==null) return false;
		for (EncontroRestauranteMesa encontroRestaurante : presenter.getVo().getListaEncontroRestauranteMesa()) {
			if (encontroRestaurante.getEncontroRestaurante().equals(restaurante)){
				if ((encontroRestaurante.getMesa().getGrupo() != null) && !encontroRestaurante.getMesa().getGrupo().equals(mesa.getGrupo())){
					if ((encontroRestaurante.getEncontroGarcon()!=null) && encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
						return true;
					}
				}
				if (encontroRestaurante.getMesa().getGrupo()==null){
					if ((encontroRestaurante.getEncontroGarcon()!=null) && encontroRestaurante.getEncontroGarcon().equals(encontroInscricao)){
						return true;
					}
				}
			}
		}
		return false;
	}

	private EncontroInscricao getGarconSorteio(EncontroInscricao afilhado1, EncontroInscricao afilhado2, Mesa mesa, EncontroRestaurante restauranteSelecionado) {
		EncontroInscricao encontroInscricao=null;
		if ((listaGarcons!=null) && (listaGarcons.size() > 0)){
			int tentativa=0;
			do{
				if ( mesa.getGrupo() == null ) {
					int randon = Random.nextInt(listaGarcons.size());
					encontroInscricao = listaGarcons.get(randon);
					if (isSelecionadoRestaurante(encontroInscricao,restauranteSelecionado,mesa,null))
						encontroInscricao = null;
				}else{
					encontroInscricao = getGarconGrupo(mesa.getGrupo());
					if (encontroInscricao == null){
						int randon = Random.nextInt(listaGarcons.size());
						encontroInscricao = listaGarcons.get(randon);
					}
					if (isSelecionadoRestauranteGrupo(encontroInscricao,restauranteSelecionado,mesa))
						encontroInscricao = null;
				}
				if (isSelecionadoCasal(encontroInscricao,restauranteSelecionado,afilhado1,afilhado2))
					encontroInscricao = null;
				if (isSelecionadoAfilhado(encontroInscricao,restauranteSelecionado,afilhado1,afilhado2))
					encontroInscricao = null;
				tentativa++;
			}while((encontroInscricao == null) && (tentativa < 1000));
		}
		return encontroInscricao;
	}

	private EncontroInscricao getGarconGrupo(RestauranteGrupo grupo) {
		EncontroInscricao encontroInscricao = null;
		if( grupo==null) return null;
		for (EncontroRestauranteMesa encontroRestaurante : presenter.getVo().getListaEncontroRestauranteMesa()) {
			if (encontroRestaurante.getMesa().getGrupo().equals(grupo))
				return encontroRestaurante.getEncontroGarcon();
		}
		return encontroInscricao;
	}

	private boolean isSelecionadoAfilhado(EncontroInscricao encontroInscricao, EncontroRestaurante restauranteSelecionado2, EncontroInscricao afilhado1, EncontroInscricao afilhado2) {
		if( encontroInscricao==null) return false;
		if((afilhado1 != null) && afilhado1.getCasal().getCasalPadrinho().equals(encontroInscricao.getCasal()))
			return true;
		if((afilhado2 != null) && afilhado2.getCasal().getCasalPadrinho().equals(encontroInscricao.getCasal()))
			return true;
		return false;
	}

	@Override
	public void setListaRestaurantes(List<EncontroRestaurante> lista) {
		listaRestaurantes = lista;
		ListBoxUtil.populate(restauranteListBox, false, lista );
		if (presenter.getEncontroSelecionado().getDataPublicacaoRestaurante()==null){
			gerarDistribuicaoButton.setVisible(true);
			encontroMaitreButton.setVisible(true);
			limpaDistribuicaoButton.setVisible(true);
		}
	}

	public List<EncontroRestaurante> getListaRestaurantes() {
		return listaRestaurantes;
	}

}