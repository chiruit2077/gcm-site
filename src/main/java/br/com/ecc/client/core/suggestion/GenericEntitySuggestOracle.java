package br.com.ecc.client.core.suggestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ecc.client.service.EntidadeGenericaService;
import br.com.ecc.client.service.EntidadeGenericaServiceAsync;
import br.com.ecc.model._WebBaseEntity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public class GenericEntitySuggestOracle extends SuggestOracle {

    private Request currentRequest;
    private Callback currentCallback;
    
    private EntidadeGenericaServiceAsync service = GWT.create(EntidadeGenericaService.class);
    private Map<String, Object> queryParams = new HashMap<String, Object>();
    private String suggestQuery;
    private Integer limiteExibicao = 15;
    private Integer minimoCaracteres = 1;
    private TipoQueryEnum tipoQuery = TipoQueryEnum.ENTIDADE;
    private List<_WebBaseEntity> listaEntidades;
    private List<Object> listaObjetos;
    private boolean habilitado = true;

    @Override
    public void requestSuggestions(Request request, Callback callback) {
    	if(!habilitado) return;
        this.currentRequest = request;
        this.currentCallback = callback;
        if(getSuggestQuery()!=null && !getSuggestQuery().equals("")){
        	if(request.getQuery().length()>=getMinimoCaracteres()){
        		getQueryParams().put("key", "%"+request.getQuery()+"%");
        		if(tipoQuery.equals(TipoQueryEnum.ENTIDADE)){
	        		service.listaEntidades(getSuggestQuery(), getQueryParams(), new AsyncCallback<List<_WebBaseEntity>>() {
	        			@Override
	        			public void onFailure(Throwable caught) {
	        				caught.printStackTrace();
	        			}
	        			@Override
	        			public void onSuccess(List<_WebBaseEntity> result) {
	        				setListaEntidades(result);
	        				createSuggestionsForGroupIdsEntidade(result);
	        			}
	        		});
        		} else if(tipoQuery.equals(TipoQueryEnum.OBJECT)){
	        		service.listaObjetos(getSuggestQuery(), getQueryParams(), new AsyncCallback<List<Object>>() {
	        			@Override
	        			public void onFailure(Throwable caught) {
	        				caught.printStackTrace();
	        			}
	        			@Override
	        			public void onSuccess(List<Object> result) {
	        				setListaObjetos(result);
	        				createSuggestionsForGroupIdsObject(result);
	        			}
	        		});
        		}
        	} else {
        		if(tipoQuery.equals(TipoQueryEnum.ENTIDADE)){
        			setListaEntidades(new ArrayList<_WebBaseEntity>());
        		} else if(tipoQuery.equals(TipoQueryEnum.OBJECT)){
        			setListaObjetos(new ArrayList<Object>());
        		}
        	}
        }
    }
	private void createSuggestionsForGroupIdsObject(List<Object> entidades) {
		List<EntidadeGenericaSuggestion> suggestions = new ArrayList<EntidadeGenericaSuggestion>();
		int i=0;
		for (Object entidade : entidades) {
			suggestions.add(new EntidadeGenericaSuggestion(entidade.toString()));
			if(i>getLimiteExibicao()) break;
			i++;
		}
		Response response = new Response(suggestions);
		currentCallback.onSuggestionsReady(currentRequest, response);
	}
	private void createSuggestionsForGroupIdsEntidade(List<_WebBaseEntity> entidades) {
		List<EntidadeGenericaSuggestion> suggestions = new ArrayList<EntidadeGenericaSuggestion>();
		int i=0;
		for (_WebBaseEntity entidade : entidades) {
            suggestions.add(new EntidadeGenericaSuggestion(entidade.toString()));
	        if(i>getLimiteExibicao()) break;
	        i++;
        }
		Response response = new Response(suggestions);
		currentCallback.onSuggestionsReady(currentRequest, response);
    }

	public void setQueryParams(Map<String, Object> queryParams) {
		this.queryParams = queryParams;
	}
	public Map<String, Object> getQueryParams() {
		return queryParams;
	}
	public void setSuggestQuery(String suggestQuery) {
		this.suggestQuery = suggestQuery;
	}
	public String getSuggestQuery() {
		return suggestQuery;
	}
	public void setListaEntidades(List<_WebBaseEntity> listaEntidades) {
		this.listaEntidades = listaEntidades;
	}
	public List<_WebBaseEntity> getListaEntidades() {
		return listaEntidades;
	}
	public void setLimiteExibicao(Integer limiteExibicao) {
		this.limiteExibicao = limiteExibicao;
	}
	public Integer getLimiteExibicao() {
		return limiteExibicao;
	}
	public void setMinimoCaracteres(Integer minimoCaracteres) {
		this.minimoCaracteres = minimoCaracteres;
	}
	public Integer getMinimoCaracteres() {
		return minimoCaracteres;
	}
	public TipoQueryEnum getTipoQuery() {
		return tipoQuery;
	}
	public void setTipoQuery(TipoQueryEnum tipoQuery) {
		this.tipoQuery = tipoQuery;
	}
	public List<Object> getListaObjetos() {
		return listaObjetos;
	}
	public void setListaObjetos(List<Object> listaObjetos) {
		this.listaObjetos = listaObjetos;
	}
	public boolean isHabilitado() {
		return habilitado;
	}
	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
}