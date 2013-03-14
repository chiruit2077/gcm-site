package br.com.ecc.client.ui.sistema.secretaria;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.sistema.secretaria.EncontroRelatoriosSecretariaPresenter.ProcessaOpcao;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.EncontroHotel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EncontroRelatoriosSecretariaView extends BaseView<EncontroRelatoriosSecretariaPresenter> implements EncontroRelatoriosSecretariaPresenter.Display {

	@UiTemplate("EncontroRelatoriosSecretariaView.ui.xml")
	interface ViewUiBinder extends UiBinder<Widget, EncontroRelatoriosSecretariaView> {}
	private ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField VerticalPanel centralPanel;
	@UiField RadioButton geraCSVRadioButton;
	@UiField RadioButton relatorioRomanticoRadioButton;
	@UiField RadioButton relatorioAgrupamentoRadioButton;
	@UiField RadioButton relatorioOnibusRadioButton;
	@UiField RadioButton relatorioAlbumRadioButton;
	@UiField RadioButton relatorioOracaoAmorRadioButton;
	@UiField RadioButton relatorioNecessidadesEspeciaisRadioButton;
	@UiField RadioButton relatorioDiabeticosVegetarianosRadioButton;
	@UiField RadioButton relatorioHotelAfilhadosRadioButton;
	@UiField RadioButton relatorioHotelEncontristasRadioButton;
	@UiField RadioButton relatorioRecepcaoInicialRadioButton;
	@UiField RadioButton relatorioRecepcaoFinalRadioButton;
	@UiField ListBox hoteisListBox;
	@UiField ListBox agrupamentosListBox;

	private List<Agrupamento> agrupamentos;
	private List<EncontroHotel> encontroHoteis;

	public EncontroRelatoriosSecretariaView() {
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}

	@UiHandler("processaButton")
	public void processaButtonClickHandler(ClickEvent event){
		if (geraCSVRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.GERACSV);
		else if (relatorioAgrupamentoRadioButton.getValue()){
			Agrupamento agrupamento = (Agrupamento) ListBoxUtil.getItemSelected(agrupamentosListBox, getAgrupamentos());
			if (agrupamento != null)
				presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMAGRUPAMENTO, agrupamento);
			else
				Window.alert("Escolha um Agrupamento");
		}
		else if (relatorioRomanticoRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMFILAROMANTICO);
		else if (relatorioOnibusRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMONIBUS);
		else if (relatorioAlbumRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMALBUM);
		else if (relatorioOracaoAmorRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMORACAOAMOR);
		else if (relatorioNecessidadesEspeciaisRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMNECESSIDADESESPECIAIS);
		else if (relatorioDiabeticosVegetarianosRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMDIABETICOSVEGETARIANOS);
		else if (relatorioHotelAfilhadosRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMHOTELAFILHADOS);
		else if (relatorioHotelEncontristasRadioButton.getValue()){
			EncontroHotel encontroHotel = (EncontroHotel) ListBoxUtil.getItemSelected(hoteisListBox, getEncontroHoteis());
			if (encontroHotel != null)
				presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMHOTELENCONTRISTAS,encontroHotel);
			else
				Window.alert("Escolha um Hotel");

		}
		else if (relatorioRecepcaoInicialRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMORACAOAMOR);
		else if (relatorioRecepcaoFinalRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMORACAOAMOR);
		else
			Window.alert("Escolha uma Opção!");
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}


	@Override
	public void init(){
	}

	@Override
	public String getDisplayTitle() {
		return "Relatórios do Encontro - Secretaria";
	}

	@Override
	public void reset() {
	}

	@Override
	public void setListaAgrupamentos(List<Agrupamento> result) {
		setAgrupamentos(result);
		ListBoxUtil.populate(agrupamentosListBox, false, result);
		if (result.size()>0)
			agrupamentosListBox.setSelectedIndex(0);
	}

	public List<Agrupamento> getAgrupamentos() {
		return agrupamentos;
	}

	public void setAgrupamentos(List<Agrupamento> agrupamentos) {
		this.agrupamentos = agrupamentos;
	}

	public List<EncontroHotel> getEncontroHoteis() {
		return encontroHoteis;
	}

	public void setEncontroHoteis(List<EncontroHotel> encontroHoteis) {
		this.encontroHoteis = encontroHoteis;
	}

	@Override
	public void setListaHoteis(List<EncontroHotel> result) {
		setEncontroHoteis(result);
		ListBoxUtil.populate(hoteisListBox, false, result);
		if (result.size()>0)
			hoteisListBox.setSelectedIndex(0);
	}

}