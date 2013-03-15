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

import br.com.ecc.model.tipo.TipoAtividadeEnum;

@Entity
@SequenceGenerator(name="SQ_RESTAURANTE", sequenceName="SQ_RESTAURANTE")
@NamedQueries({
	@NamedQuery(name="restaurante.porGrupo", query="select u from Restaurante u where u.grupo = :grupo order by u.ordem ")
})
public class Restaurante extends _WebBaseEntity {

	private static final long serialVersionUID = 2236706560351316526L;

	@Id
	@GeneratedValue(generator="SQ_RESTAURANTE", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String nome;

	@ManyToOne
	@JoinColumn(name="hotel")
	private Hotel hotel;

	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;

	private Integer quantidadeMesas;

	private Integer quantidadeCasaisPorMesa;

	@ManyToOne
	@JoinColumn(name="atividade")
	private Atividade atividade;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoAtividadeEnum tipoAtividade;

	private Boolean checkMesa;

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaurante other = (Restaurante) obj;
		if (id != null) {
			if (id.equals(other.id))
				return true;
		}if (nome != null) {
			if (nome.equals(other.nome))
				return true;
		}
		return false;
	}

	public Integer getQuantidadeMesas() {
		return quantidadeMesas;
	}

	public void setQuantidadeMesas(Integer quantidadeMesas) {
		this.quantidadeMesas = quantidadeMesas;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public TipoAtividadeEnum getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividadeEnum tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
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

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Boolean isCheckMesa() {
		return checkMesa;
	}

	public void setCheckMesa(Boolean checaMesa) {
		this.checkMesa = checaMesa;
	}

	public Integer getQuantidadeCasaisPorMesa() {
		return quantidadeCasaisPorMesa;
	}

	public void setQuantidadeCasaisPorMesa(Integer quantidadeCasaisPorMesa) {
		this.quantidadeCasaisPorMesa = quantidadeCasaisPorMesa;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

}