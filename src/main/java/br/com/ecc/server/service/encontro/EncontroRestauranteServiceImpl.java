package br.com.ecc.server.service.encontro;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.encontro.EncontroRestauranteService;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.model.vo.EncontroRestauranteVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroRestauranteMesaSalvarCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroRestauranteServiceImpl extends SecureRemoteServiceServlet implements EncontroRestauranteService {

	private static final long serialVersionUID = 8640056849569068347L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontroRestaurantes", operacao=Operacao.VISUALIZAR)
	public List<EncontroRestaurante> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroRestaurante.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public EncontroRestauranteVO getVO(EncontroRestaurante encontroRestaurante) throws Exception {
		EncontroRestauranteVO vo = new EncontroRestauranteVO();

		GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
		cmd.setClazz(EncontroRestaurante.class);
		cmd.setId(encontroRestaurante.getId());
		vo.setEncontroRestaurante((EncontroRestaurante) cmd.call());

		GetEntityListCommand cmbEncontroRestauranteMesa = injector.getInstance(GetEntityListCommand.class);
		cmbEncontroRestauranteMesa.setNamedQuery("encontroRestauranteMesa.porEncontroRestaurante");
		cmbEncontroRestauranteMesa.addParameter("encontrorestaurante", vo.getEncontroRestaurante());
		vo.setListaEncontroRestauranteMesa(cmbEncontroRestauranteMesa.call());

		GetEntityListCommand cmbEncontroRestauranteMesaOutros = injector.getInstance(GetEntityListCommand.class);
		cmbEncontroRestauranteMesaOutros.setNamedQuery("encontroRestauranteMesa.porEncontroRestauranteOutro");
		cmbEncontroRestauranteMesaOutros.addParameter("encontrorestaurante", vo.getEncontroRestaurante());
		cmbEncontroRestauranteMesaOutros.addParameter("encontro", vo.getEncontroRestaurante().getEncontro());
		vo.setListaEncontroRestauranteMesaOutros(cmbEncontroRestauranteMesaOutros.call());

		GetEntityListCommand cmbMesa = injector.getInstance(GetEntityListCommand.class);
		cmbMesa.setNamedQuery("mesa.porRestaurante");
		cmbMesa.addParameter("restaurante", vo.getEncontroRestaurante().getRestuarante());
		vo.setListaMesas(cmbMesa.call());

		List<AgrupamentoVO> listaAgrupamentoVO = new ArrayList<AgrupamentoVO>();
		GetEntityListCommand cmdAgrupamento = injector.getInstance(GetEntityListCommand.class);
		cmdAgrupamento.setNamedQuery("agrupamento.porEncontro");
		cmdAgrupamento.addParameter("encontro", vo.getEncontroRestaurante().getEncontro());
		List<Agrupamento> listaAgrupamemto = (List<Agrupamento>) cmdAgrupamento.call();
		for (Agrupamento agrupamento : listaAgrupamemto) {
			AgrupamentoVO voagrup = new AgrupamentoVO();
			voagrup.setAgrupamento(agrupamento);
			GetEntityListCommand cmdMenbro = injector.getInstance(GetEntityListCommand.class);
			cmdMenbro.setNamedQuery("agrupamentoMembro.porAgrupamento");
			cmdMenbro.addParameter("agrupamento", agrupamento);
			voagrup.setListaMembros((List<AgrupamentoMembro>) cmdMenbro.call());
			listaAgrupamentoVO.add(voagrup);
		}
		vo.setListaAgrupamentosVO(listaAgrupamentoVO);


		GetEntityListCommand cmdAfilhados = injector.getInstance(GetEntityListCommand.class);
		cmdAfilhados.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmdAfilhados.addParameter("encontro", vo.getEncontroRestaurante().getEncontro());
		vo.setListaAfilhados(cmdAfilhados.call());

		GetEntityListCommand cmdEncontristas = injector.getInstance(GetEntityListCommand.class);
		cmdEncontristas.setNamedQuery("encontroInscricao.porEncontroEncontristas");
		cmdEncontristas.addParameter("encontro", vo.getEncontroRestaurante().getEncontro());
		vo.setListaEncontristas(cmdEncontristas.call());

		return vo;
	}


	@Override
	public EncontroRestauranteVO salvaRestaurante(EncontroRestauranteVO vo)
			throws Exception {
		EncontroRestauranteMesaSalvarCommand cmd = injector.getInstance(EncontroRestauranteMesaSalvarCommand.class);
		cmd.setVo(vo);
		return cmd.call();
	}

	@Override
	public EncontroRestaurante salva(EncontroRestaurante restaurante) {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(restaurante);
		return (EncontroRestaurante)cmd.call();
	}

	@Override
	public void exclui(EncontroRestaurante restaurante) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(restaurante);
		cmd.call();
	}
}