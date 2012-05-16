package br.com.ecc.server.command.basico;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model._WebBaseEntity;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class DeleteEntityCommand implements Callable<Boolean>{
	@Inject EntityManager em;
	
	private _WebBaseEntity baseEntity;
	
	@Override
	@Transactional
	public Boolean call() throws Exception {
		em.remove(em.merge(baseEntity));
		return true;
	}

	public _WebBaseEntity getBaseEntity() {
		return baseEntity;
	}
	public void setBaseEntity(_WebBaseEntity baseEntity) {
		this.baseEntity = baseEntity;
	}
}