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

import br.com.ecc.model.tipo.TipoAtividadeItemEnum;

@Entity
@SequenceGenerator(name="SQ_ATIVIDADEITEM", sequenceName="SQ_ATIVIDADEITEM")
@NamedQueries({
	@NamedQuery(name="atividadeItem.porAtividade", query="select u from AtividadeItem u where u.atividade = :atividade order by u.tipo")
})
public class AtividadeItem extends _WebBaseEntity {

	private static final long serialVersionUID = -7660765423256052049L;

	@Id
	@GeneratedValue(generator="SQ_ATIVIDADEITEM", strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="atividade")
	private Atividade atividade;
	
	@Column(columnDefinition="TEXT")
	private char[] texto;
	
	@Enumerated(EnumType.STRING)
	private TipoAtividadeItemEnum tipo;

	@Version
	private Integer version;


	public Integer getId() {
		return id;
	}

	public char[] getTexto() {
		return texto;
	}

	public Integer getVersion() {
		return version;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTexto(char[] texto) {
		this.texto = texto;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public TipoAtividadeItemEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoAtividadeItemEnum tipo) {
		this.tipo = tipo;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}


	

}