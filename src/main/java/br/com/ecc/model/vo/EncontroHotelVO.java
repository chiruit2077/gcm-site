package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroHotelQuarto;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroRestauranteGarcon;
import br.com.ecc.model.HotelAgrupamento;
import br.com.ecc.model.Mesa;
import br.com.ecc.model.Quarto;

public class EncontroHotelVO implements Serializable {

	private static final long serialVersionUID = -4597813444263505728L;

	private EncontroHotel encontroHotel;
	private List<HotelAgrupamento> listaHotelAgrupamentos;
	private List<Quarto> listaQuartos;
	private List<Mesa> listaMesas;
	private List<EncontroHotelQuarto> listaEncontroQuartos;
	private List<EncontroRestauranteGarcon> listaEncontroRestauranteGarcon;
	private List<AgrupamentoVO> listaAgrupamentosVO;
	private List<EncontroInscricao> listaAfilhados;
	private List<EncontroInscricao> listaEncontristas;

	public EncontroHotel getEncontroHotel() {
		return encontroHotel;
	}
	public void setEncontroHotel(EncontroHotel encontroHotel) {
		this.encontroHotel = encontroHotel;
	}
	public List<HotelAgrupamento> getListaHotelAgrupamentos() {
		return listaHotelAgrupamentos;
	}
	public void setListaHotelAgrupamentos(List<HotelAgrupamento> listaAgrupamentos) {
		this.listaHotelAgrupamentos = listaAgrupamentos;
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
	public List<EncontroRestauranteGarcon> getListaEncontroRestauranteGarcon() {
		return listaEncontroRestauranteGarcon;
	}
	public void setListaEncontroRestauranteGarcon(
			List<EncontroRestauranteGarcon> listaEncontroRestauranteGarcon) {
		this.listaEncontroRestauranteGarcon = listaEncontroRestauranteGarcon;
	}
	public List<Mesa> getListaMesas() {
		return listaMesas;
	}
	public void setListaMesas(List<Mesa> listaMesas) {
		this.listaMesas = listaMesas;
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
}