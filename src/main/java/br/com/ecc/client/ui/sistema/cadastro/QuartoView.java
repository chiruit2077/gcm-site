package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.color.colorpicker.ColorPickerDialog;
import br.com.ecc.client.ui.component.color.dialog.DialogClosedEvent;
import br.com.ecc.client.ui.component.color.dialog.IDialogClosedHandler;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.model.Quarto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class QuartoView extends BaseView<QuartoPresenter> implements QuartoPresenter.Display {

	@UiTemplate("QuartoView.ui.xml")
	interface QuartoViewUiBinder extends UiBinder<Widget, QuartoView> {}
	private QuartoViewUiBinder uiBinder = GWT.create(QuartoViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	
	@UiField CheckBox carpeteCheckBox;
	@UiField CheckBox suporteCheckBox;
	@UiField TextBox numeroQuartoTextBox;
	@UiField TextBox corQuartoTextBox;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	
	@UiField(provided=true) FlexTable quartoFlexTable;
	private FlexTableUtil quartoTableUtil = new FlexTableUtil();

	DateTimeFormat dfGlobal = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
	
	private Quarto entidadeEditada;
	
	public QuartoView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}
	
	private void criaTabela() {
		quartoFlexTable = new FlexTable();
		quartoFlexTable.setStyleName("portal-formSmall");
		quartoTableUtil.initialize(quartoFlexTable);
		
		quartoTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		quartoTableUtil.addColumn("Número", "50", HasHorizontalAlignment.ALIGN_CENTER);
		quartoTableUtil.addColumn("Suporte TV", null, HasHorizontalAlignment.ALIGN_LEFT);
		quartoTableUtil.addColumn("Carpete", "30", HasHorizontalAlignment.ALIGN_LEFT);
		quartoTableUtil.addColumn("Cor", "30", HasHorizontalAlignment.ALIGN_LEFT);
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
		entidadeEditada.setNumeroQuarto(numeroQuartoTextBox.getValue());
		entidadeEditada.setGrupo(presenter.getGrupoSelecionado());
		entidadeEditada.setCarpete(carpeteCheckBox.getValue());
		entidadeEditada.setSuporteTV(suporteCheckBox.getValue());
		entidadeEditada.setCor(corQuartoTextBox.getValue());
		presenter.salvar(entidadeEditada);
	}
	private void edita(Quarto quarto) {
		limpaCampos();
		if(quarto == null){
			entidadeEditada = new Quarto();
		} else {
			entidadeEditada = quarto;
			defineCampos(quarto);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		numeroQuartoTextBox.setFocus(true);
	}
	
	public void limpaCampos(){
		numeroQuartoTextBox.setValue(null);
		carpeteCheckBox.setValue(null);
		suporteCheckBox.setValue(null);
		corQuartoTextBox.setValue(null);
		setColor("ffffff");
	}

	public void defineCampos(Quarto quarto){
		numeroQuartoTextBox.setValue(quarto.getNumeroQuarto());
		carpeteCheckBox.setValue(quarto.getCarpete());
		suporteCheckBox.setValue(quarto.getSuporteTV());
		corQuartoTextBox.setValue(quarto.getCor());
		
		if( quarto.getCor().equals("")){
			setColor("ffffff");
		} else {
			setColor(quarto.getCor());
		}
		
	}
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de Quartos";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		quartoTableUtil.clearData();
	}
	@Override
	public void populaQuartos(List<Quarto> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "quarto", "quartos", "");
		quartoTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		HTML html;
		String cor="";
		for (final Quarto quarto: lista) {
			Object dados[] = new Object[5];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(quarto);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este quarto?")){
						presenter.excluir(quarto);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			dados[1] = quarto.getNumeroQuarto();
			if(quarto.getSuporteTV()!=null){
				dados[2] = quarto.getSuporteTV()?"Sim":"Não";
			}
			if(quarto.getCarpete()!=null){
				dados[3] = quarto.getCarpete()?"Sim":"Não";
			}
			if( quarto.getCor().equals("")){
				cor = "ffffff";
			} else {
				cor = quarto.getCor();
			}
			html = new HTML("<span style='width:100px;background-color:#" + cor + ";'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
			dados[4] = html;
			quartoTableUtil.addRow(dados,row+1);
			row++;
		}
		quartoTableUtil.applyDataRowStyles();
	}
	
	private void pickColor() {
		final ColorPickerDialog dlg = new ColorPickerDialog();
		dlg.setColor(corQuartoTextBox.getText());
		dlg.addDialogClosedHandler(new IDialogClosedHandler() {
			public void dialogClosed(DialogClosedEvent event) {
				if (!event.isCanceled()) {
					setColor(dlg.getColor());
				}
			}
		});
		dlg.center();
	}
	private void setColor(String color) {
		corQuartoTextBox.setValue(color);
		setCorFundo(color);
	}
	
	private native void setCorFundo(String cor) /*-{
		$wnd.changecss('ECCWeb.css', '.portal-corFundo', 'background-color', "#"+cor);
		$wnd.changecss('ECCWeb.css', '.portal-corFundo', 'color', "#"+cor);
	}-*/;
	
	@UiHandler("selecionaCorButton")
	public void selecionaCorButtonClickHandler(ClickEvent event){
		pickColor();
	}
}