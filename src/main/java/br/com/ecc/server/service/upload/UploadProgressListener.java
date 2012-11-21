package br.com.ecc.server.service.upload;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

import br.com.ecc.client.ui.component.upload.UploadedFile;
import br.com.ecc.server.SessionHelper;

public final class UploadProgressListener implements ProgressListener {

	private static final double COMPLETE_PERECENTAGE = 100d;
	private int percentage = -1;
	private HttpSession session;
	private String fileName;
	
	public UploadProgressListener(HttpSession session, String fileName) {
	    this.session = session;
	    this.fileName=fileName;
	  }

	
	@Override
	public void update(final long bytesRead, final long totalBytes, final int items) {
		int percentage = (int) Math.floor(((double) bytesRead / (double) totalBytes) * COMPLETE_PERECENTAGE);

		if (this.percentage == percentage) {
			return;
		}

		this.percentage = percentage;

		List<UploadedFile> listaUploadedFile = SessionHelper.getUploadFile(session);
		for (int i=0; i<listaUploadedFile.size(); i++) {
			UploadedFile uf = listaUploadedFile.get(i);
			if(uf.getNomeArquivo().equals(fileName)){
				uf.setProgresso(percentage);
				listaUploadedFile.set(i, uf);
				break;
			}
		}
		SessionHelper.setUploadFile(session, listaUploadedFile);
	}
}