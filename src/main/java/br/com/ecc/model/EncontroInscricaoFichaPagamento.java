package br.com.ecc.model;

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

import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoFichaStatusEnum;

@Entity
@SequenceGenerator(name="SQ_ENCONTROINSCRICAOFICHAP", sequenceName="SQ_ENCONTROINSCRICAOFICHAP")
@NamedQueries({
	@NamedQuery(name="encontroInscricaoFichaPagamento.porEncontro", query="select u from EncontroInscricaoFichaPagamento u where u.encontro = :encontro order by u.tipo, u.ficha "),
	@NamedQuery(name="encontroInscricaoFichaPagamento.deletePorEncontro", query="delete from EncontroInscricaoFichaPagamento u where u.encontro = :encontro "),
	
	@NamedQuery(name="encontroInscricaoFichaPagamento.deletePorEncontroInscricao", 
			query="delete from EncontroInscricaoFichaPagamento u where u.encontroInscricao = :encontroInscricao "),
	
	@NamedQuery(name="encontroInscricaoFichaPagamento.porEncontroReservada", query="select u from EncontroInscricaoFichaPagamento u where u.encontro = :encontro and u.status = 'RESERVADO' order by u.tipo, u.ficha "),
	@NamedQuery(name="encontroInscricaoFichaPagamento.porFichaEncontro", query="select u from EncontroInscricaoFichaPagamento u where u.encontro = :encontro and u.ficha = :ficha "),
	@NamedQuery(name="encontroInscricaoFichaPagamento.porVagalLivre", query="select u from EncontroInscricaoFichaPagamento u where u.encontro = :encontro and " +
			"u.encontroInscricao is null and u.tipo = :tipo and u.status = :status order by u.ficha ")
})
public class EncontroInscricaoFichaPagamento extends _WebBaseEntity {

	private static final long serialVersionUID = -7855394794631705539L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROINSCRICAOFICHAP", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontroInscricao")
	private EncontroInscricao encontroInscricao;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoInscricaoCasalEnum tipo;

	private Integer ficha;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoInscricaoFichaStatusEnum status;

	@Column(length=254)
	private String observacao;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return ficha.toString();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public TipoInscricaoCasalEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoInscricaoCasalEnum tipo) {
		this.tipo = tipo;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getFicha() {
		return ficha;
	}
	public void setFicha(Integer ficha) {
		this.ficha = ficha;
	}
	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}
	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public TipoInscricaoFichaStatusEnum getStatus() {
		return status;
	}
	public void setStatus(TipoInscricaoFichaStatusEnum status) {
		this.status = status;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}