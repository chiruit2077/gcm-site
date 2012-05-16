package br.com.ecc.client.core.mvp.presenter;

import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.eventbus.WebEventBus;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.core.mvp.presenter.Presenter;


@SuppressWarnings("rawtypes")
public abstract class BasePresenter<D extends BaseDisplay> implements Presenter {
	
	private D display;
	private WebResource resource;
	private String className;
	
	private StateHistory stateHistory;
	
	public BasePresenter(D display, WebResource resource) {
		this.display = display;
		this.resource = resource;
		this.display.setPresenter(this);
		//estatisticaHelper = new EstatisticaHelper();
		bind();
		this.stateHistory = new StateHistory(this.getClass());
	}
	
	public D getDisplay() {
		return display;
	}

	public WebResource getWebResource() {
		return resource;
	}

	public void setDisplay(D display) {
		this.display = display;
	}

	public void setWebResource(WebResource resource) {
		this.resource = resource;
	}

	public StateHistory getStateHistory() {
		return stateHistory;
	}

	public void setStateHistory(StateHistory stateHistory) {
		this.stateHistory = stateHistory;
	}

	public void updatePresenterState(StateHistory newState) {
		getWebResource().getPlaceManager().updatePlace(getStateHistory(), newState);
		setStateHistory(newState);
	}
	
	public void removeEvents() {
		((WebEventBus)getWebResource().getEventBus()).removePresenterEvents(this);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}