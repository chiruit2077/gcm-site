package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.cadastro.OrganogramaService;
import br.com.ecc.client.service.cadastro.OrganogramaServiceAsync;
import br.com.ecc.client.service.cadastro.PapelService;
import br.com.ecc.client.service.cadastro.PapelServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Organograma;
import br.com.ecc.model.Papel;
import br.com.ecc.model.vo.OrganogramaVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class OrganogramaAreaLayoutPresenter extends BasePresenter<OrganogramaAreaLayoutPresenter.Display> {

	public interface Display extends BaseDisplay {
		void setOrganogramaSelecionado(Organograma Organograma);
		void setListaOrganogramas(List<Organograma> lista);
		void populaPapeis(List<Papel> lista);
		void populaEntidades(OrganogramaVO vo);
	}

	public OrganogramaAreaLayoutPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	OrganogramaServiceAsync service = GWT.create(OrganogramaService.class);
	PapelServiceAsync servicoPapel = GWT.create(PapelService.class);
	private Organograma organogramaSelecionado;
	private OrganogramaVO vo;
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
				buscaOrganogramas();
				buscaAtividades();
			}
		});

	}

	public void buscaAtividades(){
		getDisplay().showWaitMessage(true);
		servicoPapel.lista(getGrupoSelecionado(), new WebAsyncCallback<List<Papel>>(getDisplay()) {
			@Override
			protected void success(List<Papel> lista) {
				getDisplay().populaPapeis(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void buscaOrganogramas(){
		getDisplay().showWaitMessage(true);
		service.lista(getGrupoSelecionado(), new WebAsyncCallback<List<Organograma>>(getDisplay()) {
			@Override
			protected void success(List<Organograma> lista) {
				getDisplay().setListaOrganogramas(lista);
				if (lista.size() > 0){
					getDisplay().setOrganogramaSelecionado(lista.get(0));
					setOrganogramaSelecionado(lista.get(0));
				}
				buscaVO();
			}
		});
	}

	public void buscaVO(){
		if (organogramaSelecionado != null){
			getDisplay().showWaitMessage(true);
			service.getVO(organogramaSelecionado, new WebAsyncCallback<OrganogramaVO>(getDisplay()) {
				@Override
				protected void success(OrganogramaVO vo) {
					setVo(vo);
					getDisplay().populaEntidades(vo);
					getDisplay().showWaitMessage(false);
				}
			});
		}
		getDisplay().showWaitMessage(false);
	}
	public void salvar() {
		getDisplay().showWaitMessage(true);
		service.salvaOrganograma(getVo(), new WebAsyncCallback<OrganogramaVO>(getDisplay()) {
			@Override
			public void success(OrganogramaVO vo) {
				getDisplay().reset();
				setVo(vo);
				getDisplay().populaEntidades(vo);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public Organograma getOrganogramaSelecionado() {
		return organogramaSelecionado;
	}

	public void setOrganogramaSelecionado(Organograma organogramaSelecionado) {
		this.organogramaSelecionado = organogramaSelecionado;
	}

	public OrganogramaVO getVo() {
		return vo;
	}

	public void setVo(OrganogramaVO vo) {
		this.vo = vo;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

}