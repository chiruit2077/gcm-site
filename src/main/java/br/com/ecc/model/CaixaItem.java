package br.com.ecc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="SQ_CAIXAITEM", sequenceName="SQ_CAIXAITEM")
@NamedQueries({
	@NamedQuery(name="caixaItem.porCaixa", query="select u from CaixaItem u where u.caixa = :caixa"),
	@NamedQuery(name="caixaItem.deletePorCaixa", query="delete from CaixaItem u where u.caixa = :caixa "),
	@NamedQuery(name="caixaItem.deletePorCaixatoNotIn", query="delete from CaixaItem u where u.caixa = :caixa and u not in (:lista)")
})
public class CaixaItem extends _WebBaseEntity {

	private static final long serialVersionUID = 3898984795183127605L;

	@Id
	@GeneratedValue(generator="SQ_CAIXAITEM", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="caixa")
	private Caixa caixa;
	
	@ManyToOne
	@JoinColumn(name="itemPatrimonio")
	private ItemPatrimonio itemPatrimonio;
	
	private Integer quantidade;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ItemPatrimonio getItemPatrimonio() {
		return itemPatrimonio;
	}
	public void setItemPatrimonio(ItemPatrimonio itemPatrimonio) {
		this.itemPatrimonio = itemPatrimonio;
	}
	public Caixa getCaixa() {
		return caixa;
	}
	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}