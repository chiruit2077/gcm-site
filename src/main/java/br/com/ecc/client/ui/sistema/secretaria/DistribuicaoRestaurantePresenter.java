package br.com.ecc.client.ui.sistema.secretaria;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.encontro.EncontroRestauranteService;
import br.com.ecc.client.service.encontro.EncontroRestauranteServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.EncontroRestauranteVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class DistribuicaoRestaurantePresenter extends BasePresenter<DistribuicaoRestaurantePresenter.Display> {

	public interface Display extends BaseDisplay {
		void setRestauranteSelecionado(EncontroRestaurante restaurante);
		void setListaRestaurantes(List<EncontroRestaurante> lista);
		void populaEntidades(EncontroRestauranteVO vo);
	}

	public DistribuicaoRestaurantePresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroRestauranteServiceAsync service = GWT.create(EncontroRestauranteService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private EncontroRestaurante encontroRestauranteSelecionado;
	private EncontroRestauranteVO vo;

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
		serviceEncontro.lista(grupoSelecionado, new WebAsyncCallback<List<Encontro>>(getDisplay()) {
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
			}
		});
	}

	public void buscaRestaurantes(){
		getDisplay().showWaitMessage(true);
		service.lista(encontroSelecionado, new WebAsyncCallback<List<EncontroRestaurante>>(getDisplay()) {
			@Override
			protected void success(List<EncontroRestaurante> lista) {
				getDisplay().setListaRestaurantes(lista);
				if (lista.size() > 0){
					getDisplay().setRestauranteSelecionado(lista.get(0));
					setEncontroRestauranteSelecionado(lista.get(0));
				}
				buscaVO();
			}
		});
	}

	public void buscaVO(){
		if (encontroRestauranteSelecionado != null){
			getDisplay().showWaitMessage(true);
			service.getVO(encontroRestauranteSelecionado, new WebAsyncCallback<EncontroRestauranteVO>(getDisplay()) {
				@Override
				protected void success(EncontroRestauranteVO vo) {
					setVo(vo);
					getDisplay().populaEntidades(vo);
					getDisplay().showWaitMessage(false);
				}
			});
		}
		getDisplay().showWaitMessage(false);
	}
	public void salvar() {
		getDisplay().showWaitMessage(true);
		service.salvaRestaurante(getVo(), new WebAsyncCallback<EncontroRestauranteVO>(getDisplay()) {
			@Override
			public void success(EncontroRestauranteVO vo) {
				getDisplay().reset();
				setVo(vo);
				getDisplay().populaEntidades(vo);
				getDisplay().showWaitMessage(false);
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

	public EncontroRestaurante getEncontroRestauranteSelecionado() {
		return encontroRestauranteSelecionado;
	}

	public void setEncontroRestauranteSelecionado(
			EncontroRestaurante encontroRestauranteSelecionado) {
		this.encontroRestauranteSelecionado = encontroRestauranteSelecionado;
	}

	public EncontroRestauranteVO getVo() {
		return vo;
	}

	public void setVo(EncontroRestauranteVO vo) {
		this.vo = vo;
	}

}