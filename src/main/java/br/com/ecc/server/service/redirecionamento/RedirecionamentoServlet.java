package br.com.ecc.server.service.redirecionamento;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.util.StringTokenizer;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.tipo.TipoVariavelEnum;
import br.com.ecc.model.vo.ParametrosRedirecionamentoVO;
import br.com.ecc.server.SessionHelper;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;
import br.com.ecc.server.util.CriptoUtil;
import br.com.ecc.servlet.DownloadFileServlet;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class RedirecionamentoServlet extends DownloadFileServlet {
	private static final long serialVersionUID = -1165158597712744726L;
	
	@Inject Injector injector;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		throw new ServletException("Get not supported!");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		try {
			String imageLinkId = processaParametros(req);
			if(imageLinkId!=null && !imageLinkId.equals("")){
				GetEntityCommand entityCmd = injector.getInstance(GetEntityCommand.class);
				entityCmd.setClazz(ArquivoDigital.class);
				entityCmd.setId(Integer.valueOf(imageLinkId));
				ArquivoDigital arquivo = (ArquivoDigital) entityCmd.call();
				writeResponse(resp, arquivo.getNomeArquivo(), arquivo.getTamanho(), arquivo.getMimeType(), arquivo.getDados(), false);
			} else {
				resp.sendRedirect("/");
			}
		} catch (Exception e) {
			try {
				resp.getOutputStream().print("<script>alert('Endereço não reconhecido');</script>");
				resp.sendRedirect("/");
			} catch (IOException e1) {}
		}
	}

	@SuppressWarnings("unchecked")
	private String processaParametros(HttpServletRequest req) throws Exception{
		ParametrosRedirecionamentoVO parametros = new ParametrosRedirecionamentoVO();
		String ce = req.getParameter("ce");
		String imageLinkId = "";
		if(ce!=null && ce.length()==50){
			String ref = req.getParameter("ref");
			ref = CriptoUtil.Decodifica(ref);
			
			String c = buscaParametro(ref, "c");
			String p = buscaParametro(ref, "p");
			String m = buscaParametro(ref, "m");
			imageLinkId = buscaParametro(ref, "i");
			
			boolean param = false;
			GetEntityListCommand cmdList = injector.getInstance(GetEntityListCommand.class);
			GetEntityCommand entityCmd = injector.getInstance(GetEntityCommand.class);
			if(c!=null){
				entityCmd.setClazz(Casal.class);
				entityCmd.setId(Integer.valueOf(c));
				parametros.setCasal((Casal) entityCmd.call());
				
				cmdList.setNamedQuery("mensagemDestinatario.porMensagemCasal");
				cmdList.addParameter("casal", parametros.getCasal());
				param = true;
			} else if(p!=null){
				entityCmd.setClazz(Pessoa.class);
				entityCmd.setId(Integer.valueOf(p));
				parametros.setPessoa((Pessoa) entityCmd.call());
				
				cmdList.setNamedQuery("mensagemDestinatario.porMensagemPessoa");
				cmdList.addParameter("pessoa", parametros.getPessoa());
				param = true;
			}
			if(m!=null && param){
				entityCmd.setClazz(Mensagem.class);
				entityCmd.setId(Integer.valueOf(m));
				parametros.setMensagem((Mensagem) entityCmd.call());

				cmdList.addParameter("mensagem", parametros.getMensagem());
				List<MensagemDestinatario> lista = cmdList.call();
				if(lista.size()>0){
					MensagemDestinatario md = lista.get(0);
					md.setDataConfirmacao(new Date());
					try {
						SaveEntityCommand cmdSalvar = injector.getInstance(SaveEntityCommand.class);
						cmdSalvar.setBaseEntity(md);
						cmdSalvar.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		//redirecionamento a LINKs
		if(req.getRequestURL().indexOf(TipoVariavelEnum.LINK_FICHA.getModule())>=0){
			parametros.setPresenterCode(PresenterCodeEnum.CASAL);
		} else if(req.getRequestURL().indexOf(TipoVariavelEnum.LINK_INSCRICAO.getModule())>=0){
			parametros.setPresenterCode(PresenterCodeEnum.ENCONTRO_INSCRICAO);
		} else if(req.getRequestURL().indexOf(TipoVariavelEnum.LINK_PLANILHA.getModule())>=0){
			parametros.setPresenterCode(PresenterCodeEnum.ENCONTRO_PLANILHA);
		}
		SessionHelper.setParametros(req.getSession(), parametros);
		return imageLinkId;
	}

	private String buscaParametro(String dados, String parametro) {
		StringTokenizer tok = new StringTokenizer(dados, "&");
		for (String p : tok.getTokens()) {
			if(p.indexOf(parametro+"=")>=0){
				return p.substring(p.indexOf(parametro+"=")+(parametro+"=").length());
			}
		}
		return null;
	}
	
}