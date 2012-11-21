package br.com.ecc.server.command;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.server.command.basico.ECCBaseCommand;

public class EnviaEmailCommand extends ECCBaseCommand<Void> {

	private static final long serialVersionUID = -3503312538232518320L;

	private String destinatario;
	private String destinatariosCopia;
	private String destinatariosCopiaOculta;
	private String assunto;
	private String mensagem;
	private Boolean naoEsperar;
	
	private Transport transport;
	private Session mailSession;
	private Boolean fecharConexao = false;

	@Override
	public Void call() throws Exception {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", "smtp.gmail.com");
		props.put("mail.smtps.auth", "true");
		mailSession = Session.getDefaultInstance(props);
		mailSession.setDebug(false);
		final MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(assunto, "UTF-8");
		message.setText(mensagem, "UTF-8");
		message.setHeader("Content-Type", "text/html; charset=UTF-8");
		//message.setContent(mensagem, "text/html");
		message.setFrom(new InternetAddress("ecc.uberlandia@gmail.com", "ECC Uberlandia"));
		if(destinatario!=null){
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
		}
		if(destinatariosCopia!=null && !destinatariosCopia.equals("")){
			message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(destinatariosCopia));
		}
		if(destinatariosCopiaOculta!=null && !destinatariosCopiaOculta.equals("")){
			message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(destinatariosCopiaOculta));
		}
		if(naoEsperar!=null && naoEsperar){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						envia(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		} else {
			envia(message);
		}
		return null;
	}

	private void envia(final MimeMessage message) throws Exception {
		try {
			if(transport==null){
				transport = conecta();
			}
			transport.sendMessage(message, message.getAllRecipients());
			if(fecharConexao!=null && fecharConexao){
				transport.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			String dest = "";
			for (int i = 0; i < message.getAllRecipients().length; i++) {
				if(!dest.equals("")) dest+=",";
				dest += message.getAllRecipients()[i];
			}
			throw new WebException("Erro ao enviar email: " + e.getMessage() +  "\nDestinatarios:" + dest);
		}
	}

	public Transport conecta() throws Exception {
		transport = mailSession.getTransport();
		transport.connect("smtp.gmail.com", 465, "ecc.uberlandia@gmail.com","ecc1508udia");
		return transport;
	}

	public String getAssunto() {
		return assunto;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getDestinatariosCopia() {
		return destinatariosCopia;
	}
	public void setDestinatariosCopia(String destinatariosCopia) {
		this.destinatariosCopia = destinatariosCopia;
	}
	public String getDestinatariosCopiaOculta() {
		return destinatariosCopiaOculta;
	}
	public void setDestinatariosCopiaOculta(String destinatariosCopiaOculta) {
		this.destinatariosCopiaOculta = destinatariosCopiaOculta;
	}
	public Boolean getNaoEsperar() {
		return naoEsperar;
	}
	public void setNaoEsperar(Boolean naoEsperar) {
		this.naoEsperar = naoEsperar;
	}
	public Boolean getFecharConexao() {
		return fecharConexao;
	}
	public void setFecharConexao(Boolean fecharConexao) {
		this.fecharConexao = fecharConexao;
	}
}