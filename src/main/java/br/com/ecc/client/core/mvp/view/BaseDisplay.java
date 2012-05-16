package br.com.ecc.client.core.mvp.view;

import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.core.mvp.view.Display;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("rawtypes")
public interface BaseDisplay extends Display {
    Widget asWidget();
    String getDisplayTitle();
	public void draw();
	public void showMessage(String message);
	public void showError(String error);
	public void showWaitMessage(boolean show);
	<T extends BasePresenter> void setPresenter(T presenter);
    <T extends BasePresenter>  T getPresenter();
	public void reset();
}