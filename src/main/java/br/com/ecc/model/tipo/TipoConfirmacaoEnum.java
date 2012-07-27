package br.com.ecc.model.tipo;

public enum TipoConfirmacaoEnum {
	CONFIRMADO("Confirmado"),
	DESISTENCIA("Desistência");
	
	private String nome;
	
	private TipoConfirmacaoEnum(String nome) {
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