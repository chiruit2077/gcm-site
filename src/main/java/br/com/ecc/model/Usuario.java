package br.com.ecc.model;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;

@Entity
@SequenceGenerator(name="SQ_USUARIO", sequenceName="SQ_USUARIO")
@NamedQueries({
	@NamedQuery(name="usuario.todos", query="select u from Usuario u order by u.pessoa.nome"),
	@NamedQuery(name="usuario.porEmail", query="select u from Usuario u where u.pessoa.email = :email"),
	@NamedQuery(name="usuario.porPessoa", query="select u from Usuario u where u.pessoa = :pessoa")
})
public class Usuario extends _WebBaseEntity {
	private static final long serialVersionUID = 6344322920680027869L;

	@Id
	@GeneratedValue(generator="SQ_USUARIO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="pessoa")
	private Pessoa pessoa;
	
	@Column(length=50)
	private String senha;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoNivelUsuarioEnum nivel;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimoAcesso;

	@Version
	private Integer version;
	
	@Transient
	private String senhaCripto;
	
	@Override
	public String toString() {
		return getNome();
	}
	
	public String getEmail() {
		return getPessoa().getEmail();
	}
	public String getNome() {
		return getPessoa().getNome();
	}
	public String getApelido() {
		return getPessoa().getApelido();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public TipoNivelUsuarioEnum getNivel() {
		return nivel;
	}
	public void setNivel(TipoNivelUsuarioEnum nivel) {
		this.nivel = nivel;
	}
	public String getSenhaCripto() {
		return senhaCripto;
	}
	public void setSenhaCripto(String senhaCripto) {
		this.senhaCripto = senhaCripto;
	}
	public Date getUltimoAcesso() {
		return ultimoAcesso;
	}
	public void setUltimoAcesso(Date ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}
}