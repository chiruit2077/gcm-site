package br.com.ecc.core.mvp.infra.exception;

public class WebRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 2291734016293245888L;

	public WebRuntimeException() {
		super();
	}

	public WebRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebRuntimeException(String message) {
		super(message);
	}

	public WebRuntimeException(Throwable cause) {
		super(cause);
	}
	
}
