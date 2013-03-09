package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RestauranteView extends BaseView<RestaurantePresenter> implements RestaurantePresenter.Display {

	@UiTemplate("RestauranteView.ui.xml")
	interface RestauranteViewUiBinder extends UiBinder<Widget, RestauranteView> {}
	private RestauranteViewUiBinder uiBinder = GWT.create(RestauranteViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;

	@UiField NumberTextBox quantidadeMesas;
	@UiField NumberTextBox quantidadeCasaisPorMesa;
	@UiField TextBox nomeTextBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;


	@UiField(provided=true) FlexTable restauranteFlexTable;
	private FlexTableUtil restauranteTableUtil = new FlexTableUtil();


	private Restaurante entidadeEditada;

	public RestauranteView() {
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
		restauranteTableUtil.addColumn("Qtde Mesas", null, HasHorizontalAlignment.ALIGN_LEFT);
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
		entidadeEditada.setNome(nomeTextBox.getValue());
		entidadeEditada.setQuantidadeMesas(Integer.parseInt(quantidadeMesas.getText()));
		presenter.salvar(entidadeEditada);
	}
	private void edita(Restaurante restaurante) {
		limpaCampos();
		if(restaurante == null){
			entidadeEditada = new Restaurante();
		} else {
			entidadeEditada = restaurante;
			defineCampos(restaurante);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}

	public void limpaCampos(){
		nomeTextBox.setValue(null);
		quantidadeMesas.setValue(null);
		quantidadeCasaisPorMesa.setValue(null);
	}

	public void defineCampos(Restaurante restaurante){
		nomeTextBox.setValue(restaurante.getNome());
		quantidadeMesas.setValue(restaurante.getQuantidadeMesas().toString());
		quantidadeCasaisPorMesa.setValue(restaurante.getQuantidadeCasaisPorMesa().toString());

	}

	@Override
	public String getDisplayTitle() {
		return "Cadastro de Restaurantes";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		restauranteTableUtil.clearData();
	}
	@Override
	public void populaRestaurantes(List<Restaurante> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "restaurante", "restaurantes", "");
		restauranteTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Restaurante restaurante: lista) {
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
			dados[1] = restaurante.getNome();
			dados[2] = restaurante.getQuantidadeMesas().toString();
			restauranteTableUtil.addRow(dados,row+1);
			row++;
		}
		restauranteTableUtil.applyDataRowStyles();
	}

}