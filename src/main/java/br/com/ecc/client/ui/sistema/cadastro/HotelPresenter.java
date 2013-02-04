package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.cadastro.HotelService;
import br.com.ecc.client.service.cadastro.HotelServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Hotel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class HotelPresenter extends BasePresenter<HotelPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaHoteis(List<Hotel> lista);
	}

	public HotelPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	HotelServiceAsync service = GWT.create(HotelService.class);
	private Grupo grupoSelecionado;

	@Override
	public void bind() {
	}

	public void fechar(){
		getDisplay().showWaitMessage(true);
		getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	}

	@Override
	public void init() {
		GrupoServiceAsync serviceGrupo = GWT.create(GrupoService.class);
		serviceGrupo.lista(new WebAsyncCallback<List<Grupo>>(getDisplay()) {
			@Override
			protected void success(List<Grupo> listaGrupo) {
				String cookie = Cookies.getCookie("grupoSelecionado");
				for (Grupo grupo : listaGrupo) {
					if(grupo.getNome().equals(cookie)){
						setGrupoSelecionado(grupo);
						break;
					}
				}
				buscaHoteis(grupoSelecionado);
			}
		});
	}

	public void buscaHoteis(Grupo grupo){
		getDisplay().showWaitMessage(true);
		service.lista(grupo, new WebAsyncCallback<List<Hotel>>(getDisplay()) {
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
				buscaHoteis(grupoSelecionado);
			}
		});
	}

	public void excluir(Hotel hotelEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(hotelEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaHoteis(grupoSelecionado);
			}
		});
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}
}