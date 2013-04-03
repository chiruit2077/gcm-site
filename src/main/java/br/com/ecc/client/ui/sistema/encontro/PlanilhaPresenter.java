package br.com.ecc.client.ui.sistema.encontro;

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
import br.com.ecc.client.service.encontro.EncontroAtividadeInscricaoService;
import br.com.ecc.client.service.encontro.EncontroAtividadeInscricaoServiceAsync;
import br.com.ecc.client.service.encontro.EncontroAtividadeService;
import br.com.ecc.client.service.encontro.EncontroAtividadeServiceAsync;
import br.com.ecc.client.util.DownloadResourceHelper;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Atividade;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Papel;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoExibicaoPlanilhaEnum;
import br.com.ecc.model.tipo.TipoNivelUsuarioEnum;
import br.com.ecc.model.vo.DadosLoginVO;
import br.com.ecc.model.vo.EncontroVO;
import br.com.ecc.model.vo.GrupoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class PlanilhaPresenter extends BasePresenter<PlanilhaPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaPlanilha();
		void populaAtividades(List<Atividade> listaAtividades);
		void populaInscricao();
		void populaPapel(List<Papel> listaPapel);
		EncontroPeriodo getPeriodoSelecionado();
		TipoExibicaoPlanilhaEnum getTipoExibicaoPlanilhaSelecionado();
		void populaPeriodos();
	}

	EncontroAtividadeServiceAsync serviceEncontroAtividade = GWT.create(EncontroAtividadeService.class);
	EncontroAtividadeInscricaoServiceAsync serviceEncontroInscricaoAtividade = GWT.create(EncontroAtividadeInscricaoService.class);

	private List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao;
	private List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricaoFull;
	private GrupoVO grupoEncontroVO;
	private EncontroVO encontroVO;
	private Casal casal;
	private Usuario usuario;
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;

	public PlanilhaPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	@Override
	public void bind() {
	}
	public void fechar(){
		getDisplay().showWaitMessage(true);
		getWebResource().getEventBus().fireEvent(new ExecutaMenuEvent());
	}
	@Override
	public void init() {
		getDisplay().showWaitMessage(true);
		AdministracaoServiceAsync service = GWT.create(AdministracaoService.class);
		service.getDadosLogin(new WebAsyncCallback<DadosLoginVO>(getDisplay()) {
			@Override
			protected void success(DadosLoginVO dadosLoginVO) {
				setCasal(dadosLoginVO.getCasal());
				setUsuario(dadosLoginVO.getUsuario());
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
						getDadosGrupoVO();
					}
				});
				getDisplay().reset();
			}
		});
	}

	public void salvarAtividade(EncontroAtividade encontroAtividade) {
		getDisplay().showWaitMessage(true);
		/*Integer pos = null;
		if(encontroAtividade.getId()!=null){
			EncontroAtividade ea;
			for (int i=0; i<grupoEncontroVO.getListaAtividade().size(); i++) {
				ea = encontroVO.getListaEncontroAtividade().get(i);
				if(ea.getId().equals(encontroAtividade.getId())){
					pos = i;
					break;
				}
			}
		}
		final Integer indice = pos;*/
		serviceEncontroAtividade.salva(encontroAtividade, new WebAsyncCallback<EncontroAtividade>(getDisplay()) {
			@Override
			public void success(EncontroAtividade encontroAtividade) {
				/*if(indice!=null){
					encontroVO.getListaEncontroAtividade().set(indice, encontroAtividade);
				} else {
					encontroVO.getListaEncontroAtividade().add(encontroAtividade);
				}
				getDisplay().populaPlanilha();
				getDisplay().showWaitMessage(false);*/
				buscaDadosPlanilha();
			}
		});
	}

	public void excluirAtividade(final EncontroAtividade encontroAtividade) {
		getDisplay().showWaitMessage(true);
		serviceEncontroAtividade.exclui(encontroAtividade, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				/*encontroVO.getListaEncontroAtividade().remove(encontroAtividade);
				getDisplay().populaPlanilha();
				getDisplay().showWaitMessage(false)*/;
				buscaDadosPlanilha();
			}
		});
	}
	public void salvarInscricao(EncontroAtividadeInscricao encontroAtividadeInscricao) {
		getDisplay().showWaitMessage(true);
		/*Integer pos = null;
		if(encontroAtividadeInscricao.getId()!=null){
			EncontroAtividadeInscricao eai;
			for (int i=0; i<listaEncontroAtividadeInscricao.size(); i++) {
				eai = listaEncontroAtividadeInscricao.get(i);
				if(eai.getId().equals(encontroAtividadeInscricao.getId())){
					pos = i;
					break;
				}
			}
		}
		final Integer indice = pos;*/
		serviceEncontroInscricaoAtividade.salva(encontroAtividadeInscricao, new WebAsyncCallback<EncontroAtividadeInscricao>(getDisplay()) {
			@Override
			public void success(EncontroAtividadeInscricao encontroAtividadeInscricao) {
				/*if(indice!=null){
					listaEncontroAtividadeInscricao.set(indice, encontroAtividadeInscricao);
				} else {
					listaEncontroAtividadeInscricao.add(encontroAtividadeInscricao);
				}
				getDisplay().populaPlanilha();
				getDisplay().showWaitMessage(false);*/
				buscaDadosPlanilha();
			}
		});
	}
	public void salvarInscricoes(EncontroAtividade encontroAtividade, EncontroInscricao encontroInscricao, List<EncontroAtividadeInscricao> listaParticipantes) {
		getDisplay().showWaitMessage(true);
		serviceEncontroInscricaoAtividade.salvaInscricoes(encontroSelecionado, encontroAtividade, encontroInscricao, listaParticipantes, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void result) {
				buscaDadosPlanilha();
			}
		});
	}

	public void excluirEncontroAtividadeInscricao(final EncontroAtividadeInscricao encontroAtividadeInscricao) {
		getDisplay().showWaitMessage(true);
		serviceEncontroInscricaoAtividade.exclui(encontroAtividadeInscricao, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				/*listaEncontroAtividadeInscricao.remove(encontroAtividadeInscricao);
				getDisplay().populaPlanilha();
				getDisplay().showWaitMessage(false);*/
				buscaDadosPlanilha();
			}
		});
	}

	public void getDadosGrupoVO() {
		GrupoServiceAsync service = GWT.create(GrupoService.class);
		service.getVO(grupoSelecionado, new WebAsyncCallback<GrupoVO>(getDisplay()) {
			@Override
			protected void success(GrupoVO grupoEncontroVO) {
				setGrupoEncontroVO(grupoEncontroVO);
				getDisplay().populaAtividades(grupoEncontroVO.getListaAtividade());
				getDisplay().populaPapel(grupoEncontroVO.getListaPapel());


				String cookie = Cookies.getCookie("encontroSelecionado");
				for (Encontro encontro : grupoEncontroVO.getListaEncontro()) {
					if(encontro.toString().equals(cookie)){
						setEncontroSelecionado(encontro);
						break;
					}
				}
				getDadosEncontroVO(encontroSelecionado);
			}
		});
	}

	public void getDadosEncontroVO(Encontro encontro){
		getDisplay().showWaitMessage(true);
		EncontroServiceAsync service = GWT.create(EncontroService.class);
		service.getVO(encontro, true, new WebAsyncCallback<EncontroVO>(getDisplay()) {
			@Override
			protected void success(EncontroVO encontroVO) {
				setEncontroVO(encontroVO);
				getDisplay().populaPeriodos();
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void buscaDadosPlanilha() {
		getDisplay().showWaitMessage(true);
		serviceEncontroInscricaoAtividade.listaFiltrado(
				encontroSelecionado,
				getDisplay().getPeriodoSelecionado(),
				getDisplay().getTipoExibicaoPlanilhaSelecionado(),
				new WebAsyncCallback<List<EncontroAtividadeInscricao>>(getDisplay()) {
			@Override
			protected void success(List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao) {
				setListaEncontroAtividadeInscricao(listaEncontroAtividadeInscricao);
				getDisplay().populaInscricao();
				getDisplay().populaPlanilha();
				getDisplay().showWaitMessage(false);
			}
		});
		serviceEncontroInscricaoAtividade.lista(
				encontroSelecionado,new WebAsyncCallback<List<EncontroAtividadeInscricao>>(getDisplay()) {
			@Override
			protected void success(List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao) {
				setListaEncontroAtividadeInscricaoFull(listaEncontroAtividadeInscricao);
			}
		});
	}


	public void imprimirPlanilha(Boolean exportarExcel) {
		getDisplay().showWaitMessage(true);
		serviceEncontroInscricaoAtividade.imprimePlanilha(
				encontroSelecionado,
				getDisplay().getPeriodoSelecionado(),
				getDisplay().getTipoExibicaoPlanilhaSelecionado(),
				exportarExcel,
				new WebAsyncCallback<Integer>(getDisplay()) {
			@Override
			protected void success(Integer idRelatorio) {
				DownloadResourceHelper.showReport(idRelatorio, getDisplay().getDisplayTitle(), "");
				getDisplay().showWaitMessage(false);
			}
		});

	}

	public GrupoVO getGrupoEncontroVO() {
		return grupoEncontroVO;
	}
	public void setGrupoEncontroVO(GrupoVO grupoEncontroVO) {
		this.grupoEncontroVO = grupoEncontroVO;
	}
	public Casal getCasal() {
		return casal;
	}
	public void setCasal(Casal casal) {
		this.casal = casal;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<EncontroAtividadeInscricao> getListaEncontroAtividadeInscricao() {
		return listaEncontroAtividadeInscricao;
	}
	public void setListaEncontroAtividadeInscricao(List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao) {
		this.listaEncontroAtividadeInscricao = listaEncontroAtividadeInscricao;
	}
	public EncontroVO getEncontroVO() {
		return encontroVO;
	}
	public void setEncontroVO(EncontroVO encontroVO) {
		this.encontroVO = encontroVO;
	}

	public boolean isCoordenador() {
		boolean bco = false;
		if(usuario.getNivel()!=null && usuario.getNivel().equals(TipoNivelUsuarioEnum.ADMINISTRADOR)){
			bco = true;
		}
		if(!bco){
			for (Casal c : encontroVO.getListaCoordenadores()) {
				if(c.getId().equals(casal.getId())){
					bco=true;
					break;
				}
			}
		}
		return bco;
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

	public List<EncontroAtividadeInscricao> getListaEncontroAtividadeInscricaoFull() {
		return listaEncontroAtividadeInscricaoFull;
	}

	public void setListaEncontroAtividadeInscricaoFull(
			List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricaoFull) {
		this.listaEncontroAtividadeInscricaoFull = listaEncontroAtividadeInscricaoFull;
	}
}