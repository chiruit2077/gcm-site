package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.EventHandler;

public interface PlaceUpdateEventHandler extends EventHandler {
	 void onUpdatePlace(PlaceUpdateEvent event);
}