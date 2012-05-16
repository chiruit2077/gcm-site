package br.com.ecc.core.mvp.infra.exception;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WebMessage implements Serializable, IsSerializable {

	private static final long serialVersionUID = -4681837326202714853L;

	public WebMessage() {
	}
	
	private String key;
	private String message;
	public String getKey() {
		return key;
	}
	
	public WebMessage(String key, String message) {
		super();
		this.key = key;
		this.message = message;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
