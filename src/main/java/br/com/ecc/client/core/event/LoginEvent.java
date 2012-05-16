package br.com.ecc.client.core.event;

import br.com.ecc.model.Usuario;

import com.google.gwt.event.shared.GwtEvent;

public class LoginEvent extends GwtEvent<LoginEventHandler>{

	private Usuario usuarioLS;
	private Integer presenterCode;
	
	public static Type<LoginEventHandler> TYPE = new Type<LoginEventHandler>();
	
	public Usuario getUsuarioLS() {
		return usuarioLS;
	}
	public void setUsuarioLS(Usuario usuarioLS) {
		this.usuarioLS = usuarioLS;
	}
	public Integer getPresenterCode() {
		return presenterCode;
	}
	public void setPresenterCode(Integer presenterCode) {
		this.presenterCode = presenterCode;
	}

	@Override
	public Type<LoginEventHandler> getAssociatedType() {
		return TYPE;
	}
	@Override
	protected void dispatch(LoginEventHandler handler) {
		handler.onProcessa(this);
	}
}