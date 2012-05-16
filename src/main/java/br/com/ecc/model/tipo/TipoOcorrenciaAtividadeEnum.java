package br.com.ecc.model.tipo;

public enum TipoOcorrenciaAtividadeEnum {
	EXCLUSIVA("Exclusiva"),
	SIMULTANEA("Simultânea");
	
	private String nome;
	
	private TipoOcorrenciaAtividadeEnum(String nome) {
		this.nome = nome;
	}
	@Override
	public String toString() {
		return getNome();
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}