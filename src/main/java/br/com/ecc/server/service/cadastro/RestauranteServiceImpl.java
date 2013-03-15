package br.com.ecc.server.service.cadastro;

import java.util.List;

import br.com.ecc.client.service.cadastro.RestauranteService;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Restaurante;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.RestauranteVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.RestauranteExcluirCommand;
import br.com.ecc.server.command.RestauranteSalvarCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class RestauranteServiceImpl extends SecureRemoteServiceServlet implements RestauranteService {

	private static final long serialVersionUID = -8974131200881289882L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar restaurantes", operacao=Operacao.VISUALIZAR)
	public List<Restaurante> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("restaurante.porGrupo");
		cmd.addParameter("grupo", grupo);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public RestauranteVO getVO(Restaurante restaurante) throws Exception {
		RestauranteVO vo = new RestauranteVO();

		GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
		cmd.setClazz(Restaurante.class);
		cmd.setId(restaurante.getId());
		vo.setRestaurante((Restaurante) cmd.call());

		GetEntityListCommand cmbMesa = injector.getInstance(GetEntityListCommand.class);
		cmbMesa.setNamedQuery("mesa.porRestaurante");
		cmbMesa.addParameter("restaurante", vo.getRestaurante());
		vo.setListaMesas(cmbMesa.call());

		GetEntityListCommand cmdGrupo = injector.getInstance(GetEntityListCommand.class);
		cmdGrupo.setNamedQuery("restauranteGrupo.porRestaurante");
		cmdGrupo.addParameter("restaurante", vo.getRestaurante());
		vo.setListaGrupos(cmdGrupo.call());

		GetEntityListCommand cmbTitulos = injector.getInstance(GetEntityListCommand.class);
		cmbTitulos.setNamedQuery("restauranteTitulo.porRestaurante");
		cmbTitulos.addParameter("restaurante", vo.getRestaurante());
		vo.setListaTitulos(cmbTitulos.call());

		return vo;
	}


	@Override
	public RestauranteVO salvaRestaurante(RestauranteVO vo) throws Exception {
		RestauranteSalvarCommand cmd = injector.getInstance(RestauranteSalvarCommand.class);
		cmd.setVo(vo);
		return cmd.call();
	}

	@Override
	public Restaurante salva(Restaurante restaurante) {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(restaurante);
		return (Restaurante)cmd.call();
	}

	@Override
	public void exclui(Restaurante restaurante) throws Exception {
		RestauranteExcluirCommand cmd = injector.getInstance(RestauranteExcluirCommand.class);
		cmd.setRestaurante(restaurante);
		cmd.call();
	}
}