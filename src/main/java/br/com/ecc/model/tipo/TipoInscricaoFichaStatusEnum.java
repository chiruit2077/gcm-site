package br.com.ecc.model.tipo;

public enum TipoInscricaoFichaStatusEnum {
	NORMAL("Normal", 0),
	LIBERADO("Liberado",1),
	RESERVADO("Reservado", 2);

	private String nome;
	private Integer codigo;

	private TipoInscricaoFichaStatusEnum(String nome, Integer codigo) {
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