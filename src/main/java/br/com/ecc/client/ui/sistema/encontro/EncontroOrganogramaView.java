package br.com.ecc.client.ui.sistema.encontro;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.EncontroOrganograma;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class EncontroOrganogramaView extends BaseView<EncontroOrganogramaPresenter> implements EncontroOrganogramaPresenter.Display {

	@UiTemplate("EncontroOrganogramaView.ui.xml")
	interface EncontroHotelViewUiBinder extends UiBinder<Widget, EncontroOrganogramaView> {}
	private EncontroHotelViewUiBinder uiBinder = GWT.create(EncontroHotelViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;

	@UiField ListBox organogramaListBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;


	@UiField(provided=true) FlexTable organogramaFlexTable;
	private FlexTableUtil organogramaTableUtil = new FlexTableUtil();

	private EncontroOrganograma entidadeEditada;

	public EncontroOrganogramaView() {
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
		Organograma organograma = (Organograma) ListBoxUtil.getItemSelected(organogramaListBox, presenter.getListaOrganogramas());
		entidadeEditada.setOrganograma(organograma);
		presenter.salvar(entidadeEditada);
	}
	private void edita(EncontroOrganograma organograma) {
		limpaCampos();
		if(organograma == null){
			entidadeEditada = new EncontroOrganograma();
			entidadeEditada.setEncontro(presenter.getEncontroSelecionado());
		} else {
			entidadeEditada = organograma;
			defineCampos(organograma);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		organogramaListBox.setFocus(true);
	}

	public void limpaCampos(){
		organogramaListBox.setSelectedIndex(-1);
	}

	public void defineCampos(EncontroOrganograma organograma){
		ListBoxUtil.setItemSelected(organogramaListBox, organograma.getOrganograma().getNome());

	}

	@Override
	public String getDisplayTitle() {
		return "Organogramas do Encontro";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		organogramaTableUtil.clearData();
	}
	@Override
	public void populaEncontroOrganogramas(List<EncontroOrganograma> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "organograma", "organogramas", "");
		organogramaTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroOrganograma organograma: lista) {
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
					if(Window.confirm("Deseja excluir este hotel?")){
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
			dados[1] = organograma.getOrganograma().getNome();
			organogramaTableUtil.addRow(dados,row+1);
			row++;
		}
		organogramaTableUtil.applyDataRowStyles();
	}

	public EncontroOrganograma getEntidadeEditada() {
		return entidadeEditada;
	}

	public void setEntidadeEditada(EncontroOrganograma entidadeEditada) {
		this.entidadeEditada = entidadeEditada;
	}

	@Override
	public void populaOrganogramas(List<Organograma> lista) {
		ListBoxUtil.populate(organogramaListBox, false, lista);

	}

}