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
@SequenceGenerator(name="SQ_ENCONTROTOTALIZACAO", sequenceName="SQ_ENCONTROTOTALIZACAO")
@NamedQueries({
	@NamedQuery(name="encontroTotalizacao.porEncontro", query="select u from EncontroTotalizacao u where u.encontro = :encontro order by u.nome"),
	@NamedQuery(name="encontroTotalizacao.deletePorEncontro", query="delete from EncontroTotalizacao u where u.encontro = :encontro"),
	@NamedQuery(name="encontroTotalizacao.deletePorEncontroNotIn", query="delete from EncontroTotalizacao u where u.encontro = :encontro and u not in (:lista)")
})
public class EncontroTotalizacao extends _WebBaseEntity {
	private static final long serialVersionUID = 7527922336857012761L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROTOTALIZACAO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;
	
	@Column(length=250)
	private String nome;
	
	@Version
	private Integer version;
	
	@Override
	public String toString() {
		if(getNome()!=null){
			return getNome();
		}
		return super.toString();
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
}