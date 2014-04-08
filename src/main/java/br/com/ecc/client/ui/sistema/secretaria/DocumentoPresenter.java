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
import br.com.ecc.client.service.secretaria.DocumentoService;
import br.com.ecc.client.service.secretaria.DocumentoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Documento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class DocumentoPresenter extends BasePresenter<DocumentoPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(List<Documento> lista);
		void setDocumento(Documento documento);
		Integer getTipoFiltro();
		String getTextoFiltro();
	}

	public DocumentoPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	DocumentoServiceAsync service = GWT.create(DocumentoService.class);
	private Documento documentoEditado;
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
				listar();
			}
		});
	}
	
	public void listar(){
		if(getDisplay().getTipoFiltro()==0){
			service.lista(grupoSelecionado, null, getDisplay().getTextoFiltro(), new WebAsyncCallback<List<Documento>>(getDisplay()) {
				@Override
				protected void success(List<Documento> lista) {
					getDisplay().populaEntidades(lista);
					getDisplay().showWaitMessage(false);
				}
			});
		} else {
			service.lista(grupoSelecionado, encontroSelecionado, getDisplay().getTextoFiltro(), new WebAsyncCallback<List<Documento>>(getDisplay()) {
				@Override
				protected void success(List<Documento> lista) {
					getDisplay().populaEntidades(lista);
					getDisplay().showWaitMessage(false);
				}
			});
		}
	}
	
	public void getDocumento(Integer id){
		service.getDocumento(id, new WebAsyncCallback<Documento>(getDisplay()) {
			@Override
			protected void success(Documento doc) {
				getDisplay().setDocumento(doc);
			}
		});
	}
	
	public void salvar() {
		getDisplay().showWaitMessage(true);
		service.salva(getDocumentoEditado(), new WebAsyncCallback<Documento>(getDisplay()) {
			@Override
			public void success(Documento doc) {
				setDocumentoEditado(doc);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void excluir(Documento mensagemEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(mensagemEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				setDocumentoEditado(null);
				getDisplay().reset();
				listar();
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
	public Documento getDocumentoEditado() {
		return documentoEditado;
	}
	public void setDocumentoEditado(Documento documentoEditado) {
		this.documentoEditado = documentoEditado;
	}
}