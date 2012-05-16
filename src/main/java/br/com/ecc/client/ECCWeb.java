package br.com.ecc.client;

import br.com.ecc.gin.WebGinjector;
import br.com.ecc.gin.InjectorFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class ECCWeb implements EntryPoint {
	public void onModuleLoad() {
		WebGinjector injector = InjectorFactory.getInjector();
		AppController appController = injector.getAppController();
		appController.setRootPanel(RootPanel.get());
		appController.showMain();
	}
}
