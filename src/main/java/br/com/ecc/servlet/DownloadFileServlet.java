package br.com.ecc.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.servlet.ServletException;
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
	
	protected void writeResponse(HttpServletResponse resp, String fileName, Integer length, String contentType, byte[] content,boolean forceDownload) {
		if(fileName == null || "".equals(fileName) || 
			length == null || 
				contentType == null || "".equals(contentType)||
					content == null) {
			throw new WebRuntimeException("Possíveis problemas: fileName=null, length=null, contentType=null, content=null");
		}
		
		if(forceDownload)
			resp.setHeader("content-disposition", "attachment; filename="+fileName);
		
		ByteBuffer buff = ByteBuffer.allocate(length);
		buff.put(content);

		resp.setHeader("content-length", length.toString());
		resp.setHeader("content-type", contentType);
		try {
			OutputStream out = resp.getOutputStream();
			out.write(buff.array());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
