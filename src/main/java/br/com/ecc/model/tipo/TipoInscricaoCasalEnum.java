package br.com.ecc.model.tipo;

public enum TipoInscricaoCasalEnum {
	AFILHADO("Afilhado", 0),
	ENCONTRISTA("Encontrista", 1);

	private String nome;
	private Integer codigo;

	private TipoInscricaoCasalEnum(String nome, Integer codigo) {
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

	public static TipoInscricaoCasalEnum getPorInscricaoCasal(TipoInscricaoEnum tipo){
		if (tipo.equals(TipoInscricaoEnum.AFILHADO)) return AFILHADO;
		return ENCONTRISTA;
	}
}