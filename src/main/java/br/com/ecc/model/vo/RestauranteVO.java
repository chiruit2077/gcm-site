package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Mesa;
import br.com.ecc.model.Restaurante;
import br.com.ecc.model.RestauranteGrupo;
import br.com.ecc.model.RestauranteTitulo;

public class RestauranteVO implements Serializable {

	private static final long serialVersionUID = -2710310820053031392L;

	private Restaurante restaurante;
	private List<Mesa> listaMesas;
	private List<RestauranteGrupo> listaGrupos;
	private List<RestauranteTitulo> listaTitulos;

	public Restaurante getRestaurante() {
		return restaurante;
	}
	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}
	public List<Mesa> getListaMesas() {
		return listaMesas;
	}
	public void setListaMesas(List<Mesa> listaMesas) {
		this.listaMesas = listaMesas;
	}
	public List<RestauranteGrupo> getListaGrupos() {
		return listaGrupos;
	}
	public void setListaGrupos(List<RestauranteGrupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}
	public List<RestauranteTitulo> getListaTitulos() {
		return listaTitulos;
	}
	public void setListaTitulos(List<RestauranteTitulo> listaTitulos) {
		this.listaTitulos = listaTitulos;
	}



}