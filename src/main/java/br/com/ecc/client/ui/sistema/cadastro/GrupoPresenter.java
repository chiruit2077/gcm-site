package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.event.UploadFinishedEvent;
import br.com.ecc.client.core.event.UploadFinishedHandler;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Grupo;

import com.google.gwt.core.client.GWT;

public class GrupoPresenter extends BasePresenter<GrupoPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<Grupo> lista);
		void defineLogotipo();
	}

	public GrupoPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	GrupoServiceAsync service = GWT.create(GrupoService.class);
	
	@Override
	public void bind() {
		getWebResource().getEventBus().addHandler(UploadFinishedEvent.TYPE, new UploadFinishedHandler() {
			@Override
			public void onUploadFinished(UploadFinishedEvent event) {
				getDisplay().defineLogotipo();
			}
		});
	}

	public void fechar(){
		getDisplay().showWaitMessage(true);
		getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	}
	
	@Override
	public void init() {
		service.lista( new WebAsyncCallback<List<Grupo>>(getDisplay()) {
			@Override
			protected void success(List<Grupo> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(Grupo grupoEditado) {
		getDisplay().showWaitMessage(true);
		service.salva(grupoEditado, new WebAsyncCallback<Grupo>(getDisplay()) {
			@Override
			public void success(Grupo resposta) {
				getDisplay().reset();
				init();
			}
		});
	}

	public void excluir(Grupo grupoEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(grupoEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				init();
			}
		});
	}
}