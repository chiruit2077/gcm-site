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
@SequenceGenerator(name="SQ_QUARTO", sequenceName="SQ_QUARTO")
@NamedQueries({
	@NamedQuery(name="quarto.porGrupo", query="select u from Quarto u where u.grupo = :grupo order by u.numeroQuarto")
})
public class Quarto extends _WebBaseEntity {
	private static final long serialVersionUID = -2882307088124634306L;

	@Id
	@GeneratedValue(generator="SQ_QUARTO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;
	
	private Boolean suporteTV;
	private Boolean carpete;
	
	@Column(length=10)
	private String cor;
	
	@Column(length=10)
	private String numeroQuarto;

	@Version
	private Integer version;
	
	public String getNumeroQuarto() {
		return numeroQuarto;
	}

	public void setNumeroQuarto(String numeroQuarto) {
		this.numeroQuarto = numeroQuarto;
	}
	
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

	public Boolean getSuporteTV() {
		return suporteTV;
	}

	public void setSuporteTV(Boolean suporteTV) {
		this.suporteTV = suporteTV;
	}

	public Boolean getCarpete() {
		return carpete;
	}

	public void setCarpete(Boolean carpete) {
		this.carpete = carpete;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	
	
}