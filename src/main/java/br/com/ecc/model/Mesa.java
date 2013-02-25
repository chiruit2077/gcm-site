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

import br.com.ecc.model.tipo.TipoMesaLadoEnum;

@Entity
@SequenceGenerator(name="SQ_MESA", sequenceName="SQ_MESA")
@NamedQueries({
	@NamedQuery(name="mesa.porHotel", query="select u from Mesa u where u.hotel = :hotel order by u.lado, u.ordem ")
})
public class Mesa extends _WebBaseEntity {

	private static final long serialVersionUID = 8066467472547386774L;

	@Id
	@GeneratedValue(generator="SQ_MESA", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="hotel")
	private Hotel hotel;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoMesaLadoEnum lado;

	@Column(length=30)
	private String numero;

	private Integer ordem;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return getNumero();
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

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public TipoMesaLadoEnum getLado() {
		return lado;
	}

	public void setLado(TipoMesaLadoEnum lado) {
		this.lado = lado;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mesa other = (Mesa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}