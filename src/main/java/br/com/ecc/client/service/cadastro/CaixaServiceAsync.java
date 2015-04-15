package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Caixa;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.CaixaVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CaixaServiceAsync {
	void lista(Grupo grupo, AsyncCallback<List<Caixa>> callback);
	void salva(CaixaVO caixaVO, AsyncCallback<Void> callback);
	void exclui(Caixa caixa, AsyncCallback<Void> asyncCallback);
	void getVO(Caixa caixa, AsyncCallback<CaixaVO> callback);
}