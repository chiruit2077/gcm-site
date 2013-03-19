package br.com.ecc.client.ui.main;

import java.util.Date;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseView;
import br.com.ecc.client.ui.component.ExtendedHorizontalPanel;
import br.com.ecc.client.ui.component.PortletItem;
import br.com.ecc.client.ui.component.PortletItem.Position;
import br.com.ecc.client.ui.home.HomePresenter;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.presenter.Presenter;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoCasalEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainView extends BaseView<MainPresenter> implements MainPresenter.Display {

	@UiTemplate("MainView.ui.xml")
	interface MainViewUiBinder extends UiBinder<Widget, MainView> {
	}

	private MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	public static int PORTAL_HEADER_HEIGHT = 63;
	public static final int PORTAL_FOOTER_HEIGHT = 0;

	@UiField HorizontalPanel menuHorizontalPanel;

	@UiField Label usuarioNome;
	@UiField Image usuarioImage;
	@UiField HorizontalPanel usuarioHorizontalPanel;
	DecoratedPopupPanel simplePopupUsuario = new DecoratedPopupPanel(true);

	@UiField Label grupoLabel;
	@UiField Image grupoImage;
	@UiField HorizontalPanel grupoHorizontalPanel;
	DecoratedPopupPanel simplePopupGrupo = new DecoratedPopupPanel(true);

	@UiField Label encontroLabel;
	@UiField HorizontalPanel encontroHorizontalPanel;
	DecoratedPopupPanel simplePopupEncontro = new DecoratedPopupPanel(true);

	@UiField VerticalPanel mainPanel;
	@UiField HorizontalPanel headerPanel;
	@UiField HorizontalPanel bodyContent;

	@UiField DialogBox loginDialogBox;
	@UiField TextBox emailTextBox;
	@UiField PasswordTextBox senhaTextBox;

	@UiField Button conectarButton;
	@UiField Button senhaButton;
	@UiField Button cancelarButton;

	@UiField FlowPanel waitFlowPanel;

	private Integer presenterCode;
	private Usuario usuario;
	private boolean paginaIncial = false;

	public MainView() {
		this.initWidget(uiBinder.createAndBindUi(this));

		senhaTextBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int keyCode = event.getNativeKeyCode();
				if (keyCode == KeyCodes.KEY_ENTER) {
					conectarButtonClickHandler(null);
				} else if (keyCode == KeyCodes.KEY_ESCAPE) {
					cancelarButtonClickHandler(null);
				}
			}
		});
		emailTextBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int keyCode = event.getNativeKeyCode();
				if (keyCode == KeyCodes.KEY_ENTER) {
					conectarButtonClickHandler(null);
				} else if (keyCode == KeyCodes.KEY_ESCAPE) {
					cancelarButtonClickHandler(null);
				}
			}
		});

		ClickHandler ch = new ClickHandler() {@Override public void onClick(ClickEvent arg0) { montaUsuarioPanel(); }};
		usuarioHorizontalPanel.addDomHandler(ch, ClickEvent.getType());

		simplePopupUsuario.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> arg0) {
				usuarioHorizontalPanel.setStyleName("portal-headerBar");
				usuarioNome.setStyleName("portal-headerBarText");
			}
		});

		ClickHandler chGrupo = new ClickHandler() {@Override public void onClick(ClickEvent arg0) { montaGrupoPanel(); }};
		grupoHorizontalPanel.addDomHandler(chGrupo, ClickEvent.getType());

		simplePopupGrupo.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> arg0) {
				grupoHorizontalPanel.setStyleName("portal-headerBar");
				grupoLabel.setStyleName("portal-headerBarText");
			}
		});

		ClickHandler chEncontro = new ClickHandler() {@Override public void onClick(ClickEvent arg0) { montaEncontroPanel(); }};
		encontroHorizontalPanel.addDomHandler(chEncontro, ClickEvent.getType());

		simplePopupEncontro.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> arg0) {
				encontroHorizontalPanel.setStyleName("portal-headerBar");
				encontroLabel.setStyleName("portal-headerBarText");
			}
		});

	}

	private static native void click(Element element)/*-{
		element.click();
	}-*/;

	@Override
	public void adjustWindowSize() {
		this.setBodyLayoutSize((this.getWindowHeight() - PORTAL_HEADER_HEIGHT - PORTAL_FOOTER_HEIGHT) + "px");
	}

	private native void setBodyLayoutSize(String size) /*-{
		$wnd.changecss('ECCWeb.css', '.portal-bodyLayout', 'height', size);
	}-*/;

	@Override
	public String getDisplayTitle() {
		return null;
	}

	@Override
	public void reset(){
		resetToolbar(true);
	}

	@Override
	public void resetToolbar(boolean resetUsuario) {
		paginaIncial = true;
		if(resetUsuario){
			usuarioHorizontalPanel.setVisible(false);
			usuarioNome.setText(null);
			usuarioImage.setUrl("images/user.png");
		}
		menuHorizontalPanel.clear();
		grupoHorizontalPanel.setVisible(false);
		grupoLabel.setText(null);
		encontroHorizontalPanel.setVisible(false);
		encontroLabel.setText(null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void showState(Presenter presenter, String titulo) {
		if (presenter != null) {
			BasePresenter basePresenter = (BasePresenter) presenter;
			bodyContent.clear();
			bodyContent.add(basePresenter.getDisplay().asWidget());
		}
	}

	public void habilita(boolean habilita){
		emailTextBox.setEnabled(habilita);
		senhaTextBox.setEnabled(habilita);
		conectarButton.setEnabled(habilita);
		senhaButton.setEnabled(habilita);
		cancelarButton.setEnabled(habilita);
	}

	@Override
	public void login(Integer presenterCode) {
		this.presenterCode = presenterCode;
		habilita(true);
		emailTextBox.setValue(null);
		senhaTextBox.setValue(null);
		String email = Cookies.getCookie("ecc_email");
		if(email!=null){
			emailTextBox.setValue(Cookies.getCookie("ecc_email"));
		}
		loginDialogBox.center();
		loginDialogBox.show();
		if(emailTextBox.getValue().equals("")){
			emailTextBox.setFocus(true);
		} else {
			senhaTextBox.setFocus(true);
		}
		if(!GWT.isProdMode()){
			String pass = Cookies.getCookie("ecc_pass");
			if(pass!=null && !pass.equals("")){
				senhaTextBox.setValue(Cookies.getCookie("ecc_pass"));
				conectarButton.fireEvent(new ClickEvent(){
				});
			}
		}
	}

	@UiHandler("conectarButton")
	public void conectarButtonClickHandler(ClickEvent event) {
		presenter.conectar(emailTextBox.getValue(), senhaTextBox.getValue(), presenterCode);
		senhaTextBox.setValue(null);
	}

	@UiHandler("cancelarButton")
	public void cancelarButtonClickHandler(ClickEvent event) {
		loginDialogBox.hide();
		presenter.logout();
	}

	@UiHandler("senhaButton")
	public void senhaButtonClickHandler(ClickEvent event) {
		if(emailTextBox.getValue()!=null && !emailTextBox.getValue().equals("")){
			if(Window.confirm("A senha de acesso para \"" + emailTextBox.getValue() + "\"  será redefinida e enviada por email.\nDeseja continuar?")){
				habilita(false);
				presenter.reenviarSenha(emailTextBox.getValue());
			}
		}
	}

	@Override
	public void conectado(Usuario usuario, Boolean paginaInicial) {
		this.usuario = usuario;
		loginDialogBox.hide();
		this.paginaIncial = paginaInicial;
		defineDadosUsuario();
		defineGrupo();
	}

	private void defineDadosUsuario(){
		usuarioHorizontalPanel.setVisible(true);
		usuarioNome.setText(usuario.getNome());
		if(!paginaIncial){
		}
	}

	@Override
	public void senhaEnviada(Boolean result) {
		if(result){
			loginDialogBox.hide();
			Window.alert("Senha enviada. Verifique seu e-mail.");
		}
		presenter.getWebResource().getPlaceManager().newPlaceClean(HomePresenter.class);
	}

	protected void montaUsuarioPanel() {
		simplePopupUsuario.clear();
        simplePopupUsuario.setStyleName("portal-headerBox");

        usuarioHorizontalPanel.setStyleName("portal-headerBarClicked");
        usuarioNome.setStyleName("portal-headerBarTextClicked");

        int left = usuarioHorizontalPanel.getAbsoluteLeft() + usuarioHorizontalPanel.getOffsetWidth();
        int top = usuarioHorizontalPanel.getAbsoluteTop() + usuarioHorizontalPanel.getOffsetHeight() + 1;

        VerticalPanel menuUsuarioVP = new VerticalPanel();
        menuUsuarioVP.setSpacing(7);
        menuUsuarioVP.setWidth("100%");

        if(!paginaIncial){
	        //MEUS DADOS
	        String url = "images/user.png";
	        if(presenter.getCasal()!=null && presenter.getCasal().getIdArquivoDigital()!=null){
	        	url = "eccweb/downloadArquivoDigital?thumb=true&id="+presenter.getCasal().getIdArquivoDigital();
	        }
	        PortletItem meusDadosPortlet = criaPortletItem(null, url, Position.LEFT, 50);

			//nome
	        Label apelidoLabel;
	        Label nomeLabel;
	        Label dadosLabel;
			VerticalPanel vp = new VerticalPanel();
			vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			apelidoLabel = new Label();
			apelidoLabel.setStyleName("fundo-nome");
			apelidoLabel.setText(usuario.getNome());
			vp.add(apelidoLabel);

			nomeLabel = new Label();
			nomeLabel.setStyleName("fundo-detalhe");
			if(presenter.getCasal()!=null){
				nomeLabel.setText(presenter.getCasal().getApelidos("e"));
			}
			vp.add(nomeLabel);

			dadosLabel = new Label();
			dadosLabel.setStyleName("fundo-detalheMin");
			dadosLabel.setText("(Clique aqui para editar seus dados)");
			vp.add(dadosLabel);

			meusDadosPortlet.getPortletContent().add(vp);
			meusDadosPortlet.getPortlet().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					executaMenu(PresenterCodeEnum.MEUS_DADOS);
					simplePopupUsuario.hide();
				}
			});
			menuUsuarioVP.add(meusDadosPortlet.getPortlet());
        }

        // SAIR
        PortletItem sairPortlet = criaPortletItem("Sair do sistema", "images/sair.png", Position.RIGHT, 30);
		sairPortlet.getPortlet().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm("Deseja sair do sistema?")){
					presenter.logout();
				}
			}
		});
		menuUsuarioVP.add(sairPortlet.getPortlet());

		simplePopupUsuario.add(menuUsuarioVP);
		simplePopupUsuario.show();
		simplePopupUsuario.setPopupPosition(left - simplePopupUsuario.getOffsetWidth()+1, top - 1);
	}

	protected void montaGrupoPanel() {
		if(presenter.getCasal()!=null && presenter.getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
			simplePopupGrupo.clear();
	        simplePopupGrupo.setStyleName("portal-headerBox");

	        grupoHorizontalPanel.setStyleName("portal-headerBarClicked");
	        grupoLabel.setStyleName("portal-headerBarTextClicked");

	        VerticalPanel panelVP = new VerticalPanel();
	        panelVP.setSpacing(2);
	        panelVP.setWidth("100%");

	        PortletItem portlet;
	        if(!paginaIncial){
	        	for (final Grupo grupo : presenter.getListaGrupos()) {
	        		portlet = criaPortletItem(grupo.getNome(), "eccweb/downloadArquivoDigital?id="+grupo.getIdArquivoDigital(), Position.LEFT, 30);
	        		portlet.getPortlet().addClickHandler(new ClickHandler() {
	        			@Override
	        			public void onClick(ClickEvent event) {
	        				setGrupoVisual(grupo);
	        				encontroHorizontalPanel.setVisible(false);
	        				presenter.buscaEncontros(grupo.getNome());
	        				simplePopupGrupo.hide();
	        				presenter.getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	        			}
	        		});
	        		panelVP.add(portlet.getPortlet());
				}
	        }

			simplePopupGrupo.add(panelVP);
			simplePopupGrupo.show();
			simplePopupGrupo.setPopupPosition(grupoHorizontalPanel.getAbsoluteLeft()-1, grupoHorizontalPanel.getAbsoluteTop() + grupoHorizontalPanel.getOffsetHeight());
		}
	}

	protected void montaEncontroPanel() {
		if(presenter.getCasal()!=null && !presenter.getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
			simplePopupEncontro.clear();
	        simplePopupEncontro.setStyleName("portal-headerBox");

	        int left = encontroHorizontalPanel.getAbsoluteLeft();
	        int top = encontroHorizontalPanel.getAbsoluteTop() + encontroHorizontalPanel.getOffsetHeight();

	        encontroHorizontalPanel.setStyleName("portal-headerBarClicked");
	        encontroLabel.setStyleName("portal-headerBarTextClicked");

	        VerticalPanel panelVP = new VerticalPanel();
	        panelVP.setSpacing(2);
	        panelVP.setWidth("100%");

	        PortletItem portlet;
	        if(!paginaIncial){
	        	for (final Encontro encontro : presenter.getListaEncontros()) {
	        		portlet = criaPortletItem(encontro.toString(), null, null, null);
	        		portlet.getPortlet().addClickHandler(new ClickHandler() {
	        			@Override
	        			public void onClick(ClickEvent event) {
	        				setEncontroVisual(encontro);
	        				simplePopupEncontro.hide();
	        				presenter.getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	        			}
	        		});
	        		panelVP.add(portlet.getPortlet());
				}
	        }

			simplePopupEncontro.add(panelVP);
			simplePopupEncontro.show();
			simplePopupEncontro.setPopupPosition(left - simplePopupEncontro.getOffsetWidth() + simplePopupEncontro.getOffsetWidth() - 1, top);
		}
	}

	private void executaMenu(PresenterCodeEnum p) {
		showWaitMessage(true);
		ExecutaMenuEvent e = new ExecutaMenuEvent();
		e.setJanela(p);
		WebResource.getInstanceCreated().getEventBus().fireEvent(e);
	}

	public ExtendedHorizontalPanel criaPortlet(String nome, String imageUrl){
		ExtendedHorizontalPanel portlet = new ExtendedHorizontalPanel();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setHeight("35px");
		hp.setWidth("100%");
		hp.setStyleName("portal-headerBar");
		hp.setSpacing(5);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		Label label = new Label();
		label.setStyleName("portal-headerBarText");
		label.setText(nome);
		hp.add(label);

		portlet.add(hp);
		return portlet;
	}

	public PortletItem criaPortletItem(String nome, String imageUrl, PortletItem.Position posicao, Integer imgWidth){
		PortletItem portletItem = new PortletItem();
		ExtendedHorizontalPanel portlet = new ExtendedHorizontalPanel();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setHeight("35px");
		hp.setWidth("100%");
		hp.setStyleName("portal-headerBoxItem");
		hp.setSpacing(5);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		//hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		if(imageUrl!=null && posicao!=null && posicao.equals(PortletItem.Position.LEFT)){
			HorizontalPanel img = criaPortletIcon(imageUrl, imgWidth);
			hp.add(img);
			hp.setCellWidth(img, imgWidth+"px");
		}
		if(nome!=null){
			Label label = new Label();
			label.setText(nome);
			hp.add(label);
			if(posicao!=null && posicao.equals(PortletItem.Position.RIGHT)){
				label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			}
		}
		if(imageUrl!=null && posicao!=null && posicao.equals(PortletItem.Position.RIGHT)){
			hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			HorizontalPanel img = criaPortletIcon(imageUrl, imgWidth);
			hp.add(img);
			hp.setCellWidth(img, imgWidth+"px");
		}
		portlet.add(hp);

		portletItem.setPortlet(portlet);
		portletItem.setPortletContent(hp);
		return portletItem;
	}

	private HorizontalPanel criaPortletIcon(String url, Integer width){
		HorizontalPanel hpImage = new HorizontalPanel();
		hpImage.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hpImage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Image img = new Image(url);
		img.setWidth(width+"px");
		img.setHeight("auto");
		hpImage.add(img);
		return hpImage;
	}

	@Override
	public void wait(Boolean wait) {
		if(wait){
			waitFlowPanel.setVisible(true);
		} else {
			waitFlowPanel.setVisible(false);
		}
	}

	@Override
	public void defineGrupo() {
		String grupoCookie = Cookies.getCookie("grupoSelecionado");
		boolean achou = false;
		if(grupoCookie!=null && !grupoCookie.equals("") && presenter.getListaGrupos()!=null){
			for (Grupo grupo : presenter.getListaGrupos()) {
				if(grupo.getNome().equals(grupoCookie)){
					achou = true;
					setGrupoVisual(grupo);
					break;
				}
			}
		}
		if(!achou) {
			if(presenter.getListaGrupos()!=null && presenter.getListaGrupos().size()>0 && presenter.getCasal()!=null){
				for (Grupo grupo : presenter.getListaGrupos()) {
					if(grupo.getId().equals(presenter.getCasal().getGrupo().getId())){
						achou = true;
						setGrupoVisual(grupo);
						grupoCookie = grupo.getNome();
						break;
					}
				}
				if(!achou){
					setGrupoVisual(presenter.getListaGrupos().get(0));
					grupoCookie = presenter.getListaGrupos().get(0).getNome();
					achou = true;
				}
			}
		}
		if(achou){
			grupoHorizontalPanel.setVisible(true);
			presenter.buscaEncontros(grupoCookie);
		}
	}

	private void setGrupoVisual(Grupo grupo){
		Cookies.setCookie("grupoSelecionado", grupo.toString(), new Date(System.currentTimeMillis()+(1000L*3600L*24L*30L)));
		grupoLabel.setText(grupo.getNome());
		if(grupo.getIdArquivoDigital()!=null){
			grupoImage.setUrl("eccweb/downloadArquivoDigital?id="+grupo.getIdArquivoDigital());
		}
	}

	@Override
	public void defineEncontro() {
		String encontroCookie = Cookies.getCookie("encontroSelecionado");
		boolean achou = false;
		if(encontroCookie!=null && !encontroCookie.equals("")){
			for (Encontro encontro : presenter.getListaEncontros()) {
				if(encontro.toString().equals(encontroCookie)){
					setEncontroVisual(encontro);
					achou = true;
					break;
				}
			}
		}
		if(!achou) {
			if(presenter.getListaEncontros().size()>0){
				setEncontroVisual(presenter.getListaEncontros().get(0));
				achou = true;
			}
		}
		if(achou){
			encontroHorizontalPanel.setVisible(true);
		}
	}

	private void setEncontroVisual(Encontro encontro){
		Cookies.setCookie("encontroSelecionado", encontro.toString(), new Date(System.currentTimeMillis()+(1000L*3600L*24L*30L)));
		encontroLabel.setText("Encontro: " + encontro.toString());
	}
}