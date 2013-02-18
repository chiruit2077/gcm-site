package br.com.ecc.server.service.cadastro;

import java.util.List;

import br.com.ecc.client.service.cadastro.HotelService;
import br.com.ecc.model.Hotel;
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
public class HotelServiceImpl extends SecureRemoteServiceServlet implements HotelService {

	private static final long serialVersionUID = 1098772024741745774L;
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar hoteis", operacao=Operacao.VISUALIZAR)
	public List<Hotel> lista() throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("hotel.todos");
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir hotel", operacao=Operacao.EXCLUIR)
	public void exclui(Hotel hotel) throws Exception {
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(hotel);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar hotel", operacao=Operacao.SALVAR)
	public Hotel salva(Hotel hotel) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(hotel);
		return (Hotel)cmd.call();
	}

}