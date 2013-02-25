package br.com.ecc.client.ui.sistema.hotelaria;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.HotelService;
import br.com.ecc.client.service.cadastro.HotelServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Hotel;

import com.google.gwt.core.client.GWT;

public class EncontroHotelPresenter extends BasePresenter<EncontroHotelPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaHoteis(List<Hotel> lista);
	}

	public EncontroHotelPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	HotelServiceAsync service = GWT.create(HotelService.class);

	@Override
	public void bind() {
	}

	public void fechar(){
		getDisplay().showWaitMessage(true);
		getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	}

	@Override
	public void init() {
		buscaHoteis();
	}

	public void buscaHoteis(){
		getDisplay().showWaitMessage(true);
		service.lista(new WebAsyncCallback<List<Hotel>>(getDisplay()) {
			@Override
			protected void success(List<Hotel> lista) {
				getDisplay().populaHoteis(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(Hotel hotelEditado) {
		getDisplay().showWaitMessage(true);
		service.salva(hotelEditado, new WebAsyncCallback<Hotel>(getDisplay()) {
			@Override
			public void success(Hotel hotel) {
				getDisplay().reset();
				buscaHoteis();
			}
		});
	}

	public void excluir(Hotel hotelEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(hotelEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaHoteis();
			}
		});
	}

}