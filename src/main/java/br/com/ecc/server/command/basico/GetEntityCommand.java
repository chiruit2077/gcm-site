package br.com.ecc.server.command.basico;

import java.io.Serializable;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model._WebBaseEntity;

import com.google.inject.Inject;

@SuppressWarnings("all")
public class GetEntityCommand implements Callable<_WebBaseEntity>{

	@Inject
	private EntityManager em;
	
	private Serializable id;
	
	private Class clazz;
	
	@Override
	public _WebBaseEntity call() {
		_WebBaseEntity entity = (_WebBaseEntity) getEm().find(clazz, id); 
		return entity;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public EntityManager getEm() {
		return em;
	}

}
