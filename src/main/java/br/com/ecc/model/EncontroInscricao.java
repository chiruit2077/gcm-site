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
import br.com.ecc.model.tipo.TipoInscricaoEnum;

@Entity
@SequenceGenerator(name="SQ_ENCONTROINSCRICAO", sequenceName="SQ_ENCONTROINSCRICAO")
@NamedQueries({
	@NamedQuery(name="encontroInscricao.porEncontro", query="select u from EncontroInscricao u where u.encontro = :encontro order by u.tipo"),
	@NamedQuery(name="encontroInscricao.porEncontroConfirmados",
		query="select u from EncontroInscricao u " +
				"where  u.encontro = :encontro and " +
				"		( u.tipoConfirmacao is null or u.tipoConfirmacao = 'CONFIRMADO' ) " +
				"order by u.tipo"),
	@NamedQuery(name="encontroInscricao.porEncontroConvidados",
		query="select u from EncontroInscricao u " +
			  "where u.encontro = :encontro and " +
			  "      u.tipo = 'AFILHADO' and " +
			  "	     u.tipoConfirmacao = 'CONFIRMADO' " ),
	@NamedQuery(name="encontroInscricao.porEncontroEncontristas",
				query="select u from EncontroInscricao u " +
					  "where u.encontro = :encontro and " +
					  "      u.tipo in ( 'APOIO', 'PADRINHO', 'COORDENADOR' ) and " +
					  "	     u.tipoConfirmacao = 'CONFIRMADO' " ),
	@NamedQuery(name="encontroInscricao.porEncontroCasal",
		query="select u from EncontroInscricao u where u.encontro = :encontro and u.casal = :casal order by u.id desc"),
	@NamedQuery(name="encontroInscricao.porEncontroPessoaNomeLike",
	query="select up from EncontroInscricao up LEFT OUTER JOIN FETCH up.pessoa as p " +
	  "   where up.encontro = :encontro and " +
	  "        up.tipoConfirmacao = 'CONFIRMADO' and " +
	  "       ( upper(p.nome) like upper(:key) or" +
	  "        upper(p.apelido) like upper(:key) ) " +
	  "   order by p.apelido, p.nome  " ),
	@NamedQuery(name="encontroInscricao.porEncontroCasalNomeLikeTipo",
	query="select uc from EncontroInscricao uc LEFT OUTER JOIN FETCH uc.casal as c " +
	  "   where uc.encontro = :encontro and " +
	  "        uc.tipo = :tipo and " +
	  "        uc.tipoConfirmacao = 'CONFIRMADO' and " +
	  "       ( upper(c.ele.nome) like upper(:key) or " +
	  "        upper(c.ele.apelido) like upper(:key) or " +
	  "        upper(c.ela.nome) like upper(:key) or" +
	  "        upper(c.ela.apelido) like upper(:key) )  " +
	  "   order by c.ele.apelido, c.ela.apelido, c.ele.nome, c.ela.nome " ),
	@NamedQuery(name="encontroInscricao.porEncontroCasalNomeEncontristaLike",
		query="select uc from EncontroInscricao uc LEFT OUTER JOIN FETCH uc.casal as c " +
		  "   where uc.encontro = :encontro and " +
		  "        uc.tipoConfirmacao = 'CONFIRMADO' and " +
		  "        uc.tipo in ( 'APOIO', 'PADRINHO', 'COORDENADOR', 'EXTERNO' ) and " +
		  "       ( upper(c.ele.nome) like upper(:key) or " +
		  "        upper(c.ele.apelido) like upper(:key) or " +
		  "        upper(c.ela.nome) like upper(:key) or" +
		  "        upper(c.ela.apelido) like upper(:key) )  " +
		  "   order by c.ele.apelido, c.ela.apelido, c.ele.nome, c.ela.nome " ),
    @NamedQuery(name="encontroInscricao.porEncontroCasalNomeAfilhadoLike",
			query="select uc from EncontroInscricao uc LEFT OUTER JOIN FETCH uc.casal as c " +
			  "   where uc.encontro = :encontro and " +
			  "        uc.tipoConfirmacao = 'CONFIRMADO' and " +
			  "        uc.tipo in ( 'AFILHADO' ) and " +
			  "       ( upper(c.ele.nome) like upper(:key) or " +
			  "        upper(c.ele.apelido) like upper(:key) or " +
			  "        upper(c.ela.nome) like upper(:key) or" +
			  "        upper(c.ela.apelido) like upper(:key) )  " +
			  "   order by c.ele.apelido, c.ela.apelido, c.ele.nome, c.ela.nome " )
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
	@JoinColumn(name="casal", nullable=true)
	private Casal casal;

	@ManyToOne
	@JoinColumn(name="pessoa", nullable=true)
	private Pessoa pessoa;

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoInscricaoEnum tipo;

	private Integer codigo;

	@ManyToOne
	@JoinColumn(name="fichaPagamento")
	private EncontroInscricaoFichaPagamento fichaPagamento;

	@Temporal(TemporalType.DATE)
	private Date dataMaximaParcela;

	@Column(precision=15, scale=2)
	private BigDecimal valorEncontro;

	@ManyToOne
	@JoinColumn(name="mensagemDestinatario")
	private MensagemDestinatario mensagemDestinatario;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPrenchimentoFicha;

	@Transient
	private String dataPrenchimentoFichaStr;

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

	@Transient
	public String toStringApelidos() {
		if(casal!=null) return casal.getApelidos("e");
		return pessoa.getApelido();
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
	public String getDataPrenchimentoFichaStr() {
		return dataPrenchimentoFichaStr;
	}
	public void setDataPrenchimentoFichaStr(String dataPrenchimentoFichaStr) {
		this.dataPrenchimentoFichaStr = dataPrenchimentoFichaStr;
	}
	public EncontroInscricaoFichaPagamento getFichaPagamento() {
		return fichaPagamento;
	}
	public void setFichaPagamento(EncontroInscricaoFichaPagamento fichaPagamento) {
		this.fichaPagamento = fichaPagamento;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroInscricao other = (EncontroInscricao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}