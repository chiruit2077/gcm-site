package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.tipo.TipoExibicaoPlanilhaEnum;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroAtividadeInscricaoServiceAsync {
	void lista(Encontro encontro, AsyncCallback<List<EncontroAtividadeInscricao>> callback);
	void listaFiltrado(Encontro encontro, EncontroPeriodo encontroPeriodo, TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum, AsyncCallback<List<EncontroAtividadeInscricao>> callback);
	void salva(EncontroAtividadeInscricao encontroAtividadeInscricao, AsyncCallback<EncontroAtividadeInscricao> callback);
	void salvaInscricoes(EncontroAtividade encontroAtividade, EncontroInscricao encontroInscricao, List<EncontroAtividadeInscricao> listaParticipantes, AsyncCallback<Void> callback);
	void exclui(EncontroAtividadeInscricao encontroAtividadeInscricao, AsyncCallback<Void> asyncCallback);
	void imprimePlanilha(Encontro encontro, EncontroPeriodo encontroPeriodo, TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum, Boolean exportarExcel, AsyncCallback<Integer> callback);
}