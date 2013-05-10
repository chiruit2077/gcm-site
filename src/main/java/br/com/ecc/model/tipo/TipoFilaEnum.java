package br.com.ecc.model.tipo;

public enum TipoFilaEnum {
	NORMAL("Normal"),
	GERAL("Geral"),
	POR_VAGA("Por vaga");

	private String nome;

	private TipoFilaEnum(String nome) {
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