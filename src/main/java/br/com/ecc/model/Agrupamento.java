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
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;

@Entity
@SequenceGenerator(name="SQ_AGRUPAMENTO", sequenceName="SQ_AGRUPAMENTO")
@NamedQueries({
	@NamedQuery(name="agrupamento.porGrupo", query="select u from Agrupamento u where u.grupo = :grupo order by u.nome"),
	@NamedQuery(name="agrupamento.porEncontro", query="select u from Agrupamento u where u.encontro = :encontro order by u.tipo, u.nome"),
	@NamedQuery(name="agrupamento.deletePorEncontro", query="delete from Agrupamento u where u.encontro = :encontro")
})
public class Agrupamento extends _WebBaseEntity {
	private static final long serialVersionUID = -5803383402599264292L;

	@Id
	@GeneratedValue(generator="SQ_AGRUPAMENTO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoInscricaoCasalEnum tipo;

	@Column(length=250)
	private String nome;

	@ManyToOne
	@JoinColumn(name="atividade")
	private Atividade atividade;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoAtividadeEnum tipoAtividade;

	@Version
	private Integer version;

	@Override
	public String toString() {
		if(getNome()!=null){
			return getNome() + " - " + getTipo().getNome();
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Encontro getEncontro() {
		return encontro;
	}

	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}

	public TipoInscricaoCasalEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoInscricaoCasalEnum tipo) {
		this.tipo = tipo;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agrupamento other = (Agrupamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}