package br.com.ecc.client.ui.sistema.secretaria;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.sistema.secretaria.EncontroRelatoriosSecretariaPresenter.ProcessaOpcao;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Label;
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

	public EncontroRelatoriosSecretariaView() {
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}

	@UiHandler("processaButton")
	public void processaButtonClickHandler(ClickEvent event){
		if (geraCSVRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.GERACSV);
		else if (relatorioRomanticoRadioButton.getValue())
			presenter.processa(presenter.getEncontroSelecionado(),ProcessaOpcao.RELATORIOROMATICO);
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

}