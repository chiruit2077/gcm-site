package br.com.ecc.model.tipo;

public enum TipoArquivoEnum {

	OBJECT_OLE("Objeto OLE"),
	ARQUIVO("Arquivo"),
	PICTURE("Figura");
	
	private String nome;
	
	private TipoArquivoEnum(String nome) {
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