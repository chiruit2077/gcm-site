package br.com.ecc.model;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_ENCONTROPERIODO", sequenceName="SQ_ENCONTROPERIODO")
@NamedQueries({
	@NamedQuery(name="encontroPeriodo.porEncontro", query="select u from EncontroPeriodo u where u.encontro = :encontro order by u.inicio"),
	@NamedQuery(name="encontroPeriodo.deletePorEncontro", query="delete from EncontroPeriodo u where u.encontro = :encontro"),
	@NamedQuery(name="encontroPeriodo.deletePorEncontroNotIn", query="delete from EncontroPeriodo u where u.encontro = :encontro and u not in (:lista)")
})
public class EncontroPeriodo extends _WebBaseEntity {
	private static final long serialVersionUID = 5803843606955440074L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROPERIODO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date inicio;
	
	@Column(length=100)
	private String nome;

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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
}