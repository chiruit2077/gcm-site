package br.com.ecc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_HOTELAGRUPAMENTO", sequenceName="SQ_HOTELAGRUPAMENTO")
@NamedQueries({
	@NamedQuery(name="hotelAgrupamento.porHotel", query="select u from HotelAgrupamento u where u.hotel = :hotel order by u.ordem ")
})
public class HotelAgrupamento extends _WebBaseEntity {

	private static final long serialVersionUID = -7874530870859772242L;

	@Id
	@GeneratedValue(generator="SQ_HOTELAGRUPAMENTO", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String nome;

	@Column(length=254)
	private String obs;

	private Integer quantidadeLados;

	private Integer ordem;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return nome;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getQuantidadeLados() {
		return quantidadeLados;
	}

	public void setQuantidadeLados(Integer quantidadeLados) {
		this.quantidadeLados = quantidadeLados;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

}