package br.com.ecc.client.core;


public class ImageViewItem {

	private String nome;
	private String resourcePath;
	private String contentType;
	private String miniaturePath;

	public ImageViewItem(String nome, String resourcePath, String contentType, String miniaturePath) {
		this.nome = nome;
		this.resourcePath = resourcePath;
		this.contentType = contentType;
		this.miniaturePath = miniaturePath;
	}
	
	public ImageViewItem() {
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public String getMiniaturePath() {
		return miniaturePath;
	}
	public void setMiniaturePath(String miniaturePath) {
		this.miniaturePath = miniaturePath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}