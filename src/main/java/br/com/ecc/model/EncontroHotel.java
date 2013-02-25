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

import br.com.ecc.model.tipo.TipoEncontroHotelEnum;

@Entity
@SequenceGenerator(name="SQ_ENCONTROHOTEL", sequenceName="SQ_ENCONTROHOTEL")
@NamedQueries({
	@NamedQuery(name="encontroHotel.porEncontro", query="select u from EncontroHotel u where u.encontro = :encontro order by u.tipo"),
	@NamedQuery(name="encontroHotel.porEncontroEvento", query="select u from EncontroHotel u where u.encontro = :encontro and u.tipo = 'EVENTO' ")
})
public class EncontroHotel extends _WebBaseEntity {

	private static final long serialVersionUID = 631487464767102278L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROHOTEL", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@ManyToOne
	@JoinColumn(name="hotel")
	private Hotel hotel;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoEncontroHotelEnum tipo;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return hotel.getNome() + " - " + tipo.getNome();
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
	public Hotel getHotel() {
		return hotel;
	}
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	public TipoEncontroHotelEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoEncontroHotelEnum tipo) {
		this.tipo = tipo;
	}
}