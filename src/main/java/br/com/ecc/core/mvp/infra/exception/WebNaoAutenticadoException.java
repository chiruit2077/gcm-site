package br.com.ecc.core.mvp.infra.exception;

import java.util.List;

public class WebNaoAutenticadoException extends WebSecurityException{

	private static final long serialVersionUID = 7404726467167472751L;

	public WebNaoAutenticadoException() {
		super();
	}

	public WebNaoAutenticadoException(Exception e) {
		super(e);
	}

	public WebNaoAutenticadoException(List<WebMessage> messages) {
		super(messages);
	}

	public WebNaoAutenticadoException(String message, Exception e) {
		super(message, e);
	}

	public WebNaoAutenticadoException(String message) {
		super(message);
	}

}
