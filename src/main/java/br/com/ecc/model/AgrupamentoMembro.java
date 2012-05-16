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
@SequenceGenerator(name="SQ_AGRUPAMENTOMEMBRO", sequenceName="SQ_AGRUPAMENTOMEMBRO")
@NamedQueries({
	@NamedQuery(name="agrupamentoMembro.porAgrupamento", query="select u from AgrupamentoMembro u where u.agrupamento = :agrupamento"),
	@NamedQuery(name="agrupamentoMembro.deletePorAgrupamento", 
		query="delete from AgrupamentoMembro u where u.agrupamento = :agrupamento"),
	@NamedQuery(name="agrupamentoMembro.deletePorAgrupamentoNotIn", 
		query="delete from AgrupamentoMembro u where u.agrupamento = :agrupamento and u not in (:lista)")
})
public class AgrupamentoMembro extends _WebBaseEntity {
	private static final long serialVersionUID = -1867205721993408228L;

	@Id
	@GeneratedValue(generator="SQ_AGRUPAMENTOMEMBRO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="agrupamento")
	private Agrupamento agrupamento;
	
	@ManyToOne
	@JoinColumn(name="casal")
	private Casal casal;
	
	@ManyToOne
	@JoinColumn(name="pessoa")
	private Pessoa pessoa;
	
	@Version
	private Integer version;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Agrupamento getAgrupamento() {
		return agrupamento;
	}
	public void setAgrupamento(Agrupamento agrupamento) {
		this.agrupamento = agrupamento;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}