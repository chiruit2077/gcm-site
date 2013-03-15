package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.model.Organograma;

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

public class OrganogramaView extends BaseView<OrganogramaPresenter> implements OrganogramaPresenter.Display {

	@UiTemplate("OrganogramaView.ui.xml")
	interface ViewUiBinder extends UiBinder<Widget, OrganogramaView> {}
	private ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;

	@UiField TextBox nomeTextBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;


	@UiField(provided=true) FlexTable organogramaFlexTable;
	private FlexTableUtil organogramaTableUtil = new FlexTableUtil();


	private Organograma entidadeEditada;

	public OrganogramaView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}

	private void criaTabela() {
		organogramaFlexTable = new FlexTable();
		organogramaFlexTable.setStyleName("portal-formSmall");
		organogramaTableUtil.initialize(organogramaFlexTable);

		organogramaTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		organogramaTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
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
		presenter.salvar(entidadeEditada);
	}
	private void edita(Organograma organograma) {
		limpaCampos();
		if(organograma == null){
			entidadeEditada = new Organograma();
		} else {
			entidadeEditada = organograma;
			defineCampos(organograma);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}

	public void limpaCampos(){
		nomeTextBox.setValue(null);
	}

	public void defineCampos(Organograma organograma){
		nomeTextBox.setValue(organograma.getNome());
	}

	@Override
	public String getDisplayTitle() {
		return "Cadastro de Organogramas";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		organogramaTableUtil.clearData();
	}
	@Override
	public void populaOrganogramas(List<Organograma> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "organograma", "organogramas", "");
		organogramaTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Organograma organograma: lista) {
			Object dados[] = new Object[2];

			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(organograma);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este organograma?")){
						presenter.excluir(organograma);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);

			dados[0] = hp;
			dados[1] = organograma.getNome();
			organogramaTableUtil.addRow(dados,row+1);
			row++;
		}
		organogramaTableUtil.applyDataRowStyles();
	}

}