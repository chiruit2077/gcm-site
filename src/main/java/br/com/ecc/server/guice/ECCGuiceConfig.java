package br.com.ecc.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ECCGuiceConfig extends GuiceServletContextListener {

	@Override
	protected final Injector getInjector() {
		return Guice.createInjector(
				new ECCJPAModule(),
				new ECCGuiceModule(),
				new ECCServletModule()
				);
	}
}