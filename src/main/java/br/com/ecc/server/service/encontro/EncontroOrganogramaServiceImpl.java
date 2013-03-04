package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroOrganogramaService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.EncontroOrganogramaVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroOrganogramaCoordenacaoSalvarCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroOrganogramaServiceImpl extends SecureRemoteServiceServlet implements EncontroOrganogramaService {

	private static final long serialVersionUID = 3152508065467642782L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontroOrganograma", operacao=Operacao.VISUALIZAR)
	public List<EncontroOrganograma> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroOrganograma.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public EncontroOrganogramaVO getVO(EncontroOrganograma encontroOrganograma) throws Exception {
		EncontroOrganogramaVO vo = new EncontroOrganogramaVO();

		GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
		cmd.setClazz(EncontroOrganograma.class);
		cmd.setId(encontroOrganograma.getId());
		vo.setEncontroOrganograma((EncontroOrganograma) cmd.call());

		GetEntityListCommand cmdAreas = injector.getInstance(GetEntityListCommand.class);
		cmdAreas.setNamedQuery("organogramaArea.porOrganograma");
		cmdAreas.addParameter("organograma", vo.getEncontroOrganograma().getOrganograma());
		vo.setListaOrganogramaArea(cmdAreas.call());

		GetEntityListCommand cmbCoordenacao = injector.getInstance(GetEntityListCommand.class);
		cmbCoordenacao.setNamedQuery("organogramaCoordenacao.porOrganograma");
		cmbCoordenacao.addParameter("organograma", vo.getEncontroOrganograma().getOrganograma());
		vo.setListaCoordenacao(cmbCoordenacao.call());

		GetEntityListCommand cmbEncontroOrganogramaArea = injector.getInstance(GetEntityListCommand.class);
		cmbEncontroOrganogramaArea.setNamedQuery("encontroOrganogramaArea.porEncontroOrganograma");
		cmbEncontroOrganogramaArea.addParameter("encontroorganograma", vo.getEncontroOrganograma());
		vo.setListaEncontroOrganogramaArea(cmbEncontroOrganogramaArea.call());

		GetEntityListCommand cmbEncontroOrganogramaCoordenacao = injector.getInstance(GetEntityListCommand.class);
		cmbEncontroOrganogramaCoordenacao.setNamedQuery("encontroOrganogramaCoordenacao.porEncontroOrganograma");
		cmbEncontroOrganogramaCoordenacao.addParameter("encontroorganograma", vo.getEncontroOrganograma());
		vo.setListaEncontroOrganogramaCoordenacao(cmbEncontroOrganogramaCoordenacao.call());

		return vo;
	}


	@Override
	@Permissao(nomeOperacao="Salvar encontroOrganograma", operacao=Operacao.SALVAR)
	public EncontroOrganogramaVO salvaEncontroOrganogramaCoordenacao(EncontroOrganogramaVO vo) throws Exception {
		EncontroOrganogramaCoordenacaoSalvarCommand cmd = injector.getInstance(EncontroOrganogramaCoordenacaoSalvarCommand.class);
		cmd.setEncontroOrganogramaVO(vo);
		return cmd.call();
	}


	@Override
	public EncontroOrganograma salva(EncontroOrganograma encontroOrganograma) {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(encontroOrganograma);
		return (EncontroOrganograma)cmd.call();
	}

	@Override
	public void exclui(EncontroOrganograma encontroOrganograma) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontroOrganograma);
		cmd.call();
	}
}