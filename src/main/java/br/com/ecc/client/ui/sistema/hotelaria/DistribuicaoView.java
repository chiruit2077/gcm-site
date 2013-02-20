package br.com.ecc.client.ui.sistema.hotelaria;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroHotelQuarto;
import br.com.ecc.model.HotelAgrupamento;
import br.com.ecc.model.Quarto;
import br.com.ecc.model.tipo.TipoAgrupamentoLadoEnum;
import br.com.ecc.model.tipo.TipoEncontroQuartoEnum;
import br.com.ecc.model.vo.EncontroHotelVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DistribuicaoView extends BaseView<DistribuicaoPresenter> implements DistribuicaoPresenter.Display {

	@UiTemplate("DistribuicaoView.ui.xml")
	interface DistribucaoViewUiBinder extends UiBinder<Widget, DistribuicaoView> {}
	private DistribucaoViewUiBinder uiBinder = GWT.create(DistribucaoViewUiBinder.class);

	@UiField Label tituloFormularioLabel;

	@UiField VerticalPanel centralPanel;
	@UiField VerticalPanel distribuicaoPanel;
	@UiField ListBox hotelListBox;

	private List<EncontroHotel> listaHotel;

	public DistribuicaoView() {

		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@Override
	public void populaHoteis(List<EncontroHotel> lista) {
		this.setListaHotel(lista);
		hotelListBox.clear();
		for(EncontroHotel encontroHotel : lista) {
			hotelListBox.addItem(encontroHotel.toString(), encontroHotel.getId().toString());
		}
		hotelListBox.setSelectedIndex(0);
		EncontroHotel encontroHotel = getEncontroHotelId(Integer.valueOf(hotelListBox.getValue(hotelListBox.getSelectedIndex())));
		if (encontroHotel != null){
			presenter.setEncontroHotelSelecionado(encontroHotel);
			presenter.buscaVO();
		}
	}

	@UiHandler("hotelListBox")
	public void HotelListBoxChangeHandler(ChangeEvent event) {
		EncontroHotel encontroHotel = getEncontroHotelId(Integer.valueOf(hotelListBox.getValue(hotelListBox.getSelectedIndex())));
		if (encontroHotel != null){
			presenter.setEncontroHotelSelecionado(encontroHotel);
			presenter.buscaVO();
		}
	}

	private EncontroHotel getEncontroHotelId(Integer value) {
		for (EncontroHotel encontroHotel : getListaHotel()) {
			if(encontroHotel.getId().equals(value))
				return encontroHotel;
		}
		return null;
	}

	public void limpaCampos(){
		/*nomeLabel.setText(null);
		observacaoTextBox.setText(null);
		fichaTextBox.setText(null);*/
	}

	@Override
	public String getDisplayTitle() {
		return "Distribuição de Quartos";
	}

	@Override
	public void reset() {
	}

	@Override
	public void populaEntidades(EncontroHotelVO vo) {
		distribuicaoPanel.clear();
		if (vo.getListaAgrupamentos().size() > 0) {
			VerticalPanel panel = new VerticalPanel();
			panel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
			panel.setSize("100%", "100%");
			int countagrup = 0;
			for (HotelAgrupamento agrupamento : vo.getListaAgrupamentos()) {
				if ( countagrup > 0 ){
					HorizontalPanel separador = new HorizontalPanel();
					separador.setSize("100%", "30px");
					panel.add(separador);
				}
				VerticalPanel agrupamentoPanel = new VerticalPanel();
				agrupamentoPanel.setSize("100%", "100%");
				agrupamentoPanel.setStyleName("agrupamento");
				HorizontalPanel titulo = new HorizontalPanel();
				titulo.setSize("100%", "30px");
				titulo.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				titulo.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				titulo.setStyleName("agrupamento-Titulo");
				titulo.add(new Label(agrupamento.getNome()));
				agrupamentoPanel.add(titulo);
				if ( agrupamento.getQuantidadeLados() == 2 ){
					if (true){
						VerticalPanel ladodireito = new VerticalPanel();
						ladodireito.setSize("100%", "100%");
						HorizontalPanel ladoTitulo = new HorizontalPanel();
						ladoTitulo.setSize("100%", "30px");
						ladoTitulo.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
						ladoTitulo.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
						ladoTitulo.setStyleName("agrupamento-Lado");
						ladoTitulo.add(new Label(TipoAgrupamentoLadoEnum.DIREITO.getNome()));
						ladodireito.add(ladoTitulo);
						HorizontalPanel quartos = new HorizontalPanel();
						quartos.setSize("100%", "100px");
						quartos.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
						quartos.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
						quartos.setStyleName("agrupamento-Quartos");
						quartos.add(geraQuartos(vo,agrupamento,TipoAgrupamentoLadoEnum.DIREITO));
						ladodireito.add(quartos);
						agrupamentoPanel.add(ladodireito);
					}

					HorizontalPanel separador = new HorizontalPanel();
					separador.setSize("100%", "30px");
					agrupamentoPanel.add(separador);

					if(true){
						VerticalPanel ladoesquerdo = new VerticalPanel();
						ladoesquerdo.setSize("100%", "100%");
						HorizontalPanel quartos = new HorizontalPanel();
						quartos.setSize("100%", "100px");
						quartos.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
						quartos.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
						quartos.setStyleName("agrupamento-Quartos");
						quartos.add(geraQuartos(vo,agrupamento,TipoAgrupamentoLadoEnum.ESQUERDO));
						ladoesquerdo.add(quartos);
						HorizontalPanel ladoTitulo = new HorizontalPanel();
						ladoTitulo.setSize("100%", "30px");
						ladoTitulo.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
						ladoTitulo.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
						ladoTitulo.setStyleName("agrupamento-Lado");
						ladoTitulo.add(new Label(TipoAgrupamentoLadoEnum.ESQUERDO.getNome()));
						panel.add(ladoTitulo);
						ladoesquerdo.add(ladoTitulo);
						agrupamentoPanel.add(ladoesquerdo);
					}

				}else{
					VerticalPanel ladounico = new VerticalPanel();
					ladounico.setSize("100%", "100%");
					HorizontalPanel quartos = new HorizontalPanel();
					quartos.setSize("100%", "100px");
					quartos.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
					quartos.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
					quartos.setStyleName("agrupamento-Quartos");
					quartos.add(geraQuartos(vo,agrupamento,TipoAgrupamentoLadoEnum.UNICO));
					ladounico.add(quartos);
					agrupamentoPanel.add(ladounico);
				}
				panel.add(agrupamentoPanel);
				countagrup++;
			}
			distribuicaoPanel.add(panel);
		}

	}

	private Widget geraQuartos(EncontroHotelVO vo, HotelAgrupamento agrupamento, TipoAgrupamentoLadoEnum lado) {
		ArrayList<Quarto> quartos = new ArrayList<Quarto>();
		for (Quarto quarto : vo.getListaQuartos()) {
			if (quarto.getHotelAgrupamento().equals(agrupamento) && quarto.getLado().equals(lado))
				quartos.add(quarto);
		}

		if (quartos.size()>0){
			HorizontalPanel quartosPanel = new HorizontalPanel();
			quartosPanel.setSize("100%", "100%");
			for (Quarto  quarto : quartos) {
				HorizontalPanel separador = new HorizontalPanel();
				separador.setSize("5px", "100%");
				quartosPanel.add(separador);
				VerticalPanel quartoWidget = new VerticalPanel();
				EncontroHotelQuarto encontroHotelQuarto = getEncontroHotelQuarto(vo,quarto);
				if (encontroHotelQuarto != null)
					quartoWidget.setStyleName(encontroHotelQuarto.getTipo().getStyle());
				else
					quartoWidget.setStyleName(TipoEncontroQuartoEnum.APOIO.getStyle());
				quartoWidget.setSize("80px", "100px%");
				quartoWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				quartoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
				HorizontalPanel numquarto = new HorizontalPanel();
				numquarto.setSize("100%", "20px");
				numquarto.setStyleName("agrupamento-QuartoNumero");
				numquarto.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				numquarto.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				numquarto.add(new Label(quarto.getNumeroQuarto()));

				VerticalPanel quartoNomeWidget = new VerticalPanel();
				quartoNomeWidget.setSize("100%", "80px");
				quartoNomeWidget.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				quartoNomeWidget.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
				quartoNomeWidget.add(new Label(quarto.getLado().getNome()));
				quartoNomeWidget.add(new Label("e"));
				quartoNomeWidget.add(new Label(quarto.getLado().getNome()));
				if (quarto.getLado().equals(TipoAgrupamentoLadoEnum.DIREITO)){
					quartoWidget.add(quartoNomeWidget);
					quartoWidget.add(numquarto);
					quartoWidget.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
				}else{
					quartoWidget.add(numquarto);
					quartoWidget.add(quartoNomeWidget);
				}
				quartosPanel.add(quartoWidget);
			}
			HorizontalPanel separador = new HorizontalPanel();
			separador.setSize("5px", "100%");
			quartosPanel.add(separador);

			return quartosPanel;
		}
		return new Label("QUARTOS - " + lado.getNome());
	}



	private EncontroHotelQuarto getEncontroHotelQuarto(EncontroHotelVO vo, Quarto quarto) {
		for (EncontroHotelQuarto encontroHotelQuarto : vo.getListaEncontroQuartos()) {
			if ( encontroHotelQuarto.getQuarto().equals(quarto))
				return encontroHotelQuarto;
		}
		return null;
	}

	public List<EncontroHotel> getListaHotel() {
		return listaHotel;
	}

	public void setListaHotel(List<EncontroHotel> listaHotel) {
		this.listaHotel = listaHotel;
	}

}