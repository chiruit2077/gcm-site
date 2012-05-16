package br.com.ecc.client.core.mvp;

import br.com.ecc.client.core.event.LoginEvent;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.core.mvp.infra.exception.WebNaoAutenticadoException;
import br.com.ecc.core.mvp.infra.exception.WebRuntimeException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class WebAsyncCallback <R> implements AsyncCallback<R>{
	
	BaseDisplay display;
	
	public WebAsyncCallback(BaseDisplay display) {
		super();
		this.display = display;
	}
	
	@Override
	public void onSuccess(R result) {
		success(result);
	}
	protected abstract void success(R result);

	@Override
	public void onFailure(Throwable e) {
		try {
			if (e instanceof WebNaoAutenticadoException){
				LoginEvent event = new LoginEvent();
				display.getPresenter().getWebResource().getEventBus().fireEvent(event);
			} else if (e instanceof WebException){
				//display.showMessages(NotificationType.ERROR, ((ECCException)e).getMessages());
				display.showError(e.getMessage());
				//display.showWaitMessage(false);
			} else if (e instanceof WebRuntimeException){
				display.showError(e.getMessage());
				//display.showWaitMessage(false);
			} else {
				//display.showWaitMessage(false);
				GWT.log(e.getMessage());
				e.printStackTrace();
				Window.alert("Erro inesperado. Contacte seu suporte técnico. \nERRO: " + e.getMessage());
				display.reset();
			}
		}
		finally {
			display.showWaitMessage(false);
		}
	}
	
}
