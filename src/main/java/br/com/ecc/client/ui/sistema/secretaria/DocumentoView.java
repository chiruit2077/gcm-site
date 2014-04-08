package br.com.ecc.client.ui.sistema.secretaria;

import java.util.List;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.richtext.RichTextToolbar;
import br.com.ecc.client.util.FlexTableUtil;
import br.com.ecc.client.util.FlexTableUtil.TipoColuna;
import br.com.ecc.client.util.LabelTotalUtil;
import br.com.ecc.client.util.ListBoxUtil;
import br.com.ecc.model.Documento;
import br.com.ecc.model.tipo.TipoDocumentoEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class DocumentoView extends BaseView<DocumentoPresenter> implements DocumentoPresenter.Display {

	@UiTemplate("DocumentoView.ui.xml")
	interface DocumentoViewUiBinder extends UiBinder<Widget, DocumentoView> {}
	private DocumentoViewUiBinder uiBinder = GWT.create(DocumentoViewUiBinder.class);
	
	@UiField Label tituloFormularioLabel;
	@UiField Label itemTotal;
	@UiField ListBox tipoListBox;
	@UiField TextBox filtroTextBox;
	@UiField Button filtrarButton;
	
	@UiField TextBox tituloTextBox;
	@UiField CheckBox encontroCheckBox;
	@UiField(provided=true) RichTextArea documentoRichTextArea;
	@UiField(provided=true) RichTextToolbar documentoRichTextToolbar;
	@UiField DateBox dataDateBox;
	
	@UiField VerticalPanel documentoEditavelVerticalPanel;
	@UiField FlowPanel documentoNaoEditavelVerticalPanel;
	
	@UiField DialogBox editaDialogBox;
	@UiField Button salvarButton;
	@UiField Button fecharButton;
	@UiField Button novoButton;
	
	@UiField(provided=true) FlexTable documentoFlexTable;
	private FlexTableUtil documentoTableUtil = new FlexTableUtil();
	
	@UiField ListBox tipoFiltroListBox;
	
	private DateTimeFormat dfData = DateTimeFormat.getFormat("dd-MM-yyyy");
	
	public DocumentoView() {
		criaTabela();
		
		documentoRichTextArea = new RichTextArea();
		documentoRichTextToolbar = new RichTextToolbar(documentoRichTextArea);
		
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		ListBoxUtil.populate(tipoListBox, false, TipoDocumentoEnum.values());
		
		tipoFiltroListBox.addItem("Todos");
		tipoFiltroListBox.addItem("Para o encontro atual");
		
		dataDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd-MM-yyyy")));
		dataDateBox.getTextBox().setAlignment(TextAlignment.CENTER);
	}
	
	private void criaTabela() {
		documentoFlexTable = new FlexTable();
		documentoFlexTable.setStyleName("portal-formSmall");
		documentoTableUtil.initialize(documentoFlexTable);
		
		documentoTableUtil.addColumn("", "40", HasHorizontalAlignment.ALIGN_CENTER);
		documentoTableUtil.addColumn("Data", "100", HasHorizontalAlignment.ALIGN_CENTER, TipoColuna.DATE, "dd-MM-yyyy HH:mm");
		documentoTableUtil.addColumn("Encontro", "200", HasHorizontalAlignment.ALIGN_LEFT);
		documentoTableUtil.addColumn("Titulo", null, HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	@UiHandler("filtrarButton")
	public void filtrarButtonClickHandler(ClickEvent event){
		editaDialogBox.hide();
		presenter.listar();
	}
	
	@UiHandler("fecharButton")
	public void fecharButtonClickHandler(ClickEvent event){
		filtrarButtonClickHandler(null);
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
		if(presenter.getDocumentoEditado().getId()==null){
			presenter.getDocumentoEditado().setEditavel(true);
		}
		presenter.getDocumentoEditado().setGrupo(presenter.getGrupoSelecionado());
		presenter.getDocumentoEditado().setTitulo(tituloTextBox.getValue());
		presenter.getDocumentoEditado().setData(dataDateBox.getValue());
		presenter.getDocumentoEditado().setTexto(documentoRichTextArea.getHTML().toCharArray());
		presenter.getDocumentoEditado().setTipoDocumento((TipoDocumentoEnum) ListBoxUtil.getItemSelected(tipoListBox, TipoDocumentoEnum.values()));
		if(encontroCheckBox.getValue()){
			presenter.getDocumentoEditado().setEncontro(presenter.getEncontroSelecionado());
		} else {
			presenter.getDocumentoEditado().setEncontro(null);
		}
		presenter.salvar();
	}
	private void edita(Documento documento) {
		limpaCampos();
		if(documento!=null && documento.getId()!=null){
			presenter.getDocumento(documento.getId());
		} else {
			setDocumento(new Documento());
		}
	}
	
	@Override
	public void setDocumento(Documento documento){
		presenter.setDocumentoEditado(documento);
		if(documento.getId()!=null){
			defineCampos(documento);
		}
		editaDialogBox.center();
		editaDialogBox.show();
		tituloTextBox.setFocus(true);
	}
	
	
	public void limpaCampos(){
		tituloTextBox.setValue(null);
		documentoRichTextArea.setHTML("");
		tipoListBox.setSelectedIndex(0);
		encontroCheckBox.setValue(null);
		dataDateBox.setValue(null);
	}
	
	public void enable(boolean enabled){
		tituloTextBox.setEnabled(enabled);
		documentoRichTextArea.setEnabled(enabled);
		tipoListBox.setEnabled(enabled);
		encontroCheckBox.setEnabled(enabled);
		dataDateBox.setEnabled(enabled);
		salvarButton.setEnabled(enabled);
	}

	public void defineCampos(Documento documento){
		if(documento.getEditavel()!=null && documento.getEditavel()){
			documentoEditavelVerticalPanel.setVisible(true);
			documentoNaoEditavelVerticalPanel.setVisible(false);
			enable(true);
			if(documento.getTexto()!=null){
				documentoRichTextArea.setHTML(String.valueOf(documento.getTexto()));
			}
		} else {
			documentoEditavelVerticalPanel.setVisible(false);
			documentoNaoEditavelVerticalPanel.setVisible(true);
			enable(false);
			if(documento.getTexto()!=null){
				documentoNaoEditavelVerticalPanel.add(new HTML(String.valueOf(documento.getTexto())));
			}
		}
		tituloTextBox.setValue(documento.getTitulo());
		dataDateBox.setValue(documento.getData());
		if(documento.getEncontro()!=null){
			encontroCheckBox.setValue(true);
		}
		if(documento.getTipoDocumento()!=null){
			ListBoxUtil.setItemSelected(tipoListBox, documento.getTipoDocumento().toString());
		}
	}
	
	@Override
	public String getDisplayTitle() {
		return "Cadastro de Documentos";
	}

	@Override
	public void reset() {
		editaDialogBox.hide();
		documentoTableUtil.clearData();
	}
	@Override
	public void populaEntidades(List<Documento> lista) {
		presenter.setDocumentoEditado(null);
		LabelTotalUtil.setTotal(itemTotal, lista.size(), "documento", "documentos", "o");
		documentoTableUtil.clearData();
		int row = 0;
		Image editar, excluir;
		HorizontalPanel hp;
		for (final Documento documento: lista) {
			Object dados[] = new Object[4];
			
			editar = new Image("images/edit.png");
			editar.setStyleName("portal-ImageCursor");
			editar.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					edita(documento);
				}
			});
			excluir = new Image("images/delete.png");
			excluir.setStyleName("portal-ImageCursor");
			excluir.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if(Window.confirm("Deseja excluir este documento ?")){
						presenter.excluir(documento);
					}
				}
			});
			hp = new HorizontalPanel();
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			hp.setSpacing(1);
			hp.add(editar);
			hp.add(excluir);
			
			dados[0] = hp;
			
			if(documento.getData()!=null){
				dados[1] = dfData.format(documento.getData());
			}
			if(documento.getEncontro()!=null){
				dados[2] = documento.getEncontro().toString();
			}
			dados[3] = documento.getTitulo();
			documentoTableUtil.addRow(dados,row+1);
			row++;
		}
		documentoTableUtil.applyDataRowStyles();
	}
	
	@UiHandler("tipoFiltroListBox")
	public void tipoFiltroListChangeHandler(ChangeEvent event){
		filtrarButtonClickHandler(null);
	}
	
	@Override
	public Integer getTipoFiltro(){
		return tipoFiltroListBox.getSelectedIndex();
	}
	
	@Override
	public String getTextoFiltro(){
		return filtroTextBox.getValue();
	}
}