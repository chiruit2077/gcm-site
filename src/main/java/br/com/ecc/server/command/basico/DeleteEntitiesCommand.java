package br.com.ecc.server.command.basico;

import java.util.Collection;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model._WebBaseEntity;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class DeleteEntitiesCommand implements Callable<Boolean>{
	@Inject EntityManager em;
	@SuppressWarnings("rawtypes")
	private Class clazz;
	private Collection<Integer> ids;
	
	@Override
	@Transactional
	public Boolean call() throws Exception {
		if(clazz == null) {
			throw new WebException("Use o metodo setClazz(...) antes de chamar o call()");
		} else if(ids == null) {
			throw new WebException("Use o metodo setIds(...) antes de chamar o call()");
		}
		
		for(Integer id : ids) {
			@SuppressWarnings("unchecked")
			_WebBaseEntity entity = (_WebBaseEntity)em.find(clazz, id);
			em.remove(entity);
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Class getClazz() {
		return clazz;
	}

	public void setClazz(@SuppressWarnings("rawtypes") Class clazz) {
		this.clazz = clazz;
	}

	public Collection<Integer> getIds() {
		return ids;
	}

	public void setIds(Collection<Integer> ids) {
		this.ids = ids;
	}

}
