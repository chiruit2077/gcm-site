package br.com.ecc.core.mvp.infra.exception;

import java.util.List;

public class WebSecurityException extends WebException{

	private static final long serialVersionUID = 7841745281751721079L;

	public WebSecurityException(){}
	
	public WebSecurityException(Exception e){
		super(e);
	}
	public WebSecurityException(String message, Exception e){
		super(message, e);
	}
	public WebSecurityException(String message){
		super(message);
	}
	public WebSecurityException(List<WebMessage> messages){
		super(messages);
	}
	
}
