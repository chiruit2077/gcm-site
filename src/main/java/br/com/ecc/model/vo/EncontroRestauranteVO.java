package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.EncontroRestauranteMesa;
import br.com.ecc.model.Mesa;

public class EncontroRestauranteVO implements Serializable {

	private static final long serialVersionUID = -5368823597085904402L;

	private EncontroRestaurante encontroRestaurante;
	private List<Mesa> listaMesas;
	private List<EncontroRestauranteMesa> listaEncontroRestauranteMesa;
	private List<EncontroRestauranteMesa> listaEncontroRestauranteMesaOutros;
	private List<AgrupamentoVO> listaAgrupamentosVO;
	private List<EncontroInscricao> listaAfilhados;
	private List<EncontroInscricao> listaEncontristas;
	public EncontroRestaurante getEncontroRestaurante() {
		return encontroRestaurante;
	}
	public void setEncontroRestaurante(EncontroRestaurante encontroRestaurante) {
		this.encontroRestaurante = encontroRestaurante;
	}
	public List<Mesa> getListaMesas() {
		return listaMesas;
	}
	public void setListaMesas(List<Mesa> listaMesas) {
		this.listaMesas = listaMesas;
	}
	public List<EncontroRestauranteMesa> getListaEncontroRestauranteMesa() {
		return listaEncontroRestauranteMesa;
	}
	public void setListaEncontroRestauranteMesa(
			List<EncontroRestauranteMesa> listaEncontroRestauranteGarcon) {
		this.listaEncontroRestauranteMesa = listaEncontroRestauranteGarcon;
	}
	public List<AgrupamentoVO> getListaAgrupamentosVO() {
		return listaAgrupamentosVO;
	}
	public void setListaAgrupamentosVO(List<AgrupamentoVO> listaAgrupamentosVO) {
		this.listaAgrupamentosVO = listaAgrupamentosVO;
	}
	public List<EncontroInscricao> getListaAfilhados() {
		return listaAfilhados;
	}
	public void setListaAfilhados(List<EncontroInscricao> listaAfilhados) {
		this.listaAfilhados = listaAfilhados;
	}
	public List<EncontroInscricao> getListaEncontristas() {
		return listaEncontristas;
	}
	public void setListaEncontristas(List<EncontroInscricao> listaEncontristas) {
		this.listaEncontristas = listaEncontristas;
	}
	public List<EncontroRestauranteMesa> getListaEncontroRestauranteMesaOutros() {
		return listaEncontroRestauranteMesaOutros;
	}
	public void setListaEncontroRestauranteMesaOutros(
			List<EncontroRestauranteMesa> listaEncontroRestauranteMesaOutros) {
		this.listaEncontroRestauranteMesaOutros = listaEncontroRestauranteMesaOutros;
	}


}