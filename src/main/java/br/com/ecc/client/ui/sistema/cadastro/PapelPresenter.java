package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.PapelService;
import br.com.ecc.client.service.cadastro.PapelServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Papel;
import br.com.ecc.model.Grupo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class PapelPresenter extends BasePresenter<PapelPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<Papel> lista);
	}

	public PapelPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	PapelServiceAsync service = GWT.create(PapelService.class);
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
				buscaPapeis(grupoSelecionado);
			}
		});
	}
	
	public void buscaPapeis(Grupo grupo){
		getDisplay().showWaitMessage(true);
		service.lista(grupo, new WebAsyncCallback<List<Papel>>(getDisplay()) {
			@Override
			protected void success(List<Papel> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(Papel papelEditado) {
		getDisplay().showWaitMessage(true);
		service.salva(papelEditado, new WebAsyncCallback<Papel>(getDisplay()) {
			@Override
			public void success(Papel resposta) {
				getDisplay().reset();
				buscaPapeis(grupoSelecionado);
			}
		});
	}

	public void excluir(Papel papelEditado) {
		getDisplay().showWaitMessage(true);		
		service.exclui(papelEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaPapeis(grupoSelecionado);
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