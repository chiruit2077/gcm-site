package br.com.ecc.server.service.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.ecc.client.service.RecadoService;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Recado;
import br.com.ecc.model.vo.InicioVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.command.AniversarianteCasalCommand;
import br.com.ecc.server.command.AniversariantePessoaCommand;
import br.com.ecc.server.command.EnviaEmailCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
@Transactional
public class RecadoServiceImpl extends SecureRemoteServiceServlet implements RecadoService {
	private static final long serialVersionUID = 914822776313399007L;
	
	@Inject Injector injector;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Recado> listaPorCasal(Casal casal, Boolean lido) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		if(lido){
			cmd.setNamedQuery("recado.paraCasal");
		} else {
			cmd.setNamedQuery("recado.paraCasalNaoLidos");
		}
		cmd.addParameter("casal", casal);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Recado> listaPorGrupo(Grupo grupo, Casal casal, Date inicio) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("recado.porGrupo");
		cmd.addParameter("grupo", grupo);
		cmd.addParameter("casal", casal);
		if(inicio==null){
			inicio = new Date();
		}
		cmd.addParameter("data", inicio);
		cmd.setMaxResults(10);
		return cmd.call();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Recado> listaTodosCasal(Casal casal, Date inicio) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("recado.todosCasal");
		cmd.addParameter("casal", casal);
		if(inicio==null){
			inicio = new Date();
		}
		cmd.addParameter("data", inicio);
		cmd.setMaxResults(10);
		return cmd.call();
	}

	@Override
	public Recado salvar(Recado recado) throws Exception {
		Boolean novo = false;
		if(recado.getId()==null){
			recado.setData(new Date());
			recado.setLido(false);
			novo = true;
		}
		SaveEntityCommand cmd = injector.getInstance(SaveEntityCommand.class);
		cmd.setBaseEntity(recado);
		recado = (Recado) cmd.call();
		
		if(novo){
			String destinatario = "";
			if(recado.getCasal().getEle().getEmail()!=null){
				destinatario = recado.getCasal().getEle().getEmail();
			}
			if(recado.getCasal().getEla().getEmail()!=null){
				if(!destinatario.equals("")){
					destinatario += ",";
				}
				destinatario += recado.getCasal().getEla().getEmail();
			}
			StringBuffer texto = new StringBuffer();
			texto.append("<b>Queridos " + recado.getCasal().getApelidos("e") + ",</b><br>");
			texto.append("Vocês receberam um recadinho.<br><br>");
			texto.append("<table cellpadding='3' cellspacing='0' style='border:1px solid lightgray; background-color:#dbedff;'><tr><td style='vertical-align:top;'>");
			if(recado.getCasalOrigem().getIdArquivoDigital()!=null){
				texto.append("<table><tr><td>");
				texto.append("<img width='100px' height='auto' src='http://www.eccbrasil.com.br/eccweb/downloadArquivoDigital?id="+recado.getCasalOrigem().getIdArquivoDigital() +"'/>");
				texto.append("</td></tr><tr><td>");
				texto.append("<b>"+recado.getCasalOrigem().getApelidos("e") + "</b>");
				texto.append("</td></tr></table>");
			} else {
				texto.append("<b>"+recado.getCasalOrigem().getApelidos("e") + "</b>");	
			}
			// style='border:1px solid lightgray; padding: 5px; background-color:#dbedff;'
			texto.append("</td><td style='vertical-align:top;'>");
			texto.append("<table><tr><td style='border-left:1px solid lightgray;padding: 5px;'><div>");
			texto.append(String.valueOf(recado.getMensagem()));
			texto.append("</div></td></tr></table>");
			texto.append("</td></tr></table><br>");
			texto.append("Clique no endereço a seguir para visualizar, responder ou enviar novos recados.<br>");
			texto.append("<a href='http://www.eccbrasil.com.br'>www.eccbrasil.com.br</a><br><br>");
			texto.append("<img width='100px' src='http://www.eccbrasil.com.br/eccweb/downloadArquivoDigital?id=" +recado.getCasal().getGrupo().getIdArquivoDigital() + "'/>");
	
			EnviaEmailCommand cmdEmail = injector.getInstance(EnviaEmailCommand.class);
			cmdEmail.setAssunto(recado.getCasal().getGrupo().getNome() + " - Notificação de recado");
			cmdEmail.setDestinatario(destinatario);
			cmdEmail.setMensagem(new String(texto));
			cmdEmail.setNaoEsperar(true);
			cmdEmail.call();
		}
		
		return recado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public InicioVO listaInicial(Casal casal, Encontro encontro) throws Exception {
		InicioVO vo = new InicioVO();
		
		vo.setListaRecados(listaPorCasal(casal, false));
		
		AniversariantePessoaCommand cmdA = injector.getInstance(AniversariantePessoaCommand.class);
		vo.setListaAniversarioPessoa(cmdA.call());
		
		AniversarianteCasalCommand cmdC = injector.getInstance(AniversarianteCasalCommand.class);
		vo.setListaAniversarioCasal(cmdC.call());
		
		if(encontro!=null){
			GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
			cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
			cmd.addParameter("encontro", encontro);
			vo.setListaConvidados(cmd.call());
		} else {
			vo.setListaConvidados(new ArrayList<Casal>());
		}
		Collections.sort(vo.getListaConvidados(), new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getApelidos("e").compareTo(o2.getApelidos("e"));
			}
		});
		
		return vo;
	}

}