package br.com.ecc.client.service.patrimonio;

import java.util.List;

import br.com.ecc.model.Grupo;
import br.com.ecc.model.ItemPatrimonio;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("itemPatrimonio")
public interface ItemPatrimonioService extends RemoteService {
	public List<ItemPatrimonio> listaPorGrupo(Grupo grupo) throws Exception;
	public void exclui(ItemPatrimonio itemPatrimonio) throws Exception;
	public ItemPatrimonio salva(ItemPatrimonio itemPatrimonio) throws Exception;
}
