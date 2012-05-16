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
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.Grupo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class AtividadePresenter extends BasePresenter<AtividadePresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<Atividade> lista);
	}

	public AtividadePresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	AtividadeServiceAsync service = GWT.create(AtividadeService.class);
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
				buscaAtividades(grupoSelecionado);
			}
		});
	}
	
	public void buscaAtividades(Grupo grupo){
		getDisplay().showWaitMessage(true);
		service.lista(grupo, new WebAsyncCallback<List<Atividade>>(getDisplay()) {
			@Override
			protected void success(List<Atividade> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(Atividade atividadeEditado) {
		getDisplay().showWaitMessage(true);
		service.salva(atividadeEditado, new WebAsyncCallback<Atividade>(getDisplay()) {
			@Override
			public void success(Atividade resposta) {
				getDisplay().reset();
				buscaAtividades(grupoSelecionado);
			}
		});
	}

	public void excluir(Atividade atividadeEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(atividadeEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaAtividades(grupoSelecionado);
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