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
import javax.persistence.Transient;
import javax.persistence.Version;

import br.com.ecc.model.tipo.TipoConfirmacaoEnum;
import br.com.ecc.model.tipo.TipoRespostaConviteEnum;

@Entity
@SequenceGenerator(name="SQ_ENCONTROCONVITE", sequenceName="SQ_ENCONTROCONVITE")
@NamedQueries({
	@NamedQuery(name="encontroConvite.porEncontro", query="select u from EncontroConvite u where u.encontro = :encontro order by u.encontroFila.ordem, u.ordem"),
	@NamedQuery(name="encontroConvite.deletePorEncontro", query="delete from EncontroConvite u where u.encontro = :encontro")
})
public class EncontroConvite extends _WebBaseEntity {
	private static final long serialVersionUID = 5575143873350086182L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROCONVITE", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@ManyToOne
	@JoinColumn(name="casal")
	private Casal casal;

	@ManyToOne
	@JoinColumn(name="casalConvidado")
	private Casal casalConvidado;

	@ManyToOne
	@JoinColumn(name="casalResponsavel")
	private Casal casalResponsavel;

	@ManyToOne
	@JoinColumn(name="encontroFila")
	private EncontroFila encontroFila;

	private Integer ordem;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConvite;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataResposta;

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoRespostaConviteEnum tipoResposta;

	private Boolean esconderPlanoPagamento;

	@Column(precision=15, scale=2)
	private BigDecimal valorAfilhadoPodePagar;

	@ManyToOne
	@JoinColumn(name="casalDoacao")
	private Casal casalDoacao;

	@Column(precision=15, scale=2)
	private BigDecimal valorDoacao;

	@Column(length=500)
	private String observacao;

	@ManyToOne
	@JoinColumn(name="mensagemDestinatarioFicha")
	private MensagemDestinatario mensagemDestinatarioFicha;

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoConfirmacaoEnum tipoConfirmacao;

	@Version
	private Integer version;

	@Transient
	private Boolean moverFinalFila;

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
	public Casal getCasalConvidado() {
		return casalConvidado;
	}
	public void setCasalConvidado(Casal casalConvidado) {
		this.casalConvidado = casalConvidado;
	}
	public Casal getCasalResponsavel() {
		return casalResponsavel;
	}
	public void setCasalResponsavel(Casal casalResponsavel) {
		this.casalResponsavel = casalResponsavel;
	}
	public EncontroFila getEncontroFila() {
		return encontroFila;
	}
	public void setEncontroFila(EncontroFila encontroFila) {
		this.encontroFila = encontroFila;
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
	public Date getDataConvite() {
		return dataConvite;
	}
	public void setDataConvite(Date dataConvite) {
		this.dataConvite = dataConvite;
	}
	public Date getDataResposta() {
		return dataResposta;
	}
	public void setDataResposta(Date dataResposta) {
		this.dataResposta = dataResposta;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Boolean getEsconderPlanoPagamento() {
		return esconderPlanoPagamento;
	}
	public void setEsconderPlanoPagamento(Boolean esconderPlanoPagamento) {
		this.esconderPlanoPagamento = esconderPlanoPagamento;
	}
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public TipoRespostaConviteEnum getTipoResposta() {
		return tipoResposta;
	}
	public void setTipoResposta(TipoRespostaConviteEnum tipoResposta) {
		this.tipoResposta = tipoResposta;
	}
	public Boolean getMoverFinalFila() {
		return moverFinalFila;
	}
	public void setMoverFinalFila(Boolean moverFinalFila) {
		this.moverFinalFila = moverFinalFila;
	}
	public MensagemDestinatario getMensagemDestinatarioFicha() {
		return mensagemDestinatarioFicha;
	}
	public void setMensagemDestinatarioFicha(MensagemDestinatario mensagemDestinatarioFicha) {
		this.mensagemDestinatarioFicha = mensagemDestinatarioFicha;
	}
	public TipoConfirmacaoEnum getTipoConfirmacao() {
		return tipoConfirmacao;
	}
	public void setTipoConfirmacao(TipoConfirmacaoEnum tipoConfirmacao) {
		this.tipoConfirmacao = tipoConfirmacao;
	}
	public BigDecimal getValorAfilhadoPodePagar() {
		return valorAfilhadoPodePagar;
	}
	public void setValorAfilhadoPodePagar(BigDecimal valorAfilhadoPodePagar) {
		this.valorAfilhadoPodePagar = valorAfilhadoPodePagar;
	}
	public Casal getCasalDoacao() {
		return casalDoacao;
	}
	public void setCasalDoacao(Casal casalDoacao) {
		this.casalDoacao = casalDoacao;
	}
	public BigDecimal getValorDoacao() {
		return valorDoacao;
	}
	public void setValorDoacao(BigDecimal valorDoacao) {
		this.valorDoacao = valorDoacao;
	}
}