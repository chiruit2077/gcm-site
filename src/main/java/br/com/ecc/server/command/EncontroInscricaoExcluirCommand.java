package br.com.ecc.server.command;

import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.ExecuteUpdateCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroInscricaoExcluirCommand implements Callable<Void>{

	@Inject EntityManager em;
	@Inject Injector inject;
	private EncontroInscricao encontroInscricao;

	@Override
	@Transactional
	public Void call() throws Exception {
		//removendo participações no encontro
		ExecuteUpdateCommand cmdDelete = inject.getInstance(ExecuteUpdateCommand.class);
		cmdDelete.addParameter("encontroInscricao", encontroInscricao);

		cmdDelete.setNamedQuery("encontroInscricaoPagamentoDetalhe.deletePorEncontroInscricao");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroInscricaoPagamento.deletePorEncontroInscricao");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroAtividadeInscricao.deletePorEncontroInscricao");
		cmdDelete.call();
		
		cmdDelete.setNamedQuery("encontroInscricaoFichaPagamento.deletePorEncontroInscricao");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroHotelQuarto.updatePorEncontroInscricao1");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroHotelQuarto.updatePorEncontroInscricao2");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroHotelQuarto.updatePorEncontroInscricao3");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroHotelQuarto.updatePorEncontroInscricao4");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroRestauranteMesa.updatePorEncontroGarcon");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroRestauranteMesa.updatePorEncontroAfilhado1");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroRestauranteMesa.updatePorEncontroAfilhado2");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroOrganogramaCoordenacao.updatePorEncontroInscricao1");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroOrganogramaCoordenacao.updatePorEncontroInscricao2");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroOrganogramaArea.updatePorEncontroInscricao1");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroOrganogramaArea.updatePorEncontroInscricao2");
		cmdDelete.call();

		EncontroInscricaoFichaPagamento ficha = encontroInscricao.getFichaPagamento();
		if (ficha!=null){
			ficha.setEncontroInscricao(null);
			SaveEntityCommand cmd = inject.getInstance(SaveEntityCommand.class);
			cmd.setBaseEntity(ficha);
			cmd.call();
		}

		DeleteEntityCommand cmd = inject.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontroInscricao);
		cmd.call();
		if (ficha!=null){
			cmd.setBaseEntity(ficha);
			cmd.call();
		}
		return null;
	}

	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}

	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}

}