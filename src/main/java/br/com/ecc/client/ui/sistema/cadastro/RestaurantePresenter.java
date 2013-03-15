package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.AtividadeService;
import br.com.ecc.client.service.cadastro.AtividadeServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.cadastro.HotelService;
import br.com.ecc.client.service.cadastro.HotelServiceAsync;
import br.com.ecc.client.service.cadastro.RestauranteService;
import br.com.ecc.client.service.cadastro.RestauranteServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Hotel;
import br.com.ecc.model.Restaurante;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class RestaurantePresenter extends BasePresenter<RestaurantePresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaRestaurantes(List<Restaurante> lista);
		void populaAtividades(List<Atividade> lista);
		void populaHoteis(List<Hotel> lista);
	}

	public RestaurantePresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	RestauranteServiceAsync service = GWT.create(RestauranteService.class);
	AtividadeServiceAsync servicoAtividade = GWT.create(AtividadeService.class);
	HotelServiceAsync servicoHotel = GWT.create(HotelService.class);
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
				buscaRestaurantes();
				buscaAtividades();
				buscaHoteis();
			}
		});
	}

	public void buscaRestaurantes(){
		getDisplay().showWaitMessage(true);
		service.lista(getGrupoSelecionado(),new WebAsyncCallback<List<Restaurante>>(getDisplay()) {
			@Override
			protected void success(List<Restaurante> lista) {
				getDisplay().populaRestaurantes(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void buscaAtividades(){
		getDisplay().showWaitMessage(true);
		servicoAtividade.lista(getGrupoSelecionado(), new WebAsyncCallback<List<Atividade>>(getDisplay()) {
			@Override
			protected void success(List<Atividade> lista) {
				getDisplay().populaAtividades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void buscaHoteis(){
		getDisplay().showWaitMessage(true);
		servicoHotel.lista(getGrupoSelecionado(),new WebAsyncCallback<List<Hotel>>(getDisplay()) {
			@Override
			protected void success(List<Hotel> lista) {
				getDisplay().populaHoteis(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void salvar(Restaurante restaurante) {
		getDisplay().showWaitMessage(true);
		restaurante.setGrupo(getGrupoSelecionado());
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

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}
	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

}