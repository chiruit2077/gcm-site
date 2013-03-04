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
@SequenceGenerator(name="SQ_ORGANOGRAMACOORDENACAO", sequenceName="SQ_ORGANOGRAMACOORDENACAO")
@NamedQueries({
	@NamedQuery(name="organogramaCoordenacao.porOrganograma", query="select u from OrganogramaCoordenacao u where u.organogramaArea.organograma = :organograma "),
	@NamedQuery(name="organogramaCoordenacao.porOrganogramaArea", query="select u from OrganogramaCoordenacao u where u.organogramaArea = :organogramaarea ")
})
public class OrganogramaCoordenacao extends _WebBaseEntity {

	private static final long serialVersionUID = 2959607485897246265L;

	@Id
	@GeneratedValue(generator="SQ_ORGANOGRAMACOORDENACAO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="organogramaarea")
	private OrganogramaArea organogramaArea;

	@ManyToOne
	@JoinColumn(name="papel")
	private Papel papel;

	@ManyToOne
	@JoinColumn(name="atividade")
	private Atividade atividade;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoAtividadeEnum tipoAtividade;

	@Column(length=254)
	private String descricao;

	private Integer linha;

	private Integer coluna;

	private Integer colunaSpam;

	private Integer linhaSpam;

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

	public OrganogramaArea getOrganogramaArea() {
		return organogramaArea;
	}

	public void setOrganogramaArea(OrganogramaArea organogramaArea) {
		this.organogramaArea = organogramaArea;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoAtividadeEnum getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividadeEnum tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrganogramaCoordenacao other = (OrganogramaCoordenacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

}