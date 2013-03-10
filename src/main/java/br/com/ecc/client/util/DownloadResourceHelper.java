package br.com.ecc.client.util;

import com.google.gwt.core.client.GWT;

public class DownloadResourceHelper {

	public static void showReport(Integer id, String tituloJanela, String parameters){
		DownloadResourceExecuter.showReport(id, tituloJanela, parameters, StringUtil.randomString(180), GWT.isProdMode());
	};

	public static void showResource(String resource,String contentype,String tituloJanela, boolean forceDownload, String parameters){
		DownloadResourceExecuter.showResource(resource, contentype, tituloJanela, forceDownload, parameters, StringUtil.randomString(180), GWT.isProdMode());
	};

	public static void showFisicalFile(String fisicalFile,String contentype,String tituloJanela, boolean forceDownload, String parameters, String fileName){
		DownloadResourceExecuter.showFisicalFile(fisicalFile, contentype, tituloJanela, forceDownload, parameters, StringUtil.randomString(180), fileName, GWT.isProdMode());
	};

	public static void showArquivoDigital(Integer id, String tituloJanela, boolean forceDownload, String parameters){
		DownloadResourceExecuter.showArquivoDigital(id, tituloJanela, forceDownload, parameters, GWT.isProdMode());
	};

}