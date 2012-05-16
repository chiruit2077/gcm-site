package br.com.ecc.client.ui.sistema;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.event.ExecutaMenuEventHandler;
import br.com.ecc.client.core.event.LoginEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.service.AdministracaoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.core.mvp.history.StateHistory;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.core.client.GWT;
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
						getDisplay().init();
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
}