package br.com.ecc.server;

import br.com.ecc.model.Usuario;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SecureRemoteServiceServlet extends RemoteServiceServlet{
	private static final long serialVersionUID = 2289148948352553778L;

	public Usuario getUsuario(){
		return SessionHelper.getUsuario(getThreadLocalRequest().getSession());
	}
}