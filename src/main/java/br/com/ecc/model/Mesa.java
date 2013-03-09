package br.com.ecc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_MESA", sequenceName="SQ_MESA")
@NamedQueries({
	@NamedQuery(name="mesa.porRestaurante", query="select u from Mesa u where u.restaurante = :restaurante "),
	@NamedQuery(name="mesa.deletePorRestaurante",
		query="delete from Mesa u where u.restaurante = :restaurante"),
	@NamedQuery(name="mesa.deletePorRestauranteNotIn",
		query="delete from Mesa u where u.restaurante = :restaurante and u not in (:lista)")
})
public class Mesa extends _WebBaseEntity {

	private static final long serialVersionUID = 8066467472547386774L;

	@Id
	@GeneratedValue(generator="SQ_MESA", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="restaurante")
	private Restaurante restaurante;

	@ManyToOne
	@JoinColumn(name="grupo")
	private RestauranteGrupo grupo;

	private Integer quantidadeCasais;

	@Column(length=30)
	private String numero;

	private Integer coluna;

	private Integer linha;

	private Integer colunaSpam;

	private Integer linhaSpam;

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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Integer getQuantidadeCasais() {
		return quantidadeCasais;
	}

	public void setQuantidadeCasais(Integer quantidadeCasais) {
		this.quantidadeCasais = quantidadeCasais;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public Integer getColuna() {
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public Integer getColunaSpam() {
		return colunaSpam;
	}

	public void setColunaSpam(Integer colunaSpam) {
		this.colunaSpam = colunaSpam;
	}

	public Integer getLinhaSpam() {
		return linhaSpam;
	}

	public void setLinhaSpam(Integer linhaSpam) {
		this.linhaSpam = linhaSpam;
	}

	public RestauranteGrupo getGrupo() {
		return grupo;
	}

	public void setGrupo(RestauranteGrupo grupo) {
		this.grupo = grupo;
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
		if (id != null) {
			if (id.equals(other.id))
				return true;
		}if (numero != null) {
			if (numero.equals(other.numero))
				return true;
		}
		return false;
	}

}