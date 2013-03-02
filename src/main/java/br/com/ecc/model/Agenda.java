package br.com.ecc.model;

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

import br.com.ecc.model.tipo.TipoAgendaEventoEnum;

@Entity
@SequenceGenerator(name="SQ_AGENDA", sequenceName="SQ_AGENDA")
@NamedQueries({
	@NamedQuery(name="agenda.porEncontroPeriodo", query="select u from Agenda u where u.encontro = :encontro and u.dataInicio >= :dataInicio and u.dataFim <= :dataFim order by u.dataInicio "),
	@NamedQuery(name="agenda.porEncontro", query="select u from Agenda u where u.encontro = :encontro order by u.dataInicio ")
})
public class Agenda extends _WebBaseEntity {

	private static final long serialVersionUID = 2992544559727125755L;

	@Id
	@GeneratedValue(generator="SQ_AGENDA", strategy=GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;

	@Column(length=150)
	private String titulo;

	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private TipoAgendaEventoEnum tipo;

	@Column(length=254)
	private String endereco;

	@Column(length=100)
	private String bairro;

	@Column(length=100)
	private String cidade;

	@Column(length=2)
	private String estado;

	@Column(length=15)
	private String cep;

	@ManyToOne
	@JoinColumn(name="casalResponsavel")
	private Casal casalResponsavel;

	@ManyToOne
	@JoinColumn(name="pessoaResponsavel")
	private Pessoa pessoaResponsavel;

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@Version
	private Integer version;

	@Override
	public String toString() {
		if (getTitulo()!=null){
			if (getTitulo() != null && !getTitulo().equals(""))
				return getTipo().getNome() + " - " + getTitulo();
			else
				return getTipo().getNome();
		}
		return getTipo().getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public TipoAgendaEventoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoAgendaEventoEnum tipo) {
		this.tipo = tipo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Casal getCasalResponsavel() {
		return casalResponsavel;
	}

	public void setCasalResponsavel(Casal casalResponsavel) {
		this.casalResponsavel = casalResponsavel;
	}

	public Pessoa getPessoaResponsavel() {
		return pessoaResponsavel;
	}

	public void setPessoaResponsavel(Pessoa pessoaResponsavel) {
		this.pessoaResponsavel = pessoaResponsavel;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Encontro getEncontro() {
		return encontro;
	}

	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}

	@Transient
	public String getDescricao() {
		return toString();
	}

	@Transient
	public String getEnderecoFull() {
		return getEndereco() + " " + getBairro() + " " + getCidade() + "-" + getEstado();
	}

}