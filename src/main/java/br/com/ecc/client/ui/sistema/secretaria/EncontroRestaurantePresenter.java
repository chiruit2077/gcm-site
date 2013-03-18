package br.com.ecc.client.ui.sistema.secretaria;

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
import br.com.ecc.client.service.cadastro.RestauranteService;
import br.com.ecc.client.service.cadastro.RestauranteServiceAsync;
import br.com.ecc.client.service.encontro.EncontroRestauranteService;
import br.com.ecc.client.service.encontro.EncontroRestauranteServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Restaurante;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class EncontroRestaurantePresenter extends BasePresenter<EncontroRestaurantePresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEncontroRestaurantes(List<EncontroRestaurante> lista);
		void populaRestaurantes(List<Restaurante> lista);
	}

	public EncontroRestaurantePresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroRestauranteServiceAsync service = GWT.create(EncontroRestauranteService.class);
	RestauranteServiceAsync restauranteservice = GWT.create(RestauranteService.class);

	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private List<Restaurante> listaRestaurantes = new ArrayList<Restaurante>();

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
				buscaRestaurantes();
				buscaListaRestaurantes();
			}
		});
	}

	public void buscaListaRestaurantes() {
		restauranteservice.lista(getGrupoSelecionado(),new WebAsyncCallback<List<Restaurante>>(getDisplay()) {
			@Override
			protected void success(List<Restaurante> lista) {
				setListaRestaurantes(lista);
				getDisplay().populaRestaurantes(lista);
			}
		});
	}

	public void buscaRestaurantes(){
		getDisplay().showWaitMessage(true);
		service.lista(getEncontroSelecionado(), new WebAsyncCallback<List<EncontroRestaurante>>(getDisplay()) {
			@Override
			protected void success(List<EncontroRestaurante> lista) {
				getDisplay().populaEncontroRestaurantes(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(EncontroRestaurante restaurante) {
		getDisplay().showWaitMessage(true);
		service.salva(restaurante, new WebAsyncCallback<EncontroRestaurante>(getDisplay()) {
			@Override
			public void success(EncontroRestaurante restaurante) {
				getDisplay().reset();
				buscaRestaurantes();
			}
		});
	}

	public void excluir(EncontroRestaurante restaurante) {
		getDisplay().showWaitMessage(true);
		service.exclui(restaurante, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaRestaurantes();
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

	public List<Restaurante> getListaRestaurantes() {
		return listaRestaurantes;
	}

	public void setListaRestaurantes(List<Restaurante> listaRestaurantes) {
		this.listaRestaurantes = listaRestaurantes;
	}

}