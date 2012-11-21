package br.com.ecc.client.ui.component.upload;

import java.io.Serializable;

public final class UploadedFile implements Serializable {
	private static final long serialVersionUID = 9202390725897799834L;
	
	private String nomeArquivo;
	private String caminho;
	private String tipo;
	private Integer progresso;
	private Integer idArquivoDigital;

	public UploadedFile() {
	}

	@Override
	public String toString() {
		return nomeArquivo;
	}
	public Integer getIdArquivoDigital() {
		return idArquivoDigital;
	}
	public void setIdArquivoDigital(Integer idArquivoDigital) {
		this.idArquivoDigital = idArquivoDigital;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getCaminho() {
		return caminho;
	}
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getProgresso() {
		return progresso;
	}
	public void setProgresso(Integer progresso) {
		this.progresso = progresso;
	}
}