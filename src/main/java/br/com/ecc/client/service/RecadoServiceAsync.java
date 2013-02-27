package br.com.ecc.client.service;

import java.util.Date;
import java.util.List;

import br.com.ecc.model.Agenda;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Recado;
import br.com.ecc.model.vo.InicioVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RecadoServiceAsync {
	void listaInicial(Casal casal, Encontro encontro, AsyncCallback<InicioVO> callback);
	void listaPorCasal(Casal casal, Boolean lido, AsyncCallback<List<Recado>> callback);
	void listaPorGrupo(Grupo grupo, Casal casal, Date inicio, AsyncCallback<List<Recado>> callback);
	void salvar(Recado recado, AsyncCallback<Recado> callback);
	void listaTodosCasal(Casal casal, Date inicio, AsyncCallback<List<Recado>> callback);
	void salvarAgenda(Agenda agenda, Encontro encontro, AsyncCallback<List<Agenda>> webAsyncCallback);
	void excluiAgenda(Agenda agenda, Encontro encontro, AsyncCallback<List<Agenda>> webAsyncCallback);
}