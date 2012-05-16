package br.com.ecc.client.core.event;

import br.com.ecc.core.mvp.history.StateHistory;

import com.google.gwt.event.shared.GwtEvent;

public class PlaceUpdateEvent extends GwtEvent<PlaceUpdateEventHandler> {

	private StateHistory currentStateHistory;
	private StateHistory newStateHistory;
	
	public static Type<PlaceUpdateEventHandler> TYPE = new Type<PlaceUpdateEventHandler>();

	@Override
	public Type<PlaceUpdateEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PlaceUpdateEventHandler handler) {
		handler.onUpdatePlace(this);
	}

	public StateHistory getCurrentStateHistory() {
		return currentStateHistory;
	}

	public void setCurrentStateHistory(StateHistory currentStateHistory) {
		this.currentStateHistory = currentStateHistory;
	}

	public StateHistory getNewStateHistory() {
		return newStateHistory;
	}

	public void setNewStateHistory(StateHistory newStateHistory) {
		this.newStateHistory = newStateHistory;
	}
}