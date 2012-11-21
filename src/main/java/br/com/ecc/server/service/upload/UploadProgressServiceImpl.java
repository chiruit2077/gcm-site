package br.com.ecc.server.service.upload;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.service.UploadProgressService;
import br.com.ecc.client.ui.component.upload.UploadedFile;
import br.com.ecc.server.SessionHelper;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class UploadProgressServiceImpl extends RemoteServiceServlet implements UploadProgressService {
	private static final long serialVersionUID = -2369978776140825255L;
	@Inject Injector injector;
	
	@Override
	public List<UploadedFile> getProgress() {
		return SessionHelper.getUploadFile(getThreadLocalRequest().getSession());
	}

	@Override
	public void initialize() {
		SessionHelper.setUploadFile(getThreadLocalRequest().getSession(), new ArrayList<UploadedFile>());
	}
	
	@Override
	public void setLista(List<UploadedFile> lista) {
		SessionHelper.setUploadFile(getThreadLocalRequest().getSession(), lista);
	}

	@Override
	public Integer gravaArquivoDigital(UploadedFile uf, Boolean resize) throws Exception {
		UploadImagemArquivoDigitalSalvarCommand salvarCmd = injector.getInstance(UploadImagemArquivoDigitalSalvarCommand.class);
		salvarCmd.setUploadedFile(uf);
		salvarCmd.setResize(resize);
		return salvarCmd.call();
	}
}