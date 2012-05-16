package br.com.ecc.server.service.upload;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import br.com.ecc.client.ui.component.upload.event.UploadEvent;

public final class UploadProgress {

	private static final String SESSION_KEY = "uploadProgress";
	private List<UploadEvent> events = new ArrayList<UploadEvent>();

	private UploadProgress() {
	}

	public List<UploadEvent> getEvents() {

		return events;
	}

	public void add(final UploadEvent event) {
		events.add(event);
	}

	public void clear() {
		events = new ArrayList<UploadEvent>();
	}

	public boolean isEmpty() {
		return events.isEmpty();
	}

	public static UploadProgress getUploadProgress(final HttpSession session) {
		Object attribute = session.getAttribute(SESSION_KEY);
		if (null == attribute) {
			attribute = new UploadProgress();
			session.setAttribute(SESSION_KEY, attribute);
		}

		return null == attribute ? null : (UploadProgress) attribute;
	}
}
