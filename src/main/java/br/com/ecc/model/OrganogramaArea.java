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
@SequenceGenerator(name="SQ_ORGANOGRAMAAREAS", sequenceName="SQ_ORGANOGRAMAAREAS")
@NamedQueries({
	@NamedQuery(name="organogramaArea.porOrganograma", query="select u from OrganogramaArea u where u.organograma = :organograma "),
	@NamedQuery(name="organogramaArea.deletePorOrganograma",
	query="delete from OrganogramaArea u where u.organograma = :organograma"),
	@NamedQuery(name="organogramaArea.deletePorOrganogramaNotIn",
	query="delete from OrganogramaArea u where u.organograma = :organograma and u not in (:lista)")
})
public class OrganogramaArea extends _WebBaseEntity {

	private static final long serialVersionUID = -8385403255781642493L;

	@Id
	@GeneratedValue(generator="SQ_ORGANOGRAMAAREAS", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String nome;

	@Column(length=254)
	private String obs;

	@ManyToOne
	@JoinColumn(name="organograma")
	private Organograma organograma;

	@ManyToOne
	@JoinColumn(name="papel")
	private Papel papel;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoAtividadeEnum tipoAtividade;

	private Integer linha;

	private Integer coluna;

	private Integer colunaSpam;

	private Integer linhaSpam;

	private Integer linhaCoordenacao;

	private Integer colunaCoordenacao;

	private Integer colunaSpamCoordenacao;

	private Integer linhaSpamCoordenacao;


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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Organograma getOrganograma() {
		return organograma;
	}

	public void setOrganograma(Organograma organograma) {
		this.organograma = organograma;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrganogramaArea other = (OrganogramaArea) obj;
		if (id != null) {
			if (id.equals(other.id))
				return true;
		}if (nome != null) {
			if (nome.equals(other.nome))
				return true;
		}
		return false;
	}

	public TipoAtividadeEnum getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividadeEnum tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public Integer getColuna() {
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
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

	public Integer getLinhaCoordenacao() {
		return linhaCoordenacao;
	}

	public void setLinhaCoordenacao(Integer linhaCoordenacao) {
		this.linhaCoordenacao = linhaCoordenacao;
	}

	public Integer getColunaCoordenacao() {
		return colunaCoordenacao;
	}

	public void setColunaCoordenacao(Integer colunaCoordenacao) {
		this.colunaCoordenacao = colunaCoordenacao;
	}

	public Integer getColunaSpamCoordenacao() {
		return colunaSpamCoordenacao;
	}

	public void setColunaSpamCoordenacao(Integer colunaSpamCoordenacao) {
		this.colunaSpamCoordenacao = colunaSpamCoordenacao;
	}

	public Integer getLinhaSpamCoordenacao() {
		return linhaSpamCoordenacao;
	}

	public void setLinhaSpamCoordenacao(Integer linhaSpamCoordenacao) {
		this.linhaSpamCoordenacao = linhaSpamCoordenacao;
	}

}