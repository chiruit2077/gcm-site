package br.com.ecc.server.service.cadastro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.cadastro.AgrupamentoService;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.AgrupamentoExcluirCommand;
import br.com.ecc.server.command.AgrupamentoSalvarCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class AgrupamentoServiceImpl extends SecureRemoteServiceServlet implements AgrupamentoService {
	private static final long serialVersionUID = -6986089366658250854L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar agrupamentos por grupo", operacao=Operacao.VISUALIZAR)
	public List<AgrupamentoVO> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("agrupamento.porGrupo");
		cmd.addParameter("grupo", grupo);
		List<Agrupamento> call = cmd.call();
		List<AgrupamentoVO> listavo = new ArrayList<AgrupamentoVO>();
		for (Agrupamento agrupamento : call) {
			AgrupamentoVO vo = new AgrupamentoVO();
			vo.setAgrupamento(agrupamento);
			GetEntityListCommand cmdMembros = injector.getInstance(GetEntityListCommand.class);
			cmdMembros.setNamedQuery("agrupamentoMembro.porAgrupamento");
			cmdMembros.addParameter("agrupamento", agrupamento);
			vo.setListaMembros(cmdMembros.call());
			listavo.add(vo);
		}
		return listavo;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar agrupamentos por encontro", operacao=Operacao.VISUALIZAR)
	public List<AgrupamentoVO> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("agrupamento.porEncontro");
		cmd.addParameter("encontro", encontro);
		List<Agrupamento> call = cmd.call();
		List<AgrupamentoVO> listavo = new ArrayList<AgrupamentoVO>();
		for (Agrupamento agrupamento : call) {
			AgrupamentoVO vo = new AgrupamentoVO();
			vo.setAgrupamento(agrupamento);
			GetEntityListCommand cmdMembros = injector.getInstance(GetEntityListCommand.class);
			cmdMembros.setNamedQuery("agrupamentoMembro.porAgrupamento");
			cmdMembros.addParameter("agrupamento", agrupamento);
			vo.setListaMembros(cmdMembros.call());
			listavo.add(vo);
		}
		return listavo;
	}

	@Override
	@Permissao(nomeOperacao="Excluir agrupamento", operacao=Operacao.EXCLUIR)
	public void exclui(Agrupamento agrupamento) throws Exception {
		AgrupamentoExcluirCommand cmd = injector.getInstance(AgrupamentoExcluirCommand.class);
		cmd.setAgrupamento(agrupamento);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar agrupamento", operacao=Operacao.SALVAR)
	public void salva(AgrupamentoVO agrupamentoVO) throws Exception {
		AgrupamentoSalvarCommand cmd = injector.getInstance(AgrupamentoSalvarCommand.class);
		cmd.setAgrupamentoVO(agrupamentoVO);
		cmd.call();
	}

	@Override
	@SuppressWarnings("unchecked")
	public AgrupamentoVO getVO(Agrupamento agrupamento){
		AgrupamentoVO vo = new AgrupamentoVO();

		vo.setAgrupamento(agrupamento);

		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("agrupamentoMembro.porAgrupamento");
		cmd.addParameter("agrupamento", agrupamento);
		vo.setListaMembros(cmd.call());

		return vo;
	}
}