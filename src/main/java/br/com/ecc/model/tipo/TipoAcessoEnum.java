package br.com.ecc.model.tipo;

public enum TipoAcessoEnum {
	COMPLETO("Acesso completo"),
	LEITURA("Somente leitura"),
	NENHUM("Sem acesso");
	
	private String nome;
	
	private TipoAcessoEnum(String nome) {
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