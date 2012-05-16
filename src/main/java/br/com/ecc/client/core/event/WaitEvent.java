package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.GwtEvent;

public class WaitEvent extends GwtEvent<WaitEventHandler>{

	
	public static Type<WaitEventHandler> TYPE = new Type<WaitEventHandler>();
	private Boolean wait = false;
	
	@Override
	protected void dispatch(WaitEventHandler handler) {
		handler.onWait(this);
	}
	@Override
	public Type<WaitEventHandler> getAssociatedType() {
		return TYPE;
	}
	public Boolean getWait() {
		return wait;
	}
	public void setWait(Boolean wait) {
		this.wait = wait;
	}
}