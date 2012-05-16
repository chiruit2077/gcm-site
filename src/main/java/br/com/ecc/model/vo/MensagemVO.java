package br.com.ecc.model.vo;

import java.io.Serializable;
import java.util.List;

import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;

public class MensagemVO implements Serializable {
	private static final long serialVersionUID = -8018813370380321849L;
	
	private Mensagem mensagem;
	private List<MensagemDestinatario> listaDestinatarios;

	public List<MensagemDestinatario> getListaDestinatarios() {
		return listaDestinatarios;
	}
	public void setListaDestinatarios(List<MensagemDestinatario> listaDestinatarios) {
		this.listaDestinatarios = listaDestinatarios;
	}
	public Mensagem getMensagem() {
		return mensagem;
	}
	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}

}