package br.com.ecc.client.service.secretaria;

import java.util.List;

import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.vo.MensagemVO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MensagemServiceAsync {
	void lista(Grupo grupo, AsyncCallback<List<Mensagem>> callback);
	void listaEspecial(Grupo grupoSelecionado, AsyncCallback<List<Mensagem>> callback);
	void salva(MensagemVO mensagemVO, AsyncCallback<MensagemVO> callback);
	void getVO(Mensagem mensagem, AsyncCallback<MensagemVO> callback);
	void envia(MensagemVO mensagemVO, MensagemDestinatario destinatario, Boolean reenvio, AsyncCallback<Mensagem> callback);
	void exclui(Mensagem mensagem, AsyncCallback<Void> callback);
	void enviaFicha(Mensagem mensagem, EncontroInscricao encontroInscricao, AsyncCallback<EncontroInscricao> callback);
}