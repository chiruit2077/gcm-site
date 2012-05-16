package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.upload.UploadArquivoDigital;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.model.Grupo;

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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class GrupoView extends BaseView<GrupoPresenter> implements GrupoPresenter.Display {

	@UiTemplate("GrupoView.ui.xml")
	interface GrupoViewUiBinder extends UiBinder<Widget, GrupoView> {}
	private GrupoViewUiBinder uiBinder = GWT.create(GrupoViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	
	@UiField TextBox nomeTextBox;
	@UiField TextBox estadoTextBox;
	@UiField TextBox cidadeTextBox;
	@UiField TextBox bancoTextBox;
	@UiField TextBox agenciaTextBox;
	@UiField TextBox contaTextBox;
	@UiField TextArea favorecidoTextArea;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	@UiField Image logoImage;
	@UiField UploadArquivoDigital logotipoUploadArquivoDigital;
	
	@UiField(provided=true) FlexTable grupoFlexTable;
	private FlexTableUtil grupoTableUtil = new FlexTableUtil();
	
	private Grupo entidadeEditada;
	
	public GrupoView() {
		criaTabela();
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
	}
	
	private void criaTabela() {
		grupoFlexTable = new FlexTable();
		grupoFlexTable.setStyleName("portal-formSmall");
		grupoTableUtil.initialize(grupoFlexTable);
		
		grupoTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		grupoTableUtil.addColumn("Nome", "150", HasHorizontalAlignment.ALIGN_LEFT);
		grupoTableUtil.addColumn("Cidade", "150", HasHorizontalAlignment.ALIGN_LEFT);
		grupoTableUtil.addColumn("Estado", "10", HasHorizontalAlignment.ALIGN_LEFT);
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
		entidadeEditada.setNome(nomeTextBox.getValue());
		entidadeEditada.setCidade(cidadeTextBox.getValue());
		entidadeEditada.setEstado(estadoTextBox.getValue());
		entidadeEditada.setBanco(bancoTextBox.getValue());
		entidadeEditada.setAgencia(agenciaTextBox.getValue());
		entidadeEditada.setConta(contaTextBox.getValue());
		entidadeEditada.setFavorecidoConta(favorecidoTextArea.getValue());
		
		presenter.salvar(entidadeEditada);
	}
	private void edita(Grupo grupo) {
		limpaCampos();
		if(grupo == null){
			entidadeEditada = new Grupo();
		} else {
			entidadeEditada = grupo;
			defineCampos(grupo);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		nomeTextBox.setFocus(true);
	}
	
	public void limpaCampos(){
		nomeTextBox.setValue(null);
		cidadeTextBox.setValue(null);
		estadoTextBox.setValue(null);
		bancoTextBox.setValue(null);
		agenciaTextBox.setValue(null);
		contaTextBox.setValue(null);
		favorecidoTextArea.setValue(null);
		logoImage.setUrl("images/empty.jpg");
	}

	public void defineCampos(Grupo grupo){
		nomeTextBox.setValue(grupo.getNome());
		cidadeTextBox.setValue(grupo.getCidade());
		estadoTextBox.setValue(grupo.getEstado());
		bancoTextBox.setValue(grupo.getBanco());
		agenciaTextBox.setValue(grupo.getAgencia());
		contaTextBox.setValue(grupo.getConta());
		favorecidoTextArea.setValue(grupo.getFavorecidoConta());
		if(grupo.getIdArquivoDigital()!=null){
			logoImage.setUrl("eccweb/downloadArquivoDigital?id="+grupo.getIdArquivoDigital());
		}
	}
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de Grupos";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		grupoTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<Grupo> lista) {
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "grupo", "grupos", "");
		grupoTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Grupo grupo: lista) {
			Object dados[] = new Object[4];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(grupo);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este grupo ?")){
						presenter.excluir(grupo);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			
			dados[1] = grupo.getNome();
			dados[2] = grupo.getCidade();
			dados[3] = grupo.getEstado();
			grupoTableUtil.addRow(dados,row+1);
			row++;
		}
		grupoTableUtil.applyDataRowStyles();
	}

	@Override
	public void defineLogotipo() {
		logoImage.setUrl("eccweb/downloadArquivoDigital?id="+logotipoUploadArquivoDigital.getIdArquivoDigital());
		entidadeEditada.setIdArquivoDigital(logotipoUploadArquivoDigital.getIdArquivoDigital());
	}
}