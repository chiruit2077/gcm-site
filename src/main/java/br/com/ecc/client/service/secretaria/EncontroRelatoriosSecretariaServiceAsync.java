package br.com.ecc.client.service.secretaria;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroRelatoriosSecretariaServiceAsync {
	void imprimeRelatorioRomantico(Encontro encontro,AsyncCallback<Integer> callback);
	void geraCSV(Encontro encontro, String name, AsyncCallback<Integer> callback);
	void imprimeRelatorioAgrupamento(Encontro encontro, Agrupamento agrupamento, AsyncCallback<Integer> callback);
	void imprimeRelatorioOnibus(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioAlbum(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioOracaoAmor(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioNecessidadesEspeciais(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioDiabeticosVegetarianos(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioHotelAfilhados(EncontroHotel encontroHotel, AsyncCallback<Integer> callback);
	void imprimeRelatorioHotelEncontristas(EncontroHotel encontroHotel, AsyncCallback<Integer> callback);
	void imprimeRelatorioRecepcaoFinal(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioRecepcaoInicial(Encontro encontro, AsyncCallback<Integer> callback);
}