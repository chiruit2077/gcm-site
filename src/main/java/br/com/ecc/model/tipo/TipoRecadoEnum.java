package br.com.ecc.model.tipo;

public enum TipoRecadoEnum {
	ENCONTRISTA("Todos os encontristas"),
	PRIVADO("Apenas o destinatário");
	
	private String nome;
	
	private TipoRecadoEnum(String nome) {
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