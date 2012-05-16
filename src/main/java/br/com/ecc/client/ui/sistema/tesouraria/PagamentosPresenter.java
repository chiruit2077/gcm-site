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
import br.com.ecc.client.service.encontro.EncontroInscricaoService;
import br.com.ecc.client.service.encontro.EncontroInscricaoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoPagamento;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class PagamentosPresenter extends BasePresenter<PagamentosPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<EncontroInscricaoPagamento> lista);

		void populaCodigos(List<EncontroInscricao> result);

		void populaParcelas(List<EncontroInscricaoPagamento> listaPagamento);
	}

	public PagamentosPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroInscricaoServiceAsync service = GWT.create(EncontroInscricaoService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	
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
				listaCodigos();
			}
		});
	}
	public void buscaPagamentos(){
		service.listaPagamentos(encontroSelecionado, new WebAsyncCallback<List<EncontroInscricaoPagamento>>(getDisplay()) {
			@Override
			protected void success(List<EncontroInscricaoPagamento> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(EncontroInscricaoPagamento pagamento) {
		getDisplay().showWaitMessage(true);
		service.salvaPagamento(pagamento, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void result) {
				getDisplay().reset();
				buscaPagamentos();
			}
		});
	}
	public void listaCodigos() {
		service.lista(encontroSelecionado, new WebAsyncCallback<List<EncontroInscricao>>(getDisplay()) {
			@Override
			protected void success(List<EncontroInscricao> result) {
				getDisplay().populaCodigos(result);
				buscaPagamentos();
			}
		});
	}
	public void buscaParcelas(EncontroInscricao encontroInscricao) {
		getDisplay().showWaitMessage(true);
		service.getVO(encontroInscricao, new WebAsyncCallback<EncontroInscricaoVO>(getDisplay()) {
			@Override
			protected void success(EncontroInscricaoVO result) {
				getDisplay().populaParcelas(result.getListaPagamento());
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

}