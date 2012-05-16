package br.com.ecc.client.ui.sistema;

import java.io.Serializable;
import java.util.Map;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.util.PresenterShow;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.core.mvp.presenter.Presenter;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SistemaView extends BaseView<SistemaPresenter> implements SistemaPresenter.Display {

	public static int PORTAL_HEADER_HEIGHT = 100;
	public static int PORTAL_TOOLBAR_HEIGHT = 46;
	public static int PORTAL_HISTORYPANEL_HEIGHT = 20;
	public static int PORTAL_FOOTER_HEIGHT = 40;

	@UiField VerticalPanel contentPortlet;
	@UiField MenuBar sistemaMenuBar;
	
	@UiTemplate("SistemaView.ui.xml")
	interface SistemaViewUiBinder extends UiBinder<Widget, SistemaView> {
	}
	
	private SistemaViewUiBinder uiBinder = GWT.create(SistemaViewUiBinder.class);

	public SistemaView() {
		this.initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void init() {
		if(presenter.getUsuario()!=null){
			montaMenu();
		}
	}
	
	private void montaMenu() {
		MenuItem menuItem;
		
		boolean administrador = presenter.getUsuario().getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR);
		boolean convidado = presenter.getCasal().getTipoCasal()==null || presenter.getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO);
		
		//Cadastro
		menuItem = new MenuItem("Cadastro", new MenuBar(true));
		sistemaMenuBar.addItem(menuItem);
		
		if(administrador){
			menuItem.getSubMenu().addItem("Grupo", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.GRUPO); } });
		}
		
		menuItem.getSubMenu().addItem("Casal", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.CASAL); } });
		
		if(administrador){
			menuItem.getSubMenu().addItem("Encontro", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.ENCONTRO); } });
			menuItem.getSubMenu().addItem("Atividade", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.ATIVIDADE); } });
			menuItem.getSubMenu().addItem("Papel", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.PAPEL); } });
			menuItem.getSubMenu().addItem("Quarto", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.QUARTO); } });
		}
		
		//Secretaria
		if(administrador){
			menuItem = new MenuItem("Secretaria", new MenuBar(true));
			sistemaMenuBar.addItem(menuItem);
			
			menuItem.getSubMenu().addItem("Agrupamento", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.AGRUPAMENTO); } });
			menuItem.getSubMenu().addItem("Mensagens", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.MENSAGEM); } });
			
		}
		
		//Patrimonio
		if(administrador){
			menuItem = new MenuItem("Patrimônio", new MenuBar(true));
			sistemaMenuBar.addItem(menuItem);
			menuItem.getSubMenu().addItem("Cadastro", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.ITEM_PATRIMONIO); } });
		}
		//Tesouraria
		if(administrador){
			menuItem = new MenuItem("Tesouraria", new MenuBar(true));
			sistemaMenuBar.addItem(menuItem);
			menuItem.getSubMenu().addItem("Pagamentos", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.TESOURARIA_PAGAMENTOS); } });
		}
		
		//if(!presenter.getUsuario().getNivel().equals(TipoNivelUsuarioEnum.CONVIDADO)){
		if(!convidado){
			//Encontro
			menuItem = new MenuItem("Encontro", new MenuBar(true));
			sistemaMenuBar.addItem(menuItem);
			
			menuItem.getSubMenu().addItem("Convites ao encontro", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.ENCONTRO_CONVITE); } });
			menuItem.getSubMenu().addItem("Inscrição no encontro", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.ENCONTRO_INSCRICAO); } });
			menuItem.getSubMenu().addItem("Planilha de atividades", new Command() { @Override public void execute() { executaMenu(PresenterCodeEnum.ENCONTRO_PLANILHA); } });
		}
		
		
	}
	private void executaMenu(PresenterCodeEnum p) {
		//executaMenu(presenter, null);
		ExecutaMenuEvent e = new ExecutaMenuEvent();
		e.setJanela(p);
		WebResource.getInstanceCreated().getEventBus().fireEvent(e);
	}
	
	@SuppressWarnings("rawtypes")
	public void executaMenu(Class<? extends Presenter> presenter, Map<String, Serializable> parametros) {
		StateHistory state = new StateHistory(presenter);
		if(parametros!=null){
			for(String key : parametros.keySet()){
				state.put(key, parametros.get(key));
			}
		}
		PresenterShow.showPresenter(state,contentPortlet,this.presenter);
	}
	
	@Override
	public String getDisplayTitle() {
		return "Sistema Inicio";
	}

	@Override
	public void reset() {
	}

	@Override
	public void showPresenter(StateHistory stateHistory) {
		showWaitMessage(true);
		PresenterShow.showPresenter(stateHistory,contentPortlet,this.presenter);
	}

}