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
@SequenceGenerator(name="SQ_ENCONTRORESTAURANTEMESA", sequenceName="SQ_ENCONTRORESTAURANTEMESA")
@NamedQueries({
		@NamedQuery(name="encontroRestauranteMesa.porEncontroRestaurante", query="select u from EncontroRestauranteMesa u " +
					" where u.encontroRestaurante = :encontrorestaurante "),
		@NamedQuery(name="encontroRestauranteMesa.porEncontroRestauranteOutro", query="select u from EncontroRestauranteMesa u " +
						" where u.encontroRestaurante != :encontrorestaurante and u.encontroRestaurante.encontro = :encontro "),
		@NamedQuery(name="encontroRestauranteMesa.updatePorEncontroGarcon",
			query="update EncontroRestauranteMesa u set u.encontroGarcon = null " +
		 			"where u.encontroGarcon = :encontroInscricao " ),
		@NamedQuery(name="encontroRestauranteMesa.updatePorEncontroAfilhado1",
			query="update EncontroRestauranteMesa u set u.encontroAfilhado1 = null " +
		 			"where u.encontroAfilhado1 = :encontroInscricao " ),
		@NamedQuery(name="encontroRestauranteMesa.updatePorEncontroAfilhado2",
			query="update EncontroRestauranteMesa u set u.encontroAfilhado2 = null " +
		 			"where u.encontroAfilhado2 = :encontroInscricao " )
})

public class EncontroRestauranteMesa extends _WebBaseEntity {

	private static final long serialVersionUID = 8564481583369974061L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTRORESTAURANTEMESA", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontroRestaurante")
	private EncontroRestaurante encontroRestaurante;

	@ManyToOne
	@JoinColumn(name="mesa")
	private Mesa mesa;

	@ManyToOne
	@JoinColumn(name="encontroAfilhado1")
	private EncontroInscricao encontroAfilhado1;

	@ManyToOne
	@JoinColumn(name="encontroAfilhado2")
	private EncontroInscricao encontroAfilhado2;

	@ManyToOne
	@JoinColumn(name="encontroGarcon")
	private EncontroInscricao encontroGarcon;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return mesa.toString();
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
	public EncontroInscricao getEncontroAfilhado1() {
		return encontroAfilhado1;
	}
	public void setEncontroAfilhado1(EncontroInscricao encontroAfilhado1) {
		this.encontroAfilhado1 = encontroAfilhado1;
	}
	public EncontroInscricao getEncontroAfilhado2() {
		return encontroAfilhado2;
	}
	public void setEncontroAfilhado2(EncontroInscricao encontroAfilhado2) {
		this.encontroAfilhado2 = encontroAfilhado2;
	}
	public EncontroInscricao getEncontroGarcon() {
		return encontroGarcon;
	}
	public void setEncontroGarcon(EncontroInscricao encontroGarcon) {
		this.encontroGarcon = encontroGarcon;
	}
	public Mesa getMesa() {
		return mesa;
	}
	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroRestauranteMesa other = (EncontroRestauranteMesa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mesa == null) {
			if (other.mesa != null)
				return false;
		} else if (!mesa.equals(other.mesa))
			return false;
		return true;
	}
	public EncontroRestaurante getEncontroRestaurante() {
		return encontroRestaurante;
	}
	public void setEncontroRestaurante(EncontroRestaurante encontroRestaurante) {
		this.encontroRestaurante = encontroRestaurante;
	}

}