package br.com.ecc.model.tipo;

public enum TipoCasalEnum {
	ENCONTRISTA("Encontrista"),
	CONVIDADO("Convidado");
	
	private String nome;
	
	private TipoCasalEnum(String nome) {
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