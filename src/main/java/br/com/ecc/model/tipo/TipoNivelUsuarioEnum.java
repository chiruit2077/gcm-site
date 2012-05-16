package br.com.ecc.model.tipo;

public enum TipoNivelUsuarioEnum {
	ADMINISTRADOR("Administrador"),
	NORMAL("Normal");
	
	private String nome;
	
	private TipoNivelUsuarioEnum(String nome) {
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