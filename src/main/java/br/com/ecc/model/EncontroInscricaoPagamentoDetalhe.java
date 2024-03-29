package br.com.ecc.model;

import java.math.BigDecimal;

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

import br.com.ecc.model.tipo.TipoPagamentoDetalheEnum;
import br.com.ecc.model.tipo.TipoPagamentoLancamentoEnum;

@Entity
@SequenceGenerator(name="SQ_FORMAPAGAMENTODET", sequenceName="SQ_FORMAPAGAMENTODET")
@NamedQueries({
	@NamedQuery(name="encontroInscricaoPagamentoDetalhe.porEncontroInscricao",
		query="select u from EncontroInscricaoPagamentoDetalhe u where u.encontroInscricao = :encontroInscricao"),
	@NamedQuery(name="encontroInscricaoPagamentoDetalhe.porEncontroInscricaoOutraCredito",
		query="select u from EncontroInscricaoPagamentoDetalhe u where u.encontroInscricaoOutra = :encontroInscricao and u.tipoDetalhe='OUTRAINSCRICAO' and u.tipoLancamento = 'CREDITO' "),
	@NamedQuery(name="encontroInscricaoPagamentoDetalhe.deletePorEncontro",
		query="delete from EncontroInscricaoPagamentoDetalhe u where u.encontroInscricao in ( select a from EncontroInscricao a where a.encontro = :encontro )"),
	@NamedQuery(name="encontroInscricaoPagamentoDetalhe.deletePorEncontroInscricao",
		query="delete from EncontroInscricaoPagamentoDetalhe u where u.encontroInscricao = :encontroInscricao"),
	@NamedQuery(name="encontroInscricaoPagamentoDetalhe.deletePorEncontroInscricaoNotIn",
		query="delete from EncontroInscricaoPagamentoDetalhe u where u.encontroInscricao = :encontroInscricao and u not in (:lista)")
})
public class EncontroInscricaoPagamentoDetalhe extends _WebBaseEntity {
	private static final long serialVersionUID = -6488036335274853783L;

	@Id
	@GeneratedValue(generator="SQ_FORMAPAGAMENTODET", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontroInscricao")
	private EncontroInscricao encontroInscricao;

	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(length=50)
	private TipoPagamentoDetalheEnum tipoDetalhe;

	@Enumerated(EnumType.STRING)
	@Column(length=50)
	private TipoPagamentoLancamentoEnum tipoLancamento;

	@ManyToOne
	@JoinColumn(name="encontroInscricaoOutra")
	private EncontroInscricao encontroInscricaoOutra;

	@Column(precision=15, scale=2)
	private BigDecimal valor;

	@Column(precision=15, scale=2)
	private BigDecimal valorUnitario;

	private Integer quantidade;

	private Boolean editavel;

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
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getValorUnitario() {
		if (valorUnitario == null) return getValor();
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public Integer getQuantidade() {
		if (quantidade == null) return 1;
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	public Boolean getEditavel() {
		if (editavel==null) return true;
		return editavel;
	}
	public void setEditavel(Boolean editavel) {
		this.editavel = editavel;
	}
	public TipoPagamentoDetalheEnum getTipoDetalhe() {
		if (tipoDetalhe==null) return TipoPagamentoDetalheEnum.AVULSO;
		return tipoDetalhe;
	}
	public void setTipoDetalhe(TipoPagamentoDetalheEnum tipoDetalhe) {
		this.tipoDetalhe = tipoDetalhe;
	}
	public TipoPagamentoLancamentoEnum getTipoLancamento() {
		if (tipoLancamento==null) return TipoPagamentoLancamentoEnum.DEBITO;
		return tipoLancamento;
	}
	public void setTipoLancamento(TipoPagamentoLancamentoEnum tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}
	public EncontroInscricao getEncontroInscricaoOutra() {
		return encontroInscricaoOutra;
	}
	public void setEncontroInscricaoOutra(EncontroInscricao encontroInscricaoOutra) {
		this.encontroInscricaoOutra = encontroInscricaoOutra;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroInscricaoPagamentoDetalhe other = (EncontroInscricaoPagamentoDetalhe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}