package br.com.ecc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_ENCONTROHOTELQUARTO", sequenceName="SQ_ENCONTROHOTELQUARTO")
public class EncontroHotelQuarto extends _WebBaseEntity {

	private static final long serialVersionUID = -8313244441055727258L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROHOTELQUARTO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontroHotel")
	private EncontroHotel encontroHotel;

	@ManyToOne
	@JoinColumn(name="encontroInscricao")
	private EncontroInscricao encontroInscricao;

	@ManyToOne
	@JoinColumn(name="quarto")
	private Quarto quarto;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return quarto.getNumeroQuarto();
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
	public EncontroHotel getEncontroHotel() {
		return encontroHotel;
	}
	public void setEncontroHotel(EncontroHotel encontroHotel) {
		this.encontroHotel = encontroHotel;
	}
	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}
	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
	public Quarto getQuarto() {
		return quarto;
	}
	public void setQuarto(Quarto quarto) {
		this.quarto = quarto;
	}

}