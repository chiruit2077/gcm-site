package br.com.ecc.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("email")
public interface EmailService extends RemoteService {
	public void enviaEmail(String destino, String assunto, String mensagem) throws Exception;
	
}
