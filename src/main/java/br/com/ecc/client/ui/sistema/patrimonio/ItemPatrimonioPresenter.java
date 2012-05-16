package br.com.ecc.client.ui.sistema.patrimonio;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.patrimonio.ItemPatrimonioService;
import br.com.ecc.client.service.patrimonio.ItemPatrimonioServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.ItemPatrimonio;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class ItemPatrimonioPresenter extends BasePresenter<ItemPatrimonioPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<ItemPatrimonio> lista);
	}

	public ItemPatrimonioPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	ItemPatrimonioServiceAsync service = GWT.create(ItemPatrimonioService.class);
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
				buscaItens();
			}
		});
		
	}
	public void buscaItens(){
		service.listaPorGrupo(grupoSelecionado, new WebAsyncCallback<List<ItemPatrimonio>>(getDisplay()) {
			@Override
			protected void success(List<ItemPatrimonio> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	
	public void salvar(ItemPatrimonio itemPatrimonioEditado) {
		getDisplay().showWaitMessage(true);
		service.salva(itemPatrimonioEditado, new WebAsyncCallback<ItemPatrimonio>(getDisplay()) {
			@Override
			public void success(ItemPatrimonio resposta) {
				getDisplay().reset();
				init();
			}
		});
	}

	public void excluir(ItemPatrimonio itemPatrimonioEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(itemPatrimonioEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				init();
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