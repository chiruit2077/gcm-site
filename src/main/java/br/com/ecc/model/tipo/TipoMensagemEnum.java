package br.com.ecc.model.tipo;

public enum TipoMensagemEnum {
	NORMAL("Normal"),
	FICHA_AFILHADO("FICHA - Afilhado"),
	FICHA_ENCONTRISTA("FICHA - Encontrista");
	
	private String nome;
	
	private TipoMensagemEnum(String nome) {
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