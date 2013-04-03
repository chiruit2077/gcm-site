package br.com.ecc.client.util;

import com.google.gwt.core.client.GWT;

public class NavegadorUtil {

	public static String navegador="unknown";
	public static int navegadorversion=0;
	public static boolean producao = false;
	public static String baseURL="";

	static{
		NavegadorUtil.getNavegador();
		baseURL = GWT.getHostPageBaseURL();
		producao = GWT.isProdMode();
	}

	public native static String makeUrl(String url) /*-{
		return @br.com.ecc.client.util.NavegadorUtil::baseURL+"eccweb/"+url;
	}-*/;

	public native static String makeUrlResource(String url) /*-{
		return @br.com.ecc.client.util.NavegadorUtil::baseURL+url;
	}-*/;

	public native static void getNavegador()/*-{
		//testando se encontrou Firefox/x.x ou Firefox x.x;
		if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
			// capture a parcela de x.x e armazene-a como um n�mero
		   @br.com.ecc.client.util.NavegadorUtil::navegadorversion=parseInt(RegExp.$1);
		   @br.com.ecc.client.util.NavegadorUtil::navegador="firefox";
		}
		//testando se encontrou MSIE x.x
		else if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){
		   // capture a parcela de x.x e armazene-a como um n�mero
		   @br.com.ecc.client.util.NavegadorUtil::navegadorversion=parseInt(RegExp.$1);
		   @br.com.ecc.client.util.NavegadorUtil::navegador="ie";
		}
		//teste para Opera/x.x ou Opera x.x
		else if (/Opera[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
		  //capture a parcela de x.x e armazene-a como um n�mero
		  @br.com.ecc.client.util.NavegadorUtil::navegadorversion=parseInt(RegExp.$1);
		  @br.com.ecc.client.util.NavegadorUtil::navegador="opera";
		}
		//teste para Chrome/x.x ou Chrome x.x
		else if (/Chrome[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
		  //capture a parcela de x.x e armazene-a como um n�mero
		  @br.com.ecc.client.util.NavegadorUtil::navegadorversion=parseInt(RegExp.$1);
		  @br.com.ecc.client.util.NavegadorUtil::navegador="chrome";
		}
	}-*/;
}