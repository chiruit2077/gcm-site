package br.com.ecc.server.service.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import br.com.ecc.client.ui.component.upload.UploadedFile;
import br.com.ecc.client.util.StringUtil;
import br.com.ecc.server.SessionHelper;

import com.google.inject.Singleton;

@Singleton
public class UploadArquivoServlet extends HttpServlet {
	private static final long serialVersionUID = 4970431311765086089L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			uploadFile(req);
		} catch (FileUploadException fue) {
			throw new ServletException(fue);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		throw new ServletException("Get not supported!");
	}

	private void uploadFile(final HttpServletRequest request) throws FileUploadException, IOException {

		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new FileUploadException("error multipart request not found");
		}

		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		FileItemIterator fileItemIterator = servletFileUpload.getItemIterator(request);

		String uploadDirectory = System.getProperty("java.io.tmpdir") + File.separator + "UploadFile" + StringUtil.right(Math.random()+"",10) + File.separator;
		
		UploadedFile uploadedFile = new UploadedFile();
		
		while (fileItemIterator.hasNext()) {
			List<UploadedFile> listaUploadedFile = SessionHelper.getUploadFile(request.getSession());
			int posicaoLista = 0;
			for (int i=0; i<listaUploadedFile.size(); i++) {
				UploadedFile uf = listaUploadedFile.get(i);
				if(uf.getProgresso()==null){
					uploadedFile = uf;
					posicaoLista=i;
					break;
				}
			}
			FileItemStream fileItemStream = fileItemIterator.next();
			String fileName = "arquivo"+StringUtil.randomString(10)+".jpg";
			if(fileItemStream.getName()!=null && !fileItemStream.getName().equals("blob")){
				if(uploadedFile!=null && uploadedFile.getNomeArquivo()==null){
					fileName = fileItemStream.getName().substring(fileItemStream.getName().lastIndexOf(File.separator) + 1);
				} else {
					fileName = fileItemStream.getName();
				}
			} else {
				fileName = uploadedFile.getNomeArquivo();
			}
			if(fileName==null){
				fileName = "arquivo"+StringUtil.randomString(10)+".jpg";
			}
			System.out.println("Uploading: " + fileName + " to " + uploadDirectory);
			
			new File(uploadDirectory).mkdir();

			uploadedFile.setCaminho(uploadDirectory);
			uploadedFile.setNomeArquivo(fileName);
			uploadedFile.setProgresso(0);
			listaUploadedFile.set(posicaoLista, uploadedFile);
			SessionHelper.setUploadFile(request.getSession(), listaUploadedFile);
			
			UploadProgressListener uploadProgressListener = new UploadProgressListener(request.getSession(), fileName);

			UploadProgressInputStream inputStream = new UploadProgressInputStream(fileItemStream.openStream(), request.getContentLength());
			inputStream.addListener(uploadProgressListener);

			File file = new File(uploadDirectory, fileName);
			Streams.copy(inputStream, new FileOutputStream(file), true);
		}
	}
}