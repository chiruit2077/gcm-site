package br.com.ecc.server.command.basico;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ExecuteUpdateCommand implements Callable<Void> {

	private Map<String, Object> params = new HashMap<String, Object>();
	private String NamedQuery;
	
	@Inject
	EntityManager entityManager;
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Void call() throws Exception {
		Query query = entityManager.createNamedQuery(NamedQuery);
		if (params!=null){
			for (Iterator iterator = params.keySet().iterator(); iterator.hasNext();) {
				String parametro = (String) iterator.next();
				query.setParameter(parametro, params.get(parametro));
			}
		}
		query.executeUpdate();
		return null;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void addParameter(String key, Object valor){
		params.put(key, valor);
	}
	
	public String getNamedQuery() {
		return NamedQuery;
	}

	public void setNamedQuery(String queryName) {
		this.NamedQuery = queryName;
	}

}
