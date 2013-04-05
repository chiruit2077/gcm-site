package br.com.ecc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import br.com.ecc.model.tipo.TipoAtividadeEnum;
import br.com.ecc.model.tipo.TipoPreenchimentoAtividadeEnum;

import com.google.gwt.i18n.client.DateTimeFormat;

@Entity
@SequenceGenerator(name="SQ_ATIVIDADE", sequenceName="SQ_ATIVIDADE")
@NamedQueries({
	@NamedQuery(name="encontroAtividade.porEncontro", query="select u from EncontroAtividade u where u.encontro = :encontro order by u.inicio"),
	@NamedQuery(name="encontroAtividade.porEncontroCasal",
		query="select u from EncontroAtividade u " +
			  "where u.encontro = :encontro and" +
			  "      u in (Select e.encontroAtividade from EncontroAtividadeInscricao e where e.encontroInscricao.encontro = :encontro and e.encontroInscricao.casal = :casal ) )" +
			  "order by u.inicio")
})
public class EncontroAtividade extends _WebBaseEntity {


	private static final long serialVersionUID = 9141156097707357523L;

	@Id
	@GeneratedValue(generator="SQ_ATIVIDADE", strategy=GenerationType.AUTO)
	private Integer id;

	public EncontroAtividade() {
		infoErro = new ArrayList<String>();
		infoAtencao = new ArrayList<String>();
	}

	@ManyToOne
	@JoinColumn(name="encontro")
	private Encontro encontro;

	@ManyToOne
	@JoinColumn(name="atividade")
	private Atividade atividade;

	@Temporal(TemporalType.TIMESTAMP)
	private Date inicio;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fim;

	/*@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoOcorrenciaAtividadeEnum tipoOcorrencia;*/

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoAtividadeEnum tipoAtividade;

	/*@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoEncontroAtividadeProgramaEnum tipoPrograma;*/

	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoPreenchimentoAtividadeEnum tipoPreenchimento;

	private Integer quantidadeDesejada;

	private Integer porcentagem;

	private Boolean revisado;

	@Transient
	private List<String> infoErro;

	@Transient
	private List<String> infoAtencao;

	@Transient
	private Integer quantidade;

	@Version
	private Integer version;

	@Override
	public String toString() {
		if(atividade!=null){
			return DateTimeFormat.getFormat("E").format(inicio) + " " +
		         DateTimeFormat.getFormat("HH:mm").format(inicio) + " " + DateTimeFormat.getFormat("HH:mm").format(fim) + " " + tipoAtividade.getNome() + " - " + atividade.getNome();
		}
		return super.toString();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncontroAtividade other = (EncontroAtividade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

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
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public Atividade getAtividade() {
		return atividade;
	}
	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
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
	/*public TipoEncontroAtividadeProgramaEnum getTipoPrograma() {
		return tipoPrograma;
	}
	public void setTipoPrograma(TipoEncontroAtividadeProgramaEnum tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}*/
	public Integer getQuantidadeDesejada() {
		return quantidadeDesejada;
	}
	public void setQuantidadeDesejada(Integer quantidadeDesejada) {
		this.quantidadeDesejada = quantidadeDesejada;
	}
	public TipoAtividadeEnum getTipoAtividade() {
		return tipoAtividade;
	}
	public void setTipoAtividade(TipoAtividadeEnum tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}
	/*public TipoOcorrenciaAtividadeEnum getTipoOcorrencia() {
		return tipoOcorrencia;
	}
	public void setTipoOcorrencia(TipoOcorrenciaAtividadeEnum tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}*/

	public List<String> getInfoAtencao() {
		return infoAtencao;
	}

	public void setInfoAtencao(List<String> infoAtencao) {
		this.infoAtencao = infoAtencao;
	}

	public List<String> getInfoErro() {
		return infoErro;
	}

	public void setInfoErro(List<String> infoErro) {
		this.infoErro = infoErro;
	}

	public TipoPreenchimentoAtividadeEnum getTipoPreenchimento() {
		if (tipoPreenchimento==null) return TipoPreenchimentoAtividadeEnum.VARIAVEL;
		return tipoPreenchimento;
	}

	public void setTipoPreenchimento(TipoPreenchimentoAtividadeEnum tipoPreenchimento) {
		this.tipoPreenchimento = tipoPreenchimento;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Boolean getRevisado() {
		if (revisado==null) return false;
		return revisado;
	}

	public void setRevisado(Boolean revisado) {
		this.revisado = revisado;
	}

	@Transient
	public Long getDuracao() {
		if (getFim() == null || getInicio() == null) return new Long(0);
		return (getFim().getTime() - getInicio().getTime()) / 1000 / 60;
	}

	@Transient
	public Integer getPorcentagemReal() {
		if (getPorcentagem()!=null) return getPorcentagem();
		return getTipoAtividade().getPorcentagem();
	}


	public Integer getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Integer porcentagem) {
		this.porcentagem = porcentagem;
	}

}