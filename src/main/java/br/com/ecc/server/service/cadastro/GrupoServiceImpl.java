package br.com.ecc.server.service.cadastro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.model.vo.GrupoVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class GrupoServiceImpl extends SecureRemoteServiceServlet implements GrupoService {
	private static final long serialVersionUID = -6986089366658250854L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar grupos", operacao=Operacao.VISUALIZAR)
	public List<Grupo> lista() throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("grupo.todos");
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Excluir grupo", operacao=Operacao.EXCLUIR)
	public void exclui(Grupo grupo) throws Exception {
		//dependencias
		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
		cmdEntidade.setNamedQuery("encontro.porGrupo");
		cmdEntidade.addParameter("grupo", grupo);
		List<Encontro> encontros = cmdEntidade.call();
		if(encontros!=null && encontros.size()>0){
			throw new WebException("Erro ao excluir Grupo. \nJá existem encontros neste grupo.");
		}
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(grupo);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar grupo", operacao=Operacao.SALVAR)
	public Grupo salva(Grupo grupo) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(grupo);
		return (Grupo)cmd.call();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public GrupoVO getVO(Grupo grupo){
		GrupoVO vo = new GrupoVO();
		
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("atividade.porGrupo");
		cmd.addParameter("grupo", grupo);
		vo.setListaAtividade(cmd.call());
		
		cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("papel.porGrupo");
		cmd.addParameter("grupo", grupo);
		vo.setListaPapel(cmd.call());
		
		cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontro.porGrupo");
		cmd.addParameter("grupo", grupo);
		vo.setListaEncontro(cmd.call());
		
		vo.setListaAgrupamentoVOGrupo(new ArrayList<AgrupamentoVO>());
		cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("agrupamento.porGrupo");
		cmd.addParameter("grupo", grupo);
		List<Agrupamento> listaAgrupamento = cmd.call();
		AgrupamentoVO agrupamentoVO;
		for (Agrupamento agrupamento : listaAgrupamento) {
			agrupamentoVO = new AgrupamentoVO();
			agrupamentoVO.setAgrupamento(agrupamento);
			
			cmd = injector.getInstance(GetEntityListCommand.class);
			cmd.setNamedQuery("agrupamentoMembro.porAgrupamento");
			cmd.addParameter("agrupamento", agrupamento);
			agrupamentoVO.setListaMembros(cmd.call());
			
			vo.getListaAgrupamentoVOGrupo().add(agrupamentoVO);
		}
		
		return vo;
	}
}