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
@SequenceGenerator(name="SQ_LOCAL", sequenceName="SQ_LOCAL")
@NamedQueries({
})
public class Local extends _WebBaseEntity {

	private static final long serialVersionUID = 137928733744192809L;

	@Id
	@GeneratedValue(generator="SQ_LOCAL", strategy=GenerationType.AUTO)
	private Integer id;

	@Column(length=254)
	private String nome;

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

}
