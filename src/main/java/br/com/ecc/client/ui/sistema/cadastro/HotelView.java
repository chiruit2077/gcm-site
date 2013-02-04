package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.textbox.NumberTextBox;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Hotel;
import br.com.ecc.model.tipo.TipoHotelDistribuicaoEnum;
import br.com.ecc.model.tipo.TipoHotelEnum;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HotelView extends BaseView<HotelPresenter> implements HotelPresenter.Display {

	@UiTemplate("HotelView.ui.xml")
	interface HotelViewUiBinder extends UiBinder<Widget, HotelView> {}
	private HotelViewUiBinder uiBinder = GWT.create(HotelViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;

	@UiField ListBox tipoListBox;
	@UiField ListBox distribuicaoListBox;
	@UiField NumberTextBox quantidadeQuartos;
	@UiField TextBox nomeTextBox;

	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;


	@UiField(provided=true) FlexTable hotelFlexTable;
	private FlexTableUtil hotelTableUtil = new FlexTableUtil();


	private Hotel entidadeEditada;

	public HotelView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());

		ListBoxUtil.populate(tipoListBox, false, TipoHotelEnum.values());
		ListBoxUtil.populate(distribuicaoListBox, false, TipoHotelDistribuicaoEnum.values());
	}

	private void criaTabela() {
		hotelFlexTable = new FlexTable();
		hotelFlexTable.setStyleName("portal-formSmall");
		hotelTableUtil.initialize(hotelFlexTable);

		hotelTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		hotelTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
		hotelTableUtil.addColumn("Qtde Quartos", null, HasHorizontalAlignment.ALIGN_LEFT);
		hotelTableUtil.addColumn("Tipo", "100", HasHorizontalAlignment.ALIGN_LEFT);
		hotelTableUtil.addColumn("Distribuição", "100", HasHorizontalAlignment.ALIGN_LEFT);
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
		TipoHotelEnum tipo = (TipoHotelEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoHotelEnum.values());
		TipoHotelDistribuicaoEnum distribuicao = (TipoHotelDistribuicaoEnum) ListBoxUtil.getItemSelected(distribuicaoListBox, TipoHotelDistribuicaoEnum.values());
		if (tipo == null){
			Window.alert("Escolha o Tipo!");
			return;
		}
		if (distribuicao == null){
			Window.alert("Escolha a distribuicao!");
			return;
		}
		entidadeEditada.setNome(nomeTextBox.getValue());
		entidadeEditada.setGrupo(presenter.getGrupoSelecionado());
		entidadeEditada.setTipo(tipo);
		entidadeEditada.setQuantidadeQuartos(Integer.parseInt(quantidadeQuartos.getText()));
		entidadeEditada.setDistribuicaoHotel(distribuicao);
		presenter.salvar(entidadeEditada);
	}
	private void edita(Hotel hotel) {
		limpaCampos();
		if(hotel == null){
			entidadeEditada = new Hotel();
		} else {
			entidadeEditada = hotel;
			defineCampos(hotel);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}

	public void limpaCampos(){
		nomeTextBox.setValue(null);
		quantidadeQuartos.setValue(null);
	}

	public void defineCampos(Hotel hotel){
		nomeTextBox.setValue(hotel.getNome());
		quantidadeQuartos.setValue(hotel.getQuantidadeQuartos().toString());
		ListBoxUtil.setItemSelected(tipoListBox, hotel.getTipo().getNome());
		ListBoxUtil.setItemSelected(distribuicaoListBox, hotel.getDistribuicaoHotel().getNome());

	}

	@Override
	public String getDisplayTitle() {
		return "Cadastro de Hoteis";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		hotelTableUtil.clearData();
	}
	@Override
	public void populaHoteis(List<Hotel> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "hotel", "hoteis", "");
		hotelTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Hotel hotel: lista) {
			Object dados[] = new Object[5];

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
			dados[1] = hotel.getNome();
			dados[2] = hotel.getQuantidadeQuartos().toString();
			dados[3] = hotel.getTipo().getNome();
			dados[4] = hotel.getDistribuicaoHotel().getNome();
			hotelTableUtil.addRow(dados,row+1);
			row++;
		}
		hotelTableUtil.applyDataRowStyles();
	}

}