package br.com.ecc.server.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.ecc.model.tipo.Operacao;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface Permissao {
	public Operacao operacao() default Operacao.OUTRO;
	public String nomeOperacao() default "";
}
