package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroHotelService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.EncontroHotelVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroHotelQuartoSalvarCommand;
import br.com.ecc.server.command.EncontroHotelRestauranteSalvarCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroHotelServiceImpl extends SecureRemoteServiceServlet implements EncontroHotelService {

	private static final long serialVersionUID = -2089153395558922680L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontroHotelis", operacao=Operacao.VISUALIZAR)
	public List<EncontroHotel> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroHotel.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public EncontroHotelVO getVO(EncontroHotel encontroHotel) throws Exception {
		EncontroHotelVO vo = new EncontroHotelVO();

		GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
		cmd.setClazz(EncontroHotel.class);
		cmd.setId(encontroHotel.getId());
		vo.setEncontroHotel((EncontroHotel) cmd.call());

		GetEntityListCommand cmdAgrupamentos = injector.getInstance(GetEntityListCommand.class);
		cmdAgrupamentos.setNamedQuery("hotelAgrupamento.porHotel");
		cmdAgrupamentos.addParameter("hotel", vo.getEncontroHotel().getHotel());
		vo.setListaAgrupamentos(cmdAgrupamentos.call());

		GetEntityListCommand cmbQuarto = injector.getInstance(GetEntityListCommand.class);
		cmbQuarto.setNamedQuery("quarto.porHotel");
		cmbQuarto.addParameter("hotel", vo.getEncontroHotel().getHotel());
		vo.setListaQuartos(cmbQuarto.call());

		GetEntityListCommand cmbEncontroHotelQuarto = injector.getInstance(GetEntityListCommand.class);
		cmbEncontroHotelQuarto.setNamedQuery("encontroHotelQuarto.porEncontroHotel");
		cmbEncontroHotelQuarto.addParameter("encontrohotel", vo.getEncontroHotel());
		vo.setListaEncontroQuartos(cmbEncontroHotelQuarto.call());

		GetEntityListCommand cmbEncontroRestauranteGarcon = injector.getInstance(GetEntityListCommand.class);
		cmbEncontroRestauranteGarcon.setNamedQuery("encontroRestauranteGarcon.porEncontroHotel");
		cmbEncontroRestauranteGarcon.addParameter("encontrohotel", vo.getEncontroHotel());
		vo.setListaEncontroRestauranteGarcon(cmbEncontroRestauranteGarcon.call());

		GetEntityListCommand cmbMesa = injector.getInstance(GetEntityListCommand.class);
		cmbMesa.setNamedQuery("mesa.porHotel");
		cmbMesa.addParameter("hotel", vo.getEncontroHotel().getHotel());
		vo.setListaMesas(cmbMesa.call());

		return vo;
	}


	@Override
	@Permissao(nomeOperacao="Salvar encontroInscricao", operacao=Operacao.SALVAR)
	public EncontroHotelVO salvaEncontroHotelQuarto(EncontroHotelVO encontroHotel) throws Exception {
		EncontroHotelQuartoSalvarCommand cmd = injector.getInstance(EncontroHotelQuartoSalvarCommand.class);
		cmd.setEncontroHotelVO(encontroHotel);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public EncontroHotel getEncontroHotelEvento(Encontro encontro)
			throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroHotel.porEncontroEvento");
		cmd.addParameter("encontro", encontro);
		List<EncontroHotel> call = cmd.call();
		if (call.size() == 1)
			return call.get(0);
		return null;
	}

	@Override
	public EncontroHotelVO salvaRestaurante(EncontroHotelVO encontroHotelVO)
			throws Exception {
		EncontroHotelRestauranteSalvarCommand cmd = injector.getInstance(EncontroHotelRestauranteSalvarCommand.class);
		cmd.setEncontroHotelVO(encontroHotelVO);
		return cmd.call();
	}
}