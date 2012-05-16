package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.AgrupamentoMembro;

public class AgrupamentoVO implements Serializable {
	private static final long serialVersionUID = -2270260604866527200L;
	
	private Agrupamento agrupamento;
	private List<AgrupamentoMembro> listaMembros;
	
	public Agrupamento getAgrupamento() {
		return agrupamento;
	}
	public void setAgrupamento(Agrupamento agrupamento) {
		this.agrupamento = agrupamento;
	}
	public List<AgrupamentoMembro> getListaMembros() {
		return listaMembros;
	}
	public void setListaMembros(List<AgrupamentoMembro> listaMembros) {
		this.listaMembros = listaMembros;
	}
}