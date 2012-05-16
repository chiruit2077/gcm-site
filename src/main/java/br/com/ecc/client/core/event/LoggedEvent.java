package br.com.ecc.client.core.event;

import br.com.ecc.model.Usuario;

import com.google.gwt.event.shared.GwtEvent;

public class LoggedEvent extends GwtEvent<LoggedEventHandler>{

	
	public static Type<LoggedEventHandler> TYPE = new Type<LoggedEventHandler>();
	private Usuario usuario;
	private Boolean paginaInicial = false;
	
	@Override
	protected void dispatch(LoggedEventHandler handler) {
		handler.onLogged(this);
	}
	@Override
	public Type<LoggedEventHandler> getAssociatedType() {
		return TYPE;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Boolean getPaginaInicial() {
		return paginaInicial;
	}
	public void setPaginaInicial(Boolean paginaInicial) {
		this.paginaInicial = paginaInicial;
	}
}