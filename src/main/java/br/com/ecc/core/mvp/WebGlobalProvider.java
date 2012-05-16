package br.com.ecc.core.mvp;

import com.google.inject.Provider;

public class WebGlobalProvider implements Provider<WebGlobal>{

	@Override
	public WebGlobal get() {
		return WebGlobal.getInstance();
	}

}
