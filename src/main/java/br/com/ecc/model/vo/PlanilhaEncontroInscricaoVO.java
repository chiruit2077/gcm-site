package br.com.ecc.model.vo;

import java.io.Serializable;

import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroInscricao;

public class PlanilhaEncontroInscricaoVO implements Serializable {

	private static final long serialVersionUID = 6874537373434370960L;

	private String nome;
	private String tipo;
	private EncontroInscricao encontroInscricao;
	private String atividade;
	private EncontroAtividade encontroAtividade;
	private String participante;
	private Boolean coordenacao;
	private EncontroAtividadeInscricao encontroAtividadeInscricao;

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}
	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
	public String getAtividade() {
		return atividade;
	}
	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}
	public EncontroAtividade getEncontroAtividade() {
		return encontroAtividade;
	}
	public void setEncontroAtividade(EncontroAtividade encontroAtividade) {
		this.encontroAtividade = encontroAtividade;
	}
	public String getParticipante() {
		return participante;
	}
	public void setParticipante(String participante) {
		this.participante = participante;
	}
	public EncontroAtividadeInscricao getEncontroAtividadeInscricao() {
		return encontroAtividadeInscricao;
	}
	public void setEncontroAtividadeInscricao(
			EncontroAtividadeInscricao encontroAtividadeInscricao) {
		this.encontroAtividadeInscricao = encontroAtividadeInscricao;
	}
	public Boolean getCoordenacao() {
		return coordenacao;
	}
	public void setCoordenacao(Boolean coordenacao) {
		this.coordenacao = coordenacao;
	}

}