package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
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
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Casal;
import br.com.ecc.model.CasalListaSorteio;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.CasalVO;
import br.com.ecc.model.vo.DadosLoginVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class ListagemSorteioPresenter extends BasePresenter<ListagemSorteioPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades();
	}

	public ListagemSorteioPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	CasalServiceAsync service = GWT.create(CasalService.class);
	
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private DadosLoginVO dadosLoginVO;
	private List<CasalVO> listaCasal = new ArrayList<CasalVO>();

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

	public void buscaEncontros(){
		EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
		serviceEncontro.lista(grupoSelecionado, new WebAsyncCallback<List<Encontro>>(getDisplay()) {
			@Override
			protected void success(List<Encontro> listaEncontro) {
				String cookie = Cookies.getCookie("encontroSelecionado");
				for (Encontro encontro : listaEncontro) {
					if(encontro.toString().equals(cookie)){
						setEncontroSelecionado(encontro);
						
						
						service.listaSorteio(encontroSelecionado, new WebAsyncCallback<List<CasalVO>>(getDisplay()) {
							@Override
							protected void success(List<CasalVO> lista) {
								setListaCasal(lista);
								getDisplay().populaEntidades();
							}
						});
						getDisplay().showWaitMessage(false);
						break;
					}
				}
			}
		});
	}
	
	public void salvar(final Casal casal, TipoInscricaoEnum tipo){
		service.salvarSorteio(encontroSelecionado, casal, tipo, new WebAsyncCallback<CasalListaSorteio>(getDisplay()) {
			@Override
			protected void success(CasalListaSorteio result) {
				for (CasalVO casalVO : listaCasal) {
					if(casalVO.getCasal().getId().equals(casal.getId())){
						casalVO.setCasalSorteio(result);
						break;
					}
				}
			}
		});
	}
	
	public void excluir(final CasalListaSorteio casal){
		service.excluirSorteio(casal, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			protected void success(Void result) {
				for (CasalVO casalVO : listaCasal) {
					if(casalVO.getCasal().getId().equals(casal.getId())){
						casalVO.setCasalSorteio(null);
						break;
					}
				}
			}
		});
	}


	public void imprimir() {
		getDisplay().showWaitMessage(true);
//		service.imprimeListaSorteio(listaAdicionados, new WebAsyncCallback<Integer>(getDisplay()) {
//			@Override
//			protected void success(Integer idRelatorio) {
//				getDisplay().showWaitMessage(false);
//				DownloadResourceHelper.showReport(idRelatorio, "_blank", "");
//			}
//		});
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

	public List<CasalVO> getListaCasal() {
		return listaCasal;
	}

	public void setListaCasal(List<CasalVO> listaCasal) {
		this.listaCasal = listaCasal;
	}
}