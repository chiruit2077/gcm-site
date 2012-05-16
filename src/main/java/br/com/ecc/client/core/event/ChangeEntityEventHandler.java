package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.EventHandler;

public interface ChangeEntityEventHandler extends EventHandler{
	
	public void onChange(ChangeEntityEvent event);
	
}