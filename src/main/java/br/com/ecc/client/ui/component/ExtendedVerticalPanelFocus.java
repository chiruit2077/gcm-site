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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExtendedVerticalPanelFocus extends Composite implements HasWidgets, HasAllKeyHandlers, HasClickHandlers {

    private VerticalPanel fVerticalPanel;
    private FocusPanel fFocusPanel;

    public ExtendedVerticalPanelFocus() {
        fFocusPanel = new FocusPanel();
        fVerticalPanel = new VerticalPanel();
        fVerticalPanel.setWidth("100%");
        fFocusPanel.setWidget(fVerticalPanel);
        initWidget(fFocusPanel);
    }

    @Override
    public void add(Widget w) {
        fVerticalPanel.add(w);
    }

    @Override
    public void clear() {
        fVerticalPanel.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return fVerticalPanel.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return fVerticalPanel.remove(w);
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