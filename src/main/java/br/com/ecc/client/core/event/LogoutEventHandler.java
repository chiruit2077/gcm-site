package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.EventHandler;

public interface LogoutEventHandler extends EventHandler{

	public void onLogout(LogoutEvent logoutEvent);
}