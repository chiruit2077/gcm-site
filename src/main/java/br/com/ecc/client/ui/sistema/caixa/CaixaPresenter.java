package br.com.ecc.client.ui.sistema.caixa;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.CaixaService;
import br.com.ecc.client.service.cadastro.CaixaServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.patrimonio.ItemPatrimonioService;
import br.com.ecc.client.service.patrimonio.ItemPatrimonioServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Caixa;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.ItemPatrimonio;
import br.com.ecc.model.vo.CaixaVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class CaixaPresenter extends BasePresenter<CaixaPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(List<Caixa> lista);
		void populaEntidadesTree(List<ItemPatrimonio> listaItemPatrimonio);
		void setVO(CaixaVO vo);
	}

	public CaixaPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	CaixaServiceAsync service = GWT.create(CaixaService.class);
	EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
	private CaixaVO caixaVO;
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
				buscaItens();
				lista();
			}
		});
	}
	
	public void buscaItens(){
		ItemPatrimonioServiceAsync serviceItemPatrimonio = GWT.create(ItemPatrimonioService.class);
		serviceItemPatrimonio.listaPorGrupo(grupoSelecionado, new WebAsyncCallback<List<ItemPatrimonio>>(getDisplay()) {
			@Override
			protected void success(List<ItemPatrimonio> lista) {
				getDisplay().populaEntidadesTree(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	
	public void salvar() {
		getDisplay().showWaitMessage(true);
		service.salva(caixaVO, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				lista();
			}
		});
	}
	
	public void buscaEncontros(){
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
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void excluir(Caixa caixaEditada) {
		getDisplay().showWaitMessage(true);
		service.exclui(caixaEditada, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				lista();
			}
		});
	}

	public void lista() {
		getDisplay().showWaitMessage(true);
		service.lista(grupoSelecionado, new WebAsyncCallback<List<Caixa>>(getDisplay()) {
			@Override
			public void success(List<Caixa> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	
	public void getVO(Caixa caixaEditada) {
		getDisplay().showWaitMessage(true);
		service.getVO(caixaEditada, new WebAsyncCallback<CaixaVO>(getDisplay()) {
			@Override
			public void success(CaixaVO vo) {
				setCaixaVO(vo);
				getDisplay().setVO(vo);
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
	public CaixaVO getCaixaVO() {
		return caixaVO;
	}
	public void setCaixaVO(CaixaVO caixaVO) {
		this.caixaVO = caixaVO;
	}
}