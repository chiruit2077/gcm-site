package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.AgrupamentoService;
import br.com.ecc.client.service.cadastro.AgrupamentoServiceAsync;
import br.com.ecc.client.service.cadastro.CasalService;
import br.com.ecc.client.service.cadastro.CasalServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.client.service.secretaria.MensagemService;
import br.com.ecc.client.service.secretaria.MensagemServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.Mensagem;
import br.com.ecc.model.MensagemDestinatario;
import br.com.ecc.model.tipo.TipoMensagemEnum;
import br.com.ecc.model.vo.AgrupamentoVO;
import br.com.ecc.model.vo.CasalParamVO;
import br.com.ecc.model.vo.MensagemVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

public class MensagemPresenter extends BasePresenter<MensagemPresenter.Display> {

	public interface Display extends BaseDisplay {
		void populaEntidades(List<Mensagem> lista);
		void setVO(MensagemVO mensagemVO);
		void populaAgrupamento(List<Agrupamento> result);
		void populaEncontro(List<Encontro> result);
	}

	public MensagemPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	MensagemServiceAsync service = GWT.create(MensagemService.class);
	AgrupamentoServiceAsync serviceAgrupamento = GWT.create(AgrupamentoService.class);
	EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
	private MensagemVO mensagemVO;
	private AgrupamentoVO agrupamentoVO;
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;

	private List<EncontroInscricao> listaInscricao = null;
	private List<Casal> listaEncontristas = null;

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
				getDisplay().populaEncontro(listaEncontro);
				String cookie = Cookies.getCookie("encontroSelecionado");
				for (Encontro encontro : listaEncontro) {
					if(encontro.toString().equals(cookie)){
						setEncontroSelecionado(encontro);
						break;
					}
				}
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar() {
		getDisplay().showWaitMessage(true);
		service.salva(mensagemVO, new WebAsyncCallback<MensagemVO>(getDisplay()) {
			@Override
			public void success(MensagemVO mensagem) {
				getDisplay().reset();
				init();
			}
		});
	}

	public void enviar(MensagemDestinatario destinatario, Boolean reenvio) {
		getDisplay().showWaitMessage(true);
		service.envia(mensagemVO, destinatario, reenvio, new WebAsyncCallback<Mensagem>(getDisplay()) {
			@Override
			public void success(Mensagem resposta) {
				Window.alert("Mensagem enviada");
				buscaVO(resposta);
			}
		});
	}

	public void excluir(Mensagem mensagemEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(mensagemEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				init();
			}
		});
	}
	public void buscaMensagens(TipoMensagemEnum tipo){
		getDisplay().showWaitMessage(true);
		service.lista(grupoSelecionado, tipo, new WebAsyncCallback<List<Mensagem>>(getDisplay()) {
			@Override
			protected void success(List<Mensagem> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void buscaVO(Mensagem mensagem) {
		getDisplay().showWaitMessage(true);
		service.getVO(mensagem, new WebAsyncCallback<MensagemVO>(getDisplay()) {
			@Override
			public void success(MensagemVO mensagemVO) {
				setMensagemVO(mensagemVO);
				getDisplay().setVO(mensagemVO);
				buscaAgrupamentos();
			}
		});
	}

	public void getAgrupamentoVO(Agrupamento agrupamento) {
		getDisplay().showWaitMessage(true);
		serviceAgrupamento.getVO(agrupamento, new WebAsyncCallback<AgrupamentoVO>(getDisplay()) {
			@Override
			protected void success(AgrupamentoVO vo) {
				setAgrupamentoVO(vo);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void buscaAgrupamentos(){
		serviceAgrupamento.lista(grupoSelecionado, new WebAsyncCallback<List<AgrupamentoVO>>(getDisplay()) {
			@Override
			protected void success(List<AgrupamentoVO> listavo) {
				ArrayList<Agrupamento> lista = new ArrayList<Agrupamento>();
				for (AgrupamentoVO vo : listavo) {
					lista.add(vo.getAgrupamento());
				}
				getDisplay().populaAgrupamento(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void buscaAgrupamentos(final Encontro encontro){
		serviceAgrupamento.lista(encontro, new WebAsyncCallback<List<AgrupamentoVO>>(getDisplay()) {
			@Override
			protected void success(List<AgrupamentoVO> listavo) {
				ArrayList<Agrupamento> lista = new ArrayList<Agrupamento>();
				for (AgrupamentoVO vo : listavo) {
					lista.add(vo.getAgrupamento());
				}
				getDisplay().populaAgrupamento(lista);
				serviceEncontro.listaInscricoes(encontro, false, new WebAsyncCallback<List<EncontroInscricao>>(getDisplay()) {
					@Override
					protected void success(List<EncontroInscricao> result) {
						setListaInscricao(result);
						getDisplay().showWaitMessage(false);
					}
				});
			}
		});
	}

	public void getEncontristas() {
		getDisplay().showWaitMessage(true);
		CasalParamVO vo = new CasalParamVO();
		vo.setGrupo(grupoSelecionado);
		CasalServiceAsync casalService = GWT.create(CasalService.class);
		casalService.lista(vo, new WebAsyncCallback<List<Casal>>(getDisplay()) {
			@Override
			protected void success(List<Casal> result) {
				setListaEncontristas(result);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public MensagemVO getMensagemVO() {
		return mensagemVO;
	}
	public void setMensagemVO(MensagemVO mensagemVO) {
		this.mensagemVO = mensagemVO;
	}
	public AgrupamentoVO getAgrupamentoVO() {
		return agrupamentoVO;
	}
	public void setAgrupamentoVO(AgrupamentoVO agrupamentoVO) {
		this.agrupamentoVO = agrupamentoVO;
	}
	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}
	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}
	public List<EncontroInscricao> getListaInscricao() {
		return listaInscricao;
	}
	public void setListaInscricao(List<EncontroInscricao> listaInscricao) {
		this.listaInscricao = listaInscricao;
	}
	public List<Casal> getListaEncontristas() {
		return listaEncontristas;
	}
	public void setListaEncontristas(List<Casal> listaEncontristas) {
		this.listaEncontristas = listaEncontristas;
	}
	public Encontro getEncontroSelecionado() {
		return encontroSelecionado;
	}
	public void setEncontroSelecionado(Encontro encontroSelecionado) {
		this.encontroSelecionado = encontroSelecionado;
	}
}