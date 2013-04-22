package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroInscricaoFichaPagamentoServiceAsync {
	void listaFichas(Encontro encontroSelecionado, AsyncCallback<List<EncontroInscricaoFichaPagamento>> callback);
	void salvaFicha(EncontroInscricaoFichaPagamento ficha, AsyncCallback<Void> callback);
	void geraFichasVagas(Encontro encontroSelecionado,AsyncCallback<Void> callback);
	void excluiFicha(EncontroInscricaoFichaPagamento ficha, AsyncCallback<Void> callback);
}