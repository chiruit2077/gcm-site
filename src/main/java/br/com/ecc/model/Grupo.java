package br.com.ecc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name="SQ_GRUPO", sequenceName="SQ_GRUPO")
@NamedQueries({
	@NamedQuery(name="grupo.todos", query="select u from Grupo u order by u.nome")
})
public class Grupo extends _WebBaseEntity {
	private static final long serialVersionUID = 7800262775158559474L;

	@Id
	@GeneratedValue(generator="SQ_GRUPO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(length=150)
	private String nome;
	
	@Column(length=150)
	private String cidade;
	
	@Column(length=2)
	private String estado;
	
	private Integer idArquivoDigital;
	
	@Column(length=30)
	private String banco;
	
	@Column(length=10)
	private String agencia;
	
	@Column(length=10)
	private String conta;
	
	@Column(length=200)
	private String favorecidoConta;
	
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
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getIdArquivoDigital() {
		return idArquivoDigital;
	}
	public void setIdArquivoDigital(Integer idArquivoDigital) {
		this.idArquivoDigital = idArquivoDigital;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public String getFavorecidoConta() {
		return favorecidoConta;
	}
	public void setFavorecidoConta(String favorecidoConta) {
		this.favorecidoConta = favorecidoConta;
	}
}