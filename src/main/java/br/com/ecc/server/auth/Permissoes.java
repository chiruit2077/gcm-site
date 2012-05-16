package br.com.ecc.server.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * As permissões relacionadas aqui tem o comportamento do tipo OU. Ou seja
 * @Permissoes({@Permissao(presenter=X.class),@Permissao(presenterY.class)})
 * Para acessar o recurso o usuário deverá possuir a permissao X ou a permissao Y
 * @author lucas
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface Permissoes {
	Permissao[] value();
}
