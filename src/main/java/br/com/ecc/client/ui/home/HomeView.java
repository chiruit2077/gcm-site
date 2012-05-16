package br.com.ecc.client.ui.home;

import br.com.ecc.client.core.event.LoginEvent;
import br.com.ecc.client.core.mvp.view.BaseView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HomeView extends BaseView<HomePresenter> implements HomePresenter.Display {
	
	@UiField HorizontalPanel homePanel;
	
	@UiTemplate("HomeView.ui.xml")
	interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {}
	private HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);

	public HomeView() {
		this.initWidget(uiBinder.createAndBindUi(this));
	}
	

	@UiHandler("loginAnchor")
	public void loginAnchorClickHandler(ClickEvent event) {
		presenter.getWebResource().getEventBus().fireEvent(new LoginEvent());
	}
	
	@Override
	public void adjustWindowSize() {
	}

	@Override
	public String getDisplayTitle() {
		return "Início";
	}

	@Override
	public void reset() {
	}
	

}