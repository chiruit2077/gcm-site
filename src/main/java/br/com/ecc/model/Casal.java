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

import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoSituacaoEnum;

@Entity
@SequenceGenerator(name="SQ_CASAL", sequenceName="SQ_CASAL")
@NamedQueries({
	@NamedQuery(name="casal.todos", query="select u from Casal u order by u.ele.apelido, u.ela.apelido"),
	@NamedQuery(name="casal.porGrupo", query="select u from Casal u where u.grupo = :grupo order by u.ele.apelido, u.ela.apelido"),
	@NamedQuery(name="casal.porPessoa", query="select u from Casal u where u.ele = :pessoa or u.ela =:pessoa"),
	@NamedQuery(name="casal.porGrupoNomeLike", 
	query="select u from Casal u " +
		  "where u.grupo = :grupo and " +
		  "      ( upper(u.ele.nome) like upper(:key) or " +
		  "        upper(u.ele.apelido) like upper(:key) or " +
		  "        upper(u.ela.nome) like upper(:key) or" +
		  "        upper(u.ela.apelido) like upper(:key) ) " +
		  "order by u.ele.apelido, u.ela.apelido, u.ele.nome, u.ela.nome" ),
	@NamedQuery(name="casal.porNomeLike", 
		query="select u from Casal u " +
			  "where u.situacao = 'ATIVO' and " +
			  "      ( upper(u.ele.nome) like upper(:key) or " +
			  "        upper(u.ele.apelido) like upper(:key) or " +
			  "        upper(u.ela.nome) like upper(:key) or" +
			  "        upper(u.ela.apelido) like upper(:key) ) " +
			  "order by u.ele.apelido, u.ela.apelido, u.ele.nome, u.ela.nome" )
})
public class Casal extends _WebBaseEntity {
	private static final long serialVersionUID = 3843022732976850640L;

	@Id
	@GeneratedValue(generator="SQ_CASAL", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="grupo")
	private Grupo grupo;
	
	@ManyToOne
	@JoinColumn(name="ele")
	private Pessoa ele;
	
	@ManyToOne
	@JoinColumn(name="ela")
	private Pessoa ela;
	
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
	
	@Column(length=50)
	private String telefone;
	
	@Temporal(TemporalType.DATE)
	private Date casamento;
	
	@Column(length=50)
	private String cor;
	
	@Column(length=250)
	private String lugar;
	
	@Column(length=250)
	private String atividade;
	
	@Column(length=250)
	private String musica;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoSituacaoEnum situacao;
	
	private Integer idArquivoDigital;
	
	@ManyToOne
	@JoinColumn(name="casalPadrinho")
	private Casal casalPadrinho;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date atualizacaoCadastro;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private TipoCasalEnum tipoCasal;
	
	@Version
	private Integer version;
	
	@Override
	public String toString() {
		String r = "";
		r += ele.getNome();
		r += " / ";
		r += ela.getNome();
		return r;
	}
	public String getApelidos(String separador) {
		String r = "";
		if(ele.getApelido()!=null && !ele.getApelido().equals("")){
			r += ele.getApelido();
		} else {
			r += ele.getNome();
		}
		if(separador!=null){
			r += " " + separador + " ";
		} else {
			r += " ";
		}
		if(ela.getApelido()!=null && !ela.getApelido().equals("")){
			r += ela.getApelido();
		} else {
			r += ela.getNome();
		}
		return r;
	}
	public String getEmails(String separador) {
		String r = "";
		if(ele.getEmail()!=null && !ele.getEmail().equals("")){
			r += ele.getEmail();
		}
		if(separador!=null){
			r += separador + " ";
		} else {
			r += " ";
		}
		if(ela.getEmail()!=null && !ela.getEmail().equals("")){
			r += ela.getEmail();
		}
		return r;
	}
	
	
	public Date getCasamento() {
		return casamento;
	}
	public void setCasamento(Date casamento) {
		this.casamento = casamento;
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
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
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
	public Pessoa getEle() {
		return ele;
	}
	public void setEle(Pessoa ele) {
		this.ele = ele;
	}
	public Pessoa getEla() {
		return ela;
	}
	public void setEla(Pessoa ela) {
		this.ela = ela;
	}
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	public TipoSituacaoEnum getSituacao() {
		return situacao;
	}
	public void setSituacao(TipoSituacaoEnum situacao) {
		this.situacao = situacao;
	}
	public Integer getIdArquivoDigital() {
		return idArquivoDigital;
	}
	public void setIdArquivoDigital(Integer idArquivoDigital) {
		this.idArquivoDigital = idArquivoDigital;
	}
	public Casal getCasalPadrinho() {
		return casalPadrinho;
	}
	public void setCasalPadrinho(Casal casalPadrinho) {
		this.casalPadrinho = casalPadrinho;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getCor() {
		return cor;
	}
	public void setCor(String cor) {
		this.cor = cor;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getAtividade() {
		return atividade;
	}
	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}
	public String getMusica() {
		return musica;
	}
	public void setMusica(String musica) {
		this.musica = musica;
	}
	public Date getAtualizacaoCadastro() {
		return atualizacaoCadastro;
	}
	public void setAtualizacaoCadastro(Date atualizacaoCadastro) {
		this.atualizacaoCadastro = atualizacaoCadastro;
	}
	public TipoCasalEnum getTipoCasal() {
		return tipoCasal;
	}
	public void setTipoCasal(TipoCasalEnum tipoCasal) {
		this.tipoCasal = tipoCasal;
	}
}