package br.com.ecc.gin;

import br.com.ecc.client.AppController;
import br.com.ecc.core.mvp.WebResource;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ 
	GinModule.class
}) 
public interface WebGinjector extends Ginjector {
	AppController getAppController();
	WebResource getWebResource();
}