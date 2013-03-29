package br.com.ecc.client.util;

import com.google.gwt.core.client.GWT;

public class DownloadResourceExecuter {

	public static String navegador="unknown";
	public static int navegadorversion=0;
	public static boolean producao = false;
	public static String baseURL="";

	static{
		DownloadResourceExecuter.getNavegador();
		baseURL = GWT.getHostPageBaseURL();
		producao = GWT.isProdMode();
	}

	public native static String makeUrl(String url) /*-{
		if (@br.com.ecc.client.util.DownloadResourceExecuter::producao){
			return @br.com.ecc.client.util.DownloadResourceExecuter::baseURL+"eccweb/"+url;
		}else{
			return @br.com.ecc.client.util.DownloadResourceExecuter::baseURL+"eccweb/"+url;
		}
	}-*/;

	public native static void showReport(int id, String tituloJanela, String parameters, String idCode) /*-{
		window.open(@br.com.ecc.client.util.DownloadResourceExecuter::makeUrl(Ljava/lang/String;)("report?id=" + idCode +"&reportId="+id), tituloJanela, parameters);
	}-*/;

	public native static void showResource(String resource,String contentype, String tituloJanela, boolean forceDownload, String parameters, String idCode) /*-{
		window.open(@br.com.ecc.client.util.DownloadResourceExecuter::makeUrl(Ljava/lang/String;)("dr?id=" + idCode +"&r="+resource+"&ct="+contentype+"&fd="+(forceDownload?"true":"false")), tituloJanela, parameters);
	}-*/;

	public native static void showFisicalFile(String fisicalFile,String contentype, String tituloJanela, boolean forceDownload, String parameters, String idCode, String fileName) /*-{
		window.open(@br.com.ecc.client.util.DownloadResourceExecuter::makeUrl(Ljava/lang/String;)("dr?id=" + idCode +"&ff="+fisicalFile+"&ct="+contentype+"&fd="+(forceDownload?"true":"false") + "&fn="+fileName), tituloJanela, parameters);
	}-*/;

	public native static void openLink(String link, String tituloJanela, String parameters) /*-{
		window.open(link, tituloJanela, parameters);
	}-*/;

	public native static void showArquivoDigital(int id, String tituloJanela, boolean forceDownload, String parameters) /*-{
		window.open(@br.com.ecc.client.util.DownloadResourceExecuter::makeUrl(Ljava/lang/String;)("downloadArquivoDigital?id=" + id +"&forceDownload="+(forceDownload?"true":"false")), tituloJanela, parameters);
	}-*/;

	public native static void getNavegador()/*-{
		//testando se encontrou Firefox/x.x ou Firefox x.x;
		if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
			// capture a parcela de x.x e armazene-a como um n�mero
		   @br.com.ecc.client.util.DownloadResourceExecuter::navegadorversion=parseInt(RegExp.$1);
		   @br.com.ecc.client.util.DownloadResourceExecuter::navegador="firefox";
		}
		//testando se encontrou MSIE x.x
		else if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){
		   // capture a parcela de x.x e armazene-a como um n�mero
		   @br.com.ecc.client.util.DownloadResourceExecuter::navegadorversion=parseInt(RegExp.$1);
		   @br.com.ecc.client.util.DownloadResourceExecuter::navegador="ie";
		}
		//teste para Opera/x.x ou Opera x.x
		else if (/Opera[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
		  //capture a parcela de x.x e armazene-a como um n�mero
		  @br.com.ecc.client.util.DownloadResourceExecuter::navegadorversion=parseInt(RegExp.$1);
		  @br.com.ecc.client.util.DownloadResourceExecuter::navegador="opera";
		}
		//teste para Chrome/x.x ou Chrome x.x
		else if (/Chrome[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
		  //capture a parcela de x.x e armazene-a como um n�mero
		  @br.com.ecc.client.util.DownloadResourceExecuter::navegadorversion=parseInt(RegExp.$1);
		  @br.com.ecc.client.util.DownloadResourceExecuter::navegador="chrome";
		}
	}-*/;
}