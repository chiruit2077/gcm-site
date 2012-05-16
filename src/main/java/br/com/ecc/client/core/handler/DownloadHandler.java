package br.com.ecc.client.core.handler;

import br.com.ecc.core.mvp.presenter.Presenter;

@SuppressWarnings("rawtypes")
public interface DownloadHandler {
	void finished(Presenter presenter);
	void failure(Throwable error);
}
