package br.com.ecc.core.mvp.infra.exception;

import java.util.ArrayList;
import java.util.List;

public class WebException extends Exception{

	private static final long serialVersionUID = -1458882762697237670L;

	private List<WebMessage> messages = new ArrayList<WebMessage>();
	
	public WebException(){}
	
	public WebException(Exception e){
		super(e);
	}
	public WebException(String message, Throwable e){
		super(message, e);
	}
	/*
	public PortalException(String key, String message){
		this.messages.add(new PortalMessage(key, message));
	}*/
	public WebException(List<WebMessage> messages ){
		this.messages=messages;
	}
	
	public WebException(String message) {
		super(message);
	}

	public List<WebMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<WebMessage> messages) {
		this.messages = messages;
	}
}