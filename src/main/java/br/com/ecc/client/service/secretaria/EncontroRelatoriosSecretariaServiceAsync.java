package br.com.ecc.client.service.secretaria;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroRelatoriosSecretariaServiceAsync {
	void imprimeRelatorioRomantico(Encontro encontro,AsyncCallback<Integer> callback);
	void geraCSV(Encontro encontro, String name, AsyncCallback<Integer> callback);
	void imprimeRelatorioAgrupamento(Agrupamento agrupamento, AsyncCallback<Integer> callback);
}