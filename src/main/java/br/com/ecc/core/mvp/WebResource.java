package br.com.ecc.core.mvp;

import java.io.Serializable;

import br.com.ecc.core.mvp.eventbus.EventBus;
import br.com.ecc.core.mvp.history.PlaceManager;
import br.com.ecc.core.mvp.infra.exception.WebRuntimeException;

import com.google.inject.Inject;

public class WebResource implements Serializable {
	private static final long serialVersionUID = -1025292010727087548L;
	private final EventBus eventBus;
	private final PlaceManager placeManager;
	private final WebConfig eccConfig;
	private final WebGlobal eccGlobal;
	
	private static WebResource instance ;
	
	/** retorna null se o construtor padrao nao tiver sido usado nem uma vez */
	public static WebResource getInstanceCreated() {
		if(instance == null) {
			throw new WebRuntimeException("Esse obj deve ser criado pelo GIN pelo menos uma vez antes de usar esse metodo!");
		}
		return instance;
	}
	
	@Inject
	public WebResource(EventBus eventBus, PlaceManager placeManager, WebConfig eccConfig, WebGlobal eccGlobal) {
		this.eventBus = eventBus;
		this.placeManager = placeManager;
		this.eccConfig = eccConfig;
		this.eccGlobal = eccGlobal;
		instance = this;
	}

	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	public WebConfig getECCConfig() {
		return eccConfig;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public WebGlobal getECCGlobal() {
		return eccGlobal;
	}

}
