package br.com.ecc.client.util;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.core.generator.presenter.PresenterService;
import br.com.ecc.client.core.generator.presenter.PresenterServiceImpl;
import br.com.ecc.client.core.handler.DownloadHandler;
import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.core.mvp.presenter.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PresenterShow {
	
	@SuppressWarnings("rawtypes")
	public static void showPresenter(StateHistory state, final VerticalPanel contentPortlet, final Presenter presenter ) {
		if(contentPortlet!=null){
			contentPortlet.clear();
		}
		PresenterService presenterViewService = GWT.create(PresenterServiceImpl.class);
		presenterViewService.downloadPresenter(state, new DownloadHandler() {
			@Override
			public void finished(Presenter presenterInterno) {
				BaseView display = (BaseView) presenterInterno.getDisplay();
				//contentPortlet.setTitle(display.getDisplayTitle());
				contentPortlet.add(display.asWidget());
				for (PresenterCodeEnum presenterEnum : PresenterCodeEnum.values()) {
					String pname = presenterEnum.getPresenter().toString();
					pname = pname.substring(pname.indexOf(" ")+1);
					if(pname.equals(presenterInterno.getClassName().toString())){
						updateHomeViewStateHistory(presenterEnum,presenter);
						break;
					}
				}
			}

			@Override
			public void failure(Throwable error) {
				Window.alert("Erro ao fazer o download do presenter: " + error.getMessage());
			}
		});
	}
	@SuppressWarnings("rawtypes")
	public static void updateHomeViewStateHistory(PresenterCodeEnum presenterEnum, Presenter presenter) {
		StateHistory oldStateHistory = presenter.getStateHistory();
		StateHistory newStateHistory = new StateHistory(presenter.getClassName());
		newStateHistory.put("presenterCode", presenterEnum.getCodigo());
		presenter.getWebResource().getPlaceManager().updatePlace(oldStateHistory, newStateHistory);
		presenter.setStateHistory(newStateHistory);
	}
}