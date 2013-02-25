package br.com.ecc.client.ui.sistema.hotelaria;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.encontro.EncontroHotelService;
import br.com.ecc.client.service.encontro.EncontroHotelServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.EncontroHotelVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class DistribuicaoPresenter extends BasePresenter<DistribuicaoPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(EncontroHotelVO encontroHotelVO);
		void populaHoteis(List<EncontroHotel> lista);
	}

	public DistribuicaoPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroHotelServiceAsync service = GWT.create(EncontroHotelService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private EncontroHotel encontroHotelSelecionado;
	private List<EncontroHotel> listaHotel;
	private EncontroHotelVO encontroHotelVO;

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
				buscaHoteis();
			}
		});
	}

	public void buscaHoteis(){
		getDisplay().showWaitMessage(true);
		service.lista(encontroSelecionado, new WebAsyncCallback<List<EncontroHotel>>(getDisplay()) {
			@Override
			protected void success(List<EncontroHotel> lista) {
				setListaHotel(lista);
				getDisplay().populaHoteis(lista);
			}
		});
	}

	public void buscaVO(){
		if (encontroHotelSelecionado != null){
			getDisplay().showWaitMessage(true);
			service.getVO(encontroHotelSelecionado, new WebAsyncCallback<EncontroHotelVO>(getDisplay()) {
				@Override
				protected void success(EncontroHotelVO vo) {
					setEncontroHotelVO(vo);
					getDisplay().populaEntidades(vo);
					getDisplay().showWaitMessage(false);
				}
			});
		}
		getDisplay().showWaitMessage(false);
	}
	public void salvar() {
		getDisplay().showWaitMessage(true);
		service.salva(getEncontroHotelVO(), new WebAsyncCallback<EncontroHotelVO>(getDisplay()) {
			@Override
			public void success(EncontroHotelVO vo) {
				getDisplay().reset();
				setEncontroHotelVO(vo);
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

	public EncontroHotelVO getEncontroHotelVO() {
		return encontroHotelVO;
	}

	public void setEncontroHotelVO(EncontroHotelVO encontroHotelVO) {
		this.encontroHotelVO = encontroHotelVO;
	}

	public List<EncontroHotel> getListaHotel() {
		return listaHotel;
	}

	public void setListaHotel(List<EncontroHotel> listaHotel) {
		this.listaHotel = listaHotel;
	}

	public EncontroHotel getEncontroHotelSelecionado() {
		return encontroHotelSelecionado;
	}

	public void setEncontroHotelSelecionado(EncontroHotel encontroHotelSelecionado) {
		this.encontroHotelSelecionado = encontroHotelSelecionado;
	}

}