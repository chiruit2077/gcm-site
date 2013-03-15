package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.cadastro.OrganogramaService;
import br.com.ecc.client.service.cadastro.OrganogramaServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Organograma;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class OrganogramaPresenter extends BasePresenter<OrganogramaPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaOrganogramas(List<Organograma> lista);
	}

	public OrganogramaPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	OrganogramaServiceAsync service = GWT.create(OrganogramaService.class);
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
				buscaOrganogramas();
			}
		});
	}

	public void buscaOrganogramas(){
		getDisplay().showWaitMessage(true);
		service.lista(getGrupoSelecionado(),new WebAsyncCallback<List<Organograma>>(getDisplay()) {
			@Override
			protected void success(List<Organograma> lista) {
				getDisplay().populaOrganogramas(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(Organograma organograma) {
		getDisplay().showWaitMessage(true);
		organograma.setGrupo(getGrupoSelecionado());
		service.salva(organograma, new WebAsyncCallback<Organograma>(getDisplay()) {
			@Override
			public void success(Organograma Organograma) {
				getDisplay().reset();
				buscaOrganogramas();
			}
		});
	}

	public void excluir(Organograma organograma) {
		getDisplay().showWaitMessage(true);
		service.exclui(organograma, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaOrganogramas();
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