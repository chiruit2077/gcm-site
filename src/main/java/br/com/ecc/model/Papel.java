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
	@NamedQuery(name="papel.porGrupo", query="select u from Papel u where u.grupo = :grupo order by u.nome")
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

	private Boolean aparecePlanilha;
	private Boolean chocaPlanilha;

	private Boolean padrao;
	private Boolean padrinho;
	private Boolean coordenacao;

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


	public Boolean getAparecePlanilha() {
		return aparecePlanilha;
	}


	public void setAparecePlanilha(Boolean aparecePlanilha) {
		this.aparecePlanilha = aparecePlanilha;
	}


	public Boolean getPadrao() {
		if (padrao==null) return false;
		return padrao;
	}

	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}


	public Boolean getPadrinho() {
		if (padrinho==null) return false;
		return padrinho;
	}


	public void setPadrinho(Boolean padrinho) {
		this.padrinho = padrinho;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Papel other = (Papel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public Boolean getChocaPlanilha() {
		if (chocaPlanilha==null) return false;
		return chocaPlanilha;
	}
	public void setChocaPlanilha(Boolean chocaPlanilha) {
		this.chocaPlanilha = chocaPlanilha;
	}

	public Boolean getCoordenacao() {
		if (coordenacao==null) return false;
		return coordenacao;
	}

	public void setCoordenacao(Boolean coordenacao) {
		this.coordenacao = coordenacao;
	}
}