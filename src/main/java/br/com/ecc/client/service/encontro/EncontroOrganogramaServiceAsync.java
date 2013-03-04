package br.com.ecc.client.service.encontro;

import java.util.List;

import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.vo.EncontroOrganogramaVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EncontroOrganogramaServiceAsync {

	void lista(Encontro encontro, AsyncCallback<List<EncontroOrganograma>> callback);
	void salvaEncontroOrganogramaCoordenacao(EncontroOrganogramaVO encontroOrganogramaVO,AsyncCallback<EncontroOrganogramaVO> callback);
	void getVO(EncontroOrganograma encontroOrganograma,AsyncCallback<EncontroOrganogramaVO> callback);
	void salva(EncontroOrganograma encontroOrganograma,AsyncCallback<EncontroOrganograma> callback);
	void exclui(EncontroOrganograma encontroOrganograma,AsyncCallback<Void> callback);
}
