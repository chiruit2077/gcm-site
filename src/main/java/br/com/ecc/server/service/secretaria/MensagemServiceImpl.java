package br.com.ecc.server.service.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.service.secretaria.MensagemService;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.tipo.TipoMensagemEnum;
import br.com.ecc.model.vo.MensagemVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.CasalFichaMensagemEnvioCommand;
import br.com.ecc.server.command.MensagemEnviarCommand;
import br.com.ecc.server.command.MensagemSalvarCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.ExecuteUpdateCommand;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class MensagemServiceImpl extends SecureRemoteServiceServlet implements MensagemService {
	private static final long serialVersionUID = -3012260572002519680L;
	
	@Inject Injector injector;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar mensagems", operacao=Operacao.VISUALIZAR)
	public List<Mensagem> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("mensagem.porGrupo");
		cmd.addParameter("grupo", grupo);
		List<Object[]> resultado = cmd.call();
		
		List<Mensagem> listaRetorno = new ArrayList<Mensagem>();
		Mensagem m;
		for (Object[] objects : resultado) {
			m = new Mensagem();
			m.setId((Integer) objects[0]);
			m.setGrupo((Grupo) objects[1]);
			m.setData((Date) objects[2]);
			m.setTitulo((String) objects[3]);
			m.setDescricao((String) objects[4]);
			m.setTipoMensagem((TipoMensagemEnum) objects[5]);
			m.setVersion((Integer) objects[6]);
			listaRetorno.add(m);
		}
		
		return listaRetorno;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar mensagems", operacao=Operacao.VISUALIZAR)
	public List<Mensagem> listaEspecial(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("mensagem.porGrupoTipoEspecial");
		cmd.addParameter("grupo", grupo);
		List<Object[]> resultado = cmd.call();
		
		List<Mensagem> listaRetorno = new ArrayList<Mensagem>();
		Mensagem m;
		for (Object[] objects : resultado) {
			m = new Mensagem();
			m.setId((Integer) objects[0]);
			m.setGrupo((Grupo) objects[1]);
			m.setData((Date) objects[2]);
			m.setTitulo((String) objects[3]);
			m.setDescricao((String) objects[4]);
			m.setTipoMensagem((TipoMensagemEnum) objects[5]);
			m.setVersion((Integer) objects[6]);
			listaRetorno.add(m);
		}
		
		return listaRetorno;
	}

	@Override
	@Permissao(nomeOperacao="Excluir mensagem", operacao=Operacao.EXCLUIR)
	public void exclui(Mensagem mensagem) throws Exception {
		//dependencias
//		GetEntityListCommand cmdEntidade = injector.getInstance(GetEntityListCommand.class);
//		cmdEntidade.setNamedQuery("encontro.porMensagem");
//		cmdEntidade.addParameter("mensagem", mensagem);
//		List<Encontro> encontros = cmdEntidade.call();
//		if(encontros!=null && encontros.size()>0){
//			throw new WebException("Erro ao excluir Mensagem. \nJá existem encontros neste mensagem.");
//		}
		
		ExecuteUpdateCommand cmdUpdate = injector.getInstance(ExecuteUpdateCommand.class);
		cmdUpdate.setNamedQuery("mensagemDestinatario.deletePorMensagem");
		cmdUpdate.addParameter("mensagem", mensagem);
		cmdUpdate.call();
		
		//exclusão
		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(mensagem);
		cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Salvar mensagem", operacao=Operacao.SALVAR)
	public MensagemVO salva(MensagemVO mensagemVO) throws Exception {
		MensagemSalvarCommand cmd = injector.getInstance(MensagemSalvarCommand.class);
		cmd.setMensagemVO(mensagemVO);
		return cmd.call();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public MensagemVO getVO(Mensagem mensagem){
		MensagemVO vo = new MensagemVO();
		
		GetEntityCommand cmdE = injector.getInstance(GetEntityCommand.class);
		cmdE.setClazz(Mensagem.class);
		cmdE.setId(mensagem.getId());
		mensagem = (Mensagem) cmdE.call();
		
		vo.setMensagem(mensagem);
		
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("mensagemDestinatario.porMensagem");
		cmd.addParameter("mensagem", mensagem);
		vo.setListaDestinatarios(cmd.call());
		
		return vo;
	}

	@Override
	public Mensagem envia(MensagemVO mensagemVO, MensagemDestinatario destinatario, Boolean reenvio) throws Exception {
		mensagemVO = salva(mensagemVO);
		if(destinatario!=null){
			mensagemVO.setListaDestinatarios(new ArrayList<MensagemDestinatario>());
			mensagemVO.getListaDestinatarios().add(destinatario);
		}
		
		MensagemEnviarCommand cmd = injector.getInstance(MensagemEnviarCommand.class);
		cmd.setMensagemVO(mensagemVO);
		cmd.setReenvio(reenvio);
		mensagemVO = cmd.call();
		return mensagemVO.getMensagem();
	}

	@Override
	public EncontroInscricao enviaFicha(Mensagem mensagem, EncontroInscricao encontroInscricao) throws Exception {
		CasalFichaMensagemEnvioCommand cmd = injector.getInstance(CasalFichaMensagemEnvioCommand.class);
		cmd.setMensagem(mensagem);
		cmd.setEncontroInscricao(encontroInscricao);
		return cmd.call();
	}
	
}