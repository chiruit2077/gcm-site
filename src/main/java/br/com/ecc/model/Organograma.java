package br.com.ecc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="SQ_ORGANOGRAMA", sequenceName="SQ_ORGANOGRAMA")
@NamedQueries({
	@NamedQuery(name="organograma.todos", query="select u from Organograma u order by u.nome")
})
public class Organograma extends _WebBaseEntity {

	private static final long serialVersionUID = 8109231124024487169L;

	@Id
	@GeneratedValue(generator="SQ_ORGANOGRAMA", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String nome;

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
		Organograma other = (Organograma) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}