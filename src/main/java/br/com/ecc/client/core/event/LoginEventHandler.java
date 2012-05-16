package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.EventHandler;

public interface LoginEventHandler extends EventHandler{
	
	public void onProcessa(LoginEvent event);
	
}