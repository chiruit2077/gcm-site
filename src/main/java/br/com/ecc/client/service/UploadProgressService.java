package br.com.ecc.client.service;

import java.util.List;

import br.com.ecc.client.ui.component.upload.UploadedFile;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("uploadprogress")
public interface UploadProgressService extends RemoteService {
	public void initialize() throws Exception;
	public List<UploadedFile> getProgress() throws Exception;
	public void setLista(List<UploadedFile> lista) throws Exception;
	public Integer gravaArquivoDigital(UploadedFile uf, Boolean resize) throws Exception;
}