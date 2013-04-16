package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.GwtEvent;

public class ChangeEncontroEvent extends GwtEvent<ChangeEncontroEventHandler>{

	public static Type<ChangeEncontroEventHandler> TYPE = new Type<ChangeEncontroEventHandler>();

	@Override
	public Type<ChangeEncontroEventHandler> getAssociatedType() {
		return TYPE;
	}
	@Override
	protected void dispatch(ChangeEncontroEventHandler handler) {
		handler.onChange(this);
	}
}