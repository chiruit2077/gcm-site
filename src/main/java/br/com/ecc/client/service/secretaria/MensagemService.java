package br.com.ecc.client.service.secretaria;

import java.util.List;

import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.vo.MensagemVO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mensagem")
public interface MensagemService extends RemoteService {
	public List<Mensagem> lista(Grupo grupo) throws Exception;
	public List<Mensagem> listaEspecial(Grupo grupoSelecionado) throws Exception;
	public void exclui(Mensagem mensagem) throws Exception;
	public MensagemVO salva(MensagemVO mensagemVO) throws Exception;
	public MensagemVO getVO(Mensagem mensagem) throws Exception;
	public Mensagem envia(MensagemVO mensagemVO, MensagemDestinatario destinatario, Boolean reenvio) throws Exception;
	public EncontroInscricao enviaFicha(Mensagem mensagem, EncontroInscricao encontroInscricao) throws Exception;
}