package br.com.ecc.client.service.secretaria;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.EncontroRestaurante;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroRelatoriosSecretariaServiceAsync {
	void imprimeRelatorioRomantico(Encontro encontro,AsyncCallback<Integer> callback);
	void geraCSVCorel(Encontro encontro, String name, AsyncCallback<Integer> callback);
	void geraCSVCrachas(Encontro encontro, Agrupamento agrupamento, String name, AsyncCallback<Integer> callback);
	void imprimeRelatorioAgrupamento(Encontro encontro, Agrupamento agrupamento, AsyncCallback<Integer> callback);
	void imprimeRelatorioOnibus(Encontro encontro, Agrupamento agrupamento, AsyncCallback<Integer> callback);
	void imprimeRelatorioAlbum(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioOracaoAmor(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioNecessidadesEspeciais(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioDiabeticosVegetarianos(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioHotelAfilhados(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioHotelEncontristas(EncontroHotel encontroHotel, AsyncCallback<Integer> callback);
	void imprimeRelatorioRecepcaoFinal(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioRecepcaoInicial(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioAfilhadosPadrinhos(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioPlanilha(Encontro encontro, EncontroPeriodo periodo, EncontroInscricao inscricao, AsyncCallback<Integer> callback);
	void imprimeRelatorioMalas(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioCamarim(EncontroRestaurante restaurante, AsyncCallback<Integer> callback);
	void imprimeRelatorioHotelEncontristasHospedagemParticular(Encontro encontro, AsyncCallback<Integer> callback);
	void imprimeRelatorioEntregaCesta(Encontro encontro, AsyncCallback<Integer> callback);
}