package br.com.ecc.server.service.upload;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

import br.com.ecc.client.ui.component.upload.UploadedFile;
import br.com.ecc.server.SessionHelper;

public final class UploadProgressListener implements ProgressListener {

	private static final double COMPLETE_PERECENTAGE = 100d;
	private int percentage = -1;
	private String fileName;
	private String path;
	private HttpSession session;
	
	public UploadProgressListener(String path, String fileName, HttpSession session) {
	    this.fileName = fileName;
	    this.session = session;
	    this.path = path;
	  }

	
	@Override
	public void update(final long bytesRead, final long totalBytes, final int items) {
		int percentage = (int) Math.floor(((double) bytesRead / (double) totalBytes) * COMPLETE_PERECENTAGE);

		if (this.percentage == percentage) {
			return;
		}

		this.percentage = percentage;

		UploadedFile uploadedFile = SessionHelper.getUploadFile(session);
		uploadedFile.setFilename(this.fileName);
		uploadedFile.setPath(this.path);
		uploadedFile.setProgress(percentage);
		
		SessionHelper.setUploadFile(session, uploadedFile);
	}
}