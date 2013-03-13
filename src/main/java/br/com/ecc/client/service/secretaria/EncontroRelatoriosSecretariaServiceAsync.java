package br.com.ecc.client.service.secretaria;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroRelatoriosSecretariaServiceAsync {
	void imprimeRelatorioRomantico(Encontro encontro,AsyncCallback<Integer> callback);
	void geraCSV(Encontro encontro, String name, AsyncCallback<Integer> callback);
	void imprimeRelatorioAgrupamento(Encontro encontro, Agrupamento agrupamento, AsyncCallback<Integer> callback);
	void imprimeRelatorioOnibus(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioAlbum(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioOracaoAmor(Encontro encontro, AsyncCallback<Integer> callback);
}