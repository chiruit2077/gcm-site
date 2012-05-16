package br.com.ecc.client.core.event;

import com.google.gwt.event.shared.EventHandler;

public interface PlaceEventHandler extends EventHandler {
	 void onChangePlace(PlaceEvent event);
}