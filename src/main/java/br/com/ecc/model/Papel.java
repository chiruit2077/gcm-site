package br.com.ecc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_PAPEL", sequenceName="SQ_PAPEL")
@NamedQueries({
	@NamedQuery(name="papel.porGrupo", query="select u from Papel u where u.grupo = :grupo order by u.sigla")
})
public class Papel extends _WebBaseEntity {
	private static final long serialVersionUID = -731109587772901285L;

	@Id
	@GeneratedValue(generator="SQ_PAPEL", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;
	
	@Column(length=150)
	private String nome;
	
	@Column(length=5)
	private String sigla;
	
	@Version
	private Integer version;

	@Override
	public String toString() {
		return getNome();
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
}