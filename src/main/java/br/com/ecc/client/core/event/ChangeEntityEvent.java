package br.com.ecc.client.core.event;

import br.com.ecc.model._WebBaseEntity;

import com.google.gwt.event.shared.GwtEvent;

public class ChangeEntityEvent extends GwtEvent<ChangeEntityEventHandler>{

	public static Type<ChangeEntityEventHandler> TYPE = new Type<ChangeEntityEventHandler>();
	private _WebBaseEntity entidade;

	@Override
	public Type<ChangeEntityEventHandler> getAssociatedType() {
		return TYPE;
	}
	@Override
	protected void dispatch(ChangeEntityEventHandler handler) {
		handler.onChange(this);
	}
	public _WebBaseEntity getEntidade() {
		return entidade;
	}
	public void setEntidade(_WebBaseEntity entidade) {
		this.entidade = entidade;
	}
}