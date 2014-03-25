package br.com.ecc.model.tipo;

public enum TipoDocumentoEnum {
	PUBLICO("Público"),
	PRIVADO("Privado");
	
	private String nome;
	
	private TipoDocumentoEnum(String nome) {
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