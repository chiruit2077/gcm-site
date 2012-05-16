package br.com.ecc.server.guice;

import java.util.Properties;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;

public class ECCJPAModule extends ServletModule{

	@Override
	protected void configureServlets() {
		Properties p = new Properties();
		JpaPersistModule module = new JpaPersistModule("grpUnit");
		module.properties(p);
		
		install(module);
		filter("/*").through(PersistFilter.class);
	}
}
