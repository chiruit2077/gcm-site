package br.com.ecc.model.tipo;

public enum TipoEncontroAtividadeProgramaEnum {
	PROGRAMA("Atividade do programa"),
	PARALELA("Atividade paralela");
	
	private String nome;
	
	private TipoEncontroAtividadeProgramaEnum(String nome) {
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