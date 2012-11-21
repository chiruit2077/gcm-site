package br.com.ecc.client.ui.component.upload.event;

import com.google.gwt.event.shared.GwtEvent;

public class UploadEvent extends GwtEvent<UploadEventHandler>{

	
	public static Type<UploadEventHandler> TYPE = new Type<UploadEventHandler>();
	
	@Override
	protected void dispatch(UploadEventHandler handler) {
		handler.onComplete(this);
	}
	@Override
	public Type<UploadEventHandler> getAssociatedType() {
		return TYPE;
	}
}