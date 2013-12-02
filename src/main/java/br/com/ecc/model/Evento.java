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
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_EVENTO", sequenceName="SQ_EVENTO")
@NamedQueries({
	@NamedQuery(name="evento.porGrupo", query="select u from Evento u where u.grupo = :grupo order by u.data desc")
})
public class Evento extends _WebBaseEntity {
	private static final long serialVersionUID = 693755147008235053L;

	@Id
	@GeneratedValue(generator="SQ_EVENTO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;
	
	private Date data;

	@Column(length=254)
	private String descricao;
	
	@Version
	private Integer version;

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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
}