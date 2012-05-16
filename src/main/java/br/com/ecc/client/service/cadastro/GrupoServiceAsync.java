package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.GrupoVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GrupoServiceAsync {
	void lista(AsyncCallback<List<Grupo>> callback);
	void salva(Grupo grupo, AsyncCallback<Grupo> callback);
	void exclui(Grupo grupo, AsyncCallback<Void> asyncCallback);
	void getVO(Grupo grupo, AsyncCallback<GrupoVO> callback);
}