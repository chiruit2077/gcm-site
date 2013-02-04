package br.com.ecc.model.tipo;

public enum TipoQuartoEnum {
	AFILHADO("Afilhado", 0),
	PADRINHO("Padrinho", 1),
	APOIO("Apoio", 2),
	MEDICO("Médico", 3),
	SECRETARIA("Secretaria", 4),
	CAMARIM("Camarim", 5),
	RESERVADO("Reservado", 6);

	private String nome;
	private Integer codigo;

	private TipoQuartoEnum(String nome, Integer codigo) {
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