package br.com.ecc.client.ui.sistema.tesouraria;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.encontro.EncontroInscricaoFichaPagamentoService;
import br.com.ecc.client.service.encontro.EncontroInscricaoFichaPagamentoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.model.Grupo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class FichasPresenter extends BasePresenter<FichasPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(List<EncontroInscricaoFichaPagamento> lista);
	}

	public FichasPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroInscricaoFichaPagamentoServiceAsync service = GWT.create(EncontroInscricaoFichaPagamentoService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private List<EncontroInscricaoFichaPagamento> listaFichas;

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
				buscaFichas();
			}
		});
	}

	public void buscaFichas(){
		getDisplay().showWaitMessage(true);
		service.listaFichas(encontroSelecionado, new WebAsyncCallback<List<EncontroInscricaoFichaPagamento>>(getDisplay()) {
			@Override
			protected void success(List<EncontroInscricaoFichaPagamento> lista) {
				setListaFichas(lista);
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(EncontroInscricaoFichaPagamento ficha) {
		getDisplay().showWaitMessage(true);
		service.salvaFicha(ficha, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void result) {
				getDisplay().reset();
				buscaFichas();
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

	public void geraFichasVagas() {
		getDisplay().showWaitMessage(true);
		service.geraFichasVagas(encontroSelecionado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			protected void success(Void result) {
				buscaFichas();
			}
		});
	}

	public List<EncontroInscricaoFichaPagamento> getListaFichas() {
		return listaFichas;
	}

	public void setListaFichas(List<EncontroInscricaoFichaPagamento> listaFichas) {
		this.listaFichas = listaFichas;
	}

}