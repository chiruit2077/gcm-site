package br.com.ecc.client.core.event;

import br.com.ecc.model.Usuario;

import com.google.gwt.event.shared.GwtEvent;

public class LogoutEvent extends GwtEvent<LogoutEventHandler>{
	public static Type<LogoutEventHandler> TYPE = new Type<LogoutEventHandler>();

	private Usuario usuario;

	@Override
	protected void dispatch(LogoutEventHandler handler) {
		handler.onLogout(this);
	}
	@Override
	public Type<LogoutEventHandler> getAssociatedType() {
		return TYPE;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Usuario getUsuario() {
		return usuario;
	}
}