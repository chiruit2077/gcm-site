package br.com.ecc.server.auth;

import java.util.HashMap;
import java.util.Map;

public class AuthConfigMap {

	private Map<String,String> presenterMap = new HashMap<String, String>();
	
	private AuthConfigMap(){}
	
	private static AuthConfigMap instance;
	
	public static AuthConfigMap getInstance(){
		if(instance==null){
			instance = new AuthConfigMap();
		}
		return instance;
	}
	public void addConfig(String nomeSimples,String nomeCompleto){
		presenterMap.put(nomeSimples, nomeCompleto);
	}
	public String getClassName(String nomeSimples){
		return presenterMap.get(nomeSimples);
	}
}
