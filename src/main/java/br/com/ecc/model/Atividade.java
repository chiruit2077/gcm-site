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
@SequenceGenerator(name="SQ_ATIVIDADE", sequenceName="SQ_ATIVIDADE")
@NamedQueries({
	@NamedQuery(name="atividade.porGrupo", query="select u from Atividade u where u.grupo = :grupo order by u.nome")
})
public class Atividade extends _WebBaseEntity {
	private static final long serialVersionUID = 8016064080696278205L;

	@Id
	@GeneratedValue(generator="SQ_ATIVIDADE", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;
	
	@Column(length=250)
	private String nome;
	
	@Version
	private Integer version;
	
	@Override
	public String toString() {
		return getNome();
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
}