package br.com.ecc.client.ui.sistema.cadastro;

import java.util.List;

import br.com.ecc.client.core.event.ExecutaMenuEvent;
import br.com.ecc.client.core.mvp.WebAsyncCallback;
import br.com.ecc.client.core.mvp.presenter.BasePresenter;
import br.com.ecc.client.core.mvp.view.BaseDisplay;
import br.com.ecc.client.service.cadastro.AgrupamentoService;
import br.com.ecc.client.service.cadastro.AgrupamentoServiceAsync;
import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.client.service.cadastro.EncontroServiceAsync;
import br.com.ecc.client.service.cadastro.GrupoService;
import br.com.ecc.client.service.cadastro.GrupoServiceAsync;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.AgrupamentoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class AgrupamentoPresenter extends BasePresenter<AgrupamentoPresenter.Display> {
	
	public interface Display extends BaseDisplay {
		void populaEntidades(List<Agrupamento> lista);
		void setVO(AgrupamentoVO vo);
		void repopula();
	}

	public AgrupamentoPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	AgrupamentoServiceAsync service = GWT.create(AgrupamentoService.class);
	EncontroServiceAsync serviceEncontro = GWT.create(EncontroService.class);
	private AgrupamentoVO agrupamentoVO;
	private List<EncontroInscricao> listaInscricoes;
	private Grupo grupoSelecionado;
	private Encontro encontroSelecionado;
	
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
				buscaAgrupamentos(grupoSelecionado);
				buscaEncontros();
			}
		});
	}
	public void buscaAgrupamentos(Grupo grupo){
		service.lista(grupo, new WebAsyncCallback<List<Agrupamento>>(getDisplay()) {
			@Override
			protected void success(List<Agrupamento> lista) {
				getDisplay().populaEntidades(lista);
			}
		});
	}
	public void buscaAgrupamentos(final Encontro encontro){
		getDisplay().showWaitMessage(true);
		service.lista(encontro, new WebAsyncCallback<List<Agrupamento>>(getDisplay()) {
			@Override
			protected void success(List<Agrupamento> lista) {
				getDisplay().populaEntidades(lista);
				getDisplay().showWaitMessage(false);
			}
		});
	}
	public void salvar() {
		getDisplay().showWaitMessage(true);
		service.salva(agrupamentoVO, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				getDisplay().repopula();
			}
		});
	}

	public void excluir(Agrupamento agrupamentoEditado) {
		getDisplay().showWaitMessage(true);
		service.exclui(agrupamentoEditado, new WebAsyncCallback<Void>(getDisplay()) {
			@Override
			public void success(Void resposta) {
				getDisplay().reset();
				getDisplay().repopula();
			}
		});
	}
	
	public void getVO(Agrupamento agrupamentoEditado) {
		getDisplay().showWaitMessage(true);
		service.getVO(agrupamentoEditado, new WebAsyncCallback<AgrupamentoVO>(getDisplay()) {
			@Override
			public void success(AgrupamentoVO vo) {
				setAgrupamentoVO(vo);
				getDisplay().setVO(vo);
				getDisplay().showWaitMessage(false);
			}
		});
	}

	public void buscaEncontros(){
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
				getDisplay().showWaitMessage(false);
				buscaInscricoes(encontroSelecionado);
			}
		});
	}
	public void buscaInscricoes(Encontro encontroSelecionado) {
		getDisplay().showWaitMessage(true);
		serviceEncontro.listaInscricoes(encontroSelecionado, false, new WebAsyncCallback<List<EncontroInscricao>>(getDisplay()) {
			@Override
			protected void success(List<EncontroInscricao> result) {
				setListaInscricoes(result);
				getDisplay().showWaitMessage(false);
			}
		});
		
	}
	public AgrupamentoVO getAgrupamentoVO() {
		return agrupamentoVO;
	}
	public void setAgrupamentoVO(AgrupamentoVO agrupamentoVO) {
		this.agrupamentoVO = agrupamentoVO;
	}
	public List<EncontroInscricao> getListaInscricoes() {
		return listaInscricoes;
	}
	public void setListaInscricoes(List<EncontroInscricao> listaInscricoes) {
		this.listaInscricoes = listaInscricoes;
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