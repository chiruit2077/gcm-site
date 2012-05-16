package br.com.ecc.server;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import br.com.ecc.core.mvp.infra.exception.WebNaoAutenticadoException;
import br.com.ecc.core.mvp.infra.exception.WebSecurityException;
import br.com.ecc.model.Usuario;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.auth.Permissoes;


public class SecureService implements MethodInterceptor{
	
	public SecureService(){
	}

	boolean iniciado=false;

	private void init() {
		if(!iniciado){
			iniciado=true;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		init();
		Method method = invocation.getMethod();
		Class classeDoMetodo = method.getDeclaringClass();
		Class remoteService = SecureRemoteServiceServlet.class;
		if(remoteService.isAssignableFrom(classeDoMetodo)){
			SecureRemoteServiceServlet servicoAlvo  = (SecureRemoteServiceServlet) invocation.getThis();
			Usuario usuario = servicoAlvo.getUsuario();
			if(usuario==null){
				throw new WebNaoAutenticadoException("Funcionalidade requer permissão, usuário não autenticado.");
			}
			Object retorno = invocation.proceed();
			Permissoes permissoes = method.getAnnotation(Permissoes.class);
			if(permissoes!=null){
//				Permissao[] perms = permissoes.value();
//				if(perms.length==0){
//					for (int i = 0; i < perms.length; i++) {
//						if(perms[i].tipoAcesso().getNivel()>usuario.getAcesso().getNivel()){
//							throw new ECCSecurityException("Usuário sem acesso a esta operação.");
//						}
//					}
//				}
			} 
			Permissao permissao = method.getAnnotation(Permissao.class);
			if(permissao!=null){
//				if(permissao.tipoAcesso().getNivel()<usuario.getAcesso().getNivel()){
//					throw new PortalSecurityException("Usuário sem acesso a esta operação.");
//				}
			}
//			if(!permissao.operacao().equals(Operacao.VISUALIZAR)){
//				for(OperacaoExecutadaHandler hdlr:operacoesHandlers){
//					hdlr.onOperacaoExecutada(usuario, permissao, invocation);
//				}
//			}
			return retorno;
		} else {
			throw new WebSecurityException("Para funcionalidades que requerem segurança, a classe de serviço herdar de SecureRemoteServiceServlet");
		}
	}
	
}