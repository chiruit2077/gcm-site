package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.service.AdministracaoServiceAsync;
import br.com.ecc.client.service.cadastro.AgrupamentoService;
import br.com.ecc.client.service.cadastro.AgrupamentoServiceAsync;
import br.com.ecc.client.service.cadastro.CasalService;
import br.com.ecc.client.service.cadastro.CasalServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.encontro.EncontroInscricaoService;
import br.com.ecc.client.service.encontro.EncontroInscricaoServiceAsync;
import br.com.ecc.client.util.DownloadResourceHelper;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.model.vo.CasalOpcaoRelatorioVO;
import br.com.ecc.model.vo.CasalParamVO;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class ListagemPresenter extends BasePresenter<ListagemPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(List<Casal> lista, Boolean listagem);
		void init();
		void populaAgrupamento(List<Agrupamento> lista);
	}

	public ListagemPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	CasalServiceAsync service = GWT.create(CasalService.class);
	EncontroInscricaoServiceAsync serviceEI = GWT.create(EncontroInscricaoService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private DadosLoginVO dadosLoginVO;
	private List<Casal> listaCasal = new ArrayList<Casal>();

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
						AgrupamentoServiceAsync serviceAgrupamento = GWT.create(AgrupamentoService.class);
						serviceAgrupamento.lista(grupoSelecionado, new WebAsyncCallback<List<AgrupamentoVO>>(getDisplay()) {
							@Override
							protected void success(List<AgrupamentoVO> listavo) {
								ArrayList<Agrupamento> lista = new ArrayList<Agrupamento>();
								for (AgrupamentoVO vo : listavo) {
									lista.add(vo.getAgrupamento());
								}
								getDisplay().populaAgrupamento(lista);
								buscaEncontros();
							}
						});
					}
				});
			}
		});
	}

	public void buscaEncontros(){
		EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
		serviceEncontro.lista(grupoSelecionado, new WebAsyncCallback<List<Encontro>>(getDisplay()) {
			@Override
			protected void success(List<Encontro> listaEncontro) {
				String cookie = Cookies.getCookie("encontroSelecionado");
				for (Encontro encontro : listaEncontro) {
					if(encontro.toString().equals(cookie)){
						setEncontroSelecionado(encontro);
						getDisplay().showWaitMessage(false);
						break;
					}
				}
			}
		});
	}

	public void buscaCasais(CasalParamVO casalParamVO){
		getDisplay().showWaitMessage(true);
		service.lista(casalParamVO, new WebAsyncCallback<List<Casal>>(getDisplay()) {
			@Override
			protected void success(List<Casal> lista) {
				getDisplay().populaEntidades(lista, false);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void imprimir(CasalOpcaoRelatorioVO casalOpcaoRelatorioVO) {
		getDisplay().showWaitMessage(true);
		service.imprimeLista(listaCasal, casalOpcaoRelatorioVO, new WebAsyncCallback<Integer>(getDisplay()) {
			@Override
			protected void success(Integer idRelatorio) {
				getDisplay().showWaitMessage(false);
				DownloadResourceHelper.showReport(idRelatorio, "_blank", "");
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
	public List<Casal> getListaCasal() {
		return listaCasal;
	}
	public void setListaCasal(List<Casal> listaCasal) {
		this.listaCasal = listaCasal;
	}
}