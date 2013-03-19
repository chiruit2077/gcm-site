package br.com.ecc.client.ui.home;

import java.util.List;

import br.com.ecc.client.core.event.LoginEvent;
import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.model.Grupo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class HomeView extends BaseView<HomePresenter> implements HomePresenter.Display {

	@UiField HorizontalPanel homePanel;
	@UiField HorizontalPanel gruposPanel;

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


	@Override
	public void populaLogos(List<Grupo> lista) {
		gruposPanel.clear();
		for (Grupo grupo : lista) {
			if(grupo.getIdArquivoDigital()!=null){
				Image image = new Image();
				image.setSize("400px", "400px");
				image.setUrl("eccweb/downloadArquivoDigital?id="+grupo.getIdArquivoDigital());
				gruposPanel.add(image);
			}
		}
	}


}