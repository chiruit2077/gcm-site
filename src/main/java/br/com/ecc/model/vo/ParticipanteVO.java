package br.com.ecc.model.vo;

import java.io.Serializable;

import br.com.ecc.model.EncontroInscricao;

public class ParticipanteVO implements Serializable {
	private static final long serialVersionUID = -2800919137185492168L;
	
	private EncontroInscricao encontroInscricao;
	private Integer qtdeAtividades;
	
	public Integer getQtdeAtividades() {
		return qtdeAtividades;
	}
	public void setQtdeAtividades(Integer qtdeAtividades) {
		this.qtdeAtividades = qtdeAtividades;
	}
	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}
	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
}