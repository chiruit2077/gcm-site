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
@SequenceGenerator(name="SQ_HOTEL", sequenceName="SQ_HOTEL")
@NamedQueries({
	@NamedQuery(name="hotel.todos", query="select u from Hotel u order by u.nome")
})
public class Hotel extends _WebBaseEntity {

	private static final long serialVersionUID = -6339949548694299433L;

	@Id
	@GeneratedValue(generator="SQ_HOTEL", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String nome;

	private Integer quantidadeQuartos;

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

	public Integer getQuantidadeQuartos() {
		return quantidadeQuartos;
	}

	public void setQuantidadeQuartos(Integer quantidadeQuartos) {
		this.quantidadeQuartos = quantidadeQuartos;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}