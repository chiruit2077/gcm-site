package br.com.ecc.client.service;

import java.util.List;
import java.util.Map;

import br.com.ecc.model._WebBaseEntity;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EntidadeGenericaServiceAsync {
	void listaEntidades(String queryName, Map<String, Object> params, AsyncCallback<List<_WebBaseEntity>> callback);
	void salvaEntidade(_WebBaseEntity entidade, AsyncCallback<_WebBaseEntity> callback);
	void excluiEntidade(_WebBaseEntity entidade, AsyncCallback<Void> asyncCallback);
	void listaObjetos(String queryName, Map<String, Object> params, AsyncCallback<List<Object>> callback);
}
