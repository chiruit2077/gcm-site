package br.com.ecc.model.tipo;

public enum TipoAtividadeEnum {
	PREPARO("Preparo"),
	MUSICA("Música"),
	CONDUCAO("Condução"),
	ATIVIDADE("Atividade"),
	DESMONTAGEM("Desmont.");

	private String nome;

	private TipoAtividadeEnum(String nome) {
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
}