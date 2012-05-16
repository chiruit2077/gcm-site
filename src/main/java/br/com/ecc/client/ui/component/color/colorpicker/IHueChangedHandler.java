package br.com.ecc.client.ui.component.color.colorpicker;

import com.google.gwt.event.shared.EventHandler;

public interface IHueChangedHandler extends EventHandler {
	void hueChanged(HueChangedEvent event);
}
