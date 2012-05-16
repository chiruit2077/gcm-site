package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.GwtEvent;

public class UploadFinishedEvent extends GwtEvent<UploadFinishedHandler>{

	private String result; 
	
	public static Type<UploadFinishedHandler> TYPE = new Type<UploadFinishedHandler>();

	public UploadFinishedEvent(String result) {
		this.result = result;
	}
	
	@Override
	protected void dispatch(UploadFinishedHandler handler) {
		handler.onUploadFinished(this);
	}

	@Override
	public Type<UploadFinishedHandler> getAssociatedType() {
		return TYPE;
	}

	public String getResult() {
		return result;
	}

}
