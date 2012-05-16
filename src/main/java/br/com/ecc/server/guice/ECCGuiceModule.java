package br.com.ecc.server.guice;

import org.apache.commons.logging.Log;

import br.com.ecc.server.SecureService;
import br.com.ecc.server.auth.Permissao;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

public class ECCGuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Log.class).toProvider(ECCLogProvider.class).in(Singleton.class);
		SecureService service = new SecureService();
		requestInjection(service);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Permissao.class), service);
	}

}