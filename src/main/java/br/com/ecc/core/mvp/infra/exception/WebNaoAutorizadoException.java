package br.com.ecc.core.mvp.infra.exception;

import java.util.List;

public class WebNaoAutorizadoException extends WebSecurityException{

	private static final long serialVersionUID = 6726472197113035188L;

	private String permissao;
	private String operacao;
	
	public WebNaoAutorizadoException() {
		super();
	}

	public WebNaoAutorizadoException(Exception e) {
		super(e);
	}

	public WebNaoAutorizadoException(List<WebMessage> messages) {
		super(messages);
	}

	public WebNaoAutorizadoException(String message, Exception e) {
		super(message, e);
	}

	public WebNaoAutorizadoException(String message) {
		super(message);
	}

	public String getPermissao() {
		return permissao;
	}

	public void setPermissao(String permissao) {
		this.permissao = permissao;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

}
