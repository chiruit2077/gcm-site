package br.com.ecc.server.service.core;

import java.util.List;
import java.util.Map;

import br.com.ecc.client.service.EntidadeGenericaService;
import br.com.ecc.model._WebBaseEntity;
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
public class EntidadeGenericaServiceImpl extends SecureRemoteServiceServlet implements EntidadeGenericaService {
	private static final long serialVersionUID = 4647235286124485917L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar entidade", operacao=Operacao.VISUALIZAR)
	public List<_WebBaseEntity> listaEntidades(String queryName, Map<String, Object> params) throws Exception {
		if(queryName==null){
			return null;
		}
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery(queryName);
		if(params!=null){
			cmd.setParams(params );
		}
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir entidade", operacao=Operacao.EXCLUIR)
	public void excluiEntidade(_WebBaseEntity entidade) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(entidade);
		cmd.call();
	}


	@Override
	@Permissao(nomeOperacao="Salvar entidade", operacao=Operacao.SALVAR)
	public _WebBaseEntity salvaEntidade(_WebBaseEntity entidade) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(entidade);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> listaObjetos(String queryName, Map<String, Object> params) throws Exception {
		if(queryName==null){
			return null;
		}
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery(queryName);
		if(params!=null){
			cmd.setParams(params );
		}
		return cmd.call();
	}
}