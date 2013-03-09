package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.RestauranteService;
import br.com.ecc.client.service.cadastro.RestauranteServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Restaurante;

import com.google.gwt.core.client.GWT;

public class RestaurantePresenter extends BasePresenter<RestaurantePresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaRestaurantes(List<Restaurante> lista);
	}

	public RestaurantePresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	RestauranteServiceAsync service = GWT.create(RestauranteService.class);

	@Override
	public void bind() {
	}

	public void fechar(){
		getDisplay().showWaitMessage(true);
		getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	}

	@Override
	public void init() {
		buscaRestaurantes();
	}

	public void buscaRestaurantes(){
		getDisplay().showWaitMessage(true);
		service.lista(new WebAsyncCallback<List<Restaurante>>(getDisplay()) {
			@Override
			protected void success(List<Restaurante> lista) {
				getDisplay().populaRestaurantes(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(Restaurante restaurante) {
		getDisplay().showWaitMessage(true);
		service.salva(restaurante, new WebAsyncCallback<Restaurante>(getDisplay()) {
			@Override
			public void success(Restaurante restaurante) {
				getDisplay().reset();
				buscaRestaurantes();
			}
		});
	}

	public void excluir(Restaurante restaurante) {
		getDisplay().showWaitMessage(true);
		service.exclui(restaurante, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaRestaurantes();
			}
		});
	}

}