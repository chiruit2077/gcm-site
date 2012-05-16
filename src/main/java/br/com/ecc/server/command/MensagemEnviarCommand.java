package br.com.ecc.server.command;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.client.util.StringUtil;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.tipo.TipoVariavelEnum;
import br.com.ecc.model.vo.MensagemVO;
import br.com.ecc.server.util.CriptoUtil;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class MensagemEnviarCommand implements Callable<MensagemVO>{

	@Inject EntityManager em;
	@Inject Injector injector;
	
	private MensagemVO mensagemVO;
	private Boolean reenvio;
	private Query q;

	@Override
	@Transactional
	public MensagemVO call() throws Exception {
		EnviaEmailCommand cmdEmail = injector.getInstance(EnviaEmailCommand.class);
		cmdEmail.setAssunto(mensagemVO.getMensagem().getGrupo().getNome() + " - " + mensagemVO.getMensagem().getTitulo());

		String sHTML = "";
		boolean ok;
		MensagemDestinatario d;
		for (int i=0; i<mensagemVO.getListaDestinatarios().size(); i++) {
			d = mensagemVO.getListaDestinatarios().get(i);
			ok = true;
			if(d.getDataEnvio()!=null){
				ok = false;
			}
			if(reenvio){
				ok = true;
			}
			if(ok){
				boolean enviado = false;
				if(d.getCasal()!=null){
					if(d.getCasal().getEle().getEmail()!=null && !d.getCasal().getEle().getEmail().equals("")){
						sHTML = substituiTagNome(mensagemVO.getMensagem(), d.getCasal(), d.getCasal().getEle());
						cmdEmail.setMensagem(sHTML);
						cmdEmail.setDestinatario(d.getCasal().getEle().getEmail());
						cmdEmail.call();
						enviado = true;
					}
					if(d.getCasal().getEla().getEmail()!=null && !d.getCasal().getEla().getEmail().equals("")){
						sHTML = substituiTagNome(mensagemVO.getMensagem(), d.getCasal(), d.getCasal().getEla());
						cmdEmail.setMensagem(sHTML);

						cmdEmail.setDestinatario(d.getCasal().getEla().getEmail());
						cmdEmail.call();
						enviado = true;
					}
				} else {
					if(d.getPessoa().getEmail()!=null && !d.getPessoa().getEmail().equals("")){
						sHTML = substituiTagNome(mensagemVO.getMensagem(), null, d.getPessoa());
						cmdEmail.setMensagem(sHTML);
						cmdEmail.setDestinatario(d.getPessoa().getEmail());
						cmdEmail.call();
						enviado = true;
					}
				}
				if(enviado){
					d.setDataEnvio(new Date());
					d.setDataConfirmacao(null);
					mensagemVO.getListaDestinatarios().set(i, em.merge(d));
				}
			}
		}
		return mensagemVO;
	}

	public String substituiTagNome(Mensagem mensagem, Casal c, Pessoa p) throws Exception {
		String sHTML = String.valueOf(mensagem.getMensagem());
		String nome = "", dadosUsuario = "";
		String preParametros= "&m=" + mensagem.getId();
		if(c!=null){
			nome = c.getApelidos("e");
			preParametros += "&c="+c.getId();
			
			dadosUsuario = dadosUsuario(p);
		} else {
			nome = p.getApelido();
			preParametros += "&p="+p.getId();
			
			dadosUsuario = dadosUsuario(p);
		}
		String parametros = "?ce="+StringUtil.randomString(50).toUpperCase() + "&ref=" + CriptoUtil.Codifica(preParametros);
		sHTML = sHTML.replace(TipoVariavelEnum.NOME_ENCONTRISTA.getTag(), nome);
		sHTML = sHTML.replace(TipoVariavelEnum.DADOS_USUARIO.getTag(), dadosUsuario);
		
		sHTML = sHTML.replace(TipoVariavelEnum.LINK_PLANILHA.getTag(), "<a href='http://www.eccbrasil.com.br/eccweb/"+ TipoVariavelEnum.LINK_PLANILHA.getModule()+parametros +"'>" + TipoVariavelEnum.LINK_PLANILHA.getNome() + "</a>");
		sHTML = sHTML.replace(TipoVariavelEnum.LINK_FICHA.getTag(), "<a href='http://www.eccbrasil.com.br/eccweb/"+ TipoVariavelEnum.LINK_FICHA.getModule()+parametros +"'>" + TipoVariavelEnum.LINK_FICHA.getNome() + "</a>");
		sHTML = sHTML.replace(TipoVariavelEnum.LINK_INSCRICAO.getTag(), "<a href='http://www.eccbrasil.com.br/eccweb/"+ TipoVariavelEnum.LINK_INSCRICAO.getModule()+parametros +"'>" + TipoVariavelEnum.LINK_INSCRICAO.getNome() + "</a>");

		if(sHTML.indexOf(TipoVariavelEnum.ICONE_CONFIRMACAO.getTag())>=0){
			preParametros += "&i="+mensagem.getGrupo().getIdArquivoDigital();
		}
		parametros = "?ce="+StringUtil.randomString(50).toUpperCase() + "&ref=" + CriptoUtil.Codifica(preParametros);
		sHTML = sHTML.replace(TipoVariavelEnum.ICONE_CONFIRMACAO.getTag(), "<img width='100px' src='http://www.eccbrasil.com.br/eccweb/"+ TipoVariavelEnum.ICONE_CONFIRMACAO.getModule()+parametros +"'/>");

		return sHTML;
	}
	@SuppressWarnings("unchecked")
	private String dadosUsuario(Pessoa pessoa){
		String dadosUsuario = "";
		if(pessoa.getEmail()!=null && !pessoa.getEmail().equals("")){
			q = em.createNamedQuery("usuario.porPessoa");
			q.setParameter("pessoa", pessoa);
			List<Usuario> lista = q.getResultList();
			Usuario usuario = null;
			if(lista.size()>0){
				usuario = lista.get(0);
			} else {
				try {
					usuario = new Usuario();
					usuario.setNivel(TipoNivelUsuarioEnum.NORMAL);
					usuario.setPessoa(pessoa);
					usuario.setSenha(CriptoUtil.Codifica(StringUtil.randomString(10)));
					usuario = em.merge(usuario);
				} catch (Exception e) {
					usuario = null;
					e.printStackTrace();
				}
			}
			if(usuario!=null){
				dadosUsuario = "<br><span style='font-weight:bold;'>Usuário: </span>" + pessoa.getEmail() + "<br>";
				try {
					dadosUsuario += "<span style='font-weight:bold;'>Senha: </span>" + CriptoUtil.Decodifica(usuario.getSenha()) + "<br>";
				} catch (Exception e) {
					dadosUsuario += "<span style='font-weight:bold;'>Senha: </span>UTILIZE O BOTÃO 'ESQUECI MINHA SENHA'<br>";
				}
			}
		}
		return dadosUsuario;
	}
	public MensagemVO getMensagemVO() {
		return mensagemVO;
	}
	public void setMensagemVO(MensagemVO mensagemVO) {
		this.mensagemVO = mensagemVO;
	}
	public Boolean getReenvio() {
		return reenvio;
	}
	public void setReenvio(Boolean reenvio) {
		this.reenvio = reenvio;
	}
}