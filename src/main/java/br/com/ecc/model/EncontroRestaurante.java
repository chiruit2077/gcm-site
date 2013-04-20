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
@SequenceGenerator(name="SQ_ENCONTRORESTAURANTE", sequenceName="SQ_ENCONTRORESTAURANTE")
@NamedQueries({
	@NamedQuery(name="encontroRestaurante.porEncontro", query="select u from EncontroRestaurante u where u.encontro = :encontro order by u.restaurante.ordem "),
	@NamedQuery(name="encontroRestaurante.deletePorEncontro", query="delete from EncontroRestaurante u where u.encontro = :encontro ")
})
public class EncontroRestaurante extends _WebBaseEntity {

	private static final long serialVersionUID = 7692523387883187632L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTRORESTAURANTE", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@ManyToOne
	@JoinColumn(name="restaurante")
	private Restaurante restaurante;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return getRestaurante().getNome();
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroRestaurante other = (EncontroRestaurante) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Restaurante getRestaurante() {
		return restaurante;
	}
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
}