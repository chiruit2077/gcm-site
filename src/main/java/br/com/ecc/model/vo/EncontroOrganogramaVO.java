package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.EncontroOrganogramaArea;
import br.com.ecc.model.EncontroOrganogramaCoordenacao;
import br.com.ecc.model.OrganogramaArea;
import br.com.ecc.model.OrganogramaCoordenacao;

public class EncontroOrganogramaVO implements Serializable {

	private static final long serialVersionUID = -3433536363178317315L;

	private EncontroOrganograma encontroOrganograma;
	private List<OrganogramaArea> listaOrganogramaArea;
	private List<OrganogramaCoordenacao> listaCoordenacao;
	private List<EncontroOrganogramaArea> listaEncontroOrganogramaArea;
	private List<EncontroOrganogramaCoordenacao> listaEncontroOrganogramaCoordenacao;
	public EncontroOrganograma getEncontroOrganograma() {
		return encontroOrganograma;
	}
	public void setEncontroOrganograma(EncontroOrganograma encontroOrganograma) {
		this.encontroOrganograma = encontroOrganograma;
	}
	public List<OrganogramaArea> getListaOrganogramaArea() {
		return listaOrganogramaArea;
	}
	public void setListaOrganogramaArea(List<OrganogramaArea> listaOrganogramaArea) {
		this.listaOrganogramaArea = listaOrganogramaArea;
	}
	public List<OrganogramaCoordenacao> getListaCoordenacao() {
		return listaCoordenacao;
	}
	public void setListaCoordenacao(List<OrganogramaCoordenacao> listaCoordenacao) {
		this.listaCoordenacao = listaCoordenacao;
	}
	public List<EncontroOrganogramaArea> getListaEncontroOrganogramaArea() {
		return listaEncontroOrganogramaArea;
	}
	public void setListaEncontroOrganogramaArea(
			List<EncontroOrganogramaArea> listaEncontroOrganogramaArea) {
		this.listaEncontroOrganogramaArea = listaEncontroOrganogramaArea;
	}
	public List<EncontroOrganogramaCoordenacao> getListaEncontroOrganogramaCoordenacao() {
		return listaEncontroOrganogramaCoordenacao;
	}
	public void setListaEncontroOrganogramaCoordenacao(
			List<EncontroOrganogramaCoordenacao> listaEncontroOrganogramaCoordenacao) {
		this.listaEncontroOrganogramaCoordenacao = listaEncontroOrganogramaCoordenacao;
	}


}