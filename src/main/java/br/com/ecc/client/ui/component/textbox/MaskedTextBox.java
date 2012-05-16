package br.com.ecc.client.ui.component.textbox;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Formato da mascara:<br/>
 * &nbsp;&nbsp;# isDigit <br/>
 * &nbsp;&nbsp;? isLetter <br/>
 * &nbsp;&nbsp;A isLetterOrDigit <br/>
 */
public class MaskedTextBox extends TextBox{

	private String mask;
	private ExtendedKeyPressHandler keyPressHandler;
	private BlurHandler validador;
	
	public void setMask(String mask) {
		if (mask!=null){
			if (keyPressHandler==null){
				keyPressHandler = new ExtendedKeyPressHandler(mask);
				addKeyPressHandler(keyPressHandler);
			} else {
				keyPressHandler.setMask(mask);
			}
		}
		this.mask = mask;
	}
	
	public void configuraValidador(BlurHandler validador) {
		this.setValidador(validador);
		this.addBlurHandler(this.getValidador());
	}
	
	public String getMask() {
		return mask;
	}

	public String getUnmaskedValue(){
		return keyPressHandler.unmaskedValue(getValue());
	}
	
	private class ExtendedKeyPressHandler implements KeyPressHandler {

		private String mask;

		public ExtendedKeyPressHandler(String mask) {
			this.setMask(mask);
		}
		
		@Override
		public void onKeyPress(KeyPressEvent event) {
			int keyCode = event.getNativeEvent().getKeyCode();
			if(keyCode != 8  && /* BACKSPACE */  
			   keyCode != 46 && /* DEL */ 
			   keyCode != 37 && /* < */ 
			   keyCode != 38 && /* ^ */ 
			   keyCode != 39 && /* > */ 
			   keyCode != 40 && /* V */ 
			   keyCode != 9  && /* TAB */
			   keyCode != 192   /* ALT+TAB */
			) {
				TextBox sourceTextBox = (TextBox) event.getSource();
				String vOriginal = sourceTextBox.getValue();
				int index = sourceTextBox.getCursorPos();
				
				String vOriginalSelectionRemoved = null;
				int selectionLength = sourceTextBox.getSelectionLength();
				if(selectionLength > 0) {
					vOriginalSelectionRemoved = vOriginal.substring(0, index) + 
						             vOriginal.substring(index+selectionLength, vOriginal.length());
				}
			
				int cursor = index;
				char cPress = event.getCharCode();

				String unmaskMask = unmaskedValue(getMask());
				String unmaskValor = unmaskedValue(insertInto((vOriginalSelectionRemoved == null) ? vOriginal : vOriginalSelectionRemoved, cPress, index));
				if(unmaskValor.length() > unmaskMask.length()) {
					unmaskValor = unmaskValor.substring(0, unmaskMask.length());
				}

				String novoValor = null;
				for (int i = 0; i < unmaskValor.length(); i++) {
					char cMask = unmaskMask.charAt(i);
					char cValor = unmaskValor.charAt(i);
					if ((cMask == '#' && Character.isDigit(cValor)) || 
							(cMask == '?' && Character.isLetter(cValor)) ||
								(cMask == 'A' && Character.isLetterOrDigit(cValor)) ) {
						continue;
					} else {
						novoValor = vOriginal;
						break;
					}
				}
				
				if(novoValor == null) {
					novoValor = unmaskValor;
					for (int i=0; i < getMask().length(); i++) {
						char cMask = getMask().charAt(i);
						if(cMask != '#' && cMask != '?' && cMask != 'A') {
							novoValor = insertInto(novoValor, cMask, i);
						}
					}
					cursor++;
					if(cursor != vOriginal.length()) {
						try {
							char c = getMask().charAt(cursor);
							if(c != '#' && c !=  '?' && c !=  'A') {
								cursor++;
							}
						} catch(Exception e) {
						}
					}
				}

				cancelKey(event);
				sourceTextBox.setValue(novoValor);
				if(getMask().length() > cursor) {
					sourceTextBox.setCursorPos(cursor);
				}
			}
		}
		
		public String unmaskedValue(String str) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (c == '#' || c == '?' || c == 'A' || Character.isLetterOrDigit(c)) {
					sb.append(c);
				}
			}
			return sb.toString();
		}

		public String insertInto(String str, char c, int index) {
			String result = null;
			if (index == str.length()) {
				result = str + c;
			} else if (index < 0 || index > str.length()) {
				result = new String(str);
			} else {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < str.length(); i++) {
					if (i == index) {
						sb.append(c);
					}
					sb.append(str.charAt(i));
				}
				result = sb.toString();
			}
			return result;
		}
		
		public void cancelKey(@SuppressWarnings("rawtypes") KeyEvent event) {
			if (event != null && event.getSource() != null) {
				((TextBox) event.getSource()).cancelKey();
			}
		}

		public void setMask(String mask) {
			this.mask = mask;
		}

		public String getMask() {
			return mask;
		}
	}


	public BlurHandler getValidador() {
		return validador;
	}

	private void setValidador(BlurHandler validador) {
		this.validador = validador;
	}
}

