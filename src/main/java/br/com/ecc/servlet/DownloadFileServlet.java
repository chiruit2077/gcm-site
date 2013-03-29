package br.com.ecc.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ecc.core.mvp.infra.exception.WebRuntimeException;

import com.google.inject.Inject;
import com.google.inject.Injector;


public abstract class DownloadFileServlet extends HttpServlet{

	@Inject
	protected Injector injector;

	private static final long serialVersionUID = 5915431898806084080L;

	protected void writeResponse(HttpServletResponse resp, String fileName, Integer length, String contentType, byte[] content, boolean forceDownload) throws IOException {
		if(fileName == null || "".equals(fileName) || length == null || content == null) {
			throw new WebRuntimeException("Possíveis problemas: fileName=null, length=null, content=null");
		}

		resp.setHeader("Content-Length", "" + length);
		resp.setHeader("Content-Type", (contentType != null && !"".equals(contentType)) ? contentType : "application/octet-stream" );
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		resp.setHeader("Pragma", "no-cache");


		ServletOutputStream op = resp.getOutputStream();
		op.flush();

		if (contentType != null && ( contentType.equals("text/plain") || contentType.equals("text/csv"))){
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(op, resp.getCharacterEncoding()));
			out.write(new String(content));
			out.flush();
			out.close();
		}else{
			BufferedInputStream input = null;
			BufferedOutputStream output = null;
			try {
			    input = new BufferedInputStream(new ByteArrayInputStream(content));
			    output = new BufferedOutputStream(op);
			    byte[] buffer = new byte[8192];
			    for (int size = 0; (size = input.read(buffer)) > 0;) {
			        output.write(buffer, 0, size);
			    }
			} finally {
			    if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
			    if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
			}
			op.flush();
			op.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
