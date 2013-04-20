package br.com.ecc.model;

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
@SequenceGenerator(name="SQ_ENCONTROORGANOGRAMA", sequenceName="SQ_ENCONTROORGANOGRAMA")
@NamedQueries({
	@NamedQuery(name="encontroOrganograma.porEncontro", query="select u from EncontroOrganograma u where u.encontro = :encontro order by u.organograma.nome"),
	@NamedQuery(name="encontroOrganograma.deletePorEncontro", query="delete from EncontroOrganograma u where u.encontro = :encontro")
})
public class EncontroOrganograma extends _WebBaseEntity {

	private static final long serialVersionUID = -2166155813154805917L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROORGANOGRAMA", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@ManyToOne
	@JoinColumn(name="organograma")
	private Organograma organograma;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return organograma.getNome();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Organograma getOrganograma() {
		return organograma;
	}
	public void setOrganograma(Organograma organograma) {
		this.organograma = organograma;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroOrganograma other = (EncontroOrganograma) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}