package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.event.UploadFinishedEvent;
import br.com.ecc.client.core.event.UploadFinishedHandler;
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
import br.com.ecc.client.service.cadastro.PessoaService;
import br.com.ecc.client.service.cadastro.PessoaServiceAsync;
import br.com.ecc.client.service.encontro.EncontroInscricaoService;
import br.com.ecc.client.service.encontro.EncontroInscricaoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.vo.CasalParamVO;
import br.com.ecc.model.vo.CasalVO;
import br.com.ecc.model.vo.DadosLoginVO;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class CasalPresenter extends BasePresenter<CasalPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<Casal> lista);
		void defineFoto();
		void edita(Casal casal);
		void init();
		void setVO(CasalVO resposta);
		void setEncontroInscricaoVO(EncontroInscricaoVO result);
		EncontroInscricaoVO getEncontroInscricaoVO();
		void populaAgrupamento(List<Agrupamento> lista);
	}

	public CasalPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	CasalServiceAsync service = GWT.create(CasalService.class);
	PessoaServiceAsync servicePessoa = GWT.create(PessoaService.class);
	EncontroInscricaoServiceAsync serviceEI = GWT.create(EncontroInscricaoService.class);
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	private DadosLoginVO dadosLoginVO;
	private CasalParamVO casalParamVO;
	
	@Override
	public void bind() {
		getWebResource().getEventBus().addHandler(UploadFinishedEvent.TYPE, new UploadFinishedHandler() {
			@Override
			public void onUploadFinished(UploadFinishedEvent event) {
				getDisplay().defineFoto();
			}
		});
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
						serviceAgrupamento.lista(grupoSelecionado, new WebAsyncCallback<List<Agrupamento>>(getDisplay()) {
							@Override
							protected void success(List<Agrupamento> lista) {
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
						break;
					}
				}
				if(dadosLoginVO.getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
					getDisplay().edita(dadosLoginVO.getCasal());
					getDisplay().showWaitMessage(false);
				} else {
					casalParamVO = new CasalParamVO();
					casalParamVO.setGrupo(grupoSelecionado);
					casalParamVO.setNome(getDadosLoginVO().getCasal().getEle().getNome());
					buscaCasais(casalParamVO);
				}
			}
		});
	}
	
	public void buscaCasais(CasalParamVO casalParamVO){
		getDisplay().showWaitMessage(true);
		this.casalParamVO = casalParamVO;
		service.lista(casalParamVO, new WebAsyncCallback<List<Casal>>(getDisplay()) {
			@Override
			protected void success(List<Casal> lista) {
				getDisplay().populaEntidades(lista);
				/*
				if(dadosLoginVO.getParametrosRedirecionamentoVO()!=null && dadosLoginVO.getParametrosRedirecionamentoVO().getCasal()!=null){
					getDisplay().edita(dadosLoginVO.getParametrosRedirecionamentoVO().getCasal());
				}
				*/
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(CasalVO casalVO) {
		getDisplay().showWaitMessage(true);
		service.salva(casalVO, new WebAsyncCallback<CasalVO>(getDisplay()) {
			@Override
			public void success(final CasalVO resposta) {
				if(encontroSelecionado!=null && getDisplay().getEncontroInscricaoVO()!=null){
					serviceEI.salva(getDisplay().getEncontroInscricaoVO(), new WebAsyncCallback<EncontroInscricaoVO>(getDisplay()) {
						@Override
						protected void success(EncontroInscricaoVO result) {
							/*
							Window.alert("Dados guardados com sucesso");
							getDisplay().setVO(resposta);
							if(result.getEncontroInscricao().getValorEncontro()!=null && 
							   result.getEncontroInscricao().getValorEncontro().doubleValue()!=0){
								getDisplay().setEncontroInscricaoVO(result);
							} else {
								getDisplay().setEncontroInscricaoVO(null);
							}
							getDisplay().showWaitMessage(false);
							*/
							if(dadosLoginVO.getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
								getDisplay().reset();
								fechar();
							} else {
								getDisplay().reset();
								buscaCasais(casalParamVO);
							}
						}
					});
				} else {
					/*
					Window.alert("Dados guardados com sucesso");
					getDisplay().setVO(resposta);
					getDisplay().setEncontroInscricaoVO(null);
					getDisplay().showWaitMessage(false);
					*/
					if(dadosLoginVO.getCasal().getTipoCasal().equals(TipoCasalEnum.CONVIDADO)){
						getDisplay().showWaitMessage(false);
						fechar();
					} else {
						getDisplay().reset();
						buscaCasais(casalParamVO);
					}
				}
			}
		});
	}
	
	public void getVO(Casal casal) {
		getDisplay().showWaitMessage(true);
		service.getVO(casal, new WebAsyncCallback<CasalVO>(getDisplay()) {
			@Override
			public void success(CasalVO resposta) {
				getDisplay().setVO(resposta);
				if(encontroSelecionado!=null){
					EncontroInscricaoServiceAsync serviceEI = GWT.create(EncontroInscricaoService.class);
					serviceEI.getVO(encontroSelecionado, resposta.getCasal(), new WebAsyncCallback<EncontroInscricaoVO>(getDisplay()) {
						@Override
						protected void success(EncontroInscricaoVO result) {
							getDisplay().setEncontroInscricaoVO(result);
						}
					});
				}
				getDisplay().setEncontroInscricaoVO(null);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void excluir(Casal casalEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(casalEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				buscaCasais(casalParamVO);
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