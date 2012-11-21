package br.com.ecc.client.ui.component.upload;

import com.google.gwt.user.client.ui.Image;

public class UploadImagemItem {

	public enum Status {PREVISUALIZANDO, SUBINDO, FINALIZADO};
	
	private String fileName;
	private UploadProgressBar uploadProgressBar;
	private Image image;
	private Status situacao;
	
	public UploadImagemItem(UploadProgressBar uploadProgressBar, Image image, String fileName) {
		this.uploadProgressBar=uploadProgressBar;
		this.image=image;
		this.fileName=fileName;
	}
	public UploadProgressBar getUploadProgressBar() {
		return uploadProgressBar;
	}
	public void setUploadProgressBar(UploadProgressBar uploadProgressBar) {
		this.uploadProgressBar = uploadProgressBar;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Status getSituacao() {
		return situacao;
	}
	public void setSituacao(Status situacao) {
		this.situacao = situacao;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}