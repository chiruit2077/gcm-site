package br.com.ecc.client.ui.sistema.patrimonio;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.AtividadeService;
import br.com.ecc.client.service.cadastro.AtividadeServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.patrimonio.ItemPatrimonioService;
import br.com.ecc.client.service.patrimonio.ItemPatrimonioServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.ItemPatrimonio;
import br.com.ecc.model.vo.ItemPatrimonioVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class ItemPatrimonioPresenter extends BasePresenter<ItemPatrimonioPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<ItemPatrimonio> lista);
		void setVO(ItemPatrimonioVO itemPatrimonioVO);
		void populaAtividades(List<Atividade> listaAtividade);
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
	
	public void buscaAtividade(final Grupo grupo){
		getDisplay().showWaitMessage(true);
		AtividadeServiceAsync serviceAtividade = GWT.create(AtividadeService.class);
		serviceAtividade.lista(grupo, new WebAsyncCallback<List<Atividade>>(getDisplay()) {
			@Override
			protected void success(List<Atividade> listaAtividade) {
				getDisplay().populaAtividades(listaAtividade);
			}
		});
		getDisplay().showWaitMessage(false);
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
				buscaAtividade(grupoSelecionado);
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
	public void getItemPatrimonioVO(ItemPatrimonio itemPatrimonio){
		service.getVO(itemPatrimonio, new WebAsyncCallback<ItemPatrimonioVO>(getDisplay()) {
			@Override
			protected void success(ItemPatrimonioVO lista) {
				getDisplay().setVO(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	
	public void salvar(ItemPatrimonioVO itemPatrimonioEditado) {
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