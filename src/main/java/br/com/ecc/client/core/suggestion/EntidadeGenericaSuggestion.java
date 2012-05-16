package br.com.ecc.client.core.suggestion;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class EntidadeGenericaSuggestion implements Suggestion {

    private String sugestao;

    public EntidadeGenericaSuggestion(String sugestao) {
        this.sugestao = sugestao;
    }

    public String getDisplayString() {
        return sugestao;
    }

	@Override
	public String getReplacementString() {
		return sugestao;
	}
}