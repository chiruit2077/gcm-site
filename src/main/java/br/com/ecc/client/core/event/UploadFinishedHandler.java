package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.EventHandler;

public interface UploadFinishedHandler extends EventHandler{
	
	public void onUploadFinished(UploadFinishedEvent event);
	
}
