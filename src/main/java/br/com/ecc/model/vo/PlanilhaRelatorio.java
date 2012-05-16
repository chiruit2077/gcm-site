package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PlanilhaRelatorio implements Serializable {
	private static final long serialVersionUID = -635188537247277536L;
	
	private Date inicio;
	private Date fim;
	private String dia;
	private String tipoAtividade;
	private String atividade;
	private String periodo;
	private List<String> participante;
	private List<String> papel;
	private List<Boolean> padrinho;
	private List<Boolean> nos;
	private Integer qtdeParticipantes;
	private String legenda;
	private Boolean minhaAtividade;

	public String getAtividade() {
		return atividade;
	}
	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFim() {
		return fim;
	}
	public void setFim(Date fim) {
		this.fim = fim;
	}
	public List<String> getParticipante() {
		return participante;
	}
	public void setParticipante(List<String> participante) {
		this.participante = participante;
	}
	public List<String> getPapel() {
		return papel;
	}
	public void setPapel(List<String> papel) {
		this.papel = papel;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public String getTipoAtividade() {
		return tipoAtividade;
	}
	public void setTipoAtividade(String tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}
	public Integer getQtdeParticipantes() {
		return qtdeParticipantes;
	}
	public void setQtdeParticipantes(Integer qtdeParticipantes) {
		this.qtdeParticipantes = qtdeParticipantes;
	}
	public String getLegenda() {
		return legenda;
	}
	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}
	public List<Boolean> getPadrinho() {
		return padrinho;
	}
	public void setPadrinho(List<Boolean> padrinho) {
		this.padrinho = padrinho;
	}
	public List<Boolean> getNos() {
		return nos;
	}
	public void setNos(List<Boolean> nos) {
		this.nos = nos;
	}
	public Boolean getMinhaAtividade() {
		return minhaAtividade;
	}
	public void setMinhaAtividade(Boolean minhaAtividade) {
		this.minhaAtividade = minhaAtividade;
	}
}