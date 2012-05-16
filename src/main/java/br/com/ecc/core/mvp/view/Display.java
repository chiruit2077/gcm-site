package br.com.ecc.core.mvp.view;


public interface Display {
	//void showMessages(NotificationType error, List<ECCMessage> messages);
	void showMessage(String message);
	void showError(String message);
	void showWaitMessage(boolean b);
}
