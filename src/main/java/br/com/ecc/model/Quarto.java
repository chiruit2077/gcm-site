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

import br.com.ecc.model.tipo.TipoQuartoEnum;

@Entity
@SequenceGenerator(name="SQ_QUARTO", sequenceName="SQ_QUARTO")
@NamedQueries({
	@NamedQuery(name="quarto.porHotel", query="select u from Quarto u where u.hotel = :hotel order by u.numeroQuarto")
})
public class Quarto extends _WebBaseEntity {
	private static final long serialVersionUID = -2882307088124634306L;

	@Id
	@GeneratedValue(generator="SQ_QUARTO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="hotel")
	private Hotel hotel;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoQuartoEnum tipoQuarto;

	private Boolean suporteTV;
	private Boolean carpete;
	private Boolean arCondicionado;

	@Column(length=50)
	private String cor;

	@Column(length=30)
	private String numeroQuarto;

	@Column(length=30)
	private String numeroTelefone;

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

	public Boolean getSuporteTV() {
		return suporteTV;
	}

	public void setSuporteTV(Boolean suporteTV) {
		this.suporteTV = suporteTV;
	}

	public Boolean getCarpete() {
		return carpete;
	}

	public void setCarpete(Boolean carpete) {
		this.carpete = carpete;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
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

	public Boolean getArCondicionado() {
		return arCondicionado;
	}

	public void setArCondicionado(Boolean arCondicionado) {
		this.arCondicionado = arCondicionado;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	public TipoQuartoEnum getTipoQuarto() {
		return tipoQuarto;
	}

	public void setTipoQuarto(TipoQuartoEnum tipoQuarto) {
		this.tipoQuarto = tipoQuarto;
	}

}