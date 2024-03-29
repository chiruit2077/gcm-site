package br.com.ecc.client.ui.component.color.colorpicker;

import br.com.ecc.client.ui.component.color.dialog.Dialog;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ColorPickerDialog extends Dialog {
	private SaturationLightnessPicker slPicker;
	private HuePicker huePicker;
	private String color;

	@Override
	protected Widget createDialogArea() {
		setText("Selecione a cor");
		
		HorizontalPanel panel = new HorizontalPanel();
		
		// the pickers
		slPicker = new SaturationLightnessPicker();
		panel.add(slPicker);
		huePicker = new HuePicker();
		panel.add(huePicker);

		// bind saturation/lightness picker and hue picker together
		huePicker.addHueChangedHandler(new IHueChangedHandler() {
			public void hueChanged(HueChangedEvent event) {
				slPicker.setHue(event.getHue());
			}
		});

		return panel;
	}

	public void setColor(String color) {
		int[] rgb = ColorUtils.getRGB(color);
		int[] hsl = ColorUtils.rgb2hsl(rgb);
		huePicker.setHue(hsl[0]);
		slPicker.setColor(color);
	}
	
	public String getColor() {
		return color;
	}
	
	@Override
	protected void buttonClicked(Widget button) {
		// remember color when "OK" is clicked
		if (button == getOkButton()) {
			color = slPicker.getColor();
		}
		
		close(button == getCancelButton());
	}
}
