package br.com.ecc.client.core.mvp.view;

import br.com.ecc.client.core.event.WaitEvent;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.core.mvp.eventbus.WebEventBus;
import br.com.freller.tool.client.Print;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings({ "rawtypes" })
public abstract class BaseView<P extends BasePresenter> implements BaseDisplay {

	public P presenter;
	private Widget widget;

	public BaseView() {
		bindWindow();
	}

	public void bind() {
	}

	public static final int HEADER_HEIGHT = 20;
	public static final int LEFTBAR_WIDTH = 250;
	public static final int FORM_TOOLBAR_SIZE = 38;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BasePresenter> T getPresenter() {
		return (T) this.presenter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BasePresenter> void setPresenter(T presenter) {
		this.presenter = (P) presenter;
		if(presenter != null) {
			bind();
		}
	}

	protected void initWidget(Widget widget) {
		this.widget = widget;
		adjustWindowSize();
	}

	@Override
	public Widget asWidget() {
		return this.widget;
	}

	@Override
	public void draw() {
	}

	protected int getWindowHeight(){
		try{
			return Window.getClientHeight();
		} catch (Exception e){
			return 768;
		}
	}

	protected int getWindowWidth(){
		try{
			return Window.getClientWidth();
		} catch (Exception e){
			return 1024;
		}
	}

	protected int getBodyHeight(){
		try{
			return Window.getClientHeight() - HEADER_HEIGHT;
		} catch (Exception e){
			return 768;
		}
	}
	protected int getBodyWidth(){
		try{
			return Window.getClientWidth() - LEFTBAR_WIDTH;
		} catch (Exception e){
			return 1024;
		}
	}

	protected void bindWindow() {
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent arg0) {
				adjustWindowSize();
			}
		});
	}

	public void adjustWindowSize() {
	}

	@Override
	public void showError(String error) {
		if (widget != null){
			Window.alert(error);
		}
	}

	@Override
	public void showMessage(String message) {
		if (widget != null){
			Window.alert(message);
		}
	}
	@Override
	public void showWaitMessage(boolean wait) {
		WaitEvent showMsg = new WaitEvent();
		showMsg.setWait(wait);
		WebEventBus.getInstance().fireEvent(showMsg);
	}

	public BaseView getBaseView() {
		return this;
	}

	public void printWidget(Widget widget){
		Print.it("<link rel=styleSheet type=text/css media=print href=/ECCWeb.css>"+
			    "<link rel=styleSheet type=text/css media=paper href=/paperStyle.css>",
			    widget.getElement());
	}
}
