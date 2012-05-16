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
import javax.persistence.Version;

import br.com.ecc.model.tipo.TipoRecadoEnum;

@Entity
@SequenceGenerator(name="SQ_RECADO", sequenceName="SQ_RECADO")
@NamedQueries({
	@NamedQuery(name="recado.paraCasalNaoLidos", 
		query="select u from Recado u where ( u.casal = :casal or u.casalOrigem = :casal ) and (lido is null or lido != 1) order by u.data desc"),
	@NamedQuery(name="recado.paraCasal", 
		query="select u from Recado u where ( u.casal = :casal or u.casalOrigem = :casal ) order by u.data desc"),
	@NamedQuery(name="recado.porGrupo", 
		query="select u from Recado u " +
			   "where ( u.casal.grupo = :grupo or u.casalOrigem.grupo = :grupo ) and " +
			   "      u.data < :data and " +
			   "      ( u.tipo != 'PRIVADO' or u.casal = :casal or u.casalOrigem = :casal) " +
			   "order by u.data desc"),
	@NamedQuery(name="recado.todosCasal", 
		query="select u from Recado u " +
		   "where u.data < :data and " +
		   "      ( u.tipo != 'PRIVADO' or u.casal = :casal or u.casalOrigem = :casal) " +
		   "order by u.data desc")
})
public class Recado extends _WebBaseEntity {
	private static final long serialVersionUID = 7800591628136236366L;

	@Id
	@GeneratedValue(generator="SQ_RECADO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="pai")
	private Recado pai;
	
	@ManyToOne
	@JoinColumn(name="casalOrigem")
	private Casal casalOrigem;

	@ManyToOne
	@JoinColumn(name="pessoaOrigem")
	private Pessoa pessoaOrigem;
	
	@ManyToOne
	@JoinColumn(name="casal")
	private Casal casal;
	
	@Column(columnDefinition="TEXT")
	private char[] mensagem;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoRecadoEnum tipo;
	
	private Boolean lido;
	
	@Version
	private Integer version;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Recado getPai() {
		return pai;
	}
	public void setPai(Recado pai) {
		this.pai = pai;
	}
	public Casal getCasalOrigem() {
		return casalOrigem;
	}
	public void setCasalOrigem(Casal casalOrigem) {
		this.casalOrigem = casalOrigem;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public TipoRecadoEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoRecadoEnum tipo) {
		this.tipo = tipo;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Boolean getLido() {
		return lido;
	}
	public void setLido(Boolean lido) {
		this.lido = lido;
	}

	public Pessoa getPessoaOrigem() {
		return pessoaOrigem;
	}

	public void setPessoaOrigem(Pessoa pessoaOrigem) {
		this.pessoaOrigem = pessoaOrigem;
	}

	public char[] getMensagem() {
		return mensagem;
	}

	public void setMensagem(char[] mensagem) {
		this.mensagem = mensagem;
	}
}