package br.com.ecc.client.core.event;

import br.com.ecc.core.mvp.presenter.Presenter;

import com.google.gwt.event.shared.EventHandler;

@SuppressWarnings("rawtypes")
public class WebEventHandler implements EventHandler {
	
	final Presenter presenter;
	
	public WebEventHandler(Presenter presenter) {
		this.presenter = presenter;
	}
	public Presenter getPresenter() {
		return presenter;
	}
}