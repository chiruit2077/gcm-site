package br.com.ecc.model.tipo;

public enum TipoPreenchimentoAtividadeEnum {
	TODOS("Todos"),
	TODOSEXTERNO("Todos + Externo"),
	EXTERNO("Externo"),
	PADRINHOS("Padrinhos"),
	METADE("Metade Participantes"),
	VARIAVEL("Variavel");

	private String nome;

	private TipoPreenchimentoAtividadeEnum(String nome) {
		setNome(nome);
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
