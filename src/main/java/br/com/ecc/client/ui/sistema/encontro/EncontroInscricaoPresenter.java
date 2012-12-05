package br.com.ecc.client.ui.sistema.encontro;

import java.util.List;

import br.com.ecc.client.core.PresenterCodeEnum;
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
import br.com.ecc.client.service.secretaria.MensagemService;
import br.com.ecc.client.service.secretaria.MensagemServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.vo.DadosLoginVO;
import br.com.ecc.model.vo.EncontroInscricaoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

public class EncontroInscricaoPresenter extends BasePresenter<EncontroInscricaoPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<EncontroInscricao> lista);
		void init();
		void setVO(EncontroInscricaoVO vo);
		void setDadosFicha(EncontroInscricao result);
		void edita(EncontroInscricao encontroInscricao);
		void populaMensagem(List<Mensagem> result);
	}

	public EncontroInscricaoPresenter(Display display, WebResource portalResource) {
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
		service.lista(encontroSelecionado, new WebAsyncCallback<List<EncontroInscricao>>(getDisplay()) {
			@Override
			protected void success(List<EncontroInscricao> lista) {
				getDisplay().populaEntidades(lista);
				if(dadosLoginVO.getParametrosRedirecionamentoVO()!=null &&
				   dadosLoginVO.getParametrosRedirecionamentoVO().getPresenterCode()!=null && 
				   dadosLoginVO.getParametrosRedirecionamentoVO().getPresenterCode().equals(PresenterCodeEnum.ENCONTRO_INSCRICAO)){
					dadosLoginVO.getParametrosRedirecionamentoVO().setPresenterCode(null);
					for (EncontroInscricao encontroInscricao : lista) {
						if(dadosLoginVO.getParametrosRedirecionamentoVO().getCasal()!=null && 
						   encontroInscricao.getCasal()!=null &&
						   dadosLoginVO.getParametrosRedirecionamentoVO().getCasal().getId().equals(encontroInscricao.getCasal().getId())){
							getDisplay().edita(encontroInscricao);
							break;
						} else if(dadosLoginVO.getParametrosRedirecionamentoVO().getPessoa()!=null && 
						   encontroInscricao.getPessoa()!=null &&
						   dadosLoginVO.getParametrosRedirecionamentoVO().getPessoa().getId().equals(encontroInscricao.getPessoa().getId())){
							getDisplay().edita(encontroInscricao);
							break;
						}
					}
				}
			}
		});
	}
	
	public void getVO(EncontroInscricao encontroInscricao){
		getDisplay().showWaitMessage(true);
		service.getVO(encontroInscricao, new WebAsyncCallback<EncontroInscricaoVO>(getDisplay()) {
			@Override
			protected void success(EncontroInscricaoVO vo) {
				getDisplay().setVO(vo);
				getDisplay().showWaitMessage(false);
			}

		});
	}
	public void buscaMensagem() {
		getDisplay().showWaitMessage(true);
		MensagemServiceAsync service = GWT.create(MensagemService.class);
		service.listaEspecial(grupoSelecionado, encontroSelecionado, new WebAsyncCallback<List<Mensagem>>(getDisplay()) {
			@Override
			protected void success(List<Mensagem> result) {
				getDisplay().populaMensagem(result);
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
				buscaMensagem();
				buscaEncontroInscricao();
			}
		});
	}
	public void enviaFicha(Mensagem mensagem, EncontroInscricao encontroInscricao) {
		getDisplay().showWaitMessage(true);
		MensagemServiceAsync service = GWT.create(MensagemService.class);
		service.enviaFicha(mensagem, encontroInscricao, new WebAsyncCallback<EncontroInscricao>(getDisplay()) {
			@Override
			protected void success(EncontroInscricao result) {
				getDisplay().setDadosFicha(result);
				getDisplay().showWaitMessage(false);
				Window.alert("Ficha enviada com sucesso");
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