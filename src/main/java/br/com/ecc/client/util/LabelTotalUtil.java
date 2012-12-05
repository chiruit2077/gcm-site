package br.com.ecc.client.util;

import java.util.List;

import com.google.gwt.user.client.ui.Label;

public class LabelTotalUtil {
	
	@SuppressWarnings("rawtypes")
	public static void setTotal(Label label, List lista, String nome){
		setTotal(label, lista, nome, nome+"s", "");
	}
	public static void setTotal(Label label, int tamanho, String nome){
		setTotal(label, tamanho, nome, nome+"s", "");
	}
	
	@SuppressWarnings("rawtypes")
	public static void setTotal(Label label, List lista, String nome, String nomePlural, String noneSingularGender){
		setTotal(label, lista.size(), nome, nomePlural, noneSingularGender);
	}
	
	public static void setTotal(Label label, int tamanho, String nome, String nomePlural, String noneSingularGender){
		if(nomePlural==null || nomePlural.equals("")){
			nomePlural = nome + "s";
		}
		String gender = "o";
		if(noneSingularGender!=null && !noneSingularGender.equals("")){
			gender = noneSingularGender;
		}
		if(tamanho>0){
			if(tamanho==1){
				label.setText(tamanho + " " + nome + " listad" + gender);
			} else {
				label.setText(tamanho + " " + nomePlural + " listad" + gender + "s");
			}
		} else {
			label.setText("Nenhum" + noneSingularGender + " " + nome + " listad" + gender);			
		}
	}
}
