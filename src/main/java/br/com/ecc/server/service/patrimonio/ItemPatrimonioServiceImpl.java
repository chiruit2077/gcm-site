package br.com.ecc.server.service.patrimonio;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.service.patrimonio.ItemPatrimonioService;
import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.ItemPatrimonio;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class ItemPatrimonioServiceImpl extends SecureRemoteServiceServlet implements ItemPatrimonioService {

	private static final long serialVersionUID = 2821656828386303597L;

	@Inject
	private Injector injector;
	
	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar grupos", operacao=Operacao.VISUALIZAR)
	public List<ItemPatrimonio> listaPorGrupo(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("itemPatrimonio.porGrupo");
		cmd.addParameter("grupo", grupo);
		List<ItemPatrimonio> itens = cmd.call();
		Collections.sort(itens, new Comparator<ItemPatrimonio>() {
			@Override
			public int compare(ItemPatrimonio o1, ItemPatrimonio o2) {
				if (o1.getPai()==null && o2.getPai()==null){
					return o1.getNome().compareTo(o2.getNome());
				} 
				if (o1.getPai()!=null && o2.getPai()!=null && o1.getPai().equals(o2.getPai())){
					return o1.getNome().compareTo(o2.getNome());
				}
				return 0;
			}
		});
		
		return itens;
	}

	@Override
	@Permissao(nomeOperacao="Excluir grupo", operacao=Operacao.EXCLUIR)
	public void exclui(ItemPatrimonio itemPatrimonio) throws Exception {
		//dependencias
		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
		cmdEntidade.setNamedQuery("itemPatrimonio.porItemPatrimonio");
		cmdEntidade.addParameter("itemPatrimonio", itemPatrimonio);
		if(cmdEntidade.call().size()>0){
			throw new WebException("Erro ao excluir Item. \nJá existem outros itens pertencentes a este item.");
		}
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(itemPatrimonio);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir grupo", operacao=Operacao.SALVAR)
	public ItemPatrimonio salva(ItemPatrimonio itemPatrimonio) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(itemPatrimonio);
		return (ItemPatrimonio)cmd.call();
	}

}