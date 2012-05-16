package br.com.ecc.client.service;

import java.util.List;
import java.util.Map;

import br.com.ecc.model._WebBaseEntity;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("entidadegenerica")
public interface EntidadeGenericaService extends RemoteService {
	public List<_WebBaseEntity> listaEntidades(String queryName, Map<String, Object> params) throws Exception;
	public void excluiEntidade(_WebBaseEntity entidade) throws Exception;
	public _WebBaseEntity salvaEntidade(_WebBaseEntity entidade) throws Exception;
	public List<Object> listaObjetos(String queryName, Map<String, Object> params) throws Exception;
}
