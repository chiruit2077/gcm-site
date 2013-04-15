package br.com.ecc.client.ui.sistema.encontro;

import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.model.vo.EncontroMonitorVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EncontroMonitorView extends BaseView<EncontroMonitorPresenter> implements EncontroMonitorPresenter.Display {

	@UiTemplate("EncontroMonitorView.ui.xml")
	interface ViewUiBinder extends UiBinder<Widget, EncontroMonitorView> {}
	private ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
	private final int DELAY = 1;

	@UiField Label tituloFormularioLabel;
	@UiField VerticalPanel htmlPanel;
	@UiField VerticalPanel atividadePanel;
	@UiField VerticalPanel paralelaAtividadePanel;
	@UiField VerticalPanel atividadeProximaPanel;
	@UiField VerticalPanel proximasAtividadesPanel;
	@UiField HorizontalPanel row1Panel;
	@UiField HorizontalPanel row2Panel;
	@UiField TextBox delayTextBox;

	private int delay = DELAY * 1000;
	private int height = -1;
	private int width = -1;
	protected int widthpanel = -1;
	protected int heightpanel = -1;


	public EncontroMonitorView() {
		initWidget(uiBinder.createAndBindUi(this));
		tituloFormularioLabel.setText(getDisplayTitle());
		Window.enableScrolling(false);
		Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                resizeTimer.schedule(500);
            }
        });

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            public void execute() {
            	resizeTimer.schedule(500);
            }
		});

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            public void execute() {
            	buscaDados.schedule(delay);
            }
		});
		delayTextBox.setText(String.valueOf(delay/1000));
	}

	@UiHandler("delayTextBox")
	public void delayTextBoxValueChangeEvent(ValueChangeEvent<String> event){
		if (event.getValue()!= null && !event.getValue().equals(""))
			delay = Integer.parseInt(event.getValue())*1000;
		else
			delay = DELAY;
	}

	private Timer resizeTimer = new Timer() {
        @Override
        public void run() {
            int newHeight = (int) (EncontroMonitorView.this.getWindowHeight()-100);
            int newWidth = (int) (EncontroMonitorView.this.getWindowWidth()-10);
            //967px
            if (newHeight != height || newWidth != width) {
            	height = newHeight;
            	width = newWidth;
            	widthpanel = width/2-9;
            	heightpanel = height/2-9;
            	htmlPanel.setWidth(width+"px");
            	htmlPanel.setHeight(height + "px");
            	atividadePanel.setWidth(widthpanel+"px");
            	atividadePanel.setHeight(heightpanel + "px");
            	paralelaAtividadePanel.setWidth(widthpanel+"px");
            	paralelaAtividadePanel.setHeight(heightpanel + "px");
            	atividadeProximaPanel.setWidth(widthpanel+"px");
            	atividadeProximaPanel.setHeight(heightpanel + "px");
            	proximasAtividadesPanel.setWidth(widthpanel+"px");
            	proximasAtividadesPanel.setHeight(heightpanel + "px");
            	row1Panel.setWidth(width+"px");
            	row1Panel.setHeight(heightpanel + "px");
            	row2Panel.setWidth(width+"px");
            	row2Panel.setHeight(heightpanel + "px");
            }
        }
    };

    private Timer buscaDados = new Timer() {
        @Override
        public void run() {
            presenter.buscaDados();
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                public void execute() {
                	buscaDados.schedule(delay);
                }
    		});
        }
    };

	@Override
	public String getDisplayTitle() {
		return "Monitor de Atividades";
	}

	@UiHandler("fecharImage")
	public void fecharImageClickHandler(ClickEvent event){
		presenter.fechar();
	}

	@Override
	public void reset() {
	}

	@Override
	public void populaEntidades(EncontroMonitorVO vo) {
		atividadePanel.clear();
		atividadePanel.add(new Label(vo.getEncontro().toString()));
		if (vo.getAtividade1()!=null)
			atividadePanel.add(new Label(vo.getAtividade1().toString()));
	}
}