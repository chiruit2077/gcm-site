package br.com.ecc.model.vo;

import java.io.Serializable;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;

public class CasalParamVO implements Serializable {
	private static final long serialVersionUID = 2926102079696867943L;
	
	private Grupo grupo;
	private String nome;
	private Agrupamento agrupamento;
	private TipoInscricaoEnum tipoInscricao;
	private TipoCasalEnum tipoCasal;
	private Encontro encontro;
	private Boolean todosInscritos;
	
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Agrupamento getAgrupamento() {
		return agrupamento;
	}
	public void setAgrupamento(Agrupamento agrupamento) {
		this.agrupamento = agrupamento;
	}
	public TipoInscricaoEnum getTipoInscricao() {
		return tipoInscricao;
	}
	public void setTipoInscricao(TipoInscricaoEnum tipoInscricao) {
		this.tipoInscricao = tipoInscricao;
	}
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public Boolean getTodosInscritos() {
		return todosInscritos;
	}
	public void setTodosInscritos(Boolean todosInscritos) {
		this.todosInscritos = todosInscritos;
	}
	public TipoCasalEnum getTipoCasal() {
		return tipoCasal;
	}
	public void setTipoCasal(TipoCasalEnum tipoCasal) {
		this.tipoCasal = tipoCasal;
	}
}