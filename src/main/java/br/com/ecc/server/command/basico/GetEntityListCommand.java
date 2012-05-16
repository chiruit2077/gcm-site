package br.com.ecc.server.command.basico;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model._WebBaseEntity;

import com.google.inject.Inject;
import com.google.inject.Provider;

@SuppressWarnings("all")
public class GetEntityListCommand implements Callable<List>{

	private Map<String, Object> params = new HashMap<String, Object>();

	private String namedQuery;

	private String query;
	
	private Integer maxResults;
	
	@Inject
	Provider<EntityManager> em;
	
	@Override
	public List call() {

		Query query = null;
		if(namedQuery !=null){
			query = em.get().createNamedQuery(namedQuery);
		}else if(this.query !=null){
			query = em.get().createQuery(this.query);
		}
		if (params!=null){
			for (Iterator iterator = params.keySet().iterator(); iterator.hasNext();) {
				String parametro = (String) iterator.next();
				query = query.setParameter(parametro, params.get(parametro));
			}
		}
		if(maxResults!=null){
			query.setMaxResults(maxResults);
		}
		List<_WebBaseEntity> lista = query.getResultList();
		
		return lista;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setNamedQuery(String queryName) {
		this.namedQuery = queryName;
	}

	public String getNamedQuery() {
		return namedQuery;
	}

	public void addParameter(String key, Object valor){
		params.put(key, valor);
	}
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean hasParams() {
		return !params.isEmpty();
	}
	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

}
