package br.com.ecc.model.tipo;

public enum TipoExibicaoPlanilhaEnum {
	MINHA_ATIVIDADE_MINHA_COLUNA("Minhas atividades e apenas minha coluna"),
	MINHA_ATIVIDADE_TODAS_COLUNAS("Minhas atividades e todas as colunas"),
	TODAS_ATIVIDADES_MINHA_COLUNA("Todas as atividades e apenas minha coluna"),
	COMPLETA("Planilha completa");
	
	private String nome;
	
	private TipoExibicaoPlanilhaEnum(String nome) {
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