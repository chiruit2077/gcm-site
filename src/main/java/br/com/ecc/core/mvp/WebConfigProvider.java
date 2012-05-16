package br.com.ecc.core.mvp;

import com.google.inject.Provider;

public class WebConfigProvider implements Provider<WebConfig> {

	@Override
	public WebConfig get() {
		return WebConfig.getInstance();
	}

}
