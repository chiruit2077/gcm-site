package br.com.ecc.model.tipo;

public enum TipoAtividadeItemEnum {
	PRE_REQUISITO("Pré-Requisitos", 0),
	OBJETIVO("Objetivos", 1),
	ROTEIRO("Roteiro", 2),
	RESULTADO_ESPERADO("Resultados Esperados", 3);

	private String nome;
	private Integer codigo;

	private TipoAtividadeItemEnum(String nome, Integer codigo) {
		this.setCodigo(codigo);
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
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
}