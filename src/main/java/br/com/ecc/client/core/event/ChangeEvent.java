package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.GwtEvent;

public class ChangeEvent extends GwtEvent<ChangeEventHandler>{

	
	public static Type<ChangeEventHandler> TYPE = new Type<ChangeEventHandler>();

	@Override
	public Type<ChangeEventHandler> getAssociatedType() {
		return TYPE;
	}
	@Override
	protected void dispatch(ChangeEventHandler handler) {
		handler.onChange(this);
	}
}