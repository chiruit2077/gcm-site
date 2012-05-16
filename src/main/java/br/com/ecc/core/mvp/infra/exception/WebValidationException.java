package br.com.ecc.core.mvp.infra.exception;

import java.util.List;

public class WebValidationException extends WebException{

	private static final long serialVersionUID = 5294993157411133756L;

	public WebValidationException(){}
	public WebValidationException(Exception e){
		super(e);
	}
	public WebValidationException(String message, Exception e){
		super(message, e);
	}
	/*
	public PortalValidationException(String key, String message){
		super(key, message);
	}
	*/
	public WebValidationException(List<WebMessage> messages ){
		super(messages);
	}

}
