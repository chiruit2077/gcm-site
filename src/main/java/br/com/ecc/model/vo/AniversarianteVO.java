package br.com.ecc.model.vo;

import java.io.Serializable;

import br.com.ecc.model.Casal;
import br.com.ecc.model.Pessoa;

public class AniversarianteVO implements Serializable {
	private static final long serialVersionUID = -5574540362459128639L;
	
	private Pessoa pessoa;
	private Casal casal;
	private Boolean hoje = false;
	private Boolean antigo = false;
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Boolean getHoje() {
		return hoje;
	}
	public void setHoje(Boolean hoje) {
		this.hoje = hoje;
	}
	public Boolean getAntigo() {
		return antigo;
	}
	public void setAntigo(Boolean antigo) {
		this.antigo = antigo;
	}
}