package br.com.ecc.client.util;

public class DownloadResourceExecuter {

	public native static void showReport(Integer id, String tituloJanela, String parameters, String idCode, boolean producao) /*-{
	var ie = window.navigator.appName.toLowerCase().indexOf("microsoft") >= 0;
	if (!producao){
		window.open("report?id=" + idCode +"&reportId="+id, tituloJanela, parameters);
	}else{
		if (ie){
			window.open("report?id=" + idCode +"&reportId="+id, tituloJanela, parameters);
		} else  {
			window.open("eccweb/report?id=" + idCode +"&reportId="+id, tituloJanela, parameters);
		}
	}
	}-*/;

	public native static void showResource(String resource,String contentype,String tituloJanela, boolean forceDownload, String parameters, String idCode, boolean producao) /*-{
	var ie = window.navigator.appName.toLowerCase().indexOf("microsoft") >= 0;
	if (ie){
		window.open("dr?id=" + idCode +"&r="+resource+"&ct="+contentype+"&fd="+(forceDownload?1:0), tituloJanela, parameters);
	} else  {
		window.open("eccweb/dr?id=" + idCode +"&r="+resource+"&ct="+contentype+"&fd="+(forceDownload?1:0), tituloJanela, parameters);
	}
	}-*/;

	public native static void showFisicalFile(String fisicalFile,String contentype, String tituloJanela, boolean forceDownload, String parameters, String idCode, String fileName, boolean producao) /*-{
	var ie = window.navigator.appName.toLowerCase().indexOf("microsoft") >= 0;
	if (ie){
		window.open("dr?id=" + idCode +"&ff="+fisicalFile+"&ct="+contentype+"&fd="+(forceDownload?1:0) + "&fn="+fileName, tituloJanela, parameters);
	} else  {
		window.open("eccweb/dr?id=" + idCode +"&ff="+fisicalFile+"&ct="+contentype+"&fd="+(forceDownload?1:0) + "&fn="+fileName, tituloJanela, parameters);
	}
	}-*/;

	public native static void openLink(String link, String tituloJanela, String parameters, boolean producao) /*-{
	var ie = window.navigator.appName.toLowerCase().indexOf("microsoft") >= 0;
	if (ie){
		window.open(link, tituloJanela, parameters);
	} else  {
		window.open(link, tituloJanela, parameters);
	}
	}-*/;

	public native static void showArquivoDigital(Integer id, String tituloJanela, boolean forceDownload, String parameters, boolean producao) /*-{
	var ie = window.navigator.appName.toLowerCase().indexOf("microsoft") >= 0;
	if (!producao){
		window.open("downloadArquivoDigital?id=" + id +"&forceDownload="+(forceDownload?1:0), tituloJanela, parameters);
	}else{
		if (ie){
			window.open("downloadArquivoDigital?id=" + id +"&forceDownload="+(forceDownload?1:0), tituloJanela, parameters);
		} else  {
			window.open("eccweb/downloadArquivoDigital?id=" + id +"&forceDownload="+(forceDownload?1:0), tituloJanela, parameters);
		}
	}
	}-*/;
}