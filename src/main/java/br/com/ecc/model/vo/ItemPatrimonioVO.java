package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.ItemPatrimonio;
import br.com.ecc.model.ItemPatrimonioAtividade;

public class ItemPatrimonioVO implements Serializable {
	
	private static final long serialVersionUID = -9205689773635822364L;
	
	private ItemPatrimonio itemPatrimonio;
	private List<ItemPatrimonioAtividade> listaItemPatrimonioAtividade;
	
	public List<ItemPatrimonioAtividade> getListaItemPatrimonioAtividade() {
		return listaItemPatrimonioAtividade;
	}
	public void setListaItemPatrimonioAtividade(
			List<ItemPatrimonioAtividade> listaItemPatrimonioAtividade) {
		this.listaItemPatrimonioAtividade = listaItemPatrimonioAtividade;
	}
	public ItemPatrimonio getItemPatrimonio() {
		return itemPatrimonio;
	}
	public void setItemPatrimonio(ItemPatrimonio itemPatrimonio) {
		this.itemPatrimonio = itemPatrimonio;
	}

	
	

}