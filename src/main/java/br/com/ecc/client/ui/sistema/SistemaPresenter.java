package br.com.ecc.client.ui.sistema;

import java.util.List;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.event.ExecutaMenuEventHandler;
import br.com.ecc.client.core.event.LoginEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.service.AdministracaoServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;

public class SistemaPresenter extends BasePresenter<SistemaPresenter.Display> {

	@Inject
	public SistemaPresenter(Display display, WebResource eccResource) {
		super(display, eccResource);
	}

	public interface Display extends BaseDisplay {
		void showPresenter(StateHistory state);
		void init();
	}

	private Usuario usuario;
	private Casal casal;
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;

	@Override
	public void bind() {
		getWebResource().getEventBus().addHandler(ExecutaMenuEvent.TYPE, new ExecutaMenuEventHandler() {
			@Override
			public void onExecutaMenu(ExecutaMenuEvent event) {
				if(event.getJanela()!=null){
					getDisplay().showPresenter(new StateHistory(event.getJanela().getPresenter()));
				} else  if (event.getStateHistory()!=null){
					getDisplay().showPresenter(event.getStateHistory());
				} else {
					getDisplay().showPresenter(new StateHistory(PresenterCodeEnum.SISTEMA.getPresenter()));
				}
			}
		});
	}

	@Override
	public void init() {
		final AdministracaoServiceAsync service = GWT.create(AdministracaoService.class);
		service.getDadosLogin(new WebAsyncCallback<DadosLoginVO>(getDisplay()) {
			@Override
			protected void success(DadosLoginVO dadosLoginVO) {
				setUsuario(dadosLoginVO.getUsuario());
				setCasal(dadosLoginVO.getCasal());
				if(dadosLoginVO.getParametrosRedirecionamentoVO()!=null &&
						dadosLoginVO.getParametrosRedirecionamentoVO().getPresenterCode()!=null){
					getDisplay().showPresenter(new StateHistory(dadosLoginVO.getParametrosRedirecionamentoVO().getPresenterCode().getPresenter()));
					getDisplay().init();
				} else {
					if(dadosLoginVO.getUsuario()==null){
						LoginEvent event = new LoginEvent();
						getWebResource().getEventBus().fireEvent(event);
					} else {
						Integer presenterCode = getStateHistory().getAsInteger("presenterCode");
						if(presenterCode != null) {
							for (PresenterCodeEnum presenterEnum : PresenterCodeEnum.values()) {
								if(presenterEnum.getCodigo().equals(presenterCode)){
									getDisplay().showPresenter(new StateHistory(presenterEnum.getPresenter()));
									break;
								}
							}
						} else {
							getDisplay().showPresenter(new StateHistory(PresenterCodeEnum.SISTEMA.getPresenter()));
						}
						buscaGrupo();
					}
				}
			}
		});
	}

	public void buscaGrupo() {
		GrupoServiceAsync serviceGrupo = GWT.create(GrupoService.class);
		serviceGrupo.lista(new WebAsyncCallback<List<Grupo>>(getDisplay()) {
			@Override
			protected void success(List<Grupo> listaGrupo) {
				String cookie = Cookies.getCookie("grupoSelecionado");
				for (Grupo grupo : listaGrupo) {
					if(grupo.getNome().equals(cookie)){
						setGrupoSelecionado(grupo);
						buscaEncontros();
						break;
					}
				}
				buscaEncontros();
			}
		});
	}

	public void buscaEncontros(){
		EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
		serviceEncontro.lista(grupoSelecionado, new WebAsyncCallback<List<Encontro>>(getDisplay()) {
			@Override
			protected void success(List<Encontro> listaEncontro) {
				String cookie = Cookies.getCookie("encontroSelecionado");
				for (Encontro encontro : listaEncontro) {
					if(encontro.toString().equals(cookie)){
						setEncontroSelecionado(encontro);
						getDisplay().showWaitMessage(false);
						getDisplay().init();
						break;
					}
				}
			}
		});
	}

	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public Encontro getEncontroSelecionado() {
		return encontroSelecionado;
	}

	public void setEncontroSelecionado(Encontro encontroSelecionado) {
		this.encontroSelecionado = encontroSelecionado;
	}
}