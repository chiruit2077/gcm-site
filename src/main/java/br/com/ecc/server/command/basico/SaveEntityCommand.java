package br.com.ecc.server.command.basico;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model._WebBaseEntity;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class SaveEntityCommand implements Callable<_WebBaseEntity>{
	@Inject
	EntityManager em;
	
	private _WebBaseEntity baseEntity;
	
	@Override
	@Transactional
	public _WebBaseEntity call() {
		baseEntity = em.merge(baseEntity); 
		return baseEntity;
	}

	public _WebBaseEntity getBaseEntity() {
		return baseEntity;
	}

	public void setBaseEntity(_WebBaseEntity baseEntity) {
		this.baseEntity = baseEntity;
	}

}
