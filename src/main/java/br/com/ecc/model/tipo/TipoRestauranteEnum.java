package br.com.ecc.model.tipo;

public enum TipoRestauranteEnum {
	JAPONES("Japonês", 0),
	BEMMEQUER("Café Bem Me Quer", 1),
	PESCADOR("Ilha do Pesacador", 2),
	ROMANTICO("Bistro do Amor", 4),
	COUNTRY("Café Country", 5),
	TARANTELA("Tarantela", 6);

	private String nome;
	private Integer codigo;

	private TipoRestauranteEnum(String nome, Integer codigo) {
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