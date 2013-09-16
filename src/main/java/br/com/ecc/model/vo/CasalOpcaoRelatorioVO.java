package br.com.ecc.model.vo;

import java.io.Serializable;

public class CasalOpcaoRelatorioVO implements Serializable {
	private static final long serialVersionUID = 2765592231259527848L;
	
	private String titulo;
	private Boolean email;
	private Boolean apelido;
	private Boolean documento;
	private Boolean endereco;
	private Boolean telefone;
	private Boolean alergia;
	private Boolean diabetico;
	private Boolean vegetariano;
	private Boolean hipertenso;
	private Boolean tipo;
	
	public Boolean getEmail() {
		return email;
	}
	public void setEmail(Boolean email) {
		this.email = email;
	}
	public Boolean getApelido() {
		return apelido;
	}
	public void setApelido(Boolean apelido) {
		this.apelido = apelido;
	}
	public Boolean getDocumento() {
		return documento;
	}
	public void setDocumento(Boolean documento) {
		this.documento = documento;
	}
	public Boolean getEndereco() {
		return endereco;
	}
	public void setEndereco(Boolean endereco) {
		this.endereco = endereco;
	}
	public Boolean getTelefone() {
		return telefone;
	}
	public void setTelefone(Boolean telefone) {
		this.telefone = telefone;
	}
	public Boolean getAlergia() {
		return alergia;
	}
	public void setAlergia(Boolean alergia) {
		this.alergia = alergia;
	}
	public Boolean getDiabetico() {
		return diabetico;
	}
	public void setDiabetico(Boolean diabetico) {
		this.diabetico = diabetico;
	}
	public Boolean getVegetariano() {
		return vegetariano;
	}
	public void setVegetariano(Boolean vegetariano) {
		this.vegetariano = vegetariano;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Boolean getTipo() {
		return tipo;
	}
	public void setTipo(Boolean tipo) {
		this.tipo = tipo;
	}
	public Boolean getHipertenso() {
		return hipertenso;
	}
	public void setHipertenso(Boolean hipertenso) {
		this.hipertenso = hipertenso;
	}
}