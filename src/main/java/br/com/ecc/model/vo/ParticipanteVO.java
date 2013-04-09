package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroInscricao;

public class ParticipanteVO implements Serializable {
	private static final long serialVersionUID = -2800919137185492168L;

	private EncontroInscricao encontroInscricao;
	private Integer qtdeAtividades;
	private Integer qtdeMesmaAtividades;
	private List<String> tags = new ArrayList<String>();
	private Boolean selecionado=false;
	private List<EncontroAtividadeInscricao> encontroAtividadeInscricoes = new ArrayList<EncontroAtividadeInscricao>();

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
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public Boolean getSelecionado() {
		return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	public List<EncontroAtividadeInscricao> getEncontroAtividadeInscricoes() {
		return encontroAtividadeInscricoes;
	}
	public void setEncontroAtividadeInscricoes(
			List<EncontroAtividadeInscricao> encontroAtividadeInscricoes) {
		this.encontroAtividadeInscricoes = encontroAtividadeInscricoes;
	}
	public Integer getQtdeMesmaAtividades() {
		return qtdeMesmaAtividades;
	}
	public void setQtdeMesmaAtividades(Integer qtdeMesmaAtividades) {
		this.qtdeMesmaAtividades = qtdeMesmaAtividades;
	}
}