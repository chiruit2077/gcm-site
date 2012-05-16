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
@SequenceGenerator(name="SQ_ENCONTROCONVITERESPONSAVEL", sequenceName="SQ_ENCONTROCONVITERESPONSAVEL")
@NamedQueries({
	@NamedQuery(name="encontroConviteResponsavel.porEncontro", 
			query="select u from EncontroConviteResponsavel u where u.encontro = :encontro"),
	@NamedQuery(name="encontroConviteResponsavel.deletePorEncontro", query="delete from EncontroConviteResponsavel u where u.encontro = :encontro"),
	@NamedQuery(name="encontroConviteResponsavel.deletePorEncontroNotIn", query="delete from EncontroConviteResponsavel u where u.encontro = :encontro and u not in (:lista)")
})
public class EncontroConviteResponsavel extends _WebBaseEntity {
	private static final long serialVersionUID = -2511465932463644262L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROCONVITERESPONSAVEL", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;
	
	@ManyToOne
	@JoinColumn(name="casal")
	private Casal casal;
	
	@Version
	private Integer version;
	
	@Override
	public String toString() {
		if(casal!=null){
			return casal.toString();
		}
		return super.toString();
	}

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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
}