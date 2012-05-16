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
@SequenceGenerator(name="SQ_ENCONTROTOTALIZACAOINSCRICAO", sequenceName="SQ_ENCONTROTOTALIZACAOINSCRICAO")
@NamedQueries({
	@NamedQuery(name="encontroTotalizacaoAtividade.porEncontroTotalizacao", query="select u from EncontroTotalizacaoAtividade u where u.encontroTotalizacao = :encontroTotalizacao"),
	@NamedQuery(name="encontroTotalizacaoAtividade.deletePorEncontroTotalizacao", 
		query="delete from EncontroTotalizacaoAtividade u where u.encontroTotalizacao = :encontroTotalizacao"),
	@NamedQuery(name="encontroTotalizacaoAtividade.deletePorEncontroTotalizacaoNotIn", 
		query="delete from EncontroTotalizacaoAtividade u where u.encontroTotalizacao = :encontroTotalizacao and u not in (:lista)"),
	
	@NamedQuery(name="encontroTotalizacaoAtividade.deletePorEncontro", 
		query="delete from EncontroTotalizacaoAtividade u " +
			  "where u.encontroTotalizacao in (Select o from EncontroTotalizacao o where o.encontro = :encontro) "),
	@NamedQuery(name="encontroTotalizacaoAtividade.deletePorEncontroNotIn", 
		query="delete from EncontroTotalizacaoAtividade u " +
			  "where  u.encontroTotalizacao in (Select o from EncontroTotalizacao o where o.encontro = :encontro) and " +
			  "       u.encontroTotalizacao not in (:lista)")
})
public class EncontroTotalizacaoAtividade extends _WebBaseEntity {
	private static final long serialVersionUID = -632475200060308377L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROTOTALIZACAOINSCRICAO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="encontroTotalizacao")
	private EncontroTotalizacao encontroTotalizacao;
	
	@ManyToOne
	@JoinColumn(name="atividade")
	private Atividade atividade;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoAtividadeEnum tipoAtividade;
	
	@Version
	private Integer version;

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
	public EncontroTotalizacao getEncontroTotalizacao() {
		return encontroTotalizacao;
	}
	public void setEncontroTotalizacao(EncontroTotalizacao encontroTotalizacao) {
		this.encontroTotalizacao = encontroTotalizacao;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroTotalizacaoAtividade other = (EncontroTotalizacaoAtividade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}