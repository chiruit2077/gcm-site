package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Caixa;
import br.com.ecc.model.CaixaItem;

public class CaixaVO implements Serializable {
	private static final long serialVersionUID = 3068432785578522653L;
	
	private Caixa caixa;
	private List<CaixaItem> listaCaixaItem;
	
	
	public Caixa getCaixa() {
		return caixa;
	}
	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}
	public List<CaixaItem> getListaCaixaItem() {
		return listaCaixaItem;
	}
	public void setListaCaixaItem(List<CaixaItem> listaCaixaItem) {
		this.listaCaixaItem = listaCaixaItem;
	}
	
	
}