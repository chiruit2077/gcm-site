package br.com.ecc.client.ui.component.upload;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.UploadProgressService;
import br.com.ecc.client.service.UploadProgressServiceAsync;
import br.com.ecc.client.ui.component.upload.UploadImagemItem.Status;
import br.com.ecc.client.util.StringUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
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
	private Boolean resample=true;
	
	private List<UploadImagemItem> listaImagens = new ArrayList<UploadImagemItem>();
	HorizontalPanel imgHPanel;
	VerticalPanel imgVPanel;
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
		
		imgVPanel = new VerticalPanel();
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
		form.setVisible(false);
		
		this.initWidget(uploadPanel);
		
		indiceImagens=0;

		fileUpload.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(final ChangeEvent evento) {
				final Element elementoNativo = evento.getRelativeElement();
				final String nomeArquivos = getFileNames(elementoNativo);
				serviceUpload.initialize(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						if(!multiple){
							clear();
						}
						listaImagens = new ArrayList<UploadImagemItem>();
						if(!multiple){
							imgVPanel.clear();
						}
						
						List<UploadedFile> listaArquivos = new ArrayList<UploadedFile>();
						UploadedFile uf;
						final String[] sa = nomeArquivos.split(",");
						for (String nomeArquivo : sa) {
							String fileName = nomeArquivo.indexOf("\\")>=0?nomeArquivo.substring(nomeArquivo.lastIndexOf("\\")+1):nomeArquivo;

							uf = new UploadedFile();
							uf.setTipo("image/"+fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase());
							uf.setNomeArquivo(fileName);
							
							listaArquivos.add(uf);
						}
						
						enableHTML5 = verificaCompatibilidadeHTML5();
						serviceUpload.setLista(listaArquivos, new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable arg0) {
							}

							@Override
							public void onSuccess(Void arg0) {
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
									VerticalPanel panel = new VerticalPanel();
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
										image.setHeight("350px");
										panel.setCellHeight(image, "350px");
										image.setWidth("auto");
									}
									image.getElement().removeAttribute("name");
									image.getElement().removeAttribute("id");
									image.getElement().setAttribute("name",idImage);
									image.getElement().setAttribute("id",idImage);
									
									final int indice = i;
									image.addLoadHandler(new LoadHandler() {
										@Override
										public void onLoad(LoadEvent arg0) {
											verificaImagens(indice);
										}
									});
									
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
										if(resample){
											previewImage(elementoNativo, i, idImage, 1024, GWT.getModuleBaseURL()+servletURL);
										} else {
											previewImage(elementoNativo, i, idImage, null, GWT.getModuleBaseURL()+servletURL);
										}
										itemImagem.setSituacao(UploadImagemItem.Status.PREVISUALIZANDO);
									}
									i++;
									indiceImagens++;
								}
								//uploadLista();
								if(!enableHTML5){
									form.submit();
								}
								iniciaTimer();
							}
						});
					}
					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		});
	}
	private void verificaImagens(int indice){
		boolean ok = true;
		for (int i= 0; i < listaImagens.size(); i++) {
			if(i==indice){
				listaImagens.get(i).setSituacao(Status.CARREGADA);
			}
			if(!listaImagens.get(i).getSituacao().equals(Status.CARREGADA)){
				ok = false;
			}
		}
		if(ok){
			uploadLista();
		}
	}
	
	public void clear(){
		listaImagens = new ArrayList<UploadImagemItem>();
		if(timerProgress!=null){
			timerProgress.cancel();
		}
		imgHPanel.clear();
		imgVPanel.clear();
		indiceImagens = 0;
	}
	
	private void uploadLista(){
		boolean ok = false;
		int i=0;
		for (final UploadImagemItem item : listaImagens) {
			ok = false;
			if(item.getSituacao()!=null){
				if(!item.getSituacao().equals(UploadImagemItem.Status.FINALIZADO)){
					ok = true;
				}
			} else {
				ok = true;
			}
			if(ok){
				if(!enableHTML5){
					item.getImage().setUrl("images/wait.gif");
				}
				uploadImage(i);
				if(item.getSituacao()==null || item.getSituacao().equals(UploadImagemItem.Status.PREVISUALIZANDO)){
					item.setSituacao(UploadImagemItem.Status.SUBINDO);
				}
				break;
			}
			i++;
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
								if(uf.getNomeArquivo()!=null && itemImagem.getFileName()!=null && uf.getNomeArquivo().equals(itemImagem.getFileName())){
									if(uf!=null){
										if(showProgress){
											itemImagem.getUploadProgressBar().atualiza(uf);
										}
										if(uf.getProgresso()!=null && uf.getProgresso()>=100){
											itemImagem.setSituacao(UploadImagemItem.Status.FINALIZADO);
											listaImagens.set(i, itemImagem);
											final int j = i;
											serviceUpload.gravaArquivoDigital(uf, resample, new AsyncCallback<Integer>() {
												@Override
												public void onFailure(Throwable arg0) {
												}

												@Override
												public void onSuccess(Integer id) {
													listaImagens.get(j).setIdArquivoDigital(id);
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
	private static native String getFileNames(Element what)/*-{
		return $wnd.getFileNames(what);
	}-*/;
	private static native void previewImage(Element what, Integer indice, String id, Integer resampleSize, String url)/*-{
		$wnd.preview(what, indice, id, resampleSize, url);
	}-*/;
	private static native void uploadImage(Integer indice)/*-{
		$wnd.enviaDados(indice);
	}-*/;
	private static native boolean verificaCompatibilidadeHTML5()/*-{
		return $wnd.verificaHTML5();
	}-*/;
	
	public void setShowProgress(Boolean showProgress) {
		this.showProgress = showProgress;
	}
	public String getServletURL() {
		return servletURL;
	}
	public void setServletURL(String servletURL) {
		this.servletURL = servletURL;
	}
	public void setMultiple(Boolean multiple) {
		if(multiple){
			fileUpload.getElement().setAttribute("multiple","multiple");
		} else {
			fileUpload.getElement().removeAttribute("multiple");
		}
		this.multiple = multiple;
	}
	public List<UploadImagemItem> getListaImagens() {
		return listaImagens;
	}
	public void adicionaImage(){
		fileUpload.getElement().<InputElement>cast().click();
	}
	public void setResample(Boolean resample) {
		this.resample = resample;
	}
}