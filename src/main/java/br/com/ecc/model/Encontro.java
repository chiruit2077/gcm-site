package br.com.ecc.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
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
import javax.persistence.Version;

import com.google.gwt.i18n.client.DateTimeFormat;

@Entity
@SequenceGenerator(name="SQ_ENCONTRO", sequenceName="SQ_ENCONTRO")
@NamedQueries({
	@NamedQuery(name="encontro.todos", query="select u from Encontro u order by u.grupo, u.inicio desc"),
	@NamedQuery(name="encontro.porGrupo", query="select u from Encontro u where u.grupo =:grupo order by u.inicio desc")
})
public class Encontro extends _WebBaseEntity {
	private static final long serialVersionUID = -8111395743922320149L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTRO", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;
	
	@Temporal(TemporalType.DATE)
	private Date inicio;
	
	@Temporal(TemporalType.DATE)
	private Date fim;
	
	private Integer quantidadeAfilhados;
	
	@Column(precision=15, scale=2)
	private BigDecimal valorAfilhado;
	
	@Column(precision=15, scale=2)
	private BigDecimal valorPadrinho;
	
	@Column(precision=15, scale=2)
	private BigDecimal valorApoio;
	
	@Column(precision=15, scale=2)
	private BigDecimal valorInscricao;
	
	@Temporal(TemporalType.DATE)
	private Date dataPagamentoInscricao;
	
	@Temporal(TemporalType.DATE)
	private Date dataMaximaPagamento;
	
	@Version
	private Integer version;
	
	@Override
	public String toString() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("MMMM yyyy");
		return fmt.format(inicio).toUpperCase();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFim() {
		return fim;
	}
	public void setFim(Date fim) {
		this.fim = fim;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public Integer getQuantidadeAfilhados() {
		return quantidadeAfilhados;
	}
	public void setQuantidadeAfilhados(Integer quantidadeAfilhados) {
		this.quantidadeAfilhados = quantidadeAfilhados;
	}
	public BigDecimal getValorAfilhado() {
		return valorAfilhado;
	}
	public void setValorAfilhado(BigDecimal valorAfilhado) {
		this.valorAfilhado = valorAfilhado;
	}
	public BigDecimal getValorPadrinho() {
		return valorPadrinho;
	}
	public void setValorPadrinho(BigDecimal valorPadrinho) {
		this.valorPadrinho = valorPadrinho;
	}
	public BigDecimal getValorApoio() {
		return valorApoio;
	}
	public void setValorApoio(BigDecimal valorApoio) {
		this.valorApoio = valorApoio;
	}
	public BigDecimal getValorInscricao() {
		return valorInscricao;
	}
	public void setValorInscricao(BigDecimal valorInscricao) {
		this.valorInscricao = valorInscricao;
	}
	public Date getDataPagamentoInscricao() {
		return dataPagamentoInscricao;
	}
	public void setDataPagamentoInscricao(Date dataPagamentoInscricao) {
		this.dataPagamentoInscricao = dataPagamentoInscricao;
	}
	public Date getDataMaximaPagamento() {
		return dataMaximaPagamento;
	}
	public void setDataMaximaPagamento(Date dataMaximaPagamento) {
		this.dataMaximaPagamento = dataMaximaPagamento;
	}
}