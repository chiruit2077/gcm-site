package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.model.Atividade;

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

public class AtividadeView extends BaseView<AtividadePresenter> implements AtividadePresenter.Display {

	@UiTemplate("AtividadeView.ui.xml")
	interface AtividadeViewUiBinder extends UiBinder<Widget, AtividadeView> {}
	private AtividadeViewUiBinder uiBinder = GWT.create(AtividadeViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	
	@UiField TextBox nomeTextBox;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	
	@UiField(provided=true) FlexTable atividadeFlexTable;
	private FlexTableUtil atividadeTableUtil = new FlexTableUtil();

	DateTimeFormat dfGlobal = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
	
	private Atividade entidadeEditada;
	
	public AtividadeView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}
	
	private void criaTabela() {
		atividadeFlexTable = new FlexTable();
		atividadeFlexTable.setStyleName("portal-formSmall");
		atividadeTableUtil.initialize(atividadeFlexTable);
		
		atividadeTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		atividadeTableUtil.addColumn("Nome", "600", HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
	}
	@UiHandler("novoButton")
	public void novoButtonClickHandler(ClickEvent event){
		edita(null);
	}
	
	@UiHandler("salvarButton")
	public void salvarButtonClickHandler(ClickEvent event){
		entidadeEditada.setNome(nomeTextBox.getValue());
		entidadeEditada.setGrupo(presenter.getGrupoSelecionado());
		presenter.salvar(entidadeEditada);
	}
	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}
	private void edita(Atividade atividade) {
		limpaCampos();
		if(atividade == null){
			entidadeEditada = new Atividade();
		} else {
			entidadeEditada = atividade;
			defineCampos(atividade);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}
	
	public void limpaCampos(){
		nomeTextBox.setValue(null);
	}

	public void defineCampos(Atividade atividade){
		nomeTextBox.setValue(atividade.getNome());
	}
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de Atividades";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		atividadeTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<Atividade> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "atividade", "atividades", "");
		atividadeTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Atividade atividade: lista) {
			Object dados[] = new Object[2];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(atividade);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este atividade ?")){
						presenter.excluir(atividade);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			
			dados[1] = atividade.getNome();
			atividadeTableUtil.addRow(dados,row+1);
			row++;
		}
		atividadeTableUtil.applyDataRowStyles();
	}
	
}