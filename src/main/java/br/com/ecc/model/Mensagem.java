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
import javax.persistence.Version;

import br.com.ecc.model.tipo.TipoMensagemEnum;

@Entity
@SequenceGenerator(name="SQ_MENSAGEM", sequenceName="SQ_MENSAGEM")
@NamedQueries({
	@NamedQuery(name="mensagem.porGrupo", query="select u from Mensagem u where u.grupo = :grupo order by u.data desc"),
	@NamedQuery(name="mensagem.porGrupoTipoEspecial", query="select u from Mensagem u where u.grupo = :grupo and u.tipoMensagem != 'NORMAL' order by u.data desc"),
	@NamedQuery(name="mensagem.porDestinatario", 
		query="Select distinct m.mensagem " +
				"from MensagemDestinatario m " +
				"where m.casal = :casal or" +
				"      m.pessoa = :pessoa " +
				"order by m.mensagem.data desc")
})
public class Mensagem extends _WebBaseEntity {
	private static final long serialVersionUID = 4674613540390014487L;

	@Id
	@GeneratedValue(generator="SQ_MENSAGEM", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@Column(length=254)
	private String titulo;
	
	@Column(length=254)
	private String descricao;
	
	@Column(columnDefinition="TEXT")
	private char[] mensagem;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoMensagemEnum tipoMensagem;
	
	@Version
	private Integer version;
	
	@Override
	public String toString() {
		String ret = "";
		if(descricao!=null && !descricao.equals("")){
			ret += descricao + " - ";
		}
		return ret + titulo;
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
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public char[] getMensagem() {
		return mensagem;
	}
	public void setMensagem(char[] mensagem) {
		this.mensagem = mensagem;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

	public TipoMensagemEnum getTipoMensagem() {
		return tipoMensagem;
	}

	public void setTipoMensagem(TipoMensagemEnum tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}