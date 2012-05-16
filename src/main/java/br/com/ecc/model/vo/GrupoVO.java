package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Atividade;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Papel;

public class GrupoVO implements Serializable {
	private static final long serialVersionUID = -7437331546652783194L;
	
	private List<Atividade> listaAtividade;
	private List<Papel> listaPapel;
	private List<Encontro> listaEncontro;
	private List<AgrupamentoVO> listaAgrupamentoVOGrupo;

	public List<Papel> getListaPapel() {
		return listaPapel;
	}
	public void setListaPapel(List<Papel> listaPapel) {
		this.listaPapel = listaPapel;
	}
	public List<Atividade> getListaAtividade() {
		return listaAtividade;
	}
	public void setListaAtividade(List<Atividade> listaAtividade) {
		this.listaAtividade = listaAtividade;
	}
	public List<Encontro> getListaEncontro() {
		return listaEncontro;
	}
	public void setListaEncontro(List<Encontro> listaEncontro) {
		this.listaEncontro = listaEncontro;
	}
	public List<AgrupamentoVO> getListaAgrupamentoVOGrupo() {
		return listaAgrupamentoVOGrupo;
	}
	public void setListaAgrupamentoVOGrupo(List<AgrupamentoVO> listaAgrupamentoVOGrupo) {
		this.listaAgrupamentoVOGrupo = listaAgrupamentoVOGrupo;
	}
}