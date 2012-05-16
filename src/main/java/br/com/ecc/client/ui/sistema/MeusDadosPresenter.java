package br.com.ecc.client.ui.sistema;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.event.LoggedEvent;
import br.com.ecc.client.core.event.UploadFinishedEvent;
import br.com.ecc.client.core.event.UploadFinishedHandler;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.service.AdministracaoServiceAsync;
import br.com.ecc.client.service.cadastro.CasalService;
import br.com.ecc.client.service.cadastro.CasalServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class MeusDadosPresenter extends BasePresenter<MeusDadosPresenter.Display> {

	@Inject
	public MeusDadosPresenter(Display display, WebResource sonnerResource) {
		super(display, sonnerResource);
	}

	public interface Display extends BaseDisplay {
		void setUsuario(Usuario usuario);
		void defineFoto();
	}
	
	AdministracaoServiceAsync service = GWT.create(AdministracaoService.class);
	private Casal casal;

	@Override
	public void bind() {
		getWebResource().getEventBus().addHandler(UploadFinishedEvent.TYPE, new UploadFinishedHandler() {
			@Override
			public void onUploadFinished(UploadFinishedEvent event) {
				getDisplay().defineFoto();
			}
		});
	}

	@Override
	public void init() {
		service.getDadosLogin(new WebAsyncCallback<DadosLoginVO>(getDisplay()) {
			@Override
			protected void success(DadosLoginVO dadosLoginVO) {
				setCasal(dadosLoginVO.getCasal());
				getDisplay().setUsuario(dadosLoginVO.getUsuario());
				getDisplay().showWaitMessage(false);
			}
		});
	}
	
	public void fechar(){
		getDisplay().showWaitMessage(true);
		getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	}

	public void salvar(Usuario usuario, final boolean senhaAlterada) {
		getDisplay().showWaitMessage(true);
		if(senhaAlterada){
			service.salvaUsuarioLogado(usuario, senhaAlterada, new WebAsyncCallback<Usuario>(getDisplay()) {
				@Override
				protected void success(final Usuario usuario) {
					LoggedEvent event = new LoggedEvent();
					event.setUsuario(usuario);
					getWebResource().getEventBus().fireEvent(event);
					getDisplay().showWaitMessage(false);
					Window.alert("Dados alterados");
					init();
				}
			});
		} else {
			CasalServiceAsync serviceCasal = GWT.create(CasalService.class);
			serviceCasal.salva(casal, new WebAsyncCallback<Casal>(getDisplay()) {
				@Override
				protected void success(Casal result) {
					fechar();
				}
			});
		}
	}

	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}

}