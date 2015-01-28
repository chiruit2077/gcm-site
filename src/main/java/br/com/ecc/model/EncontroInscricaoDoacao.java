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
@SequenceGenerator(name="SQ_DOACAO", sequenceName="SQ_DOACAO")
@NamedQueries({
	@NamedQuery(name="encontroInscricaoDoacao.porEncontroInscricao",
		query="select u from EncontroInscricaoDoacao u where u.encontroInscricao = :encontroInscricao order by u.data asc")
})
public class EncontroInscricaoDoacao extends _WebBaseEntity {
	private static final long serialVersionUID = 4145118246333099730L;

	@Id
	@GeneratedValue(generator="SQ_DOACAO", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontroInscricao")
	private EncontroInscricao encontroInscricao;

	@Column(precision=15, scale=2)
	private BigDecimal valor;

	@Temporal(TemporalType.DATE)
	private Date data;

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
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
}