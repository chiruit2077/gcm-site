package br.com.ecc.client.ui.main;

import java.util.Date;
import java.util.List;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.core.event.LoggedEvent;
import br.com.ecc.client.core.event.LoggedEventHandler;
import br.com.ecc.client.core.event.LoginEvent;
import br.com.ecc.client.core.event.LoginEventHandler;
import br.com.ecc.client.core.event.LogoutEvent;
import br.com.ecc.client.core.event.LogoutEventHandler;
import br.com.ecc.client.core.event.WaitEvent;
import br.com.ecc.client.core.event.WaitEventHandler;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.service.AdministracaoServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.ui.home.HomePresenter;
import br.com.ecc.client.ui.sistema.SistemaPresenter;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.core.mvp.presenter.Presenter;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class MainPresenter extends BasePresenter<MainPresenter.Display>{

	@Inject
	public MainPresenter(Display display, WebResource ECCResource) {
		super(display, ECCResource);
	}

	@SuppressWarnings("rawtypes")
	public interface Display extends BaseDisplay {
		void showState(Presenter presenter, String title);
		void conectado(Usuario usuarioLS, Boolean paginaInicial);
		void login(Integer presenterCode);
		void senhaEnviada(Boolean result);
		void resetToolbar(boolean resetUsuario);
		void wait(Boolean wait);
		void defineGrupo();
		void defineEncontro();
	}

	AdministracaoServiceAsync service = GWT.create(AdministracaoService.class);
	private Casal casal;
	private List<Grupo> listaGrupos;
	private List<Encontro> listaEncontros;

	@Override
	public void bind() {
		getWebResource().getEventBus().addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
			@Override
			public void onLogout(LogoutEvent logoutEvent) {
				getDisplay().showWaitMessage(true);
				logout();
			}
		});
		getWebResource().getEventBus().addHandler(LoginEvent.TYPE, new LoginEventHandler() {
			@Override
			public void onProcessa(final LoginEvent event) {
				setCasal(null);
				service.getDadosLogin(new WebAsyncCallback<DadosLoginVO>(getDisplay()) {
					@Override
					protected void success(DadosLoginVO dadosLoginVO) {
						if(dadosLoginVO.getUsuario()==null){
							getDisplay().login(event.getPresenterCode());
						} else {
							getDisplay().showWaitMessage(true);
							setCasal(dadosLoginVO.getCasal());
							conectado(event.getPresenterCode(), dadosLoginVO.getUsuario());
						}
					}

				});
			}
		});
		getWebResource().getEventBus().addHandler(LoggedEvent.TYPE, new LoggedEventHandler() {
			@Override
			public void onLogged(LoggedEvent event) {
				if(event.getPaginaInicial()==null || !event.getPaginaInicial()){
					getDisplay().conectado(event.getUsuario(), event.getPaginaInicial());
				}
			}
		});
		getWebResource().getEventBus().addHandler(WaitEvent.TYPE, new WaitEventHandler() {
			@Override
			public void onWait(WaitEvent event) {
				getDisplay().wait(event.getWait());
			}
		});
	}

	@Override
	public void init() {
//		Cookies.removeCookie("grupoSelecionado");
//		Cookies.removeCookie("encontroSelecionado");
		setCasal(null);
		service.getDadosLogin(new WebAsyncCallback<DadosLoginVO>(getDisplay()) {
			@Override
			protected void success(final DadosLoginVO dadosLoginVO) {
				if(dadosLoginVO.getParametrosRedirecionamentoVO()!=null){
					getWebResource().getPlaceManager().newPlaceClean(SistemaPresenter.class);
				} else {
					verificaHistory();
				}
				if(dadosLoginVO.getUsuario()!=null){
					GrupoServiceAsync serviceGrupo = GWT.create(GrupoService.class);
					serviceGrupo.lista(new WebAsyncCallback<List<Grupo>>(getDisplay()) {
						@Override
						protected void success(List<Grupo> listaGrupos) {
							setListaGrupos(listaGrupos);
							getDisplay().conectado(dadosLoginVO.getUsuario(), false);
							setCasal(dadosLoginVO.getCasal());
							getDisplay().showWaitMessage(false);
						}
					});
				} else {
					getDisplay().showWaitMessage(false);
				}
			}
		});
	}

	private void verificaHistory() {
		if(History.getToken() != null && !"".equals(History.getToken())) {
			getWebResource().getPlaceManager().currentPlace();
		} else {
			getWebResource().getPlaceManager().newPlaceClean(HomePresenter.class);
		}
	}

	public void logout(){
		service.logout(new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			protected void success(Void result) {
				Cookies.removeCookie("ecc_conectado");
				Cookies.removeCookie("ecc_userId");

				String url = Window.Location.getHref();
				url = url.substring(0,url.indexOf("#"));
				reload(url);
				//getWebResource().getPlaceManager().newPlaceClean(HomePresenter.class);
				getDisplay().resetToolbar(true);
			}
		});
	}
	native void reload(String url)/*-{
	    $wnd.location.replace(url);
	}-*/;

	public void conectar(final String email, final String senha, final Integer presenterCode) {
		service.login(email, senha, new WebAsyncCallback<DadosLoginVO>(getDisplay()) {
			@Override
			protected void success(DadosLoginVO dadosLoginVO) {
				if(dadosLoginVO.getUsuario()!=null){
					setCasal(dadosLoginVO.getCasal());
					Cookies.setCookie("ecc_email", email, new Date(System.currentTimeMillis()+(1000L*3600L*24L*30L)));
					if (!GWT.isProdMode())
						Cookies.setCookie("ecc_pass", senha, new Date(System.currentTimeMillis()+(1000L*3600L*24L*30L)));
					conectado(presenterCode, dadosLoginVO.getUsuario());
				}
			}

		});
	}
	private void conectado(Integer presenterCode, Usuario usuario) {
		GrupoServiceAsync serviceGrupo = GWT.create(GrupoService.class);
		serviceGrupo.lista(new WebAsyncCallback<List<Grupo>>(getDisplay()) {
			@Override
			protected void success(List<Grupo> listaGrupos) {
				setListaGrupos(listaGrupos);
				getDisplay().defineGrupo();
			}
		});
		if(presenterCode!=null){
			PresenterCodeEnum p = PresenterCodeEnum.getPresenterPorCodigo(presenterCode);
			if(p!=null){
				getWebResource().getPlaceManager().newPlaceClean(p.getPresenter());
			} else {
				getWebResource().getPlaceManager().currentPlace();
			}
		} else {
			boolean achou = false;
			for (StateHistory state : getWebResource().getPlaceManager().getStates()) {
				if(state.getPresenterName().indexOf("Sistema")>=0){
					achou = true;
					break;
				}
			}
			if(History.getToken() != null && !"".equals(History.getToken()) && achou) {
				getWebResource().getPlaceManager().currentPlace();
			} else {
				getWebResource().getPlaceManager().newPlaceClean(SistemaPresenter.class);
			}
		}
		LoggedEvent event = new LoggedEvent();
		event.setUsuario(usuario);
		getWebResource().getEventBus().fireEvent(event);
	}
	public void buscaEncontros(String grupoCookie) {
		for (Grupo grupo : listaGrupos) {
			if(grupo.toString().equals(grupoCookie)){
				EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
				serviceEncontro.lista(grupo, new WebAsyncCallback<List<Encontro>>(getDisplay()) {
					@Override
					protected void success(List<Encontro> listaEncontros) {
						setListaEncontros(listaEncontros);
						getDisplay().defineEncontro();
						getDisplay().showWaitMessage(false);
					}
				});
				break;
			}
		}
	}

	public void reenviarSenha(String email) {
		service.reenviarSenha(email, new WebAsyncCallback<Boolean>(getDisplay()) {
			@Override
			protected void success(Boolean result) {
				getDisplay().senhaEnviada(result);
			}
		});
	}

	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public List<Grupo> getListaGrupos() {
		return listaGrupos;
	}
	public void setListaGrupos(List<Grupo> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}
	public List<Encontro> getListaEncontros() {
		return listaEncontros;
	}
	public void setListaEncontros(List<Encontro> listaEncontros) {
		this.listaEncontros = listaEncontros;
	}
}