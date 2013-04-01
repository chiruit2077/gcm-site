package br.com.ecc.core.mvp.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.event.PlaceEvent;
import br.com.ecc.client.util.StringTokenizer;
import br.com.ecc.core.mvp.eventbus.EventBus;
import br.com.ecc.core.mvp.presenter.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class PlaceManager implements Serializable, ValueChangeHandler<String> {

	private static final long serialVersionUID = 6272122458856752177L;

	/**
	 * Apesar do IE suportar até 2048 caracteres na url coloquei 1900 por segurança e para descontar o contexto
	 */
	private static final int MAX_TOKEN_SIZE = 1900;
	private static final String HT_DELIM = "_HT_";
	public static final String HOME_PRESENTER = "br.com.ecc.client.ui.home.HomePresenter";

	private final EventBus eventBus;

	@Inject
	public PlaceManager(final EventBus eventBus) {
		History.addValueChangeHandler(this);
		this.eventBus = eventBus;
	}

	public void currentPlace() {
		if (History.getToken() != null) {
			History.fireCurrentHistoryState();
		}
	}

	public void newPlace(@SuppressWarnings("rawtypes") Class<? extends Presenter> presenter) {
		newPlace(new StateHistory(presenter));
	}

	public void newPlace(StateHistory newStateHistory) {
		newPlace(newStateHistory, true, true);
	}

	public void newPlaceClean(@SuppressWarnings("rawtypes") Class<? extends Presenter> presenter) {
		newPlace(new StateHistory(presenter), true, false);
	}

	public void newPlaceClean(StateHistory state) {
		newPlace(state, true, false);
	}

	private void newPlace(StateHistory newStateHistory, boolean refresh, boolean addToken) {
		String decodeHistoryToken = "";

		if(addToken) {
			if(History.getToken()!=null && !"".equals(History.getToken())) {
				decodeHistoryToken = CipherUtil.decodeToken(History.getToken());
			}
		}

		List<String> lstTokens = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(decodeHistoryToken, HT_DELIM);

		String newJsonHistoryToken = newStateHistory.getHistoryTokenAsJson(false);
		if(newJsonHistoryToken.length() > 350) {
			GWT.log("ATENÇÃO: O StateHistory para o Presenter: "+newStateHistory.getPresenterName()+" possui mais de 350 letras. " +
					"Você pode estar comprometendo o número de abas aberta do sistema. Token: "+newJsonHistoryToken);
		}
		for(String jhToken : st.getTokens()) {
			StateHistory sh = new StateHistory();
			sh.setHistoryTokenAsJson(jhToken);
			sh.setShow(false);

			if(sh.getHistoryTokenAsJson(false).equals(newJsonHistoryToken)) {
				sh.setShow(true);
				newStateHistory = null;
			}

			lstTokens.add( sh.getHistoryTokenAsJson() );
		}
		if(newStateHistory != null) {
			newStateHistory.setShow(true);
			lstTokens.add(newStateHistory.getHistoryTokenAsJson());
		}

		String newDecodeHistoryToken = joinTokens(lstTokens);
		if(! decodeHistoryToken.equals(newDecodeHistoryToken)) {
			newPlace(CipherUtil.encodeToken(newDecodeHistoryToken), refresh);
		}
	}

	private void newPlace(String encodeHistoryToken, boolean refresh) {
		if(encodeHistoryToken.length() > MAX_TOKEN_SIZE) {
			String message = "O número de abas em abertas estourou o mecanismo de histórico da aplicação. Tente fechar uma ou mais abas antes de continuar.";
			GWT.log(message);
			GWT.log("HistoryToken gigante que falhou: " + CipherUtil.decodeToken(encodeHistoryToken));
			Window.alert(message);
			return;
		}

		if (History.getToken().equals(encodeHistoryToken)) {
			currentPlace();
		} else {
			History.newItem( encodeHistoryToken, refresh );
		}
	}

	public void removePlace(StateHistory... tokensToRemove) {
		removePlace(false, tokensToRemove);
	}

	public void showPlace( String jsonHistoryTokenSelect ) {
		StateHistory stateHistory = new StateHistory();
		stateHistory.setHistoryTokenAsJson(jsonHistoryTokenSelect);
		newPlace(stateHistory, false, true);
	}

	public void updatePlace(StateHistory oldStateHistory, StateHistory newStateHistory) {
		if(oldStateHistory != null && newStateHistory != null) {
			newStateHistory.setShow( oldStateHistory.getShow() );

			String currentToken = oldStateHistory.getHistoryTokenAsJson(true);
			String newToken = newStateHistory.getHistoryTokenAsJson(true);

			List<String> tokens = new ArrayList<String>();
			String decodeHistoryToken = CipherUtil.decodeToken(History.getToken());
			StringTokenizer st = new StringTokenizer(decodeHistoryToken, HT_DELIM);
			for (String jsToken : st.getTokens()) {
				if(jsToken.equals(newToken)) {
					// token novo e igual a outro token ja existente, nao faz nada
					GWT.log("Não pude executar o updatePlace(...). O novo place já encontra-se aberto. token: " + newToken);
					return;
				}

				if(jsToken.equals(currentToken)) {
					tokens.add(newToken);
				}
				else {
					tokens.add(jsToken);
				}
			}

//			PlaceUpdateEvent event = new PlaceUpdateEvent();
//			event.setCurrentStateHistory(oldStateHistory);
//			event.setNewStateHistory(newStateHistory);
//			eventBus.fireEvent(event);
			newPlace( CipherUtil.encodeToken(joinTokens(tokens)), false );
		}
	}

	public void updatePlace(List<StateHistory> states) {
		List<String> tokens = new ArrayList<String>();
		boolean show = false;
		for (StateHistory state : states) {
			if(state.getShow()) {
				show = true;
				break;
			}
		}
		for (StateHistory state : states) {
			if(show == false && state.getPresenterName().equals(HOME_PRESENTER)) {
				state.setShow(true); // nao tem ninguem marcado para ser exibido, seta o home
			}
			tokens.add(state.getHistoryTokenAsJson());
		}
		newPlace( CipherUtil.encodeToken(joinTokens(tokens)), false );
	}

	private boolean possuiToken(String historyToken, StateHistory... tokensToRemove) {
		for(StateHistory state : tokensToRemove) {
			if(state.getHistoryTokenAsJson(false).equals(historyToken)) {
				return true;
			}
		}
		return false;
	}

	private void removePlace(boolean refresh, StateHistory... tokensToRemove) {
		if(tokensToRemove != null) {
			List<StateHistory> states = new ArrayList<StateHistory>();
			String decodeHistoryToken = CipherUtil.decodeToken(History.getToken());
			StringTokenizer st = new StringTokenizer(decodeHistoryToken, HT_DELIM);

			int indexNextTokenToShow = -1;
			for (int i=0; i < st.getTokens().length;  i++) {
				String jsToken = st.getTokens()[i];
				StateHistory state = new StateHistory();
				state.setHistoryTokenAsJson(jsToken);
				state.setShow(false);
				if( possuiToken(state.getHistoryTokenAsJson(), tokensToRemove) ) {
					indexNextTokenToShow = i;
				}
				else {
					states.add(state);
				}
			}
			if (states.size() <= indexNextTokenToShow) {
				indexNextTokenToShow = states.size()-1;
			}
			states.get(indexNextTokenToShow).setShow(true);

			List<String> lstTokens = new ArrayList<String>();
			for (StateHistory state : states) {
				lstTokens.add( state.getHistoryTokenAsJson() );
			}

			newPlace( CipherUtil.encodeToken(joinTokens(lstTokens)), refresh );
		}
	}

	public List<StateHistory> getStates() {
		List<StateHistory> states = new ArrayList<StateHistory>();
		String decodeHistoryToken = CipherUtil.decodeToken(History.getToken());
		StringTokenizer st = new StringTokenizer(decodeHistoryToken, HT_DELIM);
		for( String jsToken : st.getTokens() ) {
			StateHistory sh = new StateHistory();
			sh.setHistoryTokenAsJson(jsToken);
			states.add(sh);
		}
		return states;
	}

	private String joinTokens(List<String> tokens) {
		StringBuffer sb = new StringBuffer();
		sb.append(tokens.get(0));
		for(int i=1; i<tokens.size(); i++) {
			sb.append(HT_DELIM);
			sb.append(tokens.get(i));
		}
		return sb.toString();
	}

	public String getPresenterName(String jsonHistoryToken) {
		if(jsonHistoryToken!=null && !"".equals(jsonHistoryToken.trim())) {
			try {
				StateHistory stateHistory = new StateHistory();
				stateHistory.setHistoryTokenAsJson(jsonHistoryToken);

				return stateHistory.getPresenterName();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getValue() != null) {
			if("".equals(event.getValue()) ) {
				newPlace(new StateHistory( HOME_PRESENTER )); // fica no HOME de qualquer jeito!
			}
			else {
				List<StateHistory> states = new ArrayList<StateHistory>();

				String decodeHistoryToken = CipherUtil.decodeToken(event.getValue());
				StringTokenizer  sTokenizer = new StringTokenizer(decodeHistoryToken, HT_DELIM);
				for (String jsonHistoryToken : sTokenizer.getTokens()) {
					StateHistory stateHistory = new StateHistory();
					stateHistory.setHistoryTokenAsJson(jsonHistoryToken);

					states.add(stateHistory);
				}

				PlaceEvent placeEvent = new PlaceEvent();
				placeEvent.setStates(states);

				eventBus.fireEvent(placeEvent);
			}
		}
	}


}
