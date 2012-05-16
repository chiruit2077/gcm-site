package br.com.ecc.client.service;

import java.util.List;

import br.com.ecc.client.ui.component.upload.UploadedFile;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UploadProgressServiceAsync {
	void countFiles(String uploadDirectory, AsyncCallback<Integer> asyncCallback);
	void readFiles(String uploadDirectory, AsyncCallback<List<UploadedFile>> asyncCallback);
	void getProgress(AsyncCallback<UploadedFile> asyncCallback);
	void initialize(UploadedFile uploadedFile, AsyncCallback<Void> asyncCallback);
}