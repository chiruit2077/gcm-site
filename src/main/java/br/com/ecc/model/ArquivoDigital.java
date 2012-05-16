package br.com.ecc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import br.com.ecc.model.tipo.TipoArquivoEnum;

@Entity
@SequenceGenerator(name="SQ_ARQUIVODIG", sequenceName="SQ_ARQUIVODIG")
@NamedQueries({
	@NamedQuery(name="arquivoDigital.limpaLixo", 
			query="delete from ArquivoDigital ad " +
				  "where ad not in ( Select c.idArquivoDigital from Casal c ) and " +
				  "      ad not in ( Select g.idArquivoDigital from Grupo g ) ")
})
public class ArquivoDigital extends _WebBaseEntity {
	private static final long serialVersionUID = 205467600581307417L;

	@Id
	@GeneratedValue(generator="SQ_ARQUIVODIG", strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(length=254)
	private String nomeArquivo;
	
	@Column(length=254)
	private String descricao;

	@Column(length=254)
	private String mimeType;
	
	@Column(columnDefinition="BLOB")
	private byte[] dados;
	
	private Integer tamanho;
	
	@Enumerated(EnumType.STRING)
	private TipoArquivoEnum tipo;
	
	@Column(length=254)
	private Integer idLink;
	
	@Column(length=254)
	private String entidadeLink;
	
	@Version
	private Integer version;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public byte[] getDados() {
		return dados;
	}
	public void setDados(byte[] dados) {
		this.dados = dados;
	}
	public Integer getTamanho() {
		return tamanho;
	}
	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}
	public TipoArquivoEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoArquivoEnum tipo) {
		this.tipo = tipo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public Integer getIdLink() {
		return idLink;
	}
	public void setIdLink(Integer idLink) {
		this.idLink = idLink;
	}
	public String getEntidadeLink() {
		return entidadeLink;
	}
	public void setEntidadeLink(String entidadeLink) {
		this.entidadeLink = entidadeLink;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}