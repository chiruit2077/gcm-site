package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Agenda;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Recado;

public class InicioVO implements Serializable {
	private static final long serialVersionUID = 8841414858600015782L;

	private List<Recado> listaRecados;
	private List<AniversarianteVO> listaAniversarioPessoa;
	private List<AniversarianteVO> listaAniversarioCasal;
	private List<Casal> listaConvidados;
	private List<Agenda> listaAgenda;

	public List<Recado> getListaRecados() {
		return listaRecados;
	}
	public void setListaRecados(List<Recado> listaRecados) {
		this.listaRecados = listaRecados;
	}
	public List<Casal> getListaConvidados() {
		return listaConvidados;
	}
	public void setListaConvidados(List<Casal> listaConvidados) {
		this.listaConvidados = listaConvidados;
	}
	public List<AniversarianteVO> getListaAniversarioPessoa() {
		return listaAniversarioPessoa;
	}
	public void setListaAniversarioPessoa(List<AniversarianteVO> listaAniversarioPessoa) {
		this.listaAniversarioPessoa = listaAniversarioPessoa;
	}
	public List<AniversarianteVO> getListaAniversarioCasal() {
		return listaAniversarioCasal;
	}
	public void setListaAniversarioCasal(List<AniversarianteVO> listaAniversarioCasal) {
		this.listaAniversarioCasal = listaAniversarioCasal;
	}
	public List<Agenda> getListaAgenda() {
		return listaAgenda;
	}
	public void setListaAgenda(List<Agenda> listaAgenda) {
		this.listaAgenda = listaAgenda;
	}
}