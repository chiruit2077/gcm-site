package br.com.ecc.core.mvp.history;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ecc.core.mvp.presenter.Presenter;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class StateHistory implements Serializable {
	private static final String SHOW_TAB = "showTab";
	private static final String PRESENTER_NAME = "presenterName";
	private static final long serialVersionUID = -1607694356523506903L;
	private Map<String, Serializable> state = new HashMap<String, Serializable>();
	
	private String presenterName;
	
    protected StateHistory() {
    	this.state.clear();
    }
	
	@SuppressWarnings("rawtypes")
	public StateHistory(Class<? extends Presenter> presenter) {
		this(presenter.getName());
	}
	
	public StateHistory(String presenterName) {
		this();
		this.presenterName = presenterName;
	}
	
	@SuppressWarnings("unchecked")
	protected void setHistoryTokenAsJson(String jsonHistoryToken) {
		if(jsonHistoryToken != null && !"".equals(jsonHistoryToken.trim())) {
			JSONValue jsonValue = JSONParser.parseLenient( jsonHistoryToken );
			Map<String, Serializable> state = (Map<String, Serializable>) JSONUtil.parseObject(jsonValue);
			
			this.state.clear();
			presenterName = (String) state.get(PRESENTER_NAME);
			if(presenterName == null) {
				throw new RuntimeException("Nao encontrei o nome do presenter no token informado! Use o metodo getHistoryTokenAsJson()");
			}
			state.remove(PRESENTER_NAME);
			this.state = state;
		}
		else {
			throw new RuntimeException("Prezado idiota, digo, programador, voce passou um historyToken nulo ou invalido.");
		}
	}
	
	public String getHistoryTokenAsJson() {
		Map<String, Object> stateAsJson = new HashMap<String, Object>(this.state);
		stateAsJson.put(PRESENTER_NAME, presenterName);
		JSONValue jsonValue = JSONUtil.serializeAsJson(stateAsJson);
		if (jsonValue != null) {
			return jsonValue.toString();
		}
		return "";
	}
	
	public void setShow(Boolean show) {
		if(show) {
			this.put(SHOW_TAB, "true");
		}
		else {
			this.remove(SHOW_TAB);
		}
	}
	
	public boolean getShow() {
		return new Boolean(this.getAsString(SHOW_TAB));
	}
	
	public String getHistoryTokenAsJson(boolean showInfo) {
		String s = getHistoryTokenAsJson();
		if(! showInfo) {
			s = s.replaceAll("\"showTab\":\"true\", ", "");
		}
		return s;
	}
	
	public String getPresenterName() {
		return presenterName;
	}
	
	public String getPresenterSimpleName() {
		String simpleName = null;
		if(presenterName != null && !"".equals(presenterName.trim())) {
			try {
				simpleName = presenterName.substring(presenterName.lastIndexOf(".")+1, presenterName.length());
			} catch (Exception e) {}
		}
		return simpleName;
	}
	
	public Object put(String key, Serializable value) {
		if(PRESENTER_NAME.equals(key)) {
			throw new RuntimeException("A key 'presenterName' é reservado para uso interno da classe. Use outra key.");
		}
		return this.state.put(key, value);
	}
	
	public Object remove(String key) {
		if (PRESENTER_NAME.equals(key)) {
			return null;
		}
		return this.state.remove(key);
	}
	/**
	 * @see Object getAsInteger(value)
	 * @see Object getAsLong(value)
	 * @see Object getAsDouble(value)
	 * @see Object getAsDate(value)
	 * @see Object getAsString(value)
	 * @see Object getAsList(value)
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return this.state.get(key);
	}
	
	public int size() {
		return state.size();
	}
	
	public Map<String, Serializable> getUnmodifiableState() {
		return Collections.unmodifiableMap(state);
	}
	
	public Integer getAsInteger(String key) {
		Object obj = state.get(key);
		if(obj != null && obj instanceof Integer) {
			return (Integer) obj;
		}
		return null; 
	}
	
	public Long getAsLong(String key) {
		Object obj = state.get(key);
		if(obj != null && obj instanceof Long) {
			return (Long) obj;
		}
		return null; 
	}
	
	public Double getAsDouble(String key) {
		Object obj = state.get(key);
		if(obj != null && obj instanceof Double) {
			return (Double) obj;
		}
		return null; 
	}
	
	public Date getAsDate(String key) {
		Object obj = state.get(key);
		if(obj != null && obj instanceof Date) {
			return (Date) obj;
		}
		return null; 
	}

	public String getAsString(String key) {
		Object obj = state.get(key);
		if(obj != null && obj instanceof String) {
			return (String) obj;
		}
		return null; 
	}
	
	@SuppressWarnings("rawtypes")
	public List getAsList(String key) {
		Object obj = state.get(key);
		if(obj != null && obj instanceof List) {
			return (List) obj;
		}
		return null; 
	}

	public void setState(Map<String, Serializable> state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof StateHistory)) {
			return false;
		}
		
		StateHistory oState = (StateHistory) obj;
		if(! oState.getPresenterName().equals(presenterName)) {
			return false;
		}
		
		Map<String, Serializable> localState = new HashMap<String, Serializable>(this.state);
		localState.remove(SHOW_TAB);
		
		Map<String, Serializable> testState = new HashMap<String, Serializable>(oState.state);
		testState.remove(SHOW_TAB);
		
		if(! localState.equals(testState)) {
			return false;
		}
		
		return true;
	}

}
