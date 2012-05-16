package br.com.ecc.core.mvp.presenter;

import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.core.mvp.view.Display;


public interface Presenter<D extends Display> {
	Display getDisplay();
	void bind();
	void init();
	StateHistory getStateHistory();
	String getClassName( );
	
	void setStateHistory(StateHistory stateHistory);
	void updatePresenterState(StateHistory newStateHistory);

    WebResource getWebResource();
}
