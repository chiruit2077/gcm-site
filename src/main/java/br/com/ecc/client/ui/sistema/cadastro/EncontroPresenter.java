package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.AtividadeService;
import br.com.ecc.client.service.cadastro.AtividadeServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.EncontroVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class EncontroPresenter extends BasePresenter<EncontroPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<Encontro> lista);
		void setVO(EncontroVO encontroVO);
		void populaAtividades(List<Atividade> listaAtividade);
	}

	public EncontroPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroServiceAsync service = GWT.create(EncontroService.class);
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
				buscaEncontros(grupoSelecionado);
			}
		});
	}
	
	public void buscaEncontros(final Grupo grupo){
		getDisplay().showWaitMessage(true);
		service.lista(grupo, new WebAsyncCallback<List<Encontro>>(getDisplay()) {
			@Override
			protected void success(List<Encontro> lista) {
				getDisplay().populaEntidades(lista);
				AtividadeServiceAsync serviceAtividade = GWT.create(AtividadeService.class);
				serviceAtividade.lista(grupo, new WebAsyncCallback<List<Atividade>>(getDisplay()) {
					@Override
					protected void success(List<Atividade> listaAtividade) {
						getDisplay().populaAtividades(listaAtividade);
					}
				});
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void getVO(Encontro encontro) {
		getDisplay().showWaitMessage(true);
		service.getVO(encontro, false, new WebAsyncCallback<EncontroVO>(getDisplay()) {
			@Override
			public void success(EncontroVO encontroVO) {
				getDisplay().setVO(encontroVO);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(EncontroVO encontroVO) {
		getDisplay().showWaitMessage(true);
		service.salvaVO(encontroVO, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaEncontros(grupoSelecionado);
			}
		});
	}

	public void excluir(Encontro encontroEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(encontroEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaEncontros(grupoSelecionado);
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