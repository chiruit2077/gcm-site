package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.UsuarioService;
import br.com.ecc.client.service.cadastro.UsuarioServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.vo.UsuarioVO;

import com.google.gwt.core.client.GWT;

public class UsuarioPresenter extends BasePresenter<UsuarioPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<Usuario> lista);

		void setVO(UsuarioVO vo);
	}

	public UsuarioPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	UsuarioServiceAsync service = GWT.create(UsuarioService.class);
	private String nomeFiltro;
	
	@Override
	public void bind() {
	}
	
	public void fechar(){
		getDisplay().showWaitMessage(true);
		getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	}
	
	@Override
	public void init() {
	}
	
	public void buscaUsuarios(String nome){
		this.nomeFiltro = nome;
		getDisplay().showWaitMessage(true);
		service.lista(nome, new WebAsyncCallback<List<Usuario>>(getDisplay()) {
			@Override
			protected void success(List<Usuario> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(UsuarioVO usuarioVO) {
		getDisplay().showWaitMessage(true);
		service.salva(usuarioVO, new WebAsyncCallback<UsuarioVO>(getDisplay()) {
			@Override
			public void success(UsuarioVO resposta) {
				getDisplay().reset();
				buscaUsuarios(nomeFiltro);
			}
		});
	}
	public void getVO(Usuario usuario){
		getDisplay().showWaitMessage(true);
		service.getVO(usuario, new WebAsyncCallback<UsuarioVO>(getDisplay()) {
			@Override
			public void success(UsuarioVO vo) {
				getDisplay().setVO(vo);
				getDisplay().showWaitMessage(true);
			}
		});
	}
}