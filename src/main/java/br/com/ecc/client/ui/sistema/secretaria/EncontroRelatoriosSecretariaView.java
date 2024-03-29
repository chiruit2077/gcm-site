package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.core.suggestion.GenericEntitySuggestOracle;
import br.com.ecc.client.ui.sistema.secretaria.EncontroRelatoriosSecretariaPresenter.ProcessaOpcao;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.client.util.ListUtil;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.EncontroRestaurante;

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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EncontroRelatoriosSecretariaView extends BaseView<EncontroRelatoriosSecretariaPresenter> implements EncontroRelatoriosSecretariaPresenter.Display {

	@UiTemplate("EncontroRelatoriosSecretariaView.ui.xml")
	interface ViewUiBinder extends UiBinder<Widget, EncontroRelatoriosSecretariaView> {}
	private ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);

	@UiField Label tituloFormularioLabel;
	@UiField VerticalPanel centralPanel;
	@UiField RadioButton geraCSVCorelRadioButton;
	@UiField RadioButton geraCSVCrachaRadioButton;
	@UiField RadioButton geraCSVCrachaAgrupamentoRadioButton;
	@UiField RadioButton relatorioRomanticoRadioButton;
	@UiField RadioButton relatorioAgrupamentoRadioButton;
	@UiField RadioButton relatorioAfilhadosPadrinhosRadioButton;
	@UiField RadioButton relatorioOnibusRadioButton;
	@UiField RadioButton relatorioAlbumRadioButton;
	@UiField RadioButton relatorioCestaRadioButton;
	@UiField RadioButton relatorioOracaoAmorRadioButton;
	@UiField RadioButton relatorioNecessidadesEspeciaisRadioButton;
	@UiField RadioButton relatorioDiabeticosVegetarianosRadioButton;
	@UiField RadioButton relatorioHotelAfilhadosRadioButton;
	@UiField RadioButton relatorioHotelEncontristasRadioButton;
	@UiField RadioButton relatorioHotelEncontristasHospedagemParticularRadioButton;
	@UiField RadioButton relatorioRecepcaoInicialRadioButton;
	@UiField RadioButton relatorioRecepcaoFinalRadioButton;
	@UiField RadioButton relatorioPlanilhaAtividadesRadioButton;
	@UiField RadioButton relatorioPlanilhaRadioButton;
	@UiField RadioButton excelPlanilhaRadioButton;
	@UiField RadioButton relatorioMalasRadioButton;
	@UiField RadioButton relatorioCamarimRadioButton;
	@UiField ListBox hoteisListBox;
	@UiField ListBox restaurantesListBox;
	@UiField ListBox agrupamentosListBox;
	@UiField ListBox agrupamentosCrachaAgrupamentoListBox;
	@UiField ListBox agrupamentosOnibusListBox;
	@UiField ListBox periodoListBox;
	@UiField(provided = true) RadioButton casalRadio;
	@UiField(provided = true) RadioButton pessoaRadio;

	@UiField(provided = true) SuggestBox inscricaoSuggestBox1;
	private final GenericEntitySuggestOracle inscricaoSuggest1 = new GenericEntitySuggestOracle();

	private List<Agrupamento> agrupamentos;
	private List<EncontroHotel> encontroHoteis;
	private List<EncontroRestaurante> encontroRestaurantes;
	private List<EncontroPeriodo> listaPeriodo;

	public EncontroRelatoriosSecretariaView() {
		casalRadio = new RadioButton("tipo", "Por Casal");
		pessoaRadio = new RadioButton("tipo", "Por Pessoa");
		inscricaoSuggest1.setMinimoCaracteres(2);
		inscricaoSuggestBox1 = new SuggestBox(inscricaoSuggest1);

		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}


	@Override
	public void setListaPeriodos(List<EncontroPeriodo> listaPeriodo){
		setListaPeriodo(listaPeriodo);
		periodoListBox.clear();
		periodoListBox.addItem("TODOS");
		for(EncontroPeriodo periodo : listaPeriodo) {
			periodoListBox.addItem(periodo.toString());
		}
	}

	@UiHandler("processaButton")
	public void processaButtonClickHandler(ClickEvent event){
		if (geraCSVCorelRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.GERACSVCOREL);
		else if (geraCSVCrachaRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.GERACSVCRACAS);
		else if (geraCSVCrachaAgrupamentoRadioButton.getValue()){
			Agrupamento agrupamento = (Agrupamento) ListBoxUtil.getItemSelected(agrupamentosCrachaAgrupamentoListBox, getAgrupamentos());
			if (agrupamento != null)
				presenter.processa(null, ProcessaOpcao.GERACSVCRACAS, agrupamento);
			else
				Window.alert("Escolha um Agrupamento");
		}
		else if (relatorioAfilhadosPadrinhosRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMAFILHADOSPADRINHOS);
		else if (relatorioAgrupamentoRadioButton.getValue()){
			Agrupamento agrupamento = (Agrupamento) ListBoxUtil.getItemSelected(agrupamentosListBox, getAgrupamentos());
			if (agrupamento != null)
				presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMAGRUPAMENTO, agrupamento);
			else
				Window.alert("Escolha um Agrupamento");
		}
		else if (relatorioRomanticoRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMFILAROMANTICO);
		else if (relatorioOnibusRadioButton.getValue()){
			Agrupamento agrupamento = (Agrupamento) ListBoxUtil.getItemSelected(agrupamentosOnibusListBox, getAgrupamentos());
			if (agrupamento != null)
				presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMONIBUS, agrupamento);
			else
				presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMONIBUS);
		}
		else if (relatorioAlbumRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMALBUM);
		else if (relatorioMalasRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMMALAS);
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
		else if (relatorioHotelEncontristasHospedagemParticularRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMHOTELENCONTRISTASHOSPEDAGEMPARTICULAR);
		else if (relatorioCamarimRadioButton.getValue()){
			EncontroRestaurante encontroRestaurante = (EncontroRestaurante) ListBoxUtil.getItemSelected(restaurantesListBox, getEncontroRestaurantes());
			if (encontroRestaurante != null)
				presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMCAMARIM,encontroRestaurante);
			else
				Window.alert("Escolha um Restaurante");

		}
		else if (relatorioRecepcaoInicialRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMRECEPCAOINICIAL);
		else if (relatorioRecepcaoFinalRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMRECEPCAOFINAL);
		else if (relatorioPlanilhaAtividadesRadioButton.getValue()){
			EncontroPeriodo periodo = (EncontroPeriodo) ListBoxUtil.getItemSelected(periodoListBox, getListaPeriodo());
			EncontroInscricao inscricao = (EncontroInscricao)ListUtil.getEntidadePorNome(inscricaoSuggest1.getListaEntidades(), inscricaoSuggestBox1.getValue());
			List<Object> params = new ArrayList<Object>();
			params.add(inscricao);
			params.add(periodo);
			presenter.processa(presenter.getEncontroSelecionado(), ProcessaOpcao.LISTAGEMPLANILHAATIVIDADES, params);
		}
		else if (relatorioCestaRadioButton.getValue()){
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMCESTA);
		}
		else if (relatorioPlanilhaRadioButton.getValue()){
			EncontroPeriodo periodo = (EncontroPeriodo) ListBoxUtil.getItemSelected(periodoListBox, getListaPeriodo());
			presenter.processa(presenter.getEncontroSelecionado(), ProcessaOpcao.PLANILHAPDF, periodo);
		}
		else if (excelPlanilhaRadioButton.getValue()){
			EncontroPeriodo periodo = (EncontroPeriodo) ListBoxUtil.getItemSelected(periodoListBox, getListaPeriodo());
			presenter.processa(presenter.getEncontroSelecionado(), ProcessaOpcao.PLANILHAEXCEL, periodo);
		}
		else if (relatorioCamarimRadioButton.getValue()){
			EncontroRestaurante encontroRestaurante = (EncontroRestaurante) ListBoxUtil.getItemSelected(restaurantesListBox, getEncontroRestaurantes());
			if (encontroRestaurante != null)
				presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMCAMARIM,encontroRestaurante);
			else
				Window.alert("Escolha um Restaurante");

		}
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
		ListBoxUtil.populate(agrupamentosCrachaAgrupamentoListBox, false, result);
		ListBoxUtil.populate(agrupamentosOnibusListBox, false, result);
		if (result.size()>0){
			agrupamentosListBox.setSelectedIndex(0);
			agrupamentosCrachaAgrupamentoListBox.setSelectedIndex(-1);
			agrupamentosOnibusListBox.setSelectedIndex(-1);
		}
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


	public List<EncontroPeriodo> getListaPeriodo() {
		return listaPeriodo;
	}


	public void setListaPeriodo(List<EncontroPeriodo> listaPeriodo) {
		this.listaPeriodo = listaPeriodo;
	}

	@UiHandler("casalRadio")
	public void casalRadioClickHandler(ClickEvent event){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
	}

	@UiHandler("pessoaRadio")
	public void pessoaRadioClickHandler(ClickEvent event){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroPessoaNomeLike");
	}


	@Override
	public void setSuggestInscricao() {
		casalRadio.setValue(true);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("encontro", presenter.getEncontroSelecionado());
		inscricaoSuggest1.setQueryParams(params);
		inscricaoSuggest1.setSuggestQuery("encontroInscricao.porEncontroCasalNomeEncontristaLike");
	}


	public List<EncontroRestaurante> getEncontroRestaurantes() {
		return encontroRestaurantes;
	}


	public void setEncontroRestaurantes(List<EncontroRestaurante> encontroRestaurantes) {
		this.encontroRestaurantes = encontroRestaurantes;
	}


	@Override
	public void setListaRestaurantes(List<EncontroRestaurante> result) {
		setEncontroRestaurantes(result);
		ListBoxUtil.populate(restaurantesListBox, false, result);
		if (encontroRestaurantes.size()>0)
			restaurantesListBox.setSelectedIndex(0);

	}

}