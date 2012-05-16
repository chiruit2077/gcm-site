package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.EventHandler;

public interface LoggedEventHandler extends EventHandler{
	
	public void onLogged(LoggedEvent event);
	
}
