package br.com.ecc.client.util;


public class DownloadResourceExecuter {

	static{
		NavegadorUtil.getNavegador();
	}

	public native static void showReport(int id, String tituloJanela, String parameters, String idCode) /*-{
		window.open(@br.com.ecc.client.util.NavegadorUtil::makeUrl(Ljava/lang/String;)("report?id=" + idCode +"&reportId="+id), tituloJanela, parameters);
	}-*/;

	public native static void showResource(String resource,String contentype, String tituloJanela, boolean forceDownload, String parameters, String idCode) /*-{
		window.open(@br.com.ecc.client.util.NavegadorUtil::makeUrl(Ljava/lang/String;)("dr?id=" + idCode +"&r="+resource+"&ct="+contentype+"&fd="+(forceDownload?"true":"false")), tituloJanela, parameters);
	}-*/;

	public native static void showFisicalFile(String fisicalFile,String contentype, String tituloJanela, boolean forceDownload, String parameters, String idCode, String fileName) /*-{
		window.open(@br.com.ecc.client.util.NavegadorUtil::makeUrl(Ljava/lang/String;)("dr?id=" + idCode +"&ff="+fisicalFile+"&ct="+contentype+"&fd="+(forceDownload?"true":"false") + "&fn="+fileName), tituloJanela, parameters);
	}-*/;

	public native static void openLink(String link, String tituloJanela, String parameters) /*-{
		window.open(link, tituloJanela, parameters);
	}-*/;

	public native static void showArquivoDigital(int id, String tituloJanela, boolean forceDownload, String parameters) /*-{
		window.open(@br.com.ecc.client.util.NavegadorUtil::makeUrl(Ljava/lang/String;)("downloadArquivoDigital?id=" + id +"&forceDownload="+(forceDownload?"true":"false")), tituloJanela, parameters);
	}-*/;

}