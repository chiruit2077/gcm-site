package br.com.ecc.client.ui.sistema.encontro;

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
import br.com.ecc.client.service.cadastro.OrganogramaService;
import br.com.ecc.client.service.cadastro.OrganogramaServiceAsync;
import br.com.ecc.client.service.encontro.EncontroOrganogramaService;
import br.com.ecc.client.service.encontro.EncontroOrganogramaServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Organograma;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class EncontroOrganogramaPresenter extends BasePresenter<EncontroOrganogramaPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEncontroOrganogramas(List<EncontroOrganograma> lista);
		void populaOrganogramas(List<Organograma> lista);
	}

	public EncontroOrganogramaPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroOrganogramaServiceAsync service = GWT.create(EncontroOrganogramaService.class);
	OrganogramaServiceAsync organogramaService = GWT.create(OrganogramaService.class);

	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private List<Organograma> listaOrganogramas = new ArrayList<Organograma>();

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
				buscaEncontroOrganogramas();
				buscaListaOrganogramas();
			}
		});
	}

	public void buscaListaOrganogramas() {
		organogramaService.lista(getGrupoSelecionado(),new WebAsyncCallback<List<Organograma>>(getDisplay()) {
			@Override
			protected void success(List<Organograma> lista) {
				setListaOrganogramas(lista);
				getDisplay().populaOrganogramas(lista);
			}
		});
	}

	public void buscaEncontroOrganogramas(){
		getDisplay().showWaitMessage(true);
		service.lista(getEncontroSelecionado(), new WebAsyncCallback<List<EncontroOrganograma>>(getDisplay()) {
			@Override
			protected void success(List<EncontroOrganograma> lista) {
				getDisplay().populaEncontroOrganogramas(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(EncontroOrganograma entidade) {
		getDisplay().showWaitMessage(true);
		service.salva(entidade, new WebAsyncCallback<EncontroOrganograma>(getDisplay()) {
			@Override
			public void success(EncontroOrganograma hotel) {
				getDisplay().reset();
				buscaEncontroOrganogramas();
			}
		});
	}

	public void excluir(EncontroOrganograma entidade) {
		getDisplay().showWaitMessage(true);
		service.exclui(entidade, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaEncontroOrganogramas();
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

	public List<Organograma> getListaOrganogramas() {
		return listaOrganogramas;
	}

	public void setListaOrganogramas(List<Organograma> listaOrganogramas) {
		this.listaOrganogramas = listaOrganogramas;
	}

}