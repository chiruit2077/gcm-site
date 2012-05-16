package br.com.ecc.client.ui.component.upload;

import br.com.ecc.client.core.event.UploadFinishedEvent;
import br.com.ecc.core.mvp.eventbus.WebEventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Widget;

public class UploadArquivoDigital extends Composite {

	@UiTemplate("UploadArquivoDigital.ui.xml")
	interface UploadArquivoDigitalUiBinder extends UiBinder<Widget, UploadArquivoDigital> {}
	private UploadArquivoDigitalUiBinder uiBinder = GWT.create(UploadArquivoDigitalUiBinder.class);

	@UiField(provided=true) FormPanel uploadForm;
	@UiField FileUpload arquivo;
	//@UiField TextBox textItem;
	
	private Integer idArquivoDigital;
	
	public UploadArquivoDigital() {
		uploadForm = new FormPanel();
		uploadForm.setAction("eccweb/uploadArquivoDigital");
		uploadForm.setMethod(FormPanel.METHOD_POST);
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);

		initWidget(uiBinder.createAndBindUi(this));
		
		arquivo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent arg0) {
				uploadForm.submit();
			}
		});
	}
	
	public void limpaCampos() {
		//uploadForm.reset();
	}
	
	@UiHandler("uploadForm")
	public void uploadFormSubmitHandler(SubmitEvent event){
		/*
		if (textItem.getValue()==null || "".equals(textItem.getValue())){
			Window.alert("Informe a descrição do arquivo.");
			event.cancel();
		}
		*/
		if (arquivo.getFilename()==null || "".equals(arquivo.getFilename())){
			Window.alert("Selecione o arquivo a enviar.");
			event.cancel();
		}
	}
	
	@UiHandler("uploadForm")
	public void uploadFormSubmitCompleteHandler(SubmitCompleteEvent event){
		if (event.getResults()!=null){
			UploadFinishedEvent evt = new UploadFinishedEvent(event.getResults());
			try {
				idArquivoDigital = Integer.parseInt(event.getResults());
			} catch (Exception e) {
				idArquivoDigital = 0;
			}
			WebEventBus.getInstance().fireEvent(evt);
		}
	}
	
	public String getFilename(){
		return arquivo.getFilename();
	}
	public Integer getIdArquivoDigital(){
		return idArquivoDigital;
	}
	public void setEnabled(boolean enabled){
		arquivo.setEnabled(enabled);
//		getTextItem().setEnabled(enabled);
	}
//	public TextBox getTextItem() {
//		return textItem;
//	}

}