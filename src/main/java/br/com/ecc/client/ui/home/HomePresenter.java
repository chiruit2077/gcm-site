package br.com.ecc.client.ui.home;

import br.com.ecc.client.core.generator.presenter.PresenterService;
import br.com.ecc.client.core.generator.presenter.PresenterServiceImpl;
import br.com.ecc.client.core.handler.DownloadHandler;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.core.mvp.presenter.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;

public class HomePresenter extends BasePresenter<HomePresenter.Display> {

	@Inject
	public HomePresenter(Display display, WebResource sonnerResource) {
		super(display, sonnerResource);
	}

	public interface Display extends BaseDisplay {
	}

	@Override
	public void bind() {
	}

	@Override
	public void init() {
	}
	
	@SuppressWarnings("rawtypes")
	public void showPresenter(StateHistory state, final VerticalPanel contentPortlet) {
		((BasePresenter)this).removeEvents();
		if(contentPortlet!=null){
			contentPortlet.clear();
		}
		PresenterService presenterViewService = GWT.create(PresenterServiceImpl.class);
		presenterViewService.downloadPresenter(state, new DownloadHandler() {
			@Override
			public void finished(Presenter presenterInterno) {
				((BasePresenter)presenterInterno).removeEvents();
				BaseView display = (BaseView) presenterInterno.getDisplay();
				contentPortlet.add(display.asWidget());
			}

			@Override
			public void failure(Throwable error) {
				Window.alert("Erro ao fazer o download do presenter: " + error.getMessage());
			}
		});
	}
}