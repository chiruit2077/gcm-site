package br.com.ecc.client.ui.sistema.encontro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.service.AdministracaoServiceAsync;
import br.com.ecc.client.service.cadastro.CasalService;
import br.com.ecc.client.service.cadastro.CasalServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.encontro.EncontroConviteResponsavelService;
import br.com.ecc.client.service.encontro.EncontroConviteResponsavelServiceAsync;
import br.com.ecc.client.service.encontro.EncontroConviteService;
import br.com.ecc.client.service.encontro.EncontroConviteServiceAsync;
import br.com.ecc.client.service.encontro.EncontroFilaService;
import br.com.ecc.client.service.encontro.EncontroFilaServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroConvite;
import br.com.ecc.model.EncontroConviteResponsavel;
import br.com.ecc.model.EncontroFila;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class EncontroConvitePresenter extends BasePresenter<EncontroConvitePresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(List<EncontroConvite> lista);
		void init();
		void populaFilas(List<EncontroFila> result);
		void populaResponsaveis();
		void setCasal(Casal casal);
	}

	public EncontroConvitePresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroConviteServiceAsync service = GWT.create(EncontroConviteService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private DadosLoginVO dadosLoginVO;
	private List<EncontroConviteResponsavel> listaResponsavel;
	private List<EncontroConvite> listaConvites;

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
			protected void success(DadosLoginVO dadosLoginVO) {
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

	public void buscaEncontroConvite(){
		getDisplay().showWaitMessage(true);
		service.lista(encontroSelecionado, new WebAsyncCallback<List<EncontroConvite>>(getDisplay()) {
			@Override
			protected void success(List<EncontroConvite> lista) {
				setListaConvites(lista);
				getDisplay().populaEntidades(lista);
			}
		});
	}
	public void salvar(EncontroConvite convite) {
		getDisplay().showWaitMessage(true);
		service.salva(convite, dadosLoginVO.getUsuario(), new WebAsyncCallback<EncontroConvite>(getDisplay()) {
			@Override
			public void success(EncontroConvite resposta) {
				getDisplay().reset();
				buscaEncontroConvite();
			}
		});
	}

	public void excluir(EncontroConvite convite) {
		getDisplay().showWaitMessage(true);
		service.exclui(convite, dadosLoginVO.getUsuario(), new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaEncontroConvite();
			}
		});
	}

	public void buscaFilas() {
		EncontroFilaServiceAsync service = GWT.create(EncontroFilaService.class);
		service.lista(encontroSelecionado, new WebAsyncCallback<List<EncontroFila>>(getDisplay()) {
			@Override
			protected void success(List<EncontroFila> result) {
				getDisplay().populaFilas(result);
			}
		});

	}
	public void salvarFila(EncontroFila encontroFila) {
		EncontroFilaServiceAsync service = GWT.create(EncontroFilaService.class);
		service.salva(encontroFila, new WebAsyncCallback<EncontroFila>(getDisplay()) {
			@Override
			protected void success(EncontroFila result) {
				buscaFilas();
			}
		});

	}
	public void excluirFila(EncontroFila encontroFila) {
		EncontroFilaServiceAsync service = GWT.create(EncontroFilaService.class);
		service.exclui(encontroFila, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			protected void success(Void result) {
				buscaFilas();
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
				EncontroConviteResponsavelServiceAsync serviceResponsavel = GWT.create(EncontroConviteResponsavelService.class);
				serviceResponsavel.lista(encontroSelecionado, new WebAsyncCallback<List<EncontroConviteResponsavel>>(getDisplay()) {
					@Override
					protected void success(List<EncontroConviteResponsavel> result) {
						setListaResponsavel(result);
						getDisplay().populaResponsaveis();
						buscaFilas();
						buscaEncontroConvite();
					}
				});
			}
		});
	}

	public void salvarCasal(Casal casal) {
		CasalServiceAsync serviceCasal = GWT.create(CasalService.class);
		serviceCasal.salva(casal, new WebAsyncCallback<Casal>(getDisplay()) {
			@Override
			protected void success(Casal result) {
				getDisplay().setCasal(result);
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
	public List<EncontroConviteResponsavel> getListaResponsavel() {
		return listaResponsavel;
	}
	public void setListaResponsavel(List<EncontroConviteResponsavel> listaResponsavel) {
		this.listaResponsavel = listaResponsavel;
	}
	public List<EncontroConvite> getListaConvites() {
		return listaConvites;
	}
	public void setListaConvites(List<EncontroConvite> listaConvites) {
		this.listaConvites = listaConvites;
	}
}