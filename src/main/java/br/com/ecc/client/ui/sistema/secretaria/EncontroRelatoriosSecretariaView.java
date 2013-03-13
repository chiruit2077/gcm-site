package br.com.ecc.client.ui.sistema.secretaria;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.sistema.secretaria.EncontroRelatoriosSecretariaPresenter.ProcessaOpcao;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Agrupamento;

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
	@UiField RadioButton relatorioAgrupamentosRadioButton;
	@UiField ListBox agrupamentosListBox;

	private List<Agrupamento> agrupamentos;

	public EncontroRelatoriosSecretariaView() {
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}

	@UiHandler("processaButton")
	public void processaButtonClickHandler(ClickEvent event){
		if (geraCSVRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.GERACSV);
		else if (relatorioAgrupamentosRadioButton.getValue()){
			Agrupamento agrupamento = (Agrupamento) ListBoxUtil.getItemSelected(agrupamentosListBox, getAgrupamentos());
			if (agrupamento != null)
				presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMAGRUPAMENTO, agrupamento);
			else
				Window.alert("Escolha o Agrupamento");
		}
		else if (relatorioRomanticoRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.LISTAGEMFILAROMANTICO);
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

}