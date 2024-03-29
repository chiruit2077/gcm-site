package br.com.ecc.client.service.patrimonio;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.ItemPatrimonio;
import br.com.ecc.model.vo.ItemPatrimonioVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ItemPatrimonioServiceAsync {
	
	void listaPorGrupo(Grupo grupo, AsyncCallback<List<ItemPatrimonio>> callback);
	void getVO(ItemPatrimonio itemPatrimonio,	AsyncCallback<ItemPatrimonioVO> callback);
	void exclui(ItemPatrimonio itemPatrimonio, AsyncCallback<Void> callback);
	void salva(ItemPatrimonioVO itemPatrimonio, AsyncCallback<ItemPatrimonio> callback);
}
