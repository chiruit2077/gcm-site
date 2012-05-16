package br.com.ecc.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ecc.server.service.core.multipart.FileRequest;
import br.com.ecc.server.service.core.multipart.MultipartRequest;

import com.google.inject.Singleton;

@Singleton
public abstract class BaseUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 4970431311765086089L;

	/**
	 * Usar assim: FileRequest file = multipartRequest.getFile("arquivo"); <br>
	 *             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 *             String meuParam = multipartRequest.getParam("meuParam"); <br>
	 *             <br>
	 * Lançar uma exception se quiser dizer que o processo de upload vai falhar. 
	 * <br><br>
	 * Tratar o resultado no client assim:
	 * <br>
	 * &nbsp;&nbsp;&nbsp;form.addUploadCallback(new JSUploadCallback() { <br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public void success(String success) {  }<br><br>

		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public void failure(String error) {  }<br>
	   &nbsp;&nbsp;&nbsp;});<br>
	   <br>
	   @return Qualquer mensagem que seja util para voce. Vai aparecer no success(String success) no client. return null por padrao retorna o nome do arquivo para o cliente.
	 */
	public abstract String doPost(MultipartRequest multipartRequest) throws Exception;

	protected boolean decorateOutput = true;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		resp.setDateHeader("Expires", 0);
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setCharacterEncoding("UTF-8");
		
		StringBuffer htmlResponse = new StringBuffer();
		htmlResponse.append("<html>");
		htmlResponse.append("<head></head>");
		htmlResponse.append("<body>");
		
		String callbackMethodName = "";
		try {
			MultipartRequest multipartRequest = new MultipartRequest(req);
			callbackMethodName = multipartRequest.getParam("callbackUpload");
			FileRequest file = multipartRequest.getFile("arquivo");

			String success = doPost(multipartRequest);
			if(success == null || "".equals(success.trim())) {
				success = file.getFileName();
			}

			if (decorateOutput){
				htmlResponse.append("<script type=\"text/javascript\"> window.top."+ callbackMethodName +"("+true+", \""+ success +"\"); </script>");
			} else {
				htmlResponse.append(success);
			}
		} catch(Throwable e) {
			if (decorateOutput){
				htmlResponse.append("<script type=\"text/javascript\"> window.top."+ callbackMethodName +"("+false+", \""+e.getMessage()+"\"); </script>");
			} else {
				htmlResponse.append(e.getMessage());
			}
		}
		
		htmlResponse.append("</body>");
		htmlResponse.append("</html>");

		OutputStream out = resp.getOutputStream();
		out.write(htmlResponse.toString().getBytes());
		out.flush();
		out.close();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		throw new ServletException("Get not supported!");
	}
	
}
