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

import br.com.ecc.model.tipo.TipoItemPatrimonioEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;

@Entity
@SequenceGenerator(name="SQ_ITEMPATRIMONIO", sequenceName="SQ_ITEMPATRIMONIO")
@NamedQueries({
	@NamedQuery(name="itemPatrimonio.porGrupo", query="select i from ItemPatrimonio i where i.grupo = :grupo"),
	@NamedQuery(name="itemPatrimonio.porItemPatrimonio", query="select i from ItemPatrimonio i where i.pai = :itemPatrimonio")
})
public class ItemPatrimonio extends _WebBaseEntity {

	private static final long serialVersionUID = -7308443596578333125L;

	@Id
	@GeneratedValue(generator="SQ_ITEMPATRIMONIO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;
	
	@ManyToOne
	@JoinColumn(name="pai")
	private ItemPatrimonio pai;
	
	private String nome;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoSituacaoEnum situacao;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoItemPatrimonioEnum tipo;
	
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

	public ItemPatrimonio getPai() {
		return pai;
	}

	public void setPai(ItemPatrimonio pai) {
		this.pai = pai;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoSituacaoEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(TipoSituacaoEnum situacao) {
		this.situacao = situacao;
	}

	public TipoItemPatrimonioEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoItemPatrimonioEnum tipo) {
		this.tipo = tipo;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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
		ItemPatrimonio other = (ItemPatrimonio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
