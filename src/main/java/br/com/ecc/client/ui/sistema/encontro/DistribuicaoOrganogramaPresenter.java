package br.com.ecc.client.ui.sistema.encontro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.encontro.EncontroOrganogramaService;
import br.com.ecc.client.service.encontro.EncontroOrganogramaServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.EncontroOrganogramaVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class DistribuicaoOrganogramaPresenter extends BasePresenter<DistribuicaoOrganogramaPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(EncontroOrganogramaVO vo);
		void populaOrganogramas(List<EncontroOrganograma> lista);
	}

	public DistribuicaoOrganogramaPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroOrganogramaServiceAsync service = GWT.create(EncontroOrganogramaService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private EncontroOrganograma encontroOrganogramaSelecionado;
	private List<EncontroOrganograma> listaEncontroOrganograma;
	private EncontroOrganogramaVO vo;

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
				buscaEncontroOrganogramas();
			}
		});
	}

	public void buscaEncontroOrganogramas(){
		getDisplay().showWaitMessage(true);
		service.lista(encontroSelecionado, new WebAsyncCallback<List<EncontroOrganograma>>(getDisplay()) {
			@Override
			protected void success(List<EncontroOrganograma> lista) {
				setListaEncontroOrganograma(lista);
				getDisplay().populaOrganogramas(lista);
			}
		});
	}

	public void buscaVO(){
		if (getEncontroOrganogramaSelecionado() != null){
			getDisplay().showWaitMessage(true);
			service.getVO(getEncontroOrganogramaSelecionado(), new WebAsyncCallback<EncontroOrganogramaVO>(getDisplay()) {
				@Override
				protected void success(EncontroOrganogramaVO vo) {
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
		service.salvaEncontroOrganogramaCoordenacao(getVo(), new WebAsyncCallback<EncontroOrganogramaVO>(getDisplay()) {
			@Override
			public void success(EncontroOrganogramaVO vo) {
				getDisplay().reset();
				setVo(vo);
				getDisplay().populaEntidades(vo);
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

	public EncontroOrganogramaVO getVo() {
		return vo;
	}

	public void setVo(EncontroOrganogramaVO vo) {
		this.vo = vo;
	}

	public void setListaEncontroOrganograma(List<EncontroOrganograma> listaEncontroOrganograma) {
		this.listaEncontroOrganograma = listaEncontroOrganograma;
	}

	public void setEncontroOrganogramaSelecionado(
			EncontroOrganograma encontroOrganogramaSelecionado) {
		this.encontroOrganogramaSelecionado = encontroOrganogramaSelecionado;
	}

	public EncontroOrganograma getEncontroOrganogramaSelecionado() {
		return encontroOrganogramaSelecionado;
	}

	public List<EncontroOrganograma> getListaEncontroOrganograma() {
		return listaEncontroOrganograma;
	}

}