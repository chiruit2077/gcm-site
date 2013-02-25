package br.com.ecc.model.tipo;

public enum TipoEncontroQuartoEnum {
	AFILHADO("Afilhado", "agrupamento-QuartoAfilhado", 0),
	PADRINHO("Padrinho", "agrupamento-QuartoPadrinho", 1),
	APOIO("Apoio", "agrupamento-Quarto", 2),
	MEDICO("Médico", "agrupamento-QuartoMedico", 3),
	SECRETARIA("Secretaria", "agrupamento-QuartoSecretaria", 4),
	CAMARIM("Camarim", "agrupamento-QuartoCamarim", 5),
	RESERVADO("Reservado","agrupamento-QuartoReservado", 6),
	SOLTEIROS("Solteiros","agrupamento-QuartoSolteiro", 7),
	COORDENADOR("Coordenador","agrupamento-QuartoCoordenador", 8);

	private String nome;
	private Integer codigo;
	private String style;

	private TipoEncontroQuartoEnum(String nome, String style, Integer codigo) {
		this.setCodigo(codigo);
		this.setStyle(style);
		this.setNome(nome);
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
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
}