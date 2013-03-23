package br.com.ecc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_PESSOA", sequenceName="SQ_PESSOA")
@NamedQueries({
	@NamedQuery(name="pessoa.porNomeLike", query="select u from Pessoa u where upper(u.nome) like upper(:key)"),
	@NamedQuery(name="pessoa.semUsuario",
		query="select p from Pessoa p " +
			  "where p.email is not null and " +
			  "      p.email != '' and " +
			  "      p not in (Select u.pessoa from Usuario u)"),
	@NamedQuery(name="pessoa.todos", query="select u from Pessoa u order by u.nome")
})
public class Pessoa extends _WebBaseEntity {
	private static final long serialVersionUID = -4244047722091540572L;

	@Id
	@GeneratedValue(generator="SQ_PESSOA", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=150)
	private String nome;

	@Column(length=50)
	private String apelido;

	@Temporal(TemporalType.DATE)
	private Date nascimento;

	@Column(length=150)
	private String email;

	@Column(length=100)
	private String telefoneCelular;

	@Column(length=50)
	private String telefoneComercial;

	@Column(length=50)
	private String telefone;

	@Column(length=30)
	private String rg;

	@Column(length=30)
	private String expedidor;

	@Column(length=30)
	private String cpf;

	@Column(length=50)
	private String naturalidade;

	@Column(length=50)
	private String profissao;

	private Boolean alergico;
	@Column(length=150)
	private String alergia;

	private Boolean vegetariano;
	private Boolean diabetico;
	private Boolean hipertenso;

	@Column(length=250)
	private String necessidadesEspeciais;

	@Temporal(TemporalType.DATE)
	private Date dataAtualizacao;

	@Transient
	private String tag;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return getNome();
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public String getApelido() {
		if (apelido!=null && !apelido.equals(""))
			return apelido;
		else{
			String[] split = nome.trim().split(" ");
			if (split.length>0){
				return split[0];
			}
		}
		return apelido;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRg() {
		return rg;
	}
	public void setRg(String rg) {
		this.rg = rg;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Date getNascimento() {
		return nascimento;
	}
	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}
	public Boolean getVegetariano() {
		return vegetariano;
	}
	public void setVegetariano(Boolean vegetariano) {
		this.vegetariano = vegetariano;
	}
	public Boolean getAlergico() {
		return alergico;
	}
	public void setAlergico(Boolean alergico) {
		this.alergico = alergico;
	}
	public Boolean getDiabetico() {
		return diabetico;
	}
	public void setDiabetico(Boolean diabetico) {
		this.diabetico = diabetico;
	}
	public Boolean getHipertenso() {
		return hipertenso;
	}
	public void setHipertenso(Boolean hipertenso) {
		this.hipertenso = hipertenso;
	}
	public String getNecessidadesEspeciais() {
		return necessidadesEspeciais;
	}
	public void setNecessidadesEspeciais(String necessidadesEspeciais) {
		this.necessidadesEspeciais = necessidadesEspeciais;
	}
	public String getTelefoneComercial() {
		return telefoneComercial;
	}
	public void setTelefoneComercial(String telefoneComercial) {
		this.telefoneComercial = telefoneComercial;
	}
	public String getExpedidor() {
		return expedidor;
	}
	public void setExpedidor(String expedidor) {
		this.expedidor = expedidor;
	}
	public String getNaturalidade() {
		return naturalidade;
	}
	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}
	public String getProfissao() {
		return profissao;
	}
	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}
	public String getAlergia() {
		return alergia;
	}
	public void setAlergia(String alergia) {
		this.alergia = alergia;
	}
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	public String getTelefoneCelular() {
		return telefoneCelular;
	}
	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}