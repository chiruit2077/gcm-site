package br.com.ecc.server.service.cadastro;

import java.util.List;

import br.com.ecc.client.service.cadastro.OrganogramaService;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Organograma;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.OrganogramaVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.OrganogramaExcluirCommand;
import br.com.ecc.server.command.OrganogramaSalvarCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class OrganogramaServiceImpl extends SecureRemoteServiceServlet implements OrganogramaService {

	private static final long serialVersionUID = -7625976189032315511L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar organograma", operacao=Operacao.VISUALIZAR)
	public List<Organograma> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("organograma.porGrupo");
		cmd.addParameter("grupo", grupo);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir organograma", operacao=Operacao.EXCLUIR)
	public void exclui(Organograma organograma) throws Exception {
		OrganogramaExcluirCommand cmd = injector.getInstance(OrganogramaExcluirCommand.class);
		cmd.setOrganograma(organograma);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar organograma", operacao=Operacao.SALVAR)
	public Organograma salva(Organograma organograma) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(organograma);
		return (Organograma)cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public OrganogramaVO getVO(Organograma organograma) throws Exception {
		OrganogramaVO vo = new OrganogramaVO();

		GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
		cmd.setClazz(Organograma.class);
		cmd.setId(organograma.getId());
		vo.setOrganograma((Organograma) cmd.call());

		GetEntityListCommand cmbAreas = injector.getInstance(GetEntityListCommand.class);
		cmbAreas.setNamedQuery("organogramaArea.porOrganograma");
		cmbAreas.addParameter("organograma", vo.getOrganograma());
		vo.setListaAreas(cmbAreas.call());

		GetEntityListCommand cmbCoordenacoes = injector.getInstance(GetEntityListCommand.class);
		cmbCoordenacoes.setNamedQuery("organogramaCoordenacao.porOrganograma");
		cmbCoordenacoes.addParameter("organograma", vo.getOrganograma());
		vo.setListaCoordenacoes(cmbCoordenacoes.call());

		return vo;
	}


	@Override
	public OrganogramaVO salvaOrganograma(OrganogramaVO vo) throws Exception {
		OrganogramaSalvarCommand cmd = injector.getInstance(OrganogramaSalvarCommand.class);
		cmd.setVo(vo);
		return cmd.call();
	}

}