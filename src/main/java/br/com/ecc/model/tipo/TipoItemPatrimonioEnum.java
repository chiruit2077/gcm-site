package br.com.ecc.model.tipo;

public enum TipoItemPatrimonioEnum {
	AGRUPAMENTO("Agrupamento"),
	ITEM("Item");
	
	private String nome;
	
	private TipoItemPatrimonioEnum(String nome) {
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