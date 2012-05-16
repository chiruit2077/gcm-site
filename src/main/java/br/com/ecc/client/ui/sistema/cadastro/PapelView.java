package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.model.Papel;

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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PapelView extends BaseView<PapelPresenter> implements PapelPresenter.Display {

	@UiTemplate("PapelView.ui.xml")
	interface PapelViewUiBinder extends UiBinder<Widget, PapelView> {}
	private PapelViewUiBinder uiBinder = GWT.create(PapelViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	
	@UiField TextBox nomeTextBox;
	@UiField TextBox siglaTextBox;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	
	@UiField(provided=true) FlexTable papelFlexTable;
	private FlexTableUtil papelTableUtil = new FlexTableUtil();

	DateTimeFormat dfGlobal = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
	
	private Papel entidadeEditada;
	
	public PapelView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}
	
	private void criaTabela() {
		papelFlexTable = new FlexTable();
		papelFlexTable.setStyleName("portal-formSmall");
		papelTableUtil.initialize(papelFlexTable);
		
		papelTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		papelTableUtil.addColumn("Sigla", "50", HasHorizontalAlignment.ALIGN_CENTER);
		papelTableUtil.addColumn("Nome", "300", HasHorizontalAlignment.ALIGN_LEFT);
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
		entidadeEditada.setSigla(siglaTextBox.getValue());
		entidadeEditada.setNome(nomeTextBox.getValue());		
		entidadeEditada.setGrupo(presenter.getGrupoSelecionado());
		presenter.salvar(entidadeEditada);
	}
	private void edita(Papel papel) {
		limpaCampos();
		if(papel == null){
			entidadeEditada = new Papel();
		} else {
			entidadeEditada = papel;
			defineCampos(papel);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		siglaTextBox.setFocus(true);
	}
	
	public void limpaCampos(){
		siglaTextBox.setValue(null);
		nomeTextBox.setValue(null);		
	}

	public void defineCampos(Papel papel){
		siglaTextBox.setValue(papel.getSigla());
		nomeTextBox.setValue(papel.getNome());
	}
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de Papeis";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		papelTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<Papel> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "papel", "papeis", "");
		papelTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Papel papel: lista) {
			Object dados[] = new Object[3];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(papel);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este papel ?")){
						presenter.excluir(papel);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			dados[1] = papel.getSigla();
			dados[2] = papel.getNome();
			papelTableUtil.addRow(dados,row+1);
			row++;
		}
		papelTableUtil.applyDataRowStyles();
	}
}