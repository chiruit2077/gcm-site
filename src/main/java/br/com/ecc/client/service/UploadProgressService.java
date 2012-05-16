package br.com.ecc.client.service;

import java.util.List;

import br.com.ecc.client.ui.component.upload.UploadedFile;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("uploadprogress")
public interface UploadProgressService extends RemoteService {
	public int countFiles(String uploadDirectory);
	public List<UploadedFile> readFiles(String uploadDirectory);
	public UploadedFile getProgress();
	void initialize(UploadedFile uploadedFile);
}