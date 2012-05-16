package br.com.ecc.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import javax.persistence.Entity;

@Entity
@SequenceGenerator(name="SQ_SUBGRUPOPATRIMONIO", sequenceName="SQ_SUBGRUPOPATRIMONIO")
public class SubGrupoPatrimonio extends _WebBaseEntity {

	private static final long serialVersionUID = -6266687617570938982L;

	@Id
	@GeneratedValue(generator="SQ_SUBGRUPOPATRIMONIO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="grupoPatrimonio")
	private GrupoPatrimonio grupoPatrimonio;
	
	private String nome;
	
	@Version
	private Integer version;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public GrupoPatrimonio getGrupoPatrimonio() {
		return grupoPatrimonio;
	}

	public void setGrupoPatrimonio(GrupoPatrimonio grupoPatrimonio) {
		this.grupoPatrimonio = grupoPatrimonio;
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
	
}
