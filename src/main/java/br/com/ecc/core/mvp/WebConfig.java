package br.com.ecc.core.mvp;

import java.io.Serializable;

public class WebConfig implements Serializable{
	private static final long serialVersionUID = 5771256745581135610L;
	private static WebConfig instance;
	public static final Integer MAXQUERYRESULTS = 500;
	public static String MAXQUERYRESULTSMESSAGE = "A quantidade de registros selecionados é muito grande, melhore os seus critérios de busca.";

	public WebConfig() {
	}

	public static WebConfig getInstance() {
		if(instance == null) {
			instance = new WebConfig();
		}
		return instance;
	}
}