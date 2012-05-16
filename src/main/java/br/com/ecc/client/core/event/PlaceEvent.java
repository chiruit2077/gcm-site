package br.com.ecc.client.core.event;

import java.util.List;

import br.com.ecc.core.mvp.history.StateHistory;

import com.google.gwt.event.shared.GwtEvent;

public class PlaceEvent extends GwtEvent<PlaceEventHandler> {

	private List<StateHistory> states;
	
	public static Type<PlaceEventHandler> TYPE = new Type<PlaceEventHandler>();

	@Override
	public Type<PlaceEventHandler> getAssociatedType() {
		return TYPE;
	}
	@Override
	protected void dispatch(PlaceEventHandler handler) {
		handler.onChangePlace(this);
	}
	public List<StateHistory> getStates() {
		return states;
	}
	public void setStates(List<StateHistory> states) {
		this.states = states;
	}
}