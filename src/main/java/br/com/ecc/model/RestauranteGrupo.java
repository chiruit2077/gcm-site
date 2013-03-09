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
@SequenceGenerator(name="SQ_RESTAURANTEGRUPO", sequenceName="SQ_RESTAURANTEGRUPO")
@NamedQueries({
	@NamedQuery(name="restauranteGrupo.porRestaurante", query="select u from RestauranteGrupo u where u.restaurante = :restaurante "),
	@NamedQuery(name="restauranteGrupo.deletePorRestaurante",
		query="delete from RestauranteGrupo u where u.restaurante = :restaurante"),
	@NamedQuery(name="restauranteGrupo.deletePorRestauranteNotIn",
		query="delete from RestauranteGrupo u where u.restaurante = :restaurante and u not in (:lista)")
})
public class RestauranteGrupo extends _WebBaseEntity {

	private static final long serialVersionUID = 3149605858399670740L;

	@Id
	@GeneratedValue(generator="SQ_RESTAURANTEGRUPO", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=100)
	private String nome;

	@ManyToOne
	@JoinColumn(name="restaurante")
	private Restaurante restaurante;

	@Version
	private Integer version;

	public String toString() {
		return nome;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestauranteGrupo other = (RestauranteGrupo) obj;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}