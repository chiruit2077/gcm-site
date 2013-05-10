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

import br.com.ecc.model.tipo.TipoFilaEnum;

@Entity
@SequenceGenerator(name="SQ_ENCONTROFILA", sequenceName="SQ_ENCONTROFILA")
@NamedQueries({
	@NamedQuery(name="encontroFila.porEncontro", query="select u from EncontroFila u where u.encontro = :encontro order by u.ordem, u.nome"),
	@NamedQuery(name="encontroFila.porEncontroFilaGeral", query="select u from EncontroFila u where u.encontro = :encontro and u.tipoFila = 'GERAL'"),
	@NamedQuery(name="encontroFila.deletePorEncontro", query="delete from EncontroFila u where u.encontro = :encontro")
})
public class EncontroFila extends _WebBaseEntity {
	private static final long serialVersionUID = 7634193874228521460L;

	@Id
	@GeneratedValue(generator="SQ_ENCONTROFILA", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@Column(length=250)
	private String nome;

	private Integer quantidadeVagas;

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoFilaEnum tipoFila;

	private Integer ordem;

	@Version
	private Integer version;

	@Override
	public String toString() {
		return nome;
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getQuantidadeVagas() {
		return quantidadeVagas;
	}
	public void setQuantidadeVagas(Integer quantidadeVagas) {
		this.quantidadeVagas = quantidadeVagas;
	}
	public TipoFilaEnum getTipoFila() {
		return tipoFila;
	}
	public void setTipoFila(TipoFilaEnum tipoFila) {
		this.tipoFila = tipoFila;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}