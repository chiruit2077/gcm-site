package br.com.ecc.client.util;

import java.math.BigDecimal;

import com.google.gwt.i18n.client.NumberFormat;

public class NumberUtil {
	
	public static BigDecimal parseBigDecimal(String str){
		if(str==null){
			return null;
		}
		str = str.replaceAll("\\.", "");
		str = str.replaceAll(",", ".");
		return new BigDecimal(str);
	}
	public static Integer parseInteger(String str){
		if(str==null){
			return null;
		}
		return Integer.parseInt(str);
	}
	public static Double parseDouble(String str){
		if(str==null){
			return null;
		}
		str = str.replaceAll("\\.", "");
		str = str.replaceAll(",", ".");
		return Double.parseDouble(str);
	}
	public static Float parseFloat(String str){
		if(str==null){
			return null;
		}
		str = str.replaceAll("\\.", "");
		str = str.replaceAll(",", ".");
		return Float.parseFloat(str);
	}
	

	public static String toString(BigDecimal big){
		if(big==null){
			return null;
		}
		String str = big.toString();
		int posicaoPonto = str.lastIndexOf('.');
		String intPart = null;
		String decimalPart ="";
		if(posicaoPonto>0){
			intPart = str.substring(0,posicaoPonto);
			decimalPart = ","+str.substring(posicaoPonto+1);
		}else{
			intPart = str;
			decimalPart=",00";
		}
		String resultado = null;
		while (intPart.length()>3){
			if(resultado!=null){
				resultado = intPart.substring(intPart.length()-3)+"."+resultado;
			}else{
				resultado = intPart.substring(intPart.length()-3);
			}
			intPart = intPart.substring(0,intPart.length()-3);
		}
		if(intPart.length()>0){
			if(resultado!=null){
				resultado = intPart+"."+resultado;
			}else{
				resultado = intPart;
			}
		}
		resultado = resultado+decimalPart;
		return resultado;
	}
	public static String toString(Integer integer){
		if(integer==null){
			return null;
		}
		return integer.toString();
	}
	public static String toString(Double doub){
		if(doub==null){
			return null;
		}
		NumberFormat nf = NumberFormat.getDecimalFormat();
		return nf.format(doub);
	}
	public static String toString(Float fl){
		if(fl==null){
			return null;
		}
		NumberFormat nf = NumberFormat.getDecimalFormat();
//		NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt","BR"));
		return nf.format(fl);
	}
}
