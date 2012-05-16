package br.com.ecc.client.ui.component;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExtendedHorizontalPanelFocus extends Composite implements HasWidgets, HasAllKeyHandlers, HasClickHandlers {

    private HorizontalPanel mainPanel;
    private FocusPanel fFocusPanel;

    public ExtendedHorizontalPanelFocus() {
        mainPanel = new HorizontalPanel();
        mainPanel.setWidth("100%");
        fFocusPanel = new FocusPanel();
        fFocusPanel.setWidget(mainPanel);
        initWidget(fFocusPanel);
    }

    @Override
    public void add(Widget w) {
    	mainPanel.add(w);
    }

    @Override
    public void clear() {
        mainPanel.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return mainPanel.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return mainPanel.remove(w);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
    	return fFocusPanel.addClickHandler(handler);
    }
    
    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return fFocusPanel.addKeyUpHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return fFocusPanel.addKeyDownHandler(handler);
    }

    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return fFocusPanel.addKeyPressHandler(handler);
    }
}