package br.com.ecc.client.ui.component.upload;

import br.com.ecc.client.service.UploadProgressService;
import br.com.ecc.client.service.UploadProgressServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProgressController {

	private UploadProgressServiceAsync serviceUpload = GWT.create(UploadProgressService.class);

	public ProgressController() {
	}

	public void getProgress() {
		serviceUpload.getProgress(new AsyncCallback<UploadedFile>() {
			@Override
			public void onFailure(final Throwable t) {
			}
			@Override
			public void onSuccess(UploadedFile events) {
			}
		});
	}
}