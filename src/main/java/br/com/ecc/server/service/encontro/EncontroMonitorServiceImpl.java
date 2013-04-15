package br.com.ecc.server.service.encontro;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.service.encontro.EncontroMonitorService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.EncontroMonitorVO;
import br.com.ecc.model.vo.EncontroVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroCarregaVOCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroMonitorServiceImpl extends SecureRemoteServiceServlet implements EncontroMonitorService {


	private static final long serialVersionUID = -2600412064178802906L;

	@Inject Injector injector;

	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public EncontroMonitorVO getVO(Encontro encontro, Date ref) throws Exception {
		EncontroMonitorVO vo = new EncontroMonitorVO();
		vo.setEncontro(encontro);

		EncontroCarregaVOCommand cmd = injector.getInstance(EncontroCarregaVOCommand.class);
		cmd.setEncontro(encontro);
		EncontroVO encontroVO = cmd.call();

		List<EncontroAtividade> listaEncontroAtividade = encontroVO.getListaEncontroAtividade();
		Collections.sort(listaEncontroAtividade, new Comparator<EncontroAtividade>() {
			@Override
			public int compare(EncontroAtividade o1, EncontroAtividade o2) {
				if(o1.getInicio().equals(o2.getInicio())){
					if(o1.getFim().equals(o2.getFim())){
						return o1.getAtividade().getNome().compareTo(o2.getAtividade().getNome());
					}
					return o1.getFim().compareTo(o2.getFim());
				}
				return o1.getInicio().compareTo(o2.getInicio());
			}
		});

		for (EncontroAtividade ea : listaEncontroAtividade) {
			if ((ref.after(ea.getInicio()) || ref.equals(ea.getInicio())) &&
					(ref.before(ea.getFim()) || ref.equals(ea.getFim()))){
				if (vo.getAtividade1()== null) vo.setAtividade1(ea);
				else if (vo.getAtividade2()== null) vo.setAtividade2(ea);
			}
			if (vo.getAtividade1()!= null && vo.getAtividade2()!= null) break;
		}


		/*GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
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
		cmbMesa.addParameter("restaurante", vo.getEncontroRestaurante().getRestaurante());
		vo.setListaMesas(cmbMesa.call());

		GetEntityListCommand cmdGrupo = injector.getInstance(GetEntityListCommand.class);
		cmdGrupo.setNamedQuery("restauranteGrupo.porRestaurante");
		cmdGrupo.addParameter("restaurante", vo.getEncontroRestaurante().getRestaurante());
		vo.setListaGrupos(cmdGrupo.call());

		GetEntityListCommand cmbTitulos = injector.getInstance(GetEntityListCommand.class);
		cmbTitulos.setNamedQuery("restauranteTitulo.porRestaurante");
		cmbTitulos.addParameter("restaurante", vo.getEncontroRestaurante().getRestaurante());
		vo.setListaTitulos(cmbTitulos.call());

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
		vo.setListaEncontristas(cmdEncontristas.call());*/

		return vo;
	}

}