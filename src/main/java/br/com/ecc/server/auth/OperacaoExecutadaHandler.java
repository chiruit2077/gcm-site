package br.com.ecc.server.auth;

import org.aopalliance.intercept.MethodInvocation;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.Usuario;

public interface OperacaoExecutadaHandler {

	public void onOperacaoExecutada(Usuario usuario, Permissao permissao, MethodInvocation invocation) throws WebException;
	
}
