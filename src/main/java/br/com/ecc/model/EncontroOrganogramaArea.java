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
@SequenceGenerator(name="SQ_ENCONTROORGANOAREA", sequenceName="SQ_ENCONTROORGANOAREA")
@NamedQueries({
		@NamedQuery(name="encontroOrganogramaArea.porEncontroOrganograma", query="select u from EncontroOrganogramaArea u " +
				" where u.encontroOrganograma = :encontroorganograma "),
		@NamedQuery(name="encontroOrganogramaArea.updatePorEncontroInscricao1",
				query="update EncontroOrganogramaArea u set u.encontroInscricao1 = null " +
						"where u.encontroInscricao1 = :encontroInscricao " ),
    	@NamedQuery(name="encontroOrganogramaArea.updatePorEncontroInscricao2",
				query="update EncontroOrganogramaArea u set u.encontroInscricao2 = null " +
						"where u.encontroInscricao2 = :encontroInscricao " ),
		@NamedQuery(name="encontroOrganogramaArea.deletePorEncontro", query="delete from EncontroOrganogramaArea u " +
				" where u.encontroOrganograma in ( select a from EncontroOrganograma a where a.encontro = :encontro ) ")
})

public class EncontroOrganogramaArea extends _WebBaseEntity {

	private static final long serialVersionUID = -9078931060574120248L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROORGANOAREA", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontroOrganograma")
	private EncontroOrganograma encontroOrganograma;

	@ManyToOne
	@JoinColumn(name="encontroInscricao1")
	private EncontroInscricao encontroInscricao1;

	@ManyToOne
	@JoinColumn(name="encontroInscricao2")
	private EncontroInscricao encontroInscricao2;

	@ManyToOne
	@JoinColumn(name="organogramaArea")
	private OrganogramaArea organogramaArea;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return organogramaArea.getNome();
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
	public EncontroInscricao getEncontroInscricao1() {
		return encontroInscricao1;
	}
	public void setEncontroInscricao1(EncontroInscricao encontroInscricao1) {
		this.encontroInscricao1 = encontroInscricao1;
	}
	public EncontroInscricao getEncontroInscricao2() {
		return encontroInscricao2;
	}
	public void setEncontroInscricao2(EncontroInscricao encontroInscricao2) {
		this.encontroInscricao2 = encontroInscricao2;
	}
	public EncontroOrganograma getEncontroOrganograma() {
		return encontroOrganograma;
	}
	public void setEncontroOrganograma(EncontroOrganograma encontroOrganograma) {
		this.encontroOrganograma = encontroOrganograma;
	}
	public OrganogramaArea getOrganogramaArea() {
		return organogramaArea;
	}
	public void setOrganogramaArea(OrganogramaArea organogramaArea) {
		this.organogramaArea = organogramaArea;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroOrganogramaArea other = (EncontroOrganogramaArea) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}