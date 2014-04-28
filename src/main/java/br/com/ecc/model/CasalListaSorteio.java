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

import br.com.ecc.model.tipo.TipoInscricaoEnum;

@Entity
@SequenceGenerator(name="SQ_CASALLISTASORTEIO", sequenceName="SQ_CASALLISTASORTEIO")
@NamedQueries({
	@NamedQuery(name="casalListaSorteio.porEncontro", 
			query="select u from CasalListaSorteio u where u.encontro = :encontro")
})
public class CasalListaSorteio extends _WebBaseEntity {
	private static final long serialVersionUID = 2469249457351478402L;

	@Id
	@GeneratedValue(generator="SQ_CASALLISTASORTEIO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@ManyToOne
	@JoinColumn(name="casal")
	private Casal casal;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoInscricaoEnum tipo;
	
	@Version
	private Integer version;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public TipoInscricaoEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoInscricaoEnum tipo) {
		this.tipo = tipo;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}