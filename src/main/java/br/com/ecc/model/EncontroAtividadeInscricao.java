package br.com.ecc.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_ENCONTROATIVIDADEINSCRICAO", sequenceName="SQ_ENCONTROATIVIDADEINSCRICAO")
@NamedQueries({
	@NamedQuery(name="encontroAtividadeInscricao.porEncontro",
		query="select u from EncontroAtividadeInscricao u where u.encontroInscricao.encontro = :encontro order by u.encontroAtividade.inicio"),
	@NamedQuery(name="encontroAtividadeInscricao.porEncontroInscricao",
		query="select u from EncontroAtividadeInscricao u where u.encontroInscricao = :encontroInscricao"),
	@NamedQuery(name="encontroAtividadeInscricao.porEncontroCasal",
		query="select u from EncontroAtividadeInscricao u " +
				"where u.encontroInscricao.encontro = :encontro and " +
				"	   u.encontroInscricao.casal = :casal " +
				"order by u.encontroAtividade.inicio"),
	@NamedQuery(name="encontroAtividadeInscricao.porAtividadeInscricao",
		query="select u from EncontroAtividadeInscricao u where u.encontroInscricao = :encontroInscricao and u.encontroAtividade = :encontroAtividade"),
	@NamedQuery(name="encontroAtividadeInscricao.deletePorEncontroAtividadeNotIn",
				query="delete from EncontroAtividadeInscricao u " +
					  "where u.encontroAtividade = :encontroAtividade and " +
					  "      u not in (:lista)"),
	@NamedQuery(name="encontroAtividadeInscricao.deletePorEncontroAtividade",
				query="delete from EncontroAtividadeInscricao u " +
						"where u.encontroAtividade = :encontroAtividade " ),
	@NamedQuery(name="encontroAtividadeInscricao.deletePorEncontroInscricaoNotIn",
				query="delete from EncontroAtividadeInscricao u " +
					  "where u.encontroInscricao = :encontroInscricao and " +
					  "      u not in (:lista)"),
	@NamedQuery(name="encontroAtividadeInscricao.deletePorEncontroInscricao",
				query="delete from EncontroAtividadeInscricao u " +
						"where u.encontroInscricao = :encontroInscricao " ),
	@NamedQuery(name="encontroAtividadeInscricao.deletePorEncontro",
				query="delete from EncontroAtividadeInscricao u " +
						"where u.encontroAtividade in ( select a from EncontroAtividade a where a.encontro = :encontro ) " ),
	@NamedQuery(name="encontroAtividadeInscricao.deletePorEncontroNotIn",
				query="delete from EncontroAtividadeInscricao u " +
					  "where u not in (:lista) and u.encontroAtividade in ( select a from EncontroAtividade a where a.encontro = :encontro ) " )
})
public class EncontroAtividadeInscricao extends _WebBaseEntity {


	private static final long serialVersionUID = 7806195140391414413L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROATIVIDADEINSCRICAO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontroAtividade")
	private EncontroAtividade encontroAtividade;

	@ManyToOne
	@JoinColumn(name="encontroInscricao")
	private EncontroInscricao encontroInscricao;

	@ManyToOne
	@JoinColumn(name="papel")
	private Papel papel;

	private Boolean revisado;

	@Transient
	private List<String> infoErro;

	@Version
	private Integer version;

	public EncontroAtividadeInscricao() {
		setInfoErro(new ArrayList<String>());
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
	public EncontroAtividade getEncontroAtividade() {
		return encontroAtividade;
	}
	public void setEncontroAtividade(EncontroAtividade encontroAtividade) {
		this.encontroAtividade = encontroAtividade;
	}
	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}
	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
	public Papel getPapel() {
		return papel;
	}
	public void setPapel(Papel papel) {
		this.papel = papel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroAtividadeInscricao other = (EncontroAtividadeInscricao) obj;
		if (id != null) {
			if (id.equals(other.id))
				return true;
		}
		if (getEncontroAtividade() != null && getEncontroAtividade().equals(other.getEncontroAtividade()) &&
				getEncontroInscricao() != null && getEncontroInscricao().equals(other.getEncontroInscricao()) )
			return true;
		return false;
	}

	public List<String> getInfoErro() {
		return infoErro;
	}

	public void setInfoErro(List<String> infoErro) {
		this.infoErro = infoErro;
	}

	public Boolean getRevisado() {
		if (revisado==null) return false;
		return revisado;
	}

	public void setRevisado(Boolean revisado) {
		this.revisado = revisado;
	}
}