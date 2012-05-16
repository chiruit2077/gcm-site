package br.com.ecc.client.ui.component.textbox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.CurrencyList;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A text box that will only accept numbers. ALWAYS PROVIDED
 */
public class NumberTextBox extends TextBox {
	
	private Character decimalSeparator = ','; 
	protected boolean _allowFloatingPoint;
	protected boolean _allowNegative;	
	protected boolean _currency;
	protected String _pattern = null;
	protected String _symbol = null;
	protected String _symbolMoeda = CurrencyList.get().getDefault().getCurrencySymbol();
	public enum Formato {DECIMAL, MOEDA, PERCENTUAL};

	public NumberTextBox() {
	}
	
	public NumberTextBox (final boolean allowFloatingPoint, final boolean allowNegative, int maxLength, int visibleLength){
		this(allowFloatingPoint, allowNegative, maxLength, visibleLength, Formato.DECIMAL, null);
	}
	public NumberTextBox (final boolean allowFloatingPoint, final boolean allowNegative, int maxLength, int visibleLength, Formato tipoFormato){
		this(allowFloatingPoint, allowNegative, maxLength, visibleLength, tipoFormato, null);
	}
	
	/**
	 * @param allowFloatingPoint If true, a single decimal point is part of the allowed character
	 * set.  Otherwise, only [0-9]* is accepted.
	 */
	public NumberTextBox (final boolean allowFloatingPoint, final boolean allowNegative, int maxLength, int visibleLength, Formato tipoFormato, String pattern)
	{
		_allowFloatingPoint = allowFloatingPoint;
		_allowNegative = allowNegative;
		_pattern = pattern;
		/*
		_formatoEspecial = false;
		if(tipoFormato.equals(Formato.DECIMAL)){
			numberFormat = NumberFormat.getDecimalFormat();
		} else if(tipoFormato.equals(Formato.MOEDA)){
			symbol = CurrencyList.get().getDefault().getCurrencySymbol();
			_formatoEspecial = true;
			numberFormat = NumberFormat.getCurrencyFormat();
		} else if(tipoFormato.equals(Formato.PERCENTUAL)){
			symbol = "%";
			_formatoEspecial = true;
			numberFormat = NumberFormat.getPercentFormat();
		}
		*/
		if(tipoFormato.equals(Formato.MOEDA)){
			_symbol = _symbolMoeda;
			_currency = true;
		} else if(tipoFormato.equals(Formato.PERCENTUAL)){
			_symbol = "%";
		}

		addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp (KeyUpEvent event) {
				if (event.isShiftKeyDown() || event.getNativeKeyCode() > '9' || event.getNativeKeyCode() < '0') {
					String text = getText();
					boolean foundDecimal = !allowFloatingPoint;
					boolean foundNegative = !allowNegative;
					for (int ii = 0; ii < text.length(); ii++) {
						if (text.charAt(ii) > '9' || text.charAt(ii) < '0') {
							if (text.charAt(ii) == getDecimalSeparator() && !foundDecimal) {
								foundDecimal = true;
							} else if (text.charAt(ii) == '-' && !foundNegative) {
								foundNegative = true;
								text = text.substring(0, ii) + text.substring(ii+1);
								ii--;
							} else {
								text = text.substring(0, ii) + text.substring(ii+1);
								ii--;
							}
						}
					}
					if(allowNegative && foundNegative){
						text = "-"+text;
					}
					setText(text);
				}
			}
		});
		
		addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent arg0) {
				setNumber(getNumber());
			}
		});
		
		addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent arg0) {
				if(_symbol!=null){
					if(getText().replace(_symbol, "").equals("")){
						setText(getText().replace(_symbol, ""));
					} else {
						setNumber(getNumber(), false);
					}
				} else {
					setNumber(getNumber(), false);
				}
			}
		});

		if (maxLength > 0) {
			setMaxLength(maxLength);
		}
		if (visibleLength > 0) {
			setVisibleLength(visibleLength);
		}
		setNumber(null);
	}

	/**
	 * Sets the numeric contents of this text box. Passing null will clear the box.
	 */
	public void setNumber (Number value){
		setNumber(value, true);
	}
	
	private void setNumber (Number value, boolean considerCurrency){
		String valor = "";
		if(value != null){
			if(_currency && considerCurrency){
				valor = NumberFormat.getCurrencyFormat().format(value);
			} else {
				if(_allowFloatingPoint){
					valor = value.doubleValue() +"";
					valor = valor.replaceAll("\\.", getDecimalSeparator().toString());
					if(_pattern!=null){
						valor = NumberFormat.getFormat(_pattern).format( NumberFormat.getFormat(_pattern).parse(valor)).replaceAll("\\.", "");
					} else {
						valor = NumberFormat.getDecimalFormat().format( NumberFormat.getDecimalFormat().parse(valor)).replaceAll("\\.", "");
					}
				} else {
					valor = value.intValue() + "";
				}
				if(_symbol!=null && !_currency){
					valor += _symbol;
				}
			}
		} else {
			if(_symbol!=null){
				valor = _symbol;
			}
		}
		setText(valor);
	}

	/**
	 * Get the numeric value of this box. Returns 0 if the box is empty.
	 */
	public Number getNumber (){
		String valstr = getText().length() == 0 ? null : getText();
		if(!_allowNegative){
			if(valstr!=null && valstr.indexOf("-")>=0){
				valstr = valstr.substring(0,valstr.indexOf("-"));
			}
		}
		if(valstr==null || valstr.equals("")) {
			return null;
		}
		if(_symbol!=null){
			if(valstr.replace(_symbol, "").equals("")){
				return null;
			}
			if(_currency){
				try {
					return NumberFormat.getCurrencyFormat().parse(valstr);
				} catch (Exception e) {}
			} else { 
				valstr = valstr.replace(_symbol, "");
			}
		}
		if(_allowFloatingPoint){
			return NumberFormat.getDecimalFormat().parse(valstr);
		} else {
			String valor = valstr;
			if(valstr.indexOf(".")>=0){
				valor = valstr.substring(0,valstr.indexOf("."));
			}
			if(valstr.indexOf(",")>=0){
				valor = valstr.substring(0,valstr.indexOf(","));
			}
			return  Integer.parseInt(valor);
		}
	}
	
	public Character getDecimalSeparator() {
		return decimalSeparator;
	}
	public void setDecimalSeparator(Character decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}
}