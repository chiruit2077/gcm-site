package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.EventHandler;

public interface WaitEventHandler extends EventHandler{
	
	public void onWait(WaitEvent event);
	
}