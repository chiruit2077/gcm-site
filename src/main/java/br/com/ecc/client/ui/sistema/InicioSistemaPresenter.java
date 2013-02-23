package br.com.ecc.client.ui.sistema;

import java.util.Date;
import java.util.List;

import br.com.ecc.client.core.PresenterCodeEnum;
import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.AdministracaoService;
import br.com.ecc.client.service.AdministracaoServiceAsync;
import br.com.ecc.client.service.RecadoService;
import br.com.ecc.client.service.RecadoServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Agenda;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Recado;
import br.com.ecc.model.tipo.TipoCasalEnum;
import br.com.ecc.model.vo.AniversarianteVO;
import br.com.ecc.model.vo.DadosLoginVO;
import br.com.ecc.model.vo.InicioVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class InicioSistemaPresenter extends BasePresenter<InicioSistemaPresenter.Display>{

	public InicioSistemaPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	public interface Display extends BaseDisplay {
		void init();
		void populaRecados(List<Recado> result);
		void fechaRecado();
		void populaRecadosPorGrupo(List<Recado> result);
		void createScroll();
		void populaAniversariantes(List<AniversarianteVO> listaAniversarioPessoa, List<AniversarianteVO> listaAniversarioCasal);
		void populaConvidados(List<Casal> listaConvidados);
		void populaAgenda(List<Agenda> listaAgenda);
	}

	RecadoServiceAsync recadoService = GWT.create(RecadoService.class);
	AdministracaoServiceAsync serviceAdm = GWT.create(AdministracaoService.class);
	private DadosLoginVO dadosLoginVO;
	private Boolean verTodos = false;
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;

	@Override
	public void bind() {
	}

	@Override
	public void init() {
		verTodos = false;
		serviceAdm.getDadosLogin(new WebAsyncCallback<DadosLoginVO>(getDisplay()) {
			@Override
			protected void success(DadosLoginVO dadosLoginVO) {
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
						getDisplay().init();
						if(getDadosLoginVO().getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
							boolean go = false;
//							if(getDadosLoginVO().getCasal().getCasalPadrinho()==null){
//								go=Window.confirm("Não consta no seu cadastro o nome dos seus padrinhos.\nDeseja informar o nome agora?");
//							}
							if(!go){
								buscaEncontros();
							} else {
								ExecutaMenuEvent menu = new ExecutaMenuEvent();
								menu.setJanela(PresenterCodeEnum.MEUS_DADOS);
								getWebResource().getEventBus().fireEvent(menu);
							}
						} else {
							getDisplay().showWaitMessage(false);
						}
					}
				});
			}
		});
	}

	public void buscaEncontros() {
		EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
		serviceEncontro.lista(grupoSelecionado, new WebAsyncCallback<List<Encontro>>(getDisplay()) {
			@Override
			protected void success(List<Encontro> listaEncontro) {
				String cookie = Cookies.getCookie("encontroSelecionado");
				encontroSelecionado = null;
				for (Encontro encontro : listaEncontro) {
					if(encontro.toString().equals(cookie)){
						setEncontroSelecionado(encontro);
						break;
					}
				}
				recadoService.listaInicial(getDadosLoginVO().getCasal(), encontroSelecionado, new WebAsyncCallback<InicioVO>(getDisplay()) {
					@Override
					protected void success(InicioVO vo) {
						getDisplay().populaRecados(vo.getListaRecados());
						getDisplay().populaAniversariantes(vo.getListaAniversarioPessoa(), vo.getListaAniversarioCasal());
						getDisplay().populaConvidados(vo.getListaConvidados());
						getDisplay().populaAgenda(vo.getListaAgenda());
						getDisplay().createScroll();
						getDisplay().showWaitMessage(false);
					}
				});
			}
		});
	}

	public void listaRecadosPorCasal(Casal casal, Boolean lido, final Boolean carregaAniversarios){
		getDisplay().showWaitMessage(true);
		if(dadosLoginVO.getCasal().getTipoCasal().equals(TipoCasalEnum.ENCONTRISTA)){
			recadoService.listaPorCasal(casal, lido, new WebAsyncCallback<List<Recado>>(getDisplay()) {
				@Override
				protected void success(List<Recado> result) {
					getDisplay().populaRecados(result);
					getDisplay().showWaitMessage(false);
				}
			});
		}
	}
	public void listaRecadosTodos(Date inicio){
		getDisplay().showWaitMessage(true);
		recadoService.listaTodosCasal(dadosLoginVO.getCasal(), inicio, new WebAsyncCallback<List<Recado>>(getDisplay()) {
			@Override
			protected void success(List<Recado> result) {
				getDisplay().populaRecadosPorGrupo(result);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar(Recado recado) {
		recadoService.salvar(recado, new WebAsyncCallback<Recado>(getDisplay()) {
			@Override
			protected void success(Recado recado) {
				getDisplay().fechaRecado();
				lista(false);
			}
		});
	}
	public void lista(Boolean carregaAniversarios) {
		getDisplay().showWaitMessage(true);
		listaRecadosPorCasal(getDadosLoginVO().getCasal(), verTodos, carregaAniversarios);
	}

	public DadosLoginVO getDadosLoginVO() {
		return dadosLoginVO;
	}
	public void setDadosLoginVO(DadosLoginVO dadosLoginVO) {
		this.dadosLoginVO = dadosLoginVO;
	}
	public Boolean getVerTodos() {
		return verTodos;
	}
	public void setVerTodos(Boolean verTodos) {
		this.verTodos = verTodos;
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
}