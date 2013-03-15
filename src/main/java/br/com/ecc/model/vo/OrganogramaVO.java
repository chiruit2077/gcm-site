package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Organograma;
import br.com.ecc.model.OrganogramaArea;
import br.com.ecc.model.OrganogramaCoordenacao;

public class OrganogramaVO implements Serializable {

	private static final long serialVersionUID = 3838649589869379654L;

	private Organograma organograma;
	private List<OrganogramaArea> listaAreas;
	private List<OrganogramaCoordenacao> listaCoordenacoes;

	public Organograma getOrganograma() {
		return organograma;
	}
	public void setOrganograma(Organograma organograma) {
		this.organograma = organograma;
	}
	public List<OrganogramaCoordenacao> getListaCoordenacoes() {
		return listaCoordenacoes;
	}
	public void setListaCoordenacoes(List<OrganogramaCoordenacao> listaCoordenacoes) {
		this.listaCoordenacoes = listaCoordenacoes;
	}
	public List<OrganogramaArea> getListaAreas() {
		return listaAreas;
	}
	public void setListaAreas(List<OrganogramaArea> listaAreas) {
		this.listaAreas = listaAreas;
	}

}