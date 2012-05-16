package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.vo.MensagemVO;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class CasalFichaMensagemEnvioCommand implements Callable<EncontroInscricao>{

	@Inject EntityManager em;
	@Inject Injector injector;
	
	private EncontroInscricao encontroInscricao;
	private Mensagem mensagem;;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public EncontroInscricao call() throws Exception {
		Query q = em.createNamedQuery("mensagemDestinatario.porMensagem");
		q.setParameter("mensagem", mensagem);
		List<MensagemDestinatario> lista = q.getResultList();
		
		MensagemDestinatario md = new MensagemDestinatario();
		boolean achou = false;
		for (MensagemDestinatario mensagemDestinatario : lista) {
			if(encontroInscricao.getCasal()!=null){
				if(mensagemDestinatario.getCasal()!=null && encontroInscricao.getCasal().getId().equals(mensagemDestinatario.getCasal().getId())){
					achou = true;
					md = mensagemDestinatario;
					break;
				}
			} else if(encontroInscricao.getPessoa()!=null){
				if(mensagemDestinatario.getPessoa()!=null && encontroInscricao.getPessoa().getId().equals(mensagemDestinatario.getPessoa().getId())){
					achou = true;
					md = mensagemDestinatario;
					break;
				}
			}
		}
		if(!achou){
			md = new MensagemDestinatario();
			md.setCasal(encontroInscricao.getCasal());
			md.setPessoa(encontroInscricao.getPessoa());
			md.setMensagem(mensagem);
			md = em.merge(md);
			lista.add(md);
		}
		
		MensagemVO mensagemVO = new MensagemVO();
		mensagemVO.setMensagem(mensagem);
		mensagemVO.setListaDestinatarios(new ArrayList<MensagemDestinatario>());
		mensagemVO.getListaDestinatarios().add(md);
				
		MensagemEnviarCommand cmd = injector.getInstance(MensagemEnviarCommand.class);
		cmd.setMensagemVO(mensagemVO);
		cmd.setReenvio(true);
		mensagemVO = cmd.call();
		
		encontroInscricao.setDataPrenchimentoFicha(null);
		encontroInscricao.setMensagemDestinatario(mensagemVO.getListaDestinatarios().get(0));
		encontroInscricao = em.merge(encontroInscricao);
		
		return encontroInscricao;
	}

	public EncontroInscricao getEncontroInscricao() {
		return encontroInscricao;
	}
	public void setEncontroInscricao(EncontroInscricao encontroInscricao) {
		this.encontroInscricao = encontroInscricao;
	}
	public Mensagem getMensagem() {
		return mensagem;
	}
	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}
}