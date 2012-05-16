package br.com.ecc.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@Singleton
public class DownloadResourceServlet extends DownloadFileServlet {

	private static final long serialVersionUID = 4246079617998124203L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String resource = req.getParameter("r");
		String contentType = req.getParameter("ct");
		String forceDownload = req.getParameter("fd");
		Boolean force = false;
		if(forceDownload!=null && forceDownload.equals("1")){
			force = true;
		}
		
		String fisicalFile = req.getParameter("ff");
		InputStream inputStream;
		try {
			if(fisicalFile!=null && !fisicalFile.equals("")){
				/*
				GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
				cmd.setNamedQuery("empresa.todos");
				List<Empresa> lista = cmd.call();
	
				resource = fisicalFile;
				if(lista.size()>0){
					resource = lista.get(0).getPathPublico() + File.separator + resource;
					resource = resource.replace("/", File.separator);
				}
				inputStream = new BufferedInputStream(new FileInputStream(resource));
				
				//pega o nome para download
				if(req.getParameter("fn")!=null && !req.getParameter("fn").equals("")){
					resource = req.getParameter("fn");
				}
				*/
				inputStream = getClass().getClassLoader().getResourceAsStream(resource);
			} else {
				inputStream = getClass().getClassLoader().getResourceAsStream(resource);
			}
			byte[] bytes = toByteArray(inputStream);
			writeResponse(resp, resource, bytes.length, contentType, bytes, force);
		} catch(Exception e){
			resp.getOutputStream().println("Erro ao ler o arquivo");
		}
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		output.flush();
		return output.toByteArray();
	}

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024 * 4];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	
}
