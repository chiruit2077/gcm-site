package br.com.ecc.gin;

import br.com.ecc.client.AppController;
import br.com.ecc.client.ui.main.MainPresenter;
import br.com.ecc.client.ui.main.MainView;
import br.com.ecc.core.mvp.WebConfig;
import br.com.ecc.core.mvp.WebConfigProvider;
import br.com.ecc.core.mvp.WebGlobal;
import br.com.ecc.core.mvp.WebGlobalProvider;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.eventbus.EventBus;
import br.com.ecc.core.mvp.eventbus.EventBusProvider;
import br.com.ecc.core.mvp.history.PlaceManager;

import com.google.inject.Singleton;

public class GinModule extends BaseGinModuleAbstract {

	@Override
	protected void configure() {
		bind(EventBus.class).toProvider(EventBusProvider.class).in(Singleton.class);
		bind(PlaceManager.class).in(Singleton.class);
		bind(WebConfig.class).toProvider(WebConfigProvider.class).in(Singleton.class);
		bind(WebGlobal.class).toProvider(WebGlobalProvider.class).in(Singleton.class);
		bind(WebResource.class).in(Singleton.class);
		bind(AppController.class).in(Singleton.class);
		bindPresenter(MainPresenter.class, MainPresenter.Display.class, MainView.class);	
	}

}
