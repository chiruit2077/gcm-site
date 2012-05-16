package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.EncontroTotalizacao;
import br.com.ecc.model.EncontroTotalizacaoAtividade;

public class EncontroTotalizacaoVO implements Serializable {
	private static final long serialVersionUID = 2886623058958821812L;
	
	private EncontroTotalizacao encontroTotalizacao;
	private List<EncontroTotalizacaoAtividade> listaAtividade;
	
	public EncontroTotalizacao getEncontroTotalizacao() {
		return encontroTotalizacao;
	}
	public void setEncontroTotalizacao(EncontroTotalizacao encontroTotalizacao) {
		this.encontroTotalizacao = encontroTotalizacao;
	}
	public List<EncontroTotalizacaoAtividade> getListaAtividade() {
		return listaAtividade;
	}
	public void setListaAtividade(List<EncontroTotalizacaoAtividade> listaAtividade) {
		this.listaAtividade = listaAtividade;
	}
}