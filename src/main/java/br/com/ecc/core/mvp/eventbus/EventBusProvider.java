package br.com.ecc.core.mvp.eventbus;

import com.google.inject.Provider;

public class EventBusProvider implements Provider<EventBus> {

	@Override
	public EventBus get() {
		return WebEventBus.getInstance();
	}

}
