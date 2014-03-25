package br.com.ecc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import br.com.ecc.model.tipo.TipoDocumentoEnum;

@Entity
@SequenceGenerator(name="SQ_DOCUMENTO", sequenceName="SQ_DOCUMENTO")
@NamedQueries({
	@NamedQuery(name="documento.listarPorGrupoEncontro",
		query="select u.id, u.titulo, u.data, u.tipoDocumento, u.encontro from Documento u " +
			  "where u.grupo = :grupo and u.encontro = :encontro order by u.data desc"),
    @NamedQuery(name="documento.listarPorGrupo",
		query="select u.id, u.titulo, u.data, u.tipoDocumento from Documento u " +
			  "where u.grupo = :grupo order by u.data desc")
})
public class Documento extends _WebBaseEntity {
	private static final long serialVersionUID = 4582979048640334513L;

	@Id
	@GeneratedValue(generator="SQ_DOCUMENTO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Column(length=254)
	private String titulo;

	@Column(columnDefinition="TEXT")
	private char[] texto;

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoDocumentoEnum tipoDocumento;
	
	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return titulo;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public TipoDocumentoEnum getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(TipoDocumentoEnum tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public char[] getTexto() {
		return texto;
	}
	public void setTexto(char[] texto) {
		this.texto = texto;
	}
}