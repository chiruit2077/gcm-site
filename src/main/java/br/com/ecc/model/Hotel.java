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

import br.com.ecc.model.tipo.TipoHotelDistribuicaoEnum;
import br.com.ecc.model.tipo.TipoHotelEnum;

@Entity
@SequenceGenerator(name="SQ_HOTEL", sequenceName="SQ_HOTEL")
@NamedQueries({
	@NamedQuery(name="hotel.todos", query="select u from Hotel u order by u.tipo"),
	@NamedQuery(name="hotel.porGrupo", query="select u from Hotel u where u.grupo =:grupo order by u.tipo")
})
public class Hotel extends _WebBaseEntity {

	private static final long serialVersionUID = -6339949548694299433L;

	@Id
	@GeneratedValue(generator="SQ_HOTEL", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;

	@Column(length=254)
	private String nome;

	private Integer quantidadeQuartos;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoHotelEnum tipo;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoHotelDistribuicaoEnum distribuicaoHotel;

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

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQuantidadeQuartos() {
		return quantidadeQuartos;
	}

	public void setQuantidadeQuartos(Integer quantidadeQuartos) {
		this.quantidadeQuartos = quantidadeQuartos;
	}

	public TipoHotelDistribuicaoEnum getDistribuicaoHotel() {
		return distribuicaoHotel;
	}

	public void setDistribuicaoHotel(TipoHotelDistribuicaoEnum distribuicaoHotel) {
		this.distribuicaoHotel = distribuicaoHotel;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public TipoHotelEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoHotelEnum tipo) {
		this.tipo = tipo;
	}

}