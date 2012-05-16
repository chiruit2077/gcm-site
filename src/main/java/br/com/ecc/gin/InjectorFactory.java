package br.com.ecc.gin;

import com.google.gwt.core.client.GWT;

public class InjectorFactory {
	private final WebGinjector injector;
	
	private static InjectorFactory instance;

	private InjectorFactory() {
		injector = GWT.create(WebGinjector.class);
	}
		
	public static WebGinjector getInjector() {
		if(instance == null) {
			instance = new InjectorFactory();
		}

		return instance.injector;
	}
}
