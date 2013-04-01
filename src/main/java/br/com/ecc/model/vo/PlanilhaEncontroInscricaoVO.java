package br.com.ecc.model.vo;

import java.io.Serializable;

public class PlanilhaEncontroInscricaoVO implements Serializable {

	private static final long serialVersionUID = 6874537373434370960L;

	private String ele;
	private String ela;
	private String tipo;
	private String papel;
	private Integer qtde;
	private Integer media;

	public String getEle() {
		return ele;
	}
	public void setEle(String ele) {
		this.ele = ele;
	}
	public String getEla() {
		return ela;
	}
	public void setEla(String ela) {
		this.ela = ela;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getPapel() {
		return papel;
	}
	public void setPapel(String papel) {
		this.papel = papel;
	}
	public Integer getQtde() {
		return qtde;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public Integer getMedia() {
		return media;
	}
	public void setMedia(Integer media) {
		this.media = media;
	}

}