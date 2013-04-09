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

import br.com.ecc.model.tipo.TipoCasalContatoEnum;

@Entity
@SequenceGenerator(name="SQ_CASALCONTATO", sequenceName="SQ_CASALCONTATO")
@NamedQueries({
	@NamedQuery(name="casalContato.porCasal", query="select u from CasalContato u where u.casal = :casal"),
	@NamedQuery(name="casalContato.porListaCasal", query="select u from CasalContato u where u.casal in (:casal) " +
			" and ( telefoneResidencial is not null or telefoneComercial is not null or telefoneCelular is not null ) "),
	@NamedQuery(name="casalContato.deletePorCasal",
		query="delete from CasalContato u where u.casal = :casal"),
	@NamedQuery(name="casalContato.deletePorCasalNotIn",
		query="delete from CasalContato u where u.casal = :casal and u not in (:lista)")
})
public class CasalContato extends _WebBaseEntity {
	private static final long serialVersionUID = -3541587918045154543L;

	@Id
	@GeneratedValue(generator="SQ_CASALCONTATO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="casal")
	private Casal casal;

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoCasalContatoEnum tipoContato;

	@Column(length=150)
	private String nome;

	private Integer idade;

	@Column(length=50)
	private String telefoneResidencial;

	@Column(length=50)
	private String telefoneComercial;

	@Column(length=50)
	private String telefoneCelular;

	@Column(length=150)
	private String email;

	@Version
	private Integer version;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public TipoCasalContatoEnum getTipoContato() {
		return tipoContato;
	}
	public void setTipoContato(TipoCasalContatoEnum tipoContato) {
		this.tipoContato = tipoContato;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getIdade() {
		return idade;
	}
	public void setIdade(Integer idade) {
		this.idade = idade;
	}
	public String getTelefoneComercial() {
		return telefoneComercial;
	}
	public void setTelefoneComercial(String telefoneComercial) {
		this.telefoneComercial = telefoneComercial;
	}
	public String getTelefoneCelular() {
		return telefoneCelular;
	}
	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getTelefoneResidencial() {
		return telefoneResidencial;
	}
	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}
}