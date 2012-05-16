package br.com.ecc.client.ui.component.upload;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class UploadProgressBar extends Composite {

	private HorizontalPanel panelPrincipal;
	private static final int COMPLETE_PERECENTAGE = 100;
	private static final int REMOVE_DELAY = 3000;
	
	private UploadPanel uploadPanel;

	public UploadProgressBar() {
		panelPrincipal = new HorizontalPanel();
		this.initWidget(panelPrincipal);
	}
	public void initialize(String fileName){
		uploadPanel = new UploadPanel(fileName);
		panelPrincipal.add(uploadPanel);
	}
	public void atualiza(UploadedFile uploadedFile) {
		uploadPanel.update(uploadedFile.getFilename(), uploadedFile.getProgress());
		if (uploadedFile.getProgress() == COMPLETE_PERECENTAGE) {
			Timer timer = new Timer() {
				@Override
				public void run() {
					panelPrincipal.remove(uploadPanel);
				}
			};
			timer.schedule(REMOVE_DELAY);
		}
	}

	private static final class UploadPanel extends HorizontalPanel {
		private ProgressBar bar;
		private Label label;

		public UploadPanel(String fileName) {
			setStyleName("UploadPanel");
			bar = new ProgressBar();
			label = new Label(fileName);
			add(bar);
			add(label);
			this.setWidth("100%");
			this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			this.setSpacing(0);
		}

		public void update(String filename, int percentage) {
			bar.update(percentage);
			label.setText("["+ percentage + "%] " + filename);
		}
	}
}