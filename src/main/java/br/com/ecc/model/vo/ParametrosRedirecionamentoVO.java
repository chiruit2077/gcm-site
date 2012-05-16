package br.com.ecc.model.vo;

import java.io.Serializable;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.Pessoa;

public class ParametrosRedirecionamentoVO implements Serializable {
	private static final long serialVersionUID = 8693823582019078801L;
	
	private Casal casal;
	private Pessoa pessoa;
	private Mensagem mensagem;
	private PresenterCodeEnum presenterCode;
	
	public Mensagem getMensagem() {
		return mensagem;
	}
	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}
	public PresenterCodeEnum getPresenterCode() {
		return presenterCode;
	}
	public void setPresenterCode(PresenterCodeEnum presenterCode) {
		this.presenterCode = presenterCode;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}