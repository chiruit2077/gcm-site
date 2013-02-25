package br.com.ecc.model;

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
import javax.persistence.Version;

import br.com.ecc.model.tipo.TipoRestauranteEnum;

@Entity
@SequenceGenerator(name="SQ_ENCONTRORESTAURANTEGARCON", sequenceName="SQ_ENCONTRORESTAURANTEGARCON")
@NamedQueries(
		@NamedQuery(name="encontroRestauranteGarcon.porEncontroHotel", query="select u from EncontroRestauranteGarcon u " +
				" where u.encontroHotel = :encontrohotel " +
				" order by u.tipo, u.mesa.lado, u.mesa.ordem ")
)

public class EncontroRestauranteGarcon extends _WebBaseEntity {

	private static final long serialVersionUID = 8564481583369974061L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTRORESTAURANTEGARCON", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontrohotel")
	private EncontroHotel encontroHotel;

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

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoRestauranteEnum tipo;

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
	public TipoRestauranteEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoRestauranteEnum tipo) {
		this.tipo = tipo;
	}
	public Mesa getMesa() {
		return mesa;
	}
	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}
	public EncontroHotel getEncontroHotel() {
		return encontroHotel;
	}
	public void setEncontroHotel(EncontroHotel encontroHotel) {
		this.encontroHotel = encontroHotel;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroRestauranteGarcon other = (EncontroRestauranteGarcon) obj;
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

}