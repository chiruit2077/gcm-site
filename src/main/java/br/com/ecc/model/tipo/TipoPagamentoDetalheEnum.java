package br.com.ecc.model.tipo;

public enum TipoPagamentoDetalheEnum {
	TAXAENCONTRO("VALOR DA TAXA ENCONTRO",false),
	HOSPEDAGEM("VALOR DA HOSPEDAGEM",true),
	ALIMENTACAO("VALOR DA ALIMENTACAO",true),
	OUTRAINSCRICAO("VALOR PARA OUTRA INSCRIÇÃO",false),
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