package br.com.ecc.client.ui.sistema.hotelaria;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.cadastro.HotelService;
import br.com.ecc.client.service.cadastro.HotelServiceAsync;
import br.com.ecc.client.service.encontro.EncontroHotelService;
import br.com.ecc.client.service.encontro.EncontroHotelServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Hotel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class EncontroHotelPresenter extends BasePresenter<EncontroHotelPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEncontroHoteis(List<EncontroHotel> lista);
		void populaHoteis(List<Hotel> lista);
	}

	public EncontroHotelPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroHotelServiceAsync service = GWT.create(EncontroHotelService.class);
	HotelServiceAsync hotelservice = GWT.create(HotelService.class);

	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private List<Hotel> listaHoteis = new ArrayList<Hotel>();

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
				buscaEncontros();
			}
		});
	}

	public void buscaEncontros() {
		EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
		serviceEncontro.lista(getGrupoSelecionado(), new WebAsyncCallback<List<Encontro>>(getDisplay()) {
			@Override
			protected void success(List<Encontro> listaEncontro) {
				String cookie = Cookies.getCookie("encontroSelecionado");
				for (Encontro encontro : listaEncontro) {
					if(encontro.toString().equals(cookie)){
						setEncontroSelecionado(encontro);
						break;
					}
				}
				buscaHoteis();
				buscaListaHoteis();
			}
		});
	}

	public void buscaListaHoteis() {
		hotelservice.lista(new WebAsyncCallback<List<Hotel>>(getDisplay()) {
			@Override
			protected void success(List<Hotel> lista) {
				setListaHoteis(lista);
				getDisplay().populaHoteis(lista);
			}
		});
	}

	public void buscaHoteis(){
		getDisplay().showWaitMessage(true);
		service.lista(getEncontroSelecionado(), new WebAsyncCallback<List<EncontroHotel>>(getDisplay()) {
			@Override
			protected void success(List<EncontroHotel> lista) {
				getDisplay().populaEncontroHoteis(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(EncontroHotel hotelEditado) {
		getDisplay().showWaitMessage(true);
		service.salva(hotelEditado, new WebAsyncCallback<EncontroHotel>(getDisplay()) {
			@Override
			public void success(EncontroHotel hotel) {
				getDisplay().reset();
				buscaHoteis();
			}
		});
	}

	public void excluir(EncontroHotel hotelEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(hotelEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaHoteis();
			}
		});
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

	public List<Hotel> getListaHoteis() {
		return listaHoteis;
	}

	public void setListaHoteis(List<Hotel> listaHoteis) {
		this.listaHoteis = listaHoteis;
	}

}