package br.com.ecc.client.util;

import java.util.List;

import br.com.ecc.model._WebBaseEntity;

public class ListUtil {

	public static _WebBaseEntity getEntidadePorNome(List<_WebBaseEntity> listaEntidades, String nome){
		_WebBaseEntity entidade = null;
		if(listaEntidades!=null){
			for (_WebBaseEntity entidadeLista : listaEntidades) {
				if(entidadeLista.toString().equals(nome)){
					return entidadeLista;
				}
			}
		}
		return entidade;
	}
	
}