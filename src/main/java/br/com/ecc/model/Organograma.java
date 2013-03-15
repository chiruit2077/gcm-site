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
@SequenceGenerator(name="SQ_ORGANOGRAMA", sequenceName="SQ_ORGANOGRAMA")
@NamedQueries({
	@NamedQuery(name="organograma.porGrupo", query="select u from Organograma u where u.grupo = :grupo order by u.nome")
})
public class Organograma extends _WebBaseEntity {

	private static final long serialVersionUID = 8109231124024487169L;

	@Id
	@GeneratedValue(generator="SQ_ORGANOGRAMA", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String nome;

	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return nome;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organograma other = (Organograma) obj;
		if (id != null) {
			if (id.equals(other.id))
				return true;
		}if (nome != null) {
			if (nome.equals(other.nome))
				return true;
		}
		return false;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

}