package br.com.ecc.model.tipo;

public enum TipoCasalContatoEnum {
	FILHO("Filho"),
	PAI_DELE("Pai dele"),
	MAE_DELE("Mãe dele"),
	PAI_DELA("Pai dela"),
	MAE_DELA("Mãe dela"),
	RESPONSAVEL_FILHOS("Responsável pelos filhos"),
	EMERGENCIA("Contato em emergência"),
	INDICACAO("Indicação");
	
	private String nome;
	
	private TipoCasalContatoEnum(String nome) {
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