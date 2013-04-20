package br.com.ecc.model.tipo;

public enum TipoPagamentoDetalheEnum {
	TAXAENCONTRO("TAXA ECC",false),
	HOSPEDAGEM("HOSPEDAGEM",true),
	ALIMENTACAO("ALIMENTACAO",true),
	OUTRAINSCRICAO("DOAÇÃO OUTRA INSCRIÇÃO",false),
	AVULSO("AVULSO",true);

	private String nome;
	private Boolean quantidade;

	private TipoPagamentoDetalheEnum(String nome, Boolean quantidade) {
		this.nome = nome;
		this.quantidade = quantidade;
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

	public Boolean getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Boolean quantidade) {
		this.quantidade = quantidade;
	}
}