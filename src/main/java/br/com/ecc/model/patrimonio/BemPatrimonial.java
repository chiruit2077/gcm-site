package br.com.ecc.model.patrimonio;

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

import br.com.ecc.model._WebBaseEntity;

@Entity
@SequenceGenerator(name="SQ_BEMPATRIMONIAL", sequenceName="SQ_BEMPATRIMONIAL")
@NamedQueries({
	@NamedQuery(name="bemPatrimonial.todos", query="select u from BemPatrimonial u order by u.descricao")
})
public class BemPatrimonial extends _WebBaseEntity {

	private static final long serialVersionUID = 7029483552778075783L;

	@Id
	@GeneratedValue(generator="SQ_BEMPATRIMONIAL", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String descricao;

	private Integer codigoReduzido;

	@ManyToOne
	@JoinColumn(name="fornecedor")
	private Fornecedor fornecedor;

	@Enumerated(EnumType.STRING)
	private TipoSituacaoBemPatrimonialEnum situacao;

	@ManyToOne
	@JoinColumn(name="localUso")
	private Local localUso;

	@ManyToOne
	@JoinColumn(name="localArmazenagem")
	private Local localArmazenagem;

	private BigDecimal valorAtual;

	private BigDecimal valorIncorporacao;

	private String marca;

	private String modelo;

	private BigDecimal altura;

	private BigDecimal largura;

	private BigDecimal profundidade;

	private String numeroSerie;

	private Integer vidaUtil;

	@ManyToOne
	@JoinColumn(name="bemPatrimonialHistorico")
	private BemPatrimonial bemPatrimonialHistorico;

	@Enumerated(EnumType.STRING)
	private TipoEstadoBemPatrimonialEnum estadoIncorporacao;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return getDescricao();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCodigoReduzido() {
		return codigoReduzido;
	}

	public void setCodigoReduzido(Integer codigoReduzido) {
		this.codigoReduzido = codigoReduzido;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoSituacaoBemPatrimonialEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(TipoSituacaoBemPatrimonialEnum situacao) {
		this.situacao = situacao;
	}

	public Local getLocalUso() {
		return localUso;
	}

	public void setLocalUso(Local localUso) {
		this.localUso = localUso;
	}

	public Local getLocalArmazenagem() {
		return localArmazenagem;
	}

	public void setLocalArmazenagem(Local localArmazenagem) {
		this.localArmazenagem = localArmazenagem;
	}

	public BigDecimal getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(BigDecimal valorAtual) {
		this.valorAtual = valorAtual;
	}

	public BigDecimal getValorIncorporacao() {
		return valorIncorporacao;
	}

	public void setValorIncorporacao(BigDecimal valorIncorporacao) {
		this.valorIncorporacao = valorIncorporacao;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public BigDecimal getLargura() {
		return largura;
	}

	public void setLargura(BigDecimal largura) {
		this.largura = largura;
	}

	public BigDecimal getProfundidade() {
		return profundidade;
	}

	public void setProfundidade(BigDecimal profundidade) {
		this.profundidade = profundidade;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public Integer getVidaUtil() {
		return vidaUtil;
	}

	public void setVidaUtil(Integer vidaUtil) {
		this.vidaUtil = vidaUtil;
	}

	public BemPatrimonial getBemPatrimonialHistorico() {
		return bemPatrimonialHistorico;
	}

	public void setBemPatrimonialHistorico(BemPatrimonial bemPatrimonialHistorico) {
		this.bemPatrimonialHistorico = bemPatrimonialHistorico;
	}

	public TipoEstadoBemPatrimonialEnum getEstadoIncorporacao() {
		return estadoIncorporacao;
	}

	public void setEstadoIncorporacao(
			TipoEstadoBemPatrimonialEnum estadoIncorporacao) {
		this.estadoIncorporacao = estadoIncorporacao;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}