package br.com.ecc.core.mvp.eventbus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import br.com.ecc.client.core.event.WebEventHandler;
import br.com.ecc.core.mvp.presenter.Presenter;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;

public class WebEventBus extends EventBus {

	Set<HandlerInfo> eccHandlers = new HashSet<HandlerInfo>();

	private static WebEventBus instance;

	public static WebEventBus getInstance() {
		if (instance == null) {
			instance = new WebEventBus();
		}
		return instance;
	}

	private WebEventBus() {
	}

	public <H extends WebEventHandler> HandlerRegistration addMyHandler(Type<H> type, H handler) {
		HandlerRegistration handlerRegistration = super.addHandler(type, handler);
		if(handler instanceof WebEventHandler) {
			HandlerInfo handlerInfo = new HandlerInfo();
			handlerInfo.setHandler((WebEventHandler) handler);
			handlerInfo.setHandlerRegistration(handlerRegistration);
			eccHandlers.add(handlerInfo);
		}
		return handlerRegistration;
	};

	public void removePresenterEvents(@SuppressWarnings("rawtypes") Presenter presenter) {
		Iterator<HandlerInfo> iter = eccHandlers.iterator();
		while (iter.hasNext()) {
			HandlerInfo handlerInfo = iter.next();
			WebEventHandler handler = handlerInfo.getHandler();
			if(handler.getPresenter()!=null && handler.getPresenter().equals(presenter)) {
				handlerInfo.getHandlerRegistration().removeHandler();
				iter.remove();
			}
		}
	}
	
	public void removeAllEvents() {
		eccHandlers = new HashSet<HandlerInfo>();
	}
	
	class HandlerInfo {
		HandlerRegistration handlerRegistration;
		WebEventHandler handler;
		public HandlerInfo() {
		}
		public WebEventHandler getHandler() {
			return handler;
		}
		public void setHandler(WebEventHandler handler) {
			this.handler = handler;
		}
		public HandlerRegistration getHandlerRegistration() {
			return handlerRegistration;
		}
		public void setHandlerRegistration(HandlerRegistration handlerRegistration) {
			this.handlerRegistration = handlerRegistration;
		}
	}
}
