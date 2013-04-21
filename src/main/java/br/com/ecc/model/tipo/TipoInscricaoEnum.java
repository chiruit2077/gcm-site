package br.com.ecc.model.tipo;

public enum TipoInscricaoEnum {
	AFILHADO("Afilhado", 0),
	COORDENADOR("Coordenador", 1),
	PADRINHO("Padrinho", 2),
	APOIO("Apoio", 3),
	EXTERNO("Externo", 4),
	DOACAO("Doação", 5);

	private String nome;
	private Integer codigo;

	private TipoInscricaoEnum(String nome, Integer codigo) {
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
	public static TipoInscricaoEnum getPorEncontroQuarto(TipoEncontroQuartoEnum tipo){
		if (tipo.equals(TipoEncontroQuartoEnum.AFILHADO)) return AFILHADO;
		if (tipo.equals(TipoEncontroQuartoEnum.PADRINHO)) return PADRINHO;
		if (tipo.equals(TipoEncontroQuartoEnum.COORDENADOR)) return COORDENADOR;
		return APOIO;
	}
}