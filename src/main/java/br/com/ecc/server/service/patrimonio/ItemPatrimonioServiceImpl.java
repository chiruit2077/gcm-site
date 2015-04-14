package br.com.ecc.server.service.patrimonio;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.ecc.client.service.patrimonio.ItemPatrimonioService;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.ItemPatrimonio;
import br.com.ecc.model.ItemPatrimonioAtividade;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.ItemPatrimonioVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.ExecuteUpdateCommand;
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
	
	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public ItemPatrimonioVO getVO(ItemPatrimonio itemPatrimonio) throws Exception {
		ItemPatrimonioVO vo = new ItemPatrimonioVO();

		vo.setItemPatrimonio(itemPatrimonio);
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("itemPatrimonioAtividade.porItemPatrimonio");
		cmd.addParameter("itemPatrimonio", itemPatrimonio);
		vo.setListaItemPatrimonioAtividade(cmd.call());

		return vo;
	}
	

	@Override
	@Permissao(nomeOperacao="Excluir item", operacao=Operacao.EXCLUIR)
	public void exclui(ItemPatrimonio itemPatrimonio) throws Exception {
		//dependencias
		//verificar caixas
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("itemPatrimonioAtividade.porItemPatrimonio");
//		cmdEntidade.addParameter("itemPatrimonio", itemPatrimonio);
//		if(cmdEntidade.call().size()>0){
//			throw new WebException("Erro ao excluir Item. \nJá existem outros itens pertencentes a este item.");
//		}
		
		//exclusão
		ExecuteUpdateCommand cmdu = injector.getInstance(ExecuteUpdateCommand.class);
		cmdu.setNamedQuery("itemPatrimonioAtividade.deletePorItemPatrimonio");
		cmdu.addParameter("itemPatrimonio", itemPatrimonio);
		cmdu.call();
		
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(itemPatrimonio);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir grupo", operacao=Operacao.SALVAR)
	public ItemPatrimonio salva(ItemPatrimonioVO itemPatrimonioVO) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(itemPatrimonioVO.getItemPatrimonio());
		itemPatrimonioVO.setItemPatrimonio((ItemPatrimonio)cmd.call());
		
		for (ItemPatrimonioAtividade itemAtividade : itemPatrimonioVO.getListaItemPatrimonioAtividade()) {
			itemAtividade.setItemPatrimonio(itemPatrimonioVO.getItemPatrimonio());
			cmd.setBaseEntity(itemAtividade);
			cmd.call();
		}
		
		return itemPatrimonioVO.getItemPatrimonio();
	}

}