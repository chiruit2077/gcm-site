package br.com.ecc.model.tipo;

public enum TipoAtividadeEnum {
	PREPARO("Preparo",30),
	MUSICA("Música",0),
	CONDUCAO("Condução",100),
	ATIVIDADE("Atividade",100),
	DESMONTAGEM("Desmont.",30);

	private String nome;
	private Integer porcentagem;

	private TipoAtividadeEnum(String nome, Integer porcentagem) {
		this.nome = nome;
		this.setPorcentagem(porcentagem);
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

	public Integer getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Integer porcentagem) {
		this.porcentagem = porcentagem;
	}
}