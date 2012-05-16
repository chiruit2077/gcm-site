package br.com.ecc.server.service.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
		uploadedFile.setPath(uploadDirectory);
		uploadedFile.setProgress(0);
		SessionHelper.setUploadFile(request.getSession(), uploadedFile);
		
		while (fileItemIterator.hasNext()) {
			FileItemStream fileItemStream = fileItemIterator.next();

			String fileName = fileItemStream.getName().substring(fileItemStream.getName().lastIndexOf(File.separator) + 1);
			//System.out.println("Uploading: " + fileName + " to " + uploadDirectory);
			
			new File(uploadDirectory).mkdir();

			UploadProgressListener uploadProgressListener = new UploadProgressListener(uploadDirectory, fileName, request.getSession());

			UploadProgressInputStream inputStream = new UploadProgressInputStream(fileItemStream.openStream(), request.getContentLength());
			inputStream.addListener(uploadProgressListener);

			File file = new File(uploadDirectory, fileName);

			Streams.copy(inputStream, new FileOutputStream(file), true);
		}
	}
}