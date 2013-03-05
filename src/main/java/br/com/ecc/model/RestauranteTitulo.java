package br.com.ecc.model;

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
@SequenceGenerator(name="SQ_RESTAURANTETITULO", sequenceName="SQ_RESTAURANTETITULO")
@NamedQueries({
	@NamedQuery(name="restauranteTitulo.porRestaurante", query="select u from RestauranteTitulo u where u.restaurante = :restaurante ")
})
public class RestauranteTitulo extends _WebBaseEntity {

	private static final long serialVersionUID = 6396299988799121101L;

	@Id
	@GeneratedValue(generator="SQ_RESTAURANTETITULO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="restaurante")
	private Restaurante restaurante;

	private Integer coluna;

	private Integer linha;

	private Integer colunaSpam;

	private Integer linhaSpam;

	private String titulo;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return getTitulo();
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestauranteTitulo other = (RestauranteTitulo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}