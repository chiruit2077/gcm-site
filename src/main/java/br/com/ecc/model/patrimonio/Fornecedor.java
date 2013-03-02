package br.com.ecc.model.patrimonio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.SequenceGenerator;

import br.com.ecc.model._WebBaseEntity;

@Entity
@SequenceGenerator(name="SQ_FORNECEDOR", sequenceName="SQ_FORNECEDOR")
@NamedQueries({
})
public class Fornecedor extends _WebBaseEntity {

	private static final long serialVersionUID = -7157439077596915710L;

	@Id
	@GeneratedValue(generator="SQ_FORNECEDOR", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String nome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
