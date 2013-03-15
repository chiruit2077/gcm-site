package br.com.ecc.client.service.cadastro;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.Organograma;
import br.com.ecc.model.vo.OrganogramaVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OrganogramaServiceAsync {
	void lista(Grupo grupo, AsyncCallback<List<Organograma>> callback);
	void salva(Organograma organograma, AsyncCallback<Organograma> callback);
	void exclui(Organograma organograma, AsyncCallback<Void> asyncCallback);
	void getVO(Organograma organograma, AsyncCallback<OrganogramaVO> callback);
	void salvaOrganograma(OrganogramaVO vo, AsyncCallback<OrganogramaVO> callback);
}