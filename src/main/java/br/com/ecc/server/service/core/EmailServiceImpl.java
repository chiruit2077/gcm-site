package br.com.ecc.server.service.core;

import br.com.ecc.client.service.EmailService;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.command.EnviaEmailCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EmailServiceImpl extends SecureRemoteServiceServlet implements EmailService {

	private static final long serialVersionUID = 5964000408480835472L;

	@Inject Injector injector;

	@Override
	public void enviaEmail(String destino, String assunto, String mensagem) throws Exception {
		EnviaEmailCommand cmd = injector.getInstance(EnviaEmailCommand.class);
		cmd.setAssunto(assunto);
		cmd.setDestinatario(destino);
		cmd.setMensagem(mensagem);
		
		cmd.call();
	}
}
