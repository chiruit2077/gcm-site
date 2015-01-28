package br.com.ecc.client.ui.sistema.tesouraria;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.service.AdministracaoServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.encontro.EncontroInscricaoService;
import br.com.ecc.client.service.encontro.EncontroInscricaoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.DadosLoginVO;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class DoacaoPresenter extends BasePresenter<DoacaoPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(List<EncontroInscricaoVO> lista);
		void init();
//		void setVO(EncontroInscricaoVO vo);
//		void setDadosFicha(EncontroInscricao result);
//		void edita(EncontroInscricao encontroInscricao);
	}

	public DoacaoPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroInscricaoServiceAsync service = GWT.create(EncontroInscricaoService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private DadosLoginVO dadosLoginVO;

	@Override
	public void bind() {
	}
	public void fechar(){
		getDisplay().showWaitMessage(true);
		getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	}
	@Override
	public void init() {
		AdministracaoServiceAsync serviceAdm = GWT.create(AdministracaoService.class);
		serviceAdm.getDadosLogin(new WebAsyncCallback<DadosLoginVO>(getDisplay()) {
			@Override
			protected void success(final DadosLoginVO dadosLoginVO) {
				setDadosLoginVO(dadosLoginVO);
				getDisplay().init();
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
		});
	}

	public void buscaEncontroInscricao(){
		getDisplay().showWaitMessage(true);
		service.listaVO(encontroSelecionado, new WebAsyncCallback<List<EncontroInscricaoVO>>(getDisplay()) {
			@Override
			protected void success(List<EncontroInscricaoVO> listaVO) {
				getDisplay().populaEntidades(listaVO);
			}
		});
	}

	public void getVO(EncontroInscricao encontroInscricao){
		getDisplay().showWaitMessage(true);
		service.getVO(encontroInscricao, new WebAsyncCallback<EncontroInscricaoVO>(getDisplay()) {
			@Override
			protected void success(EncontroInscricaoVO vo) {
				//getDisplay().setVO(vo);
				getDisplay().showWaitMessage(false);
			}

		});
	}
	public void salvar(EncontroInscricaoVO vo) {
		getDisplay().showWaitMessage(true);
		service.salva(vo, new WebAsyncCallback<EncontroInscricaoVO>(getDisplay()) {
			@Override
			public void success(EncontroInscricaoVO resposta) {
				getDisplay().reset();
				buscaEncontroInscricao();
			}
		});
	}

	public void excluir(EncontroInscricao encontroInscricaoEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(encontroInscricaoEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaEncontroInscricao();
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
				buscaEncontroInscricao();
			}
		});
	}
	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}
	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}
	public DadosLoginVO getDadosLoginVO() {
		return dadosLoginVO;
	}
	public void setDadosLoginVO(DadosLoginVO dadosLoginVO) {
		this.dadosLoginVO = dadosLoginVO;
	}
	public Encontro getEncontroSelecionado() {
		return encontroSelecionado;
	}
	public void setEncontroSelecionado(Encontro encontroSelecionado) {
		this.encontroSelecionado = encontroSelecionado;
	}
}