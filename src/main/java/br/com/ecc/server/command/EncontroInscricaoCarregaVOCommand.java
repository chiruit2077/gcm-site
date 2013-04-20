package br.com.ecc.server.command;

import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.vo.EncontroInscricaoVO;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class EncontroInscricaoCarregaVOCommand implements Callable<EncontroInscricaoVO>{

	@Inject EntityManager em;
	@Inject Injector injector;
	private EncontroInscricao encontroInscricao;

	@SuppressWarnings("unchecked")
	@Override
	public EncontroInscricaoVO call() throws Exception {

		EncontroInscricaoVO vo = new EncontroInscricaoVO();

		GetEntityCommand cmd = injector.getInstance(GetEntityCommand.class);
		cmd.setClazz(EncontroInscricao.class);
		cmd.setId(getEncontroInscricao().getId());
		vo.setEncontroInscricao((EncontroInscricao) cmd.call());

		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
		cmdEntidade.setNamedQuery("encontroInscricaoPagamento.porEncontroInscricao");
		cmdEntidade.addParameter("encontroInscricao", vo.getEncontroInscricao());
		vo.setListaPagamento(cmdEntidade.call());

		cmdEntidade = injector.getInstance(GetEntityListCommand.class);
		cmdEntidade.setNamedQuery("encontroInscricaoPagamentoDetalhe.porEncontroInscricao");
		cmdEntidade.addParameter("encontroInscricao", vo.getEncontroInscricao());
		vo.setListaPagamentoDetalhe(cmdEntidade.call());

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

		return vo;
	}

	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}

	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
}