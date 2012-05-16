package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.AgrupamentoVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AgrupamentoServiceAsync {
	void lista(Grupo grupo, AsyncCallback<List<Agrupamento>> callback);
	void lista(Encontro encontro, AsyncCallback<List<Agrupamento>> callback);
	void salva(AgrupamentoVO agrupamentoVO, AsyncCallback<Void> callback);
	void exclui(Agrupamento agrupamento, AsyncCallback<Void> asyncCallback);
	void getVO(Agrupamento agrupamento, AsyncCallback<AgrupamentoVO> callback);
}