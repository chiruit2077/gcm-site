package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.GwtEvent;

public class ChangeGrupoEvent extends GwtEvent<ChangeGrupoEventHandler>{

	public static Type<ChangeGrupoEventHandler> TYPE = new Type<ChangeGrupoEventHandler>();

	@Override
	public Type<ChangeGrupoEventHandler> getAssociatedType() {
		return TYPE;
	}
	@Override
	protected void dispatch(ChangeGrupoEventHandler handler) {
		handler.onChange(this);
	}
}