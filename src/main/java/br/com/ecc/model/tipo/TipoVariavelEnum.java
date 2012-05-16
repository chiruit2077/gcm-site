package br.com.ecc.model.tipo;

public enum TipoVariavelEnum {
	NOME_ENCONTRISTA("Nome do casal/pessoa", "{NOME_ENCONTRISTA}", null),
	DADOS_USUARIO("Email e senha do usuário", "{DADOS_USUARIO}", null),
	LINK_PLANILHA("Link para a planilha", "{LINK_PLANILHA}", "planilha"),
	LINK_FICHA("Link para a ficha", "{LINK_FICHA}", "ficha"),
	LINK_INSCRICAO("Link para a inscrição", "{LINK_INSCRICAO}", "inscricao"),
	ICONE_CONFIRMACAO("Icone de confirmação", "{ICONE_CONFIRMACAO}", "confirmacao");
	
	private String nome;
	private String tag;
	private String module;
	
	private TipoVariavelEnum(String nome, String tag, String module) {
		this.nome = nome;
		this.tag = tag;
		this.setModule(module);
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
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
}