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

@Entity
@SequenceGenerator(name="SQ_FORMAPAGAMENTO", sequenceName="SQ_FORMAPAGAMENTO")
@NamedQueries({
	@NamedQuery(name="encontroInscricaoPagamento.porEncontroInscricao",
		query="select u from EncontroInscricaoPagamento u where u.encontroInscricao = :encontroInscricao order by u.parcela"),
	@NamedQuery(name="encontroInscricaoPagamento.listaPagamentosPorEncontro",
		query="select u from EncontroInscricaoPagamento u " +
			  "where u.encontroInscricao.encontro = :encontro and" +
			  "      u.dataPagamento is not null " +
			  "order by u.encontroInscricao.codigo, u.parcela"),
	@NamedQuery(name="encontroInscricaoPagamento.deletePorEncontroInscricao",
		query="delete from EncontroInscricaoPagamento u where u.encontroInscricao = :encontroInscricao"),
	@NamedQuery(name="encontroInscricaoPagamento.deletePorEncontroInscricaoNotIn",
		query="delete from EncontroInscricaoPagamento u where u.encontroInscricao = :encontroInscricao and u not in (:lista)")
})
public class EncontroInscricaoPagamento extends _WebBaseEntity {
	private static final long serialVersionUID = -1928028525899583308L;

	@Id
	@GeneratedValue(generator="SQ_FORMAPAGAMENTO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontroInscricao")
	private EncontroInscricao encontroInscricao;

	private Integer parcela;

	@Column(precision=15, scale=2)
	private BigDecimal valor;

	@Temporal(TemporalType.DATE)
	private Date dataVencimento;

	@Temporal(TemporalType.DATE)
	private Date dataPagamento;

	private Boolean valorAlterado;

	@Version
	private Integer version;

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
	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}
	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
	public Integer getParcela() {
		return parcela;
	}
	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public Boolean getValorAlterado() {
		if (valorAlterado==null) return false;
		return valorAlterado;
	}
	public void setValorAlterado(Boolean valorAlterado) {
		this.valorAlterado = valorAlterado;
	}
}