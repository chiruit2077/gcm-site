package br.com.ecc.server.command;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.vo.MensagemVO;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class MensagemSalvarCommand implements Callable<MensagemVO>{

	@Inject EntityManager em;
	private MensagemVO mensagemVO;

	@Override
	@Transactional
	public MensagemVO call() throws Exception {
		Query q;

		mensagemVO.getMensagem().setData(new Date());
		mensagemVO.setMensagem(em.merge(mensagemVO.getMensagem()));
		
		//destinatarios
		List<MensagemDestinatario> novaLista = new ArrayList<MensagemDestinatario>();
		if(mensagemVO.getListaDestinatarios()!=null){
			for (MensagemDestinatario destinatario : mensagemVO.getListaDestinatarios()) {
				if(destinatario.getId()!=null){
					novaLista.add(destinatario);
				}
			}
		}
		if(novaLista.size()>0){
			q = em.createNamedQuery("mensagemDestinatario.deletePorMensagemNotIn");
			q.setParameter("mensagem", mensagemVO.getMensagem());
			q.setParameter("lista", novaLista);
			q.executeUpdate();
		} else {
			q = em.createNamedQuery("mensagemDestinatario.deletePorMensagem");
			q.setParameter("mensagem", mensagemVO.getMensagem());
			q.executeUpdate();
		}
		if(mensagemVO.getListaDestinatarios()!=null){
			MensagemDestinatario destinatario;
			for (int i = 0; i < mensagemVO.getListaDestinatarios().size(); i++) {
				destinatario = mensagemVO.getListaDestinatarios().get(i);
				destinatario.setMensagem(mensagemVO.getMensagem());
				destinatario = em.merge(destinatario);
				mensagemVO.getListaDestinatarios().set(i, destinatario);
			}
		}
		return mensagemVO;
	}
	public MensagemVO getMensagemVO() {
		return mensagemVO;
	}
	public void setMensagemVO(MensagemVO mensagemVO) {
		this.mensagemVO = mensagemVO;
	}
}