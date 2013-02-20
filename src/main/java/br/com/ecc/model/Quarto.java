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

import br.com.ecc.model.tipo.TipoAgrupamentoLadoEnum;
import br.com.ecc.model.tipo.TipoQuartoEnum;

@Entity
@SequenceGenerator(name="SQ_QUARTO", sequenceName="SQ_QUARTO")
@NamedQueries({
	@NamedQuery(name="quarto.porHotel", query="select u from Quarto u where u.hotelAgrupamento.hotel = :hotel order by u.tipoQuarto, u.numeroQuarto"),
	@NamedQuery(name="quarto.porHotelAgrupamento", query="select u from Quarto u where u.hotelAgrupamento = :hotelagrupamento order by u.ordem ")
})
public class Quarto extends _WebBaseEntity {

	private static final long serialVersionUID = 3987910441454281828L;

	@Id
	@GeneratedValue(generator="SQ_QUARTO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="hotelagrupamento")
	private HotelAgrupamento hotelAgrupamento;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoQuartoEnum tipoQuarto;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoAgrupamentoLadoEnum lado;

	@Column(length=30)
	private String numeroQuarto;

	@Column(length=30)
	private String numeroTelefone;

	private Integer ordem;

	@Version
	private Integer version;

	public String getNumeroQuarto() {
		return numeroQuarto;
	}

	public void setNumeroQuarto(String numeroQuarto) {
		this.numeroQuarto = numeroQuarto;
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

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	public HotelAgrupamento getHotelAgrupamento() {
		return hotelAgrupamento;
	}

	public void setHotelAgrupamento(HotelAgrupamento hotelAgrupamento) {
		this.hotelAgrupamento = hotelAgrupamento;
	}

	public TipoQuartoEnum getTipoQuarto() {
		return tipoQuarto;
	}

	public void setTipoQuarto(TipoQuartoEnum tipoQuarto) {
		this.tipoQuarto = tipoQuarto;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public TipoAgrupamentoLadoEnum getLado() {
		return lado;
	}

	public void setLado(TipoAgrupamentoLadoEnum lado) {
		this.lado = lado;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quarto other = (Quarto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}