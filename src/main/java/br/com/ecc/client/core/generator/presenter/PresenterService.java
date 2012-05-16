package br.com.ecc.client.core.generator.presenter;

import br.com.ecc.client.core.handler.DownloadHandler;
import br.com.ecc.core.mvp.history.StateHistory;

public interface PresenterService {
	public void downloadPresenter(final StateHistory stateHistory, final DownloadHandler handler);
}