package br.com.ecc.client.ui.sistema.hotelaria;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.Hotel;
import br.com.ecc.model.tipo.TipoEncontroHotelEnum;

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

public class EncontroHotelView extends BaseView<EncontroHotelPresenter> implements EncontroHotelPresenter.Display {

	@UiTemplate("EncontroHotelView.ui.xml")
	interface EncontroHotelViewUiBinder extends UiBinder<Widget, EncontroHotelView> {}
	private EncontroHotelViewUiBinder uiBinder = GWT.create(EncontroHotelViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;

	@UiField ListBox hotelListBox;
	@UiField ListBox tipoListBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;


	@UiField(provided=true) FlexTable hotelFlexTable;
	private FlexTableUtil hotelTableUtil = new FlexTableUtil();

	private EncontroHotel entidadeEditada;

	public EncontroHotelView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		ListBoxUtil.populate(tipoListBox, false, TipoEncontroHotelEnum.values());
	}

	private void criaTabela() {
		hotelFlexTable = new FlexTable();
		hotelFlexTable.setStyleName("portal-formSmall");
		hotelTableUtil.initialize(hotelFlexTable);

		hotelTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		hotelTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
		hotelTableUtil.addColumn("Qtde Quartos", "100", HasHorizontalAlignment.ALIGN_LEFT);
		hotelTableUtil.addColumn("Tipo", "100", HasHorizontalAlignment.ALIGN_LEFT);
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
		TipoEncontroHotelEnum tipo = (TipoEncontroHotelEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoEncontroHotelEnum.values());
		Hotel hotel = (Hotel) ListBoxUtil.getItemSelected(hotelListBox, presenter.getListaHoteis());
		entidadeEditada.setHotel(hotel);
		entidadeEditada.setTipo(tipo);
		presenter.salvar(entidadeEditada);
	}
	private void edita(EncontroHotel hotel) {
		limpaCampos();
		if(hotel == null){
			entidadeEditada = new EncontroHotel();
			entidadeEditada.setEncontro(presenter.getEncontroSelecionado());
		} else {
			entidadeEditada = hotel;
			defineCampos(hotel);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		hotelListBox.setFocus(true);
	}

	public void limpaCampos(){
		hotelListBox.setSelectedIndex(-1);
		tipoListBox.setSelectedIndex(0);
	}

	public void defineCampos(EncontroHotel hotel){
		ListBoxUtil.setItemSelected(hotelListBox, hotel.getHotel().getNome());
		ListBoxUtil.setItemSelected(tipoListBox, hotel.getTipo().getNome());

	}

	@Override
	public String getDisplayTitle() {
		return "Hoteis do Encontro";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		hotelTableUtil.clearData();
	}
	@Override
	public void populaEncontroHoteis(List<EncontroHotel> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "hotel", "hoteis", "");
		hotelTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final EncontroHotel hotel: lista) {
			Object dados[] = new Object[4];

			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(hotel);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este hotel?")){
						presenter.excluir(hotel);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);

			dados[0] = hp;
			dados[1] = hotel.getHotel().getNome();
			dados[2] = hotel.getHotel().getQuantidadeQuartos().toString();
			dados[3] = hotel.getTipo().getNome();
			hotelTableUtil.addRow(dados,row+1);
			row++;
		}
		hotelTableUtil.applyDataRowStyles();
	}

	public EncontroHotel getEntidadeEditada() {
		return entidadeEditada;
	}

	public void setEntidadeEditada(EncontroHotel entidadeEditada) {
		this.entidadeEditada = entidadeEditada;
	}

	@Override
	public void populaHoteis(List<Hotel> lista) {
		ListBoxUtil.populate(hotelListBox, false, lista);

	}

}