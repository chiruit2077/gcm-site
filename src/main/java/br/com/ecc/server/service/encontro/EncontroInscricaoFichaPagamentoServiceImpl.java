package br.com.ecc.server.service.encontro;

import java.util.List;

import br.com.ecc.client.service.encontro.EncontroInscricaoFichaPagamentoService;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.command.EncontroInscricaoFichaPagamentoGeraFichasCommand;
import br.com.ecc.server.command.EncontroInscricaoFichaPagamentoSalvarCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroInscricaoFichaPagamentoServiceImpl extends SecureRemoteServiceServlet implements EncontroInscricaoFichaPagamentoService {

	private static final long serialVersionUID = 3763111944496091407L;
	@Inject Injector injector;



	@SuppressWarnings("unchecked")
	@Override
	public List<EncontroInscricaoFichaPagamento> listaFichas(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricaoFichaPagamento.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	public void salvaFicha(EncontroInscricaoFichaPagamento ficha) throws Exception {
		EncontroInscricaoFichaPagamentoSalvarCommand cmd = injector.getInstance(EncontroInscricaoFichaPagamentoSalvarCommand.class);
		cmd.setFicha(ficha);
		cmd.call();
	}

	@Override
	public void geraFichasVagas(Encontro encontroSelecionado) throws Exception {
		EncontroInscricaoFichaPagamentoGeraFichasCommand cmd = injector.getInstance(EncontroInscricaoFichaPagamentoGeraFichasCommand.class);
		cmd.setEncontroSelecionado(encontroSelecionado);
		cmd.call();

	}

}