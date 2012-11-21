package br.com.ecc.client.ui.component.upload;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.UploadProgressService;
import br.com.ecc.client.service.UploadProgressServiceAsync;
import br.com.ecc.client.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public final class UploadImagePreview extends Composite {
	
	private String servletURL = "uploadArquivo";
	private FileUpload fileUpload;
	
	private UploadProgressServiceAsync serviceUpload = GWT.create(UploadProgressService.class);
	private Timer timerProgress;
	
	private Boolean showProgress=true;
	private Boolean multiple=false;
	private Boolean enableHTML5;
	
	private List<UploadImagemItem> listaImagens = new ArrayList<UploadImagemItem>();
	HorizontalPanel imgHPanel;
	Integer indiceImagens=0;
	
	public UploadImagePreview() {
		fileUpload = new FileUpload();
		fileUpload.setWidth("100%");
		fileUpload.setTitle("Adicionar imagem");
		fileUpload.setName("fileSelector[]");
		fileUpload.getElement().setAttribute("name","fileSelector[]");
		fileUpload.getElement().setAttribute("id","fileSelectorItem");
		fileUpload.getElement().setAttribute("accept","image/*");
		
		VerticalPanel uploadPanel = new VerticalPanel();
		uploadPanel.setWidth("100%");
		
		FlowPanel imagensFlowPanel = new FlowPanel();
		imagensFlowPanel.setWidth("100%");
		imagensFlowPanel.setHeight("400px");
		imagensFlowPanel.setStyleName("upload-FlowPanel");
		
		
		final VerticalPanel imgVPanel = new VerticalPanel();
		imgVPanel.setWidth("100%");
		imagensFlowPanel.add(imgVPanel);
		
		imgHPanel = new HorizontalPanel();
		imgHPanel.setWidth("100%");
		imgVPanel.add(imgHPanel);
		
		uploadPanel.add(imagensFlowPanel);
		
		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction("eccweb/" + servletURL);
		form.setWidget(fileUpload);
		uploadPanel.add(form);
		
		this.initWidget(uploadPanel);
		
		indiceImagens=0;

		fileUpload.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent arg0) {
				initialize();
				listaImagens = new ArrayList<UploadImagemItem>();
				String nomeArquivos = getFileNames(arg0.getRelativeElement());
				if(!multiple){
					imgVPanel.clear();
				}
				
				List<UploadedFile> listaArquivos = new ArrayList<UploadedFile>();
				UploadedFile uf;
				String[] sa = nomeArquivos.split(",");
				for (String nomeArquivo : sa) {
					String fileName = nomeArquivo.indexOf("\\")>=0?nomeArquivo.substring(nomeArquivo.lastIndexOf("\\")+1):nomeArquivo;

					uf = new UploadedFile();
					uf.setTipo("image/"+fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase());
					uf.setNomeArquivo(fileName);
					
					listaArquivos.add(uf);
				}
				
				serviceUpload.setLista(listaArquivos, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable arg0) {
					}

					@Override
					public void onSuccess(Void arg0) {
					}
				});
				
				enableHTML5 = verificaCompatibilidadeHTML5();
				int i=0;
				for (String nomeArquivo : sa) {
					if(indiceImagens % 3 == 0){
						imgHPanel = new HorizontalPanel();
						imgHPanel.setWidth("100%");
						imgHPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
						imgHPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						imgHPanel.setSpacing(2);
						imgVPanel.add(imgHPanel);
					}
					VerticalPanel  panel = new VerticalPanel();
					panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					panel.setSpacing(1);
					if(multiple){
						panel.setWidth("200px");
						panel.setHeight("150px");
					} else {
						panel.setWidth("100%");
					}
					imgHPanel.add(panel);
					if(multiple){
						imgHPanel.setCellWidth(panel, "200px");
					}
					
					//IMAGEM
					String idImage = "imageUploadItem" + StringUtil.randomString(5);
					final Image image = new Image();
					panel.add(image);
					if(multiple){
						panel.setCellHeight(image, "100px");
						image.setHeight("100px");
						image.setWidth("auto");
						idImage+=StringUtil.randomString(5);
					} else {
						image.setHeight("330px");
						panel.setCellHeight(image, "330px");
						image.setWidth("auto");
					}
					image.getElement().removeAttribute("name");
					image.getElement().removeAttribute("id");
					image.getElement().setAttribute("name",idImage);
					image.getElement().setAttribute("id",idImage);
					
					//filename
					String fileName = nomeArquivo.indexOf("\\")>=0?nomeArquivo.substring(nomeArquivo.lastIndexOf("\\")+1):nomeArquivo;
					Label l = new Label(fileName);
					l.setStyleName("label-italicBlue");
					panel.add(l);
					
					//progress
					UploadProgressBar uploadProgressBar = new UploadProgressBar();
					panel.add(uploadProgressBar);
					
					UploadImagemItem itemImagem = new UploadImagemItem(uploadProgressBar, image, fileName);
					listaImagens.add(itemImagem);
					if(showProgress){
						itemImagem.getUploadProgressBar().initialize(fileName, false);
					}
					if(enableHTML5){
						previewImage(arg0.getRelativeElement(), i, idImage, 1024, GWT.getModuleBaseURL()+servletURL);
						itemImagem.setSituacao(UploadImagemItem.Status.PREVISUALIZANDO);
					}
					i++;
					indiceImagens++;
				}
				uploadLista();
				if(!enableHTML5){
					form.submit();
				}
				iniciaTimer();
			}
		});
	}
	private void uploadLista(){
		boolean ok = false;
		for (final UploadImagemItem item : listaImagens) {
			ok = false;
			if(item.getSituacao()!=null){
				if(item.getSituacao().equals(UploadImagemItem.Status.FINALIZADO)){
					ok = true;
				}
			} else {
				ok = true;
			}
			if(ok){
				if(!enableHTML5){
					item.getImage().setUrl("images/wait.gif");
				}
				if(item.getSituacao()==null || item.getSituacao().equals(UploadImagemItem.Status.PREVISUALIZANDO)){
					item.setSituacao(UploadImagemItem.Status.SUBINDO);
				}
				break;
			}
		}
	}
	
	private void iniciaTimer() {
		timerProgress = new Timer() {
			@Override
			public void run() {
				serviceUpload.getProgress(new AsyncCallback<List<UploadedFile>>() {
					@Override
					public void onSuccess(List<UploadedFile> listaUploadedFile) {
						boolean completado = true;
						for (UploadedFile uf : listaUploadedFile) {
							for (int i=0; i<listaImagens.size(); i++) {
								final UploadImagemItem itemImagem = listaImagens.get(i);
								if(uf.getNomeArquivo().equals(itemImagem.getFileName())){
									if(uf!=null){
										if(showProgress){
											itemImagem.getUploadProgressBar().atualiza(uf);
										}
										if(uf.getProgresso()!=null && uf.getProgresso()>=100){
											itemImagem.setSituacao(UploadImagemItem.Status.FINALIZADO);
											listaImagens.set(i, itemImagem);
											serviceUpload.gravaArquivoDigital(uf, true, new AsyncCallback<Integer>() {
												@Override
												public void onFailure(Throwable arg0) {
												}

												@Override
												public void onSuccess(Integer id) {
													if(!enableHTML5){
														itemImagem.getImage().setUrl("eccweb/downloadArquivoDigital?thumb=true&id="+id);
													}
												}
											});
											uploadLista();
										} else {
											completado = false;
										}
									}
									break;
								}
							}
						}
						if(completado && showProgress){
							timerProgress.cancel();
						}
					}
					@Override
					public void onFailure(Throwable caught) {
					}
				});
				timerProgress.schedule(1000);
			}
		};
		timerProgress.schedule(1000);
	}
	protected void initialize() {
		serviceUpload.initialize(new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {

			}
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	private static native String getFileNames(Element what)/*-{
		return $wnd.getFileNames(what);
	}-*/;
	private static native void previewImage(Element what, Integer indice, String id, Integer resampleSize, String url)/*-{
		$wnd.preview(what, indice, id, resampleSize, url);
	}-*/;
	private static native boolean verificaCompatibilidadeHTML5()/*-{
		return $wnd.verificaHTML5();
	}-*/;
	
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
	public Boolean getMultiple() {
		return multiple;
	}
	public void setMultiple(Boolean multiple) {
		if(multiple){
			fileUpload.getElement().setAttribute("multiple","multiple");
		} else {
			fileUpload.getElement().removeAttribute("multiple");
		}
		this.multiple = multiple;
	}
}