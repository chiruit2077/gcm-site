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
@SequenceGenerator(name="SQ_ITEMPATRIMONIOATIV", sequenceName="SQ_ITEMPATRIMONIOATIV")
@NamedQueries({
	@NamedQuery(name="itemPatrimonioAtividade.porItemPatrimonio", query="select u from ItemPatrimonioAtividade u where u.itemPatrimonio = :itemPatrimonio"),
	@NamedQuery(name="itemPatrimonioAtividade.deletePorItemPatrimonio", 
		query="delete from ItemPatrimonioAtividade u where u.itemPatrimonio = :itemPatrimonio "),
	@NamedQuery(name="itemPatrimonioAtividade.deletePorItemPatrimonioNotIn", 
		query="delete from ItemPatrimonioAtividade u where u.itemPatrimonio = :itemPatrimonio and u not in (:lista)")
})
public class ItemPatrimonioAtividade extends _WebBaseEntity {
	private static final long serialVersionUID = 8594894241968176133L;

	@Id
	@GeneratedValue(generator="SQ_ITEMPATRIMONIOATIV", strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="itemPatrimonio")
	private ItemPatrimonio itemPatrimonio;
	
	@ManyToOne
	@JoinColumn(name="atividade")
	private Atividade atividade;

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
	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}
	public Atividade getAtividade() {
		return atividade;
	}
}