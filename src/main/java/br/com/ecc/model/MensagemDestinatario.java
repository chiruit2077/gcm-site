package br.com.ecc.model;

import java.util.Date;

import javax.persistence.Entity;
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

@Entity
@SequenceGenerator(name="SQ_MENSAGEMDESTINATARIO", sequenceName="SQ_MENSAGEMDESTINATARIO")
@NamedQueries({
	@NamedQuery(name="mensagemDestinatario.porMensagem", query="select u from MensagemDestinatario u where u.mensagem = :mensagem"),
	@NamedQuery(name="mensagemDestinatario.porMensagemCasal", query="select u from MensagemDestinatario u where u.mensagem = :mensagem and u.casal = :casal"),
	@NamedQuery(name="mensagemDestinatario.porMensagemPessoa", query="select u from MensagemDestinatario u where u.mensagem = :mensagem and u.pessoa = :pessoa"),
	@NamedQuery(name="mensagemDestinatario.deletePorMensagem", 
		query="delete from MensagemDestinatario u where u.mensagem = :mensagem "),
	@NamedQuery(name="mensagemDestinatario.deletePorMensagemNotIn", 
		query="delete from MensagemDestinatario u where u.mensagem = :mensagem and u not in (:lista)")
})
public class MensagemDestinatario extends _WebBaseEntity {
	
	private static final long serialVersionUID = 2307179341978434950L;

	@Id
	@GeneratedValue(generator="SQ_MENSAGEMDESTINATARIO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="mensagem")
	private Mensagem mensagem;
	
	@ManyToOne
	@JoinColumn(name="casal")
	private Casal casal;
	
	@ManyToOne
	@JoinColumn(name="pessoa")
	private Pessoa pessoa;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEnvio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConfirmacao;
	
	@Transient
	private String dataEnvioStr;
	
	@Transient
	private String dataConfirmacaoStr;
	
	@Version
	private Integer version;
	
	@Override
	public String toString() {
		if(casal!=null) return casal.toString();
		return pessoa.toString();
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
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public Mensagem getMensagem() {
		return mensagem;
	}
	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}
	public Date getDataEnvio() {
		return dataEnvio;
	}
	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}
	public Date getDataConfirmacao() {
		return dataConfirmacao;
	}
	public void setDataConfirmacao(Date dataConfirmacao) {
		this.dataConfirmacao = dataConfirmacao;
	}
	public String getDataEnvioStr() {
		return dataEnvioStr;
	}
	public void setDataEnvioStr(String dataEnvioStr) {
		this.dataEnvioStr = dataEnvioStr;
	}
	public String getDataConfirmacaoStr() {
		return dataConfirmacaoStr;
	}
	public void setDataConfirmacaoStr(String dataConfirmacaoStr) {
		this.dataConfirmacaoStr = dataConfirmacaoStr;
	}
}