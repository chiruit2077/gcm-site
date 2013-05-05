package br.com.ecc.client.ui.component;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class PortletItem {

	private ExtendedHorizontalPanel portlet;
	private HorizontalPanel portletContent;
	public enum Position { LEFT, RIGHT};

	public ExtendedHorizontalPanel getPortlet() {
		return portlet;
	}
	public void setPortlet(ExtendedHorizontalPanel portlet) {
		this.portlet = portlet;
	}
	public HorizontalPanel getPortletContent() {
		return portletContent;
	}
	public void setPortletContent(HorizontalPanel portletContent) {
		this.portletContent = portletContent;
	}
}
