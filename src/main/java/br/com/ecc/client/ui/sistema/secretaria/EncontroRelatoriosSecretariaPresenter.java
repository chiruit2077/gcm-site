package br.com.ecc.client.ui.sistema.secretaria;

import java.util.ArrayList;
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
import br.com.ecc.client.service.secretaria.EncontroRelatoriosSecretariaService;
import br.com.ecc.client.service.secretaria.EncontroRelatoriosSecretariaServiceAsync;
import br.com.ecc.client.util.DownloadResourceHelper;
import br.com.ecc.core.mvp.WebResource;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.vo.AgrupamentoVO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

public class EncontroRelatoriosSecretariaPresenter extends BasePresenter<EncontroRelatoriosSecretariaPresenter.Display> {

	public interface Display extends BaseDisplay {
		void init();

		void setListaAgrupamentos(List<Agrupamento> result);
	}

	public enum ProcessaOpcao {
		GERACSV,
		LISTAGEMAGRUPAMENTO,
		LISTAGEMFILAROMANTICO,
		LISTAGEMONIBUS;
	}

	public EncontroRelatoriosSecretariaPresenter(Display display, WebResource portalResource) {
		super(display, portalResource);
	}

	EncontroRelatoriosSecretariaServiceAsync service = GWT.create(EncontroRelatoriosSecretariaService.class);
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
				buscaEncontros();
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
						buscaAgrupamentos();
						break;
					}
				}
			}
		});
	}

	public void buscaAgrupamentos() {
		AgrupamentoServiceAsync servicoAgrupamento = GWT.create(AgrupamentoService.class);
		servicoAgrupamento.lista(getEncontroSelecionado(), new WebAsyncCallback<List<AgrupamentoVO>>(getDisplay()) {
			@Override
			protected void success(List<AgrupamentoVO> listavo) {
				ArrayList<Agrupamento> lista = new ArrayList<Agrupamento>();
				for (AgrupamentoVO vo : listavo) {
					lista.add(vo.getAgrupamento());
				}
				getDisplay().setListaAgrupamentos(lista);

			}
		});
	}

	public void processa(Encontro encontro, ProcessaOpcao opcao) {
		processa(encontro, opcao, null);
	}

	public void processa(Encontro encontro, ProcessaOpcao opcao, Agrupamento agrupamento) {
		getDisplay().showWaitMessage(true);
		if (opcao.equals(ProcessaOpcao.GERACSV)){
			service.geraCSV(encontro, "afilhados.csv", new WebAsyncCallback<Integer>(getDisplay()) {
				@Override
				protected void success(Integer idArquivo) {
					getDisplay().showWaitMessage(false);
					DownloadResourceHelper.showArquivoDigital(idArquivo, "_blank", true, "");
				}
			});
		}else if (opcao.equals(ProcessaOpcao.LISTAGEMAGRUPAMENTO)){
			service.imprimeRelatorioAgrupamento(agrupamento, new WebAsyncCallback<Integer>(getDisplay()) {
				@Override
				protected void success(Integer idReport) {
					getDisplay().showWaitMessage(false);
					DownloadResourceHelper.showReport(idReport, "_blank", "");
				}
			});
		}else if (opcao.equals(ProcessaOpcao.LISTAGEMFILAROMANTICO)){
			service.imprimeRelatorioRomantico(encontro, new WebAsyncCallback<Integer>(getDisplay()) {
				@Override
				protected void success(Integer idReport) {
					getDisplay().showWaitMessage(false);
					DownloadResourceHelper.showReport(idReport, "_blank", "");
				}
			});
		}else if (opcao.equals(ProcessaOpcao.LISTAGEMONIBUS)){
			service.imprimeRelatorioOnibus(encontro, new WebAsyncCallback<Integer>(getDisplay()) {
				@Override
				protected void success(Integer idReport) {
					getDisplay().showWaitMessage(false);
					DownloadResourceHelper.showReport(idReport, "_blank", "");
				}
			});
		}
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