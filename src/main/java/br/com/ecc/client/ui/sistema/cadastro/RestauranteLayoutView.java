package br.com.ecc.client.ui.sistema.cadastro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableHelper;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Mesa;
import br.com.ecc.model.Restaurante;
import br.com.ecc.model.RestauranteGrupo;
import br.com.ecc.model.RestauranteTitulo;
import br.com.ecc.model.vo.RestauranteVO;

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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RestauranteLayoutView extends BaseView<RestauranteLayoutPresenter> implements RestauranteLayoutPresenter.Display {

	@UiTemplate("RestauranteLayoutView.ui.xml")
	interface RestauranteLayoutViewUiBinder extends UiBinder<Widget, RestauranteLayoutView> {}
	private RestauranteLayoutViewUiBinder uiBinder = GWT.create(RestauranteLayoutViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField FlexTable distribuicaoPanel;
	@UiField(provided=true) FlexTable grupoFlexTable;
	private FlexTableUtil grupoTableUtil = new FlexTableUtil();
	@UiField ListBox restauranteListBox;
	@UiField DialogBox editaDialogBox;
	@UiField DialogBox editaGruposDialogBox;
	@UiField DialogBox editaGrupoDialogBox;
	@UiField HTMLPanel mesaDadosPanel;
	@UiField Label valorLabel;
	@UiField TextBox valorTextBox;
	@UiField TextBox nomeGrupoTextBox;
	@UiField ListBox grupoListBox;
	@UiField NumberTextBox quantidadeCasaisTextBox;
	@UiField NumberTextBox linhaTextBox;
	@UiField NumberTextBox linhaSpamTextBox;
	@UiField NumberTextBox colunaTextBox;
	@UiField NumberTextBox colunaSpamTextBox;
	@UiField Button salvarButton;
	@UiField Button addMesaButton;
	@UiField Button addTituloButton;
	@UiField Button salvarMesaButton;
	@UiField Button fecharMesaButton;
	@UiField Button salvarGruposButton;
	@UiField Button adicionarGrupoButton;
	@UiField Button excluirGruposButton;
	@UiField Label itemGrupoTotal;
	@UiField Button fecharGruposButton;
	@UiField Button editaGruposButton;
	@UiField Button salvarGrupoButton;
	@UiField Button fecharGrupoButton;

	private Restaurante restauranteSelecionado;
	private Mesa entidadeMesaEditada;
	private RestauranteTitulo entidadeTituloEditada;
	private RestauranteGrupo entidadeGrupoEditada;

	private List<Restaurante> listaRestaurantes;

	protected VerticalPanel mesaPanelEditado;

	private List<RestauranteGrupo> listaGrupos;


	public RestauranteLayoutView() {
		criaTableGrupo();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}

	private void criaTableGrupo() {
		grupoFlexTable = new FlexTable();
		grupoFlexTable.setStyleName("portal-formSmall");
		grupoTableUtil.initialize(grupoFlexTable);

		grupoTableUtil.addColumn("", "20", HasHorizontalAlignment.ALIGN_CENTER);
		grupoTableUtil.addColumn("Nome", null, HasHorizontalAlignment.ALIGN_LEFT);
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
	public void fecharMesaButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}

	@UiHandler("excluirMesaButton")
	public void excluirMesaButtonClickHandler(ClickEvent event){
		if (entidadeMesaEditada!=null ){
			presenter.getVo().getListaMesas().remove(entidadeMesaEditada);
		}else if (entidadeTituloEditada!=null){
			presenter.getVo().getListaTitulos().remove(entidadeTituloEditada);
		}
		if (mesaPanelEditado != null)
			distribuicaoPanel.remove(mesaPanelEditado);
		editaDialogBox.hide();
	}

	@UiHandler("editaGruposButton")
	public void editaGruposButtonClickHandle(ClickEvent event){
		listaGrupos = new ArrayList<RestauranteGrupo>(presenter.getVo().getListaGrupos());
		populaGrupos();
		editaGruposDialogBox.center();
		editaGruposDialogBox.show();
	}

	@UiHandler("excluirGruposButton")
	public void excluirGruposButtonClickHandle(ClickEvent event){
		listaGrupos.clear();
		populaGrupos();
	}

	@UiHandler("adicionarGrupoButton")
	public void adicionarGrupoButtonClickHandler(ClickEvent event){
		entidadeGrupoEditada = null;
		editaGrupo(null);
	}

	@UiHandler("fecharGrupoButton")
	public void fecharGrupoButtonClickHandler(ClickEvent event){
		editaGrupoDialogBox.hide();
	}

	@UiHandler("fecharGruposButton")
	public void fecharGruposButtonClickHandler(ClickEvent event){
		editaGruposDialogBox.hide();
	}

	@UiHandler("salvarGrupoButton")
	public void salvarGrupoButtonClickHandler(ClickEvent event){
		if(nomeGrupoTextBox.getValue()==null || nomeGrupoTextBox.getValue().equals("")){
			Window.alert("Informe o nome do grupo");
			return;
		}
		if(entidadeGrupoEditada==null){
			RestauranteGrupo grupo = new RestauranteGrupo();
			grupo.setRestaurante(getRestauranteSelecionado());
			entidadeGrupoEditada = grupo;
			listaGrupos.add(grupo);
		}
		entidadeGrupoEditada.setNome(nomeGrupoTextBox.getText());
		editaGrupoDialogBox.hide();
		populaGrupos();
	}

	@UiHandler("salvarGruposButton")
	public void salvarGruposButtonClickHandler(ClickEvent event){
		for (Mesa mesa: presenter.getVo().getListaMesas()){
			if (mesa.getGrupo() != null && !listaGrupos.contains(mesa.getGrupo())){
				mesa.setGrupo(null);
			}
		}
		presenter.getVo().getListaGrupos().clear();
		presenter.getVo().getListaGrupos().addAll(listaGrupos);
		editaGruposDialogBox.hide();
		populaEntidades(presenter.getVo());
	}

	private void editaGrupo(RestauranteGrupo grupo) {
		limpaCamposGrupo();
		defineCamposGrupo(grupo);
		editaGrupoDialogBox.center();
		editaGrupoDialogBox.show();
	}

	private void limpaCamposGrupo() {
		nomeGrupoTextBox.setText(null);
	}
	private void defineCamposGrupo(RestauranteGrupo grupo){
		if (grupo != null)
			nomeGrupoTextBox.setText(grupo.getNome());
	}

	private void populaGrupos() {
		grupoTableUtil.clearData();
		int row = 0;
		Image excluir, editar;
		HorizontalPanel hp;
		for (final RestauranteGrupo grupo: listaGrupos) {
			Object dados[] = new Object[2];

			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					editaGrupo(grupo);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este grupo ?")){
						listaGrupos.remove(grupo);
						populaGrupos();
					}
				}
			});

			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);

			dados[0] = hp;
			dados[1] = grupo.getNome();
			grupoTableUtil.addRow(dados,row+1);
			row++;
		}
		LabelTotalUtil.setTotal(itemGrupoTotal, row, "grupo", "grupos", "");
		grupoTableUtil.applyDataRowStyles();
	}

	@UiHandler("addMesaButton")
	public void addMesaButtonClickHandler(ClickEvent event){
		limpaCampos();
		mesaPanelEditado = null;
		defineCampos(new Mesa());
		editaDialogBox.setText("Dados da Mesa");
		editaDialogBox.center();
		editaDialogBox.show();
	}

	@UiHandler("addTituloButton")
	public void addTituloButtonClickHandler(ClickEvent event){
		limpaCampos();
		mesaPanelEditado = null;
		defineCampos(new RestauranteTitulo());
		editaDialogBox.setText("Dados do Titulo");
		editaDialogBox.center();
		editaDialogBox.show();
	}

	@UiHandler("salvarMesaButton")
	public void salvarMesaButtonClickHandler(ClickEvent event){
		if (entidadeMesaEditada!=null){
			if(valorTextBox.getValue()==null || valorTextBox.getValue().equals("")){
				Window.alert("Informe o Numero da Mesa");
			}
			if(quantidadeCasaisTextBox.getValue()==null || quantidadeCasaisTextBox.getValue().equals("")){
				Window.alert("Informe a Quantidade de Casais");
			}
		}
		if (entidadeTituloEditada!=null){
			Window.alert("Informe o Titulo");
			return;
		}
		if(linhaTextBox.getValue()==null || linhaTextBox.getValue().equals("")){
			Window.alert("Informe a linha");
			return;
		}
		if(colunaTextBox.getValue()==null || colunaTextBox.getValue().equals("")){
			Window.alert("Informe a coluna");
			return;
		}
		if (entidadeMesaEditada!=null){
			entidadeMesaEditada.setRestaurante(getRestauranteSelecionado());
			entidadeMesaEditada.setNumero(valorTextBox.getValue());
			if (quantidadeCasaisTextBox.getValue()!= null && !quantidadeCasaisTextBox.getValue().equals("")){
				entidadeMesaEditada.setQuantidadeCasais(Integer.valueOf(quantidadeCasaisTextBox.getValue()));
			}
			entidadeMesaEditada.setGrupo((RestauranteGrupo) ListBoxUtil.getItemSelected(grupoListBox, presenter.getVo().getListaGrupos()));
			entidadeMesaEditada.setLinha(Integer.valueOf(linhaTextBox.getValue()));
			entidadeMesaEditada.setColuna(Integer.valueOf(colunaTextBox.getValue()));
			entidadeMesaEditada.setLinhaSpam(null);
			if (linhaSpamTextBox.getValue()!= null && !linhaSpamTextBox.getValue().equals("")){
				entidadeMesaEditada.setLinhaSpam(Integer.valueOf(linhaSpamTextBox.getValue()));
			}
			entidadeMesaEditada.setColunaSpam(null);
			if (colunaSpamTextBox.getValue()!= null && !linhaSpamTextBox.getValue().equals("")){
				entidadeMesaEditada.setColunaSpam(Integer.valueOf(colunaSpamTextBox.getValue()));
			}
			if (mesaPanelEditado == null){
				presenter.getVo().getListaMesas().add(entidadeMesaEditada);
			}
		}else if (entidadeTituloEditada!=null){
			entidadeTituloEditada.setRestaurante(getRestauranteSelecionado());
			entidadeTituloEditada.setTitulo(valorTextBox.getValue());
			entidadeTituloEditada.setLinha(Integer.valueOf(linhaTextBox.getValue()));
			entidadeTituloEditada.setColuna(Integer.valueOf(colunaTextBox.getValue()));
			entidadeTituloEditada.setLinhaSpam(null);
			if (linhaSpamTextBox.getNumber()!= null && !linhaSpamTextBox.getValue().equals("")){
				entidadeTituloEditada.setLinhaSpam(linhaSpamTextBox.getNumber().intValue());
			}
			entidadeTituloEditada.setColunaSpam(null);
			if (colunaSpamTextBox.getValue()!= null && !colunaSpamTextBox.getValue().equals("")){
				entidadeTituloEditada.setColunaSpam(Integer.valueOf(colunaSpamTextBox.getValue()));
			}
			if (mesaPanelEditado == null){
				presenter.getVo().getListaTitulos().add(entidadeTituloEditada);
			}
		}
		populaEntidades(presenter.getVo());
		editaDialogBox.hide();

	}

	private void edita(Mesa mesa) {
		limpaCampos();
		defineCampos(mesa);
		editaDialogBox.setText("Dados da Mesa");
		editaDialogBox.center();
		editaDialogBox.show();
	}

	private void edita(RestauranteTitulo titulo) {
		limpaCampos();
		defineCampos(titulo);
		editaDialogBox.setText("Dados do Titulo");
		editaDialogBox.center();
		editaDialogBox.show();
	}

	@UiHandler("restauranteListBox")
	public void restauranteListBoxListBoxChangeHandler(ChangeEvent event) {
		Restaurante restaurante = (Restaurante) ListBoxUtil.getItemSelected(restauranteListBox, getListaRestaurantes());
		setRestauranteSelecionado(restaurante);
		presenter.setRestauranteSelecionado(restaurante);
		presenter.buscaVO();
	}

	public void defineCampos(Mesa mesa){
		mesaDadosPanel.setVisible(true);
		ListBoxUtil.populate(grupoListBox, true, presenter.getVo().getListaGrupos());
		entidadeMesaEditada = mesa;
		valorLabel.setText("Número da Mesa:");
		valorTextBox.setText(mesa.getNumero());
		quantidadeCasaisTextBox.setText(mesa.getQuantidadeCasais().toString());
		if (mesa.getGrupo()!=null)
			ListBoxUtil.setItemSelected(grupoListBox, mesa.getGrupo().toString());
		if (mesa.getLinha() != null)
			linhaTextBox.setText(mesa.getLinha().toString());
		if (mesa.getLinhaSpam() != null)
			linhaSpamTextBox.setText(mesa.getLinhaSpam().toString());
		if (mesa.getColuna() != null)
			colunaTextBox.setText(mesa.getColuna().toString());
		if (mesa.getColunaSpam() != null )
			colunaSpamTextBox.setText(mesa.getColunaSpam().toString());
	}

	public void defineCampos(RestauranteTitulo titulo){
		entidadeTituloEditada = titulo;
		valorLabel.setText("Título:");
		valorTextBox.setText(titulo.getTitulo());
		if (titulo.getLinha() != null)
			linhaTextBox.setText(titulo.getLinha().toString());
		if (titulo.getLinhaSpam() != null)
			linhaSpamTextBox.setText(titulo.getLinhaSpam().toString());
		if (titulo.getColuna() != null)
			colunaTextBox.setText(titulo.getColuna().toString());
		if (titulo.getColunaSpam() != null )
			colunaSpamTextBox.setText(titulo.getColunaSpam().toString());
	}

	public void limpaCampos(){
		grupoListBox.clear();
		mesaDadosPanel.setVisible(false);
		entidadeMesaEditada = null;
		entidadeTituloEditada = null;
		valorLabel.setText(null);
		valorTextBox.setText(null);
		linhaTextBox.setText(null);
		linhaSpamTextBox.setText(null);
		colunaTextBox.setText(null);
		colunaSpamTextBox.setText(null);
	}

	@Override
	public String getDisplayTitle() {
		return "Layouts dos Restaurantes";
	}

	@Override
	public void reset() {
		distribuicaoPanel.removeAllRows();
		distribuicaoPanel.clear();
	}

	@Override
	public void populaEntidades(RestauranteVO vo) {
		distribuicaoPanel.removeAllRows();
		distribuicaoPanel.clear();
		distribuicaoPanel.clear(true);
		distribuicaoPanel.setCellSpacing(10);
		distribuicaoPanel.setStyleName("restaurante");

		if (vo.getListaTitulos().size()>0){
			for (final RestauranteTitulo titulo : vo.getListaTitulos()) {
				Widget widget = geraTituloWidget(titulo);
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
				Widget widget = geraMesaWidget(mesa);
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
		showWaitMessage(false);
	}

	private Widget geraTituloWidget(final RestauranteTitulo titulo) {
		final VerticalPanel mesaPanel = new VerticalPanel();
		mesaPanel.setSize("200px", "50px");
		mesaPanel.setStyleName("restaurante-Panel");

		FocusPanel focusPanel = new FocusPanel();
		focusPanel.setSize("200px", "50px");
		final VerticalPanel tituloMesa = new VerticalPanel();
		tituloMesa.setSize("200px", "50px");
		tituloMesa.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		tituloMesa.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		tituloMesa.add(new Label(titulo.getTitulo()));

		focusPanel.add(tituloMesa);
		mesaPanel.add(focusPanel);
		focusPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mesaPanelEditado = mesaPanel;
				entidadeTituloEditada = titulo;
				edita(titulo);
			}
		});
		return mesaPanel;
	}

	private Widget geraMesaWidget(final Mesa mesa) {
			final VerticalPanel mesaPanel = new VerticalPanel();
			mesaPanel.setSize("200px", "100px");
			mesaPanel.setStyleName("restaurante-Panel");

			FocusPanel focusPanel = new FocusPanel();
			focusPanel.setSize("200px", "100px");
			final VerticalPanel tituloMesa = new VerticalPanel();
			tituloMesa.setSize("200px", "100px");
			tituloMesa.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
			tituloMesa.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
			tituloMesa.add(new Label("MESA " + mesa.getNumero()));
			if (mesa.getGrupo() != null)
				tituloMesa.add(new Label(mesa.getGrupo().toString()));
			focusPanel.add(tituloMesa);
			mesaPanel.add(focusPanel);

			focusPanel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					mesaPanelEditado = mesaPanel;
					entidadeMesaEditada = mesa;
					edita(mesa);
				}
			});
			return mesaPanel;
	}

	public Restaurante getRestauranteSelecionado() {
		return restauranteSelecionado;
	}

	@Override
	public void setRestauranteSelecionado(Restaurante restauranteSelecionado) {
		this.restauranteSelecionado = restauranteSelecionado;
		ListBoxUtil.setItemSelected(restauranteListBox, restauranteSelecionado.toString());

	}

	@Override
	public void setListaRestaurantes(List<Restaurante> lista) {
		this.listaRestaurantes = lista;
		ListBoxUtil.populate(restauranteListBox, false, lista );
	}

	public List<Restaurante> getListaRestaurantes() {
		return listaRestaurantes;
	}

}