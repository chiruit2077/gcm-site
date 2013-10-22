package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.cadastro.RestauranteService;
import br.com.ecc.client.service.cadastro.RestauranteServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Restaurante;
import br.com.ecc.model.vo.RestauranteVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class RestauranteLayoutPresenter extends BasePresenter<RestauranteLayoutPresenter.Display> {

	public interface Display extends BaseDisplay {
		void setRestauranteSelecionado(Restaurante restaurante);
		void setListaRestaurantes(List<Restaurante> lista);
		void populaEntidades(RestauranteVO vo);
	}

	public RestauranteLayoutPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	RestauranteServiceAsync service = GWT.create(RestauranteService.class);
	private Restaurante restauranteSelecionado;
	private RestauranteVO vo;
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
			}
		});

	}

	public void buscaRestaurantes(){
		getDisplay().showWaitMessage(true);
		service.lista(getGrupoSelecionado(), new WebAsyncCallback<List<Restaurante>>(getDisplay()) {
			@Override
			protected void success(List<Restaurante> lista) {
				getDisplay().setListaRestaurantes(lista);
				if (lista.size() > 0){
					setRestauranteSelecionado(lista.get(0));
				}
				buscaVO();
			}
		});
	}

	public void buscaVO(){
		if (restauranteSelecionado != null){
			getDisplay().showWaitMessage(true);
			service.getVO(restauranteSelecionado, new WebAsyncCallback<RestauranteVO>(getDisplay()) {
				@Override
				protected void success(RestauranteVO vo) {
					setVo(vo);
					restauranteSelecionado = vo.getRestaurante();
					getDisplay().populaEntidades(vo);
					getDisplay().showWaitMessage(false);
				}
			});
		}
		getDisplay().showWaitMessage(false);
	}
	public void salvar() {
		getDisplay().showWaitMessage(true);
		service.salvaRestaurante(getVo(), new WebAsyncCallback<RestauranteVO>(getDisplay()) {
			@Override
			public void success(RestauranteVO vo) {
				getDisplay().reset();
				setVo(vo);
				getDisplay().populaEntidades(vo);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public Restaurante getRestauranteSelecionado() {
		return restauranteSelecionado;
	}

	public void setRestauranteSelecionado(Restaurante restauranteSelecionado) {
		this.restauranteSelecionado = restauranteSelecionado;
		getDisplay().setRestauranteSelecionado(restauranteSelecionado);
	}

	public RestauranteVO getVo() {
		return vo;
	}

	public void setVo(RestauranteVO vo) {
		this.vo = vo;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

}