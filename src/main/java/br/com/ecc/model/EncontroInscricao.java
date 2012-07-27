package br.com.ecc.model;

import java.math.BigDecimal;
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

import br.com.ecc.model.tipo.TipoConfirmacaoEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;

@Entity
@SequenceGenerator(name="SQ_ENCONTROINSCRICAO", sequenceName="SQ_ENCONTROINSCRICAO")
@NamedQueries({
	@NamedQuery(name="encontroInscricao.porEncontro", query="select u from EncontroInscricao u where u.encontro = :encontro order by u.tipo"),
	@NamedQuery(name="encontroInscricao.porEncontroConfirmados", 
		query="select u from EncontroInscricao u " +
				"where  u.encontro = :encontro and" +
				"		( u.tipoConfirmacao is null or u.tipoConfirmacao = 'CONFIRMADO' ) " +
				"order by u.tipo"),
	@NamedQuery(name="encontroInscricao.porEncontroConvidados", 
		query="select u.casal from EncontroInscricao u where u.encontro = :encontro and u.tipo = 'AFILHADO' "),
	@NamedQuery(name="encontroInscricao.porEncontroCasal", 
		query="select u from EncontroInscricao u where u.encontro = :encontro and u.casal = :casal order by u.id desc")
})
public class EncontroInscricao extends _WebBaseEntity {
	private static final long serialVersionUID = 7982370030939310990L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROINSCRICAO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@ManyToOne
	@JoinColumn(name="casal")
	private Casal casal;
	
	@ManyToOne
	@JoinColumn(name="pessoa")
	private Pessoa pessoa;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoInscricaoEnum tipo;
	
	private Integer codigo;
	
	@Temporal(TemporalType.DATE)
	private Date dataMaximaParcela;
	
	@Column(precision=15, scale=2)
	private BigDecimal valorEncontro;
	
	@ManyToOne
	@JoinColumn(name="mensagemDestinatario")
	private MensagemDestinatario mensagemDestinatario;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPrenchimentoFicha;
	
	private Boolean esconderPlanoPagamento;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoConfirmacaoEnum tipoConfirmacao;
	
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
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public BigDecimal getValorEncontro() {
		return valorEncontro;
	}
	public void setValorEncontro(BigDecimal valorEncontro) {
		this.valorEncontro = valorEncontro;
	}
	public MensagemDestinatario getMensagemDestinatario() {
		return mensagemDestinatario;
	}
	public void setMensagemDestinatario(MensagemDestinatario mensagemDestinatario) {
		this.mensagemDestinatario = mensagemDestinatario;
	}
	public Date getDataPrenchimentoFicha() {
		return dataPrenchimentoFicha;
	}
	public void setDataPrenchimentoFicha(Date dataPrenchimentoFicha) {
		this.dataPrenchimentoFicha = dataPrenchimentoFicha;
	}

	public Date getDataMaximaParcela() {
		return dataMaximaParcela;
	}

	public void setDataMaximaParcela(Date dataMaximaParcela) {
		this.dataMaximaParcela = dataMaximaParcela;
	}

	public Boolean getEsconderPlanoPagamento() {
		return esconderPlanoPagamento;
	}

	public void setEsconderPlanoPagamento(Boolean esconderPlanoPagamento) {
		this.esconderPlanoPagamento = esconderPlanoPagamento;
	}

	public TipoConfirmacaoEnum getTipoConfirmacao() {
		return tipoConfirmacao;
	}

	public void setTipoConfirmacao(TipoConfirmacaoEnum tipoConfirmacao) {
		this.tipoConfirmacao = tipoConfirmacao;
	}
}