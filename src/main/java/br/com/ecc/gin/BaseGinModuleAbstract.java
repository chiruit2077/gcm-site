package br.com.ecc.gin;

import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.core.mvp.view.Display;

import com.google.gwt.inject.client.AbstractGinModule;
public abstract class BaseGinModuleAbstract extends AbstractGinModule {
	
	protected <D extends BaseDisplay> void bindPresenter(
			Class<? extends BasePresenter<D>> presenter, 
			Class<D> display,
			Class<? extends D> displayImpl ) {
		bind(presenter);
		bindDisplay( display, displayImpl );
	}
	
	protected <D extends Display> void bindDisplay(Class<D> display, Class<? extends D> displayImpl) {
		bind(display).to(displayImpl);
	}

}


