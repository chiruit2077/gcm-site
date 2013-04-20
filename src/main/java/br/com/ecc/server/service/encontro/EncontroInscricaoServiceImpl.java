package br.com.ecc.server.service.encontro;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.ecc.client.service.encontro.EncontroInscricaoService;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.vo.EncontroInscricaoVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.SessionHelper;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroInscricaoCarregaVOCommand;
import br.com.ecc.server.command.EncontroInscricaoSalvarCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.ExecuteUpdateCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroInscricaoServiceImpl extends SecureRemoteServiceServlet implements EncontroInscricaoService {
	private static final long serialVersionUID = -3780927709658969884L;

	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontroInscricaos", operacao=Operacao.VISUALIZAR)
	public List<EncontroInscricao> lista(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontro");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		for (EncontroInscricao encontroInscricao : lista) {
			if(encontroInscricao.getMensagemDestinatario()!=null){
				if(encontroInscricao.getMensagemDestinatario().getDataEnvio()!=null){
					encontroInscricao.getMensagemDestinatario().setDataEnvioStr(df.format(encontroInscricao.getMensagemDestinatario().getDataEnvio()));
				}
				if(encontroInscricao.getMensagemDestinatario().getDataConfirmacao()!=null){
					encontroInscricao.getMensagemDestinatario().setDataConfirmacaoStr(df.format(encontroInscricao.getMensagemDestinatario().getDataConfirmacao()));
				}
			}
			if(encontroInscricao.getDataPrenchimentoFicha()!=null){
				encontroInscricao.setDataPrenchimentoFichaStr(df.format(encontroInscricao.getDataPrenchimentoFicha()));
			}
		}

		return lista;
	}

	@Override
	@Permissao(nomeOperacao="Excluir encontroInscricao", operacao=Operacao.EXCLUIR)
	public void exclui(EncontroInscricao encontroInscricao) throws Exception {
		//removendo participações no encontro
		ExecuteUpdateCommand cmdDelete = injector.getInstance(ExecuteUpdateCommand.class);
		cmdDelete.addParameter("encontroInscricao", encontroInscricao);

		cmdDelete.setNamedQuery("encontroInscricaoPagamentoDetalhe.deletePorEncontroInscricao");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroInscricaoPagamento.deletePorEncontroInscricao");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroAtividadeInscricao.deletePorEncontroInscricao");
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
			SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
			cmd.setBaseEntity(ficha);
			cmd.call();
		}

		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontroInscricao);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Busca VO", operacao=Operacao.VISUALIZAR)
	public EncontroInscricaoVO getVO(EncontroInscricao encontroInscricao) throws Exception {
		EncontroInscricaoCarregaVOCommand cmd = injector.getInstance(EncontroInscricaoCarregaVOCommand.class);
		cmd.setEncontroInscricao(encontroInscricao);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Busca inscricao pro casal - VO", operacao=Operacao.VISUALIZAR)
	public EncontroInscricaoVO getVO(Encontro encontro, Casal casal) throws Exception {
		EncontroInscricaoVO vo=null;
		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
		cmdEntidade.setNamedQuery("encontroInscricao.porEncontroCasal");
		cmdEntidade.addParameter("encontro", encontro);
		cmdEntidade.addParameter("casal", casal);
		List<EncontroInscricao> lista = cmdEntidade.call();

		if(lista.size()>0){
			vo = getVO(lista.get(0));
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			if(vo.getEncontroInscricao().getMensagemDestinatario()!=null){
				if(vo.getEncontroInscricao().getMensagemDestinatario().getDataEnvio()!=null){
					vo.getEncontroInscricao().getMensagemDestinatario().setDataEnvioStr(df.format(vo.getEncontroInscricao().getMensagemDestinatario().getDataEnvio()));
				}
				if(vo.getEncontroInscricao().getMensagemDestinatario().getDataConfirmacao()!=null){
					vo.getEncontroInscricao().getMensagemDestinatario().setDataConfirmacaoStr(df.format(vo.getEncontroInscricao().getMensagemDestinatario().getDataConfirmacao()));
				}
			}
			if(vo.getEncontroInscricao().getDataPrenchimentoFicha()!=null){
				vo.getEncontroInscricao().setDataPrenchimentoFichaStr(df.format(vo.getEncontroInscricao().getDataPrenchimentoFicha()));
			}
		}
		return vo;
	}

	@Override
	@Permissao(nomeOperacao="Salvar encontroInscricao", operacao=Operacao.SALVAR)
	public EncontroInscricaoVO salva(EncontroInscricaoVO encontroInscricaoVO) throws Exception {
		EncontroInscricaoSalvarCommand cmd = injector.getInstance(EncontroInscricaoSalvarCommand.class);
		cmd.setEncontroInscricaoVO(encontroInscricaoVO);
		cmd.setUsuarioAtual(SessionHelper.getUsuario(getThreadLocalRequest().getSession()));
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EncontroInscricaoPagamento> listaPagamentos(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricaoPagamento.listaPagamentosPorEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@Override
	public void salvaPagamento(EncontroInscricaoPagamento pagamento) throws Exception {
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(pagamento);
		cmd.call();
	}

}