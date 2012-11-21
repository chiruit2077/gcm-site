package br.com.ecc.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ecc.core.mvp.infra.exception.WebRuntimeException;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.server.command.basico.GetEntityCommand;
import br.com.ecc.server.util.ServletUtil;

import com.google.inject.Singleton;

@Singleton
public class DownloadArquivoDigitalServlet extends DownloadFileServlet{

	private static final long serialVersionUID = -7631507979976504393L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer arquivoId = ServletUtil.getParameterAsInteger(req, "id");
		if(arquivoId != null) {
			Boolean forceDownload = ServletUtil.getParameterAsBoolean(req, "forceDownload");
			Boolean thumb = ServletUtil.getParameterAsBoolean(req, "thumb");
			if(forceDownload == null) {
				forceDownload = false;
			}
			try {
				GetEntityCommand entityCmd = injector.getInstance(GetEntityCommand.class);
				entityCmd.setClazz(ArquivoDigital.class);
				entityCmd.setId(arquivoId);
				ArquivoDigital arquivo = (ArquivoDigital) entityCmd.call();
				if(thumb && arquivo.getThumb()!=null){
					writeResponse(resp, arquivo.getNomeArquivo(), arquivo.getTamanho(), arquivo.getMimeType(), arquivo.getThumb(), forceDownload);
				} else {
					writeResponse(resp, arquivo.getNomeArquivo(), arquivo.getTamanho(), arquivo.getMimeType(), arquivo.getDados(), forceDownload);
				}
			} catch (Exception e) {
				throw new WebRuntimeException("Erro ao realizar o download do arquivo: " + arquivoId + ". " + e.getMessage());
			}
		}
	}
}
