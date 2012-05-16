package br.com.ecc.client.ui.component.upload.event;

import com.google.gwt.event.shared.EventHandler;

public interface UploadEventHandler extends EventHandler{
	
	public void onComplete(UploadEvent event);
}