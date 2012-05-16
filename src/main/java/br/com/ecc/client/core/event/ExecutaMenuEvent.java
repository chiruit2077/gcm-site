package br.com.ecc.client.core.event;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.core.mvp.history.StateHistory;

import com.google.gwt.event.shared.GwtEvent;

public class ExecutaMenuEvent extends GwtEvent<ExecutaMenuEventHandler>{

	
	public static Type<ExecutaMenuEventHandler> TYPE = new Type<ExecutaMenuEventHandler>();
	private PresenterCodeEnum janela;
	private StateHistory stateHistory;
	
	@Override
	protected void dispatch(ExecutaMenuEventHandler handler) {
		handler.onExecutaMenu(this);
	}
	@Override
	public Type<ExecutaMenuEventHandler> getAssociatedType() {
		return TYPE;
	}
	public PresenterCodeEnum getJanela() {
		return janela;
	}
	public void setJanela(PresenterCodeEnum janela) {
		this.janela = janela;
	}
	public StateHistory getStateHistory() {
		return stateHistory;
	}
	public void setStateHistory(StateHistory stateHistory) {
		this.stateHistory = stateHistory;
	}
}