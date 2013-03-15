package br.com.ecc.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.server.command.basico.DeleteEntityCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = -508274188664932639L;

	@Inject EntityManager em;
	@Inject Injector injector;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("reportId")!=null){
			ArquivoDigital relatorio = (ArquivoDigital)em.find(ArquivoDigital.class, Integer.valueOf(request.getParameter("reportId")));
			if (relatorio != null){
				ByteBuffer buff = ByteBuffer.allocate(relatorio.getTamanho());
				buff.put(relatorio.getDados());

				response.setHeader("content-length", relatorio.getTamanho().toString());
				response.setHeader("content-type", relatorio.getMimeType());
				if(relatorio.getMimeType().toUpperCase().indexOf("PDF")<0){
					response.setHeader("content-disposition", "attachment; filename="+relatorio.getNomeArquivo());
				}
				try {
					OutputStream out = response.getOutputStream();
					out.write(buff.array());
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				DeleteEntityCommand deleteCmd = injector.getInstance(DeleteEntityCommand.class);
				deleteCmd.setBaseEntity(relatorio);
				try {
					deleteCmd.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				String msg = "Relatório não encontrado";
				response.getOutputStream().println(msg);
			}
		} else {
			String msg = "Relatório não encontrado";
			response.getOutputStream().println(msg);
		}
	}
}