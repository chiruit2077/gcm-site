package br.com.ecc.client.service;

import java.util.List;

import br.com.ecc.client.ui.component.upload.UploadedFile;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UploadProgressServiceAsync {
	void getProgress(AsyncCallback<List<UploadedFile>> asyncCallback);
	void initialize(AsyncCallback<Void> asyncCallback);
	void setLista(List<UploadedFile> lista, AsyncCallback<Void> asyncCallback);
	void gravaArquivoDigital(UploadedFile uf, Boolean resize, AsyncCallback<Integer> asyncCallback);
}