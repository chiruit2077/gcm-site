package br.com.ecc.model.tipo;

public enum TipoRespostaConviteEnum {
	ACEITO("Aceito"),
	RECUSADO("Recusado"),
	ESPERA("Espera");
	
	private String nome;
	
	private TipoRespostaConviteEnum(String nome) {
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