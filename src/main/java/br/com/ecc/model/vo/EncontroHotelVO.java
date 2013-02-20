package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroHotelQuarto;
import br.com.ecc.model.HotelAgrupamento;
import br.com.ecc.model.Quarto;

public class EncontroHotelVO implements Serializable {

	private static final long serialVersionUID = -4597813444263505728L;

	private EncontroHotel encontroHotel;
	private List<HotelAgrupamento> listaAgrupamentos;
	private List<Quarto> listaQuartos;
	private List<EncontroHotelQuarto> listaEncontroQuartos;
	public EncontroHotel getEncontroHotel() {
		return encontroHotel;
	}
	public void setEncontroHotel(EncontroHotel encontroHotel) {
		this.encontroHotel = encontroHotel;
	}
	public List<HotelAgrupamento> getListaAgrupamentos() {
		return listaAgrupamentos;
	}
	public void setListaAgrupamentos(List<HotelAgrupamento> listaAgrupamentos) {
		this.listaAgrupamentos = listaAgrupamentos;
	}
	public List<Quarto> getListaQuartos() {
		return listaQuartos;
	}
	public void setListaQuartos(List<Quarto> listaQuartos) {
		this.listaQuartos = listaQuartos;
	}
	public List<EncontroHotelQuarto> getListaEncontroQuartos() {
		return listaEncontroQuartos;
	}
	public void setListaEncontroQuartos(List<EncontroHotelQuarto> listaEncontroQuartos) {
		this.listaEncontroQuartos = listaEncontroQuartos;
	}

}