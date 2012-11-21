package br.com.ecc.client.ui.component.upload;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class UploadProgressBar extends Composite {

	private HorizontalPanel panelPrincipal;
	private static final int COMPLETE_PERCENTAGE = 100;
	private static final int REMOVE_DELAY = 3000;
	
	private UploadPanel uploadPanel;

	public UploadProgressBar() {
		panelPrincipal = new HorizontalPanel();
		this.initWidget(panelPrincipal);
	}
	public void initialize(String fileName, Boolean showFileName){
		uploadPanel = new UploadPanel(fileName, showFileName);
		panelPrincipal.add(uploadPanel);
	}
	public void atualiza(UploadedFile uploadedFile) {
		if(uploadedFile.getProgresso()!=null){
			if(uploadPanel!=null){
				uploadPanel.update(uploadedFile.getNomeArquivo(), uploadedFile.getProgresso());
				if (uploadedFile.getProgresso() == COMPLETE_PERCENTAGE) {
					Timer timer = new Timer() {
						@Override
						public void run() {
							panelPrincipal.remove(uploadPanel);
						}
					};
					timer.schedule(REMOVE_DELAY);
				}
			}
		}
	}

	private static final class UploadPanel extends HorizontalPanel {
		private ProgressBar bar;
		private Label label;
		private Boolean showFileName=true;
		
		public UploadPanel(String fileName, Boolean showFileName) {
			this.showFileName=showFileName;
			setStyleName("UploadPanel");
			bar = new ProgressBar();
			add(bar);
			label = new Label();
			add(label);
			if(showFileName){
				label.setText(fileName);
			}
			this.setWidth("100%");
			this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			this.setSpacing(0);
		}

		public void update(String filename, int percentage) {
			bar.update(percentage);
			label.setText("["+ percentage + "%]");
			if(showFileName){
				label.setText(label.getText()+ " " +  filename);
			}
		}
	}
}