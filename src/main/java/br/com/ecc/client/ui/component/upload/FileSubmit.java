package br.com.ecc.client.ui.component.upload;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.UploadProgressService;
import br.com.ecc.client.service.UploadProgressServiceAsync;
import br.com.ecc.client.ui.component.upload.event.UploadEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public final class FileSubmit extends Composite {

	private String servletURL = "/eccweb/uploadArquivo";
	private Button submit;
	private FileUpload file;
	private FormPanel form;
	
	private UploadProgressServiceAsync serviceUpload = GWT.create(UploadProgressService.class);
	private Timer timerProgress;
	private UploadProgressBar uploadProgressBar;
	private UploadedFile uploadedFile;
	
	private Boolean showProgress;

	public FileSubmit() {

		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		vp.setWidth("100%");

		file = new FileUpload();
		file.setWidth("100%");
		file.setName("file");
		file.setTitle("Selecione o arquivo");

		submit = new Button("Enviar");
		submit.setTitle("Enviar arquivo");
		submit.setWidth("100%");

		HorizontalPanel uploadPanel = new HorizontalPanel();
		uploadPanel.setWidth("100%");
		uploadPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		uploadPanel.add(file);
		uploadPanel.add(submit);
		
		vp.add(uploadPanel);
		uploadProgressBar = new UploadProgressBar();
		vp.add(uploadProgressBar);

		form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(getServletURL());
		form.setWidget(vp);

		this.initWidget(form);

		submit.addClickHandler(new SubmitClickHandler());
		form.addSubmitHandler(new FormSubmitHandler());
		form.addSubmitCompleteHandler(new FormSubmitCompleteHandler());
	}
	private class FormSubmitHandler implements SubmitHandler {
		@Override
		public void onSubmit(final SubmitEvent event) {
			submit.setEnabled(false);
		}
	}
	private class FormSubmitCompleteHandler implements SubmitCompleteHandler {
		@Override
		public void onSubmitComplete(final SubmitCompleteEvent event) {
			form.reset();
			submit.setEnabled(true);
			if(showProgress){
				timerProgress.cancel();
			}
			serviceUpload.getProgress(new AsyncCallback<List<UploadedFile>>() {
				@Override
				public void onSuccess(List<UploadedFile> lista) {
					UploadedFile uf = lista.get(0);
					fireEvent(new UploadEvent());
					if(showProgress){
						uploadProgressBar.atualiza(uf);
					}
				}
				@Override
				public void onFailure(Throwable caught) {
				}
			});
		}
	}
	private final class SubmitClickHandler implements ClickHandler {
		@Override
		public void onClick(final ClickEvent event) {
			String filename = file.getFilename();
			if (filename.isEmpty()) {
				Window.alert("Selecione um arquivo");
				return;
			}
			initialize();
			uploadedFile = new UploadedFile();
			uploadedFile.setNomeArquivo(filename);
			if(showProgress){
				uploadProgressBar.initialize(filename, true);
				timerProgress = new Timer() {
					@Override
					public void run() {
						getProgress();
						timerProgress.schedule(1000);
					}
				};
				timerProgress.schedule(1000);
			}
			List<UploadedFile> lista = new ArrayList<UploadedFile>();
			lista.add(uploadedFile);
			serviceUpload.setLista(lista, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable arg0) {
				}
				
				@Override
				public void onSuccess(Void arg0) {
					form.submit();
				}
			});
		}
	}
	private void getProgress() {
		serviceUpload.getProgress(new AsyncCallback<List<UploadedFile>>() {
			@Override
			public void onSuccess(List<UploadedFile> lista) {
				UploadedFile uploadedFile = lista.get(0);
				setUploadedFile(uploadedFile);
				if(uploadedFile!=null){
					uploadProgressBar.atualiza(uploadedFile);
					if(uploadedFile.getProgresso()>=100){
						if(showProgress){
							timerProgress.cancel();
						}
					}
				}
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	private void initialize() {
		serviceUpload.initialize(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	public Boolean getShowProgress() {
		return showProgress;
	}
	public void setShowProgress(Boolean showProgress) {
		this.showProgress = showProgress;
	}
	public String getServletURL() {
		return servletURL;
	}
	public void setServletURL(String servletURL) {
		this.servletURL = servletURL;
	}
}