package br.com.ecc.client;

import java.util.List;

import br.com.ecc.client.core.event.PlaceEvent;
import br.com.ecc.client.core.event.PlaceEventHandler;
import br.com.ecc.client.core.generator.presenter.PresenterService;
import br.com.ecc.client.core.generator.presenter.PresenterServiceImpl;
import br.com.ecc.client.core.handler.DownloadHandler;
import br.com.ecc.client.ui.main.MainPresenter;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.core.mvp.presenter.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

public class AppController {

	private HasWidgets rootPanel;
	private final WebResource ECCResource;

	private final MainPresenter mainPresenter;
	private final PresenterService presenterViewService;
	
	@Inject
	public AppController(final WebResource ECCResource, final MainPresenter mainPresenter) {
		presenterViewService = GWT.create(PresenterServiceImpl.class);
		this.ECCResource = ECCResource;
		this.mainPresenter = mainPresenter;
		bind();
	}

	public void setRootPanel(final RootPanel rootPanel) {
		this.rootPanel = rootPanel;
	}

	private void bind() {
		ECCResource.getEventBus().addHandler(PlaceEvent.TYPE, new PlaceEventHandler() {
			@Override
			public void onChangePlace(PlaceEvent event) {
				List<StateHistory> states = event.getStates();
				showStates(states);
			}
		});	
	}
	
	@SuppressWarnings("rawtypes")
	private void showStates(List<StateHistory> states) {
		for(int i=0; i < states.size(); i++) {
			final StateHistory state = states.get(i);
			presenterViewService.downloadPresenter(state, new DownloadHandler() {
				@Override
				public void finished(final Presenter presenter) {
					mainPresenter.getDisplay().showState(presenter, "");
				}
				@Override
				public void failure(Throwable error) {
					System.out.println("Download error: "+error.getMessage()+" ("+state.getPresenterName()+")");
					error.printStackTrace();
				}
			});
		}
	}
	
	public void showMain() {
		if (rootPanel != null) {
			rootPanel.clear();
			mainPresenter.init();
			rootPanel.add(mainPresenter.getDisplay().asWidget());
		}
	}
}