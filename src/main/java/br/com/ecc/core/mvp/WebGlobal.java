package br.com.ecc.core.mvp;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebGlobal implements Serializable {
	
	private static final long serialVersionUID = 6288247628631249611L;

	public static final String KEY_CIDADEPADRAO = "cidadePadrao";
	public static final String KEY_LABELNOMEORGAO = "labelNomeOrgao";
	
	private static WebGlobal instance;

	private Map<String, Serializable> params = new LinkedHashMap<String, Serializable>(); 
	private WebGlobal() {
	}
	
	public static WebGlobal getInstance() {
		if(instance == null) {
			instance = new WebGlobal();
		}
		return instance;
	}
	public void put(String key, Serializable value){
		params.put(key, value);
	}
	public Map<String, Serializable> getParams() {
		return params;
	}
	public void setParams(Map<String, Serializable> params) {
		this.params = params;
	}
	public Object get(String key){
		return params.get(key);
	}
	public void remove(String key){
		params.remove(key);
	}
}