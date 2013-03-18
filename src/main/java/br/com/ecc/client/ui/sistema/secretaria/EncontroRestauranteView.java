package br.com.ecc.client.ui.sistema.secretaria;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.Restaurante;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class EncontroRestauranteView extends BaseView<EncontroRestaurantePresenter> implements EncontroRestaurantePresenter.Display {

	@UiTemplate("EncontroRestauranteView.ui.xml")
	interface EncontroHotelViewUiBinder extends UiBinder<Widget, EncontroRestauranteView> {}
	private EncontroHotelViewUiBinder uiBinder = GWT.create(EncontroHotelViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;

	@UiField ListBox restauranteListBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;


	@UiField(provided=true) FlexTable restauranteFlexTable;
	private FlexTableUtil restauranteTableUtil = new FlexTableUtil();

	private EncontroRestaurante entidadeEditada;

	public EncontroRestauranteView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}

	private void criaTabela() {
		restauranteFlexTable = new FlexTable();
		restauranteFlexTable.setStyleName("portal-formSmall");
		restauranteTableUtil.initialize(restauranteFlexTable);

		restauranteTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		restauranteTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
		restauranteTableUtil.addColumn("Qtde Mesas", "100", HasHorizontalAlignment.ALIGN_LEFT);
	}

	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event){
		edita(null);
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		Restaurante restaurante = (Restaurante) ListBoxUtil.getItemSelected(restauranteListBox, presenter.getListaRestaurantes());
		entidadeEditada.setRestuarante(restaurante);
		presenter.salvar(entidadeEditada);
	}
	private void edita(EncontroRestaurante restaurante) {
		limpaCampos();
		if(restaurante == null){
			entidadeEditada = new EncontroRestaurante();
			entidadeEditada.setEncontro(presenter.getEncontroSelecionado());
		} else {
			entidadeEditada = restaurante;
			defineCampos(restaurante);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		restauranteListBox.setFocus(true);
	}

	public void limpaCampos(){
		restauranteListBox.setSelectedIndex(-1);
	}

	public void defineCampos(EncontroRestaurante restaurante){
		ListBoxUtil.setItemSelected(restauranteListBox, restaurante.getRestaurante().getNome());

	}

	@Override
	public String getDisplayTitle() {
		return "Restaurantes do Encontro";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		restauranteTableUtil.clearData();
	}

	@Override
	public void populaEncontroRestaurantes(List<EncontroRestaurante> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "restaurante", "restaurantes", "");
		restauranteTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroRestaurante restaurante: lista) {
			Object dados[] = new Object[3];

			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(restaurante);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este restaurante?")){
						presenter.excluir(restaurante);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);

			dados[0] = hp;
			dados[1] = restaurante.getRestaurante().getNome();
			dados[2] = restaurante.getRestaurante().getQuantidadeMesas().toString();
			restauranteTableUtil.addRow(dados,row+1);
			row++;
		}
		restauranteTableUtil.applyDataRowStyles();
	}

	public EncontroRestaurante getEntidadeEditada() {
		return entidadeEditada;
	}

	public void setEntidadeEditada(EncontroRestaurante entidadeEditada) {
		this.entidadeEditada = entidadeEditada;
	}

	@Override
	public void populaRestaurantes(List<Restaurante> lista) {
		ListBoxUtil.populate(restauranteListBox, false, lista);

	}

}