package br.com.ecc.server.command.basico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Injector;

public abstract class ECCBaseCommand<T> implements Callable<T>, Serializable {

	private static final long serialVersionUID = -1595570425152701883L;

	@Inject EntityManager entityManager;
	
	@Inject Injector injector;
	
	private List<String> errors = new ArrayList<String>();

	public List<String> getErrors() {
		return errors;
	}
	public void addError(String error) {
		errors.add(error);
	}
	public Injector getInjector() {
		return injector;
	}
	public EntityManager getEntityManager() {
		return entityManager;
	}
}