package br.com.ecc.client.ui.component;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Portlet extends ExtendedVerticalPanel {
	
	private Label tituloLabel;
	private VerticalPanel portletContent;
	
	public Portlet() {
		setStyleName("portal-portlet");
		
		tituloLabel = new Label();
		tituloLabel.setHeight("20px");
		tituloLabel.setStyleName("portal-portletTitle");
		
		portletContent = new VerticalPanel();
		this.add(tituloLabel);
		this.add(portletContent);
	}

	@Override
	public void setTitle(String title) {
		tituloLabel.setText(title);
		super.setTitle(title);
	}
	
}
