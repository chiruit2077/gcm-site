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

import br.com.ecc.model.tipo.TipoEncontroQuartoEnum;

@Entity
@SequenceGenerator(name="SQ_ENCONTROHOTELQUARTO", sequenceName="SQ_ENCONTROHOTELQUARTO")
@NamedQueries({
		@NamedQuery(name="encontroHotelQuarto.porEncontroHotel", query="select u from EncontroHotelQuarto u where u.encontroHotel = :encontrohotel order by u.quarto.ordem "),
		@NamedQuery(name="encontroHotelQuarto.porListaInscricao", query="select u from EncontroHotelQuarto u where u.encontroInscricao1 in (:encontroinscricao1) order by u.quarto.ordem "),
		@NamedQuery(name="encontroHotelQuarto.porEncontroHotelListaInscricao", query="select u from EncontroHotelQuarto u where u.encontroInscricao1 in (:encontroinscricao1) order by u.quarto.ordem "),
		@NamedQuery(name="encontroHotelQuarto.porEncontroHotelInscricao",
			query="select u from EncontroHotelQuarto u where u.encontroHotel.encontro = :encontro and encontroInscricao1 = :encontroinscricao1 "),
		@NamedQuery(name="encontroHotelQuarto.updatePorEncontroInscricao1",
			query="update EncontroHotelQuarto u set u.encontroInscricao1 = null " +
					"where u.encontroInscricao1 = :encontroInscricao " ),
		@NamedQuery(name="encontroHotelQuarto.updatePorEncontroInscricao2",
			query="update EncontroHotelQuarto u set u.encontroInscricao2 = null " +
					"where u.encontroInscricao2 = :encontroInscricao " ),
		@NamedQuery(name="encontroHotelQuarto.updatePorEncontroInscricao3",
			query="update EncontroHotelQuarto u set u.encontroInscricao3 = null " +
					"where u.encontroInscricao3 = :encontroInscricao " ),
		@NamedQuery(name="encontroHotelQuarto.updatePorEncontroInscricao4",
			query="update EncontroHotelQuarto u set u.encontroInscricao4 = null " +
				"where u.encontroInscricao4 = :encontroInscricao " ),
		@NamedQuery(name="encontroHotelQuarto.deletePorEncontro",
		    query="delete from EncontroHotelQuarto u where u.encontroHotel in ( select a from EncontroHotel a where a.encontro = :encontro ) "),

})

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
	private EncontroInscricao encontroInscricao1;

	@ManyToOne
	@JoinColumn(name="encontroInscricao2")
	private EncontroInscricao encontroInscricao2;

	@ManyToOne
	@JoinColumn(name="encontroInscricao3")
	private EncontroInscricao encontroInscricao3;

	@ManyToOne
	@JoinColumn(name="encontroInscricao4")
	private EncontroInscricao encontroInscricao4;

	@ManyToOne
	@JoinColumn(name="quarto")
	private Quarto quarto;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoEncontroQuartoEnum tipo;

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
	public Quarto getQuarto() {
		return quarto;
	}
	public void setQuarto(Quarto quarto) {
		this.quarto = quarto;
	}
	public TipoEncontroQuartoEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoEncontroQuartoEnum tipo) {
		this.tipo = tipo;
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
	public EncontroInscricao getEncontroInscricao3() {
		return encontroInscricao3;
	}
	public void setEncontroInscricao3(EncontroInscricao encontroInscricao3) {
		this.encontroInscricao3 = encontroInscricao3;
	}
	public EncontroInscricao getEncontroInscricao4() {
		return encontroInscricao4;
	}
	public void setEncontroInscricao4(EncontroInscricao encontroInscricao4) {
		this.encontroInscricao4 = encontroInscricao4;
	}
	public EncontroHotel getEncontroHotel() {
		return encontroHotel;
	}
	public void setEncontroHotel(EncontroHotel encontroHotel) {
		this.encontroHotel = encontroHotel;
	}

}