package br.com.ecc.server.service.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ecc.client.service.cadastro.EncontroService;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroFila;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroInscricaoFichaPagamento;
import br.com.ecc.model.EncontroOrganograma;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.Grupo;
import br.com.ecc.model.tipo.Operacao;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.EncontroVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.auth.Permissao;
import br.com.ecc.server.command.EncontroCarregaVOCommand;
import br.com.ecc.server.command.EncontroInscricaoFichaPagamentoGeraFichasCommand;
import br.com.ecc.server.command.EncontroSalvarCommand;
import br.com.ecc.server.command.basico.DeleteEntityCommand;
import br.com.ecc.server.command.basico.ExecuteUpdateCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;
import br.com.ecc.server.command.basico.SaveEntityCommand;
import br.com.ecc.server.util.BeanUtil;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
public class EncontroServiceImpl extends SecureRemoteServiceServlet implements EncontroService {

	private static final long serialVersionUID = 1944513084207357906L;

	@Inject Injector injector;
	@Inject EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	@Permissao(nomeOperacao="Listar encontros", operacao=Operacao.VISUALIZAR)
	public List<Encontro> lista(Grupo grupo) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontro.porGrupo");
		cmd.addParameter("grupo", grupo);
		return cmd.call();
	}

	@Override
	@Permissao(nomeOperacao="Excluir encontro", operacao=Operacao.EXCLUIR)
	public void exclui(Encontro encontro) throws Exception {
		ExecuteUpdateCommand cmdDelete = injector.getInstance(ExecuteUpdateCommand.class);
		cmdDelete.addParameter("encontro", encontro);

		cmdDelete.setNamedQuery("encontroPeriodo.deletePorEncontro");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroConviteResponsavel.deletePorEncontro");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroTotalizacao.deletePorEncontro");
		cmdDelete.call();

		cmdDelete.setNamedQuery("encontroTotalizacaoAtividade.deletePorEncontro");
		cmdDelete.call();

		DeleteEntityCommand cmd = injector.getInstance(DeleteEntityCommand.class);
		cmd.setBaseEntity(encontro);
		cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@Permissao(nomeOperacao="Salvar encontro vo", operacao=Operacao.SALVAR)
	public void salvaVO(EncontroVO encontroVO, Boolean copia, EncontroVO ultimoVO) throws Exception {
		EncontroSalvarCommand cmd = injector.getInstance(EncontroSalvarCommand.class);
		cmd.setEncontroVO(encontroVO);
		cmd.setCopia(copia);
		EncontroVO novoVO = cmd.call();
		if (copia){
			SaveEntityCommand cmdSalva = injector.getInstance(SaveEntityCommand.class);

			GetEntityListCommand cmdList = injector.getInstance(GetEntityListCommand.class);
			cmdList.addParameter("encontro", ultimoVO.getEncontro());

			cmdList.setNamedQuery("encontroRestaurante.porEncontro");
			List<EncontroRestaurante> listaRestaurantes = (List<EncontroRestaurante>) cmdList.call();
			for (EncontroRestaurante encontroRestaurante : listaRestaurantes) {
				EncontroRestaurante novo = new EncontroRestaurante();
				BeanUtil.copyProperties(encontroRestaurante,novo);
				novo.setId(null);
				novo.setEncontro(novoVO.getEncontro());
				novo.setVersion(0);
				cmdSalva.setBaseEntity(novo);
				cmdSalva.call();
			}

			cmdList.setNamedQuery("encontroFila.porEncontro");
			List<EncontroFila> listaFila = (List<EncontroFila>) cmdList.call();
			for (EncontroFila fila : listaFila) {
				EncontroFila novo = new EncontroFila();
				BeanUtil.copyProperties(fila,novo);
				novo.setId(null);
				novo.setEncontro(novoVO.getEncontro());
				novo.setVersion(0);
				cmdSalva.setBaseEntity(novo);
				cmdSalva.call();
			}

			long tempoacrescimo = encontroVO.getEncontro().getInicio().getTime() - ultimoVO.getEncontro().getInicio().getTime();
			cmdList.setNamedQuery("encontroAtividade.porEncontro");
			List<EncontroAtividade> listaAtividade = (List<EncontroAtividade>) cmdList.call();
			for (EncontroAtividade atividade : listaAtividade) {
				EncontroAtividade novo = new EncontroAtividade();
				BeanUtil.copyProperties(atividade,novo);
				novo.setInicio(new Date(atividade.getInicio().getTime()+tempoacrescimo));
				novo.setFim(new Date(atividade.getFim().getTime()+tempoacrescimo));
				novo.setId(null);
				novo.setEncontro(novoVO.getEncontro());
				novo.setVersion(0);
				cmdSalva.setBaseEntity(novo);
				cmdSalva.call();
			}

			cmdList.setNamedQuery("encontroHotel.porEncontro");
			List<EncontroHotel> listaHotel = (List<EncontroHotel>) cmdList.call();
			for (EncontroHotel hotel : listaHotel) {
				EncontroHotel novo = new EncontroHotel();
				BeanUtil.copyProperties(hotel,novo);
				novo.setId(null);
				novo.setEncontro(novoVO.getEncontro());
				novo.setVersion(0);
				cmdSalva.setBaseEntity(novo);
				cmdSalva.call();
			}

			cmdList.setNamedQuery("encontroOrganograma.porEncontro");
			List<EncontroOrganograma> listaOrganograma = (List<EncontroOrganograma>) cmdList.call();
			for (EncontroOrganograma organograma : listaOrganograma) {
				EncontroOrganograma novo = new EncontroOrganograma();
				BeanUtil.copyProperties(organograma,novo);
				novo.setId(null);
				novo.setEncontro(novoVO.getEncontro());
				novo.setVersion(0);
				cmdSalva.setBaseEntity(novo);
				cmdSalva.call();
			}

			cmdList.setNamedQuery("agrupamento.porEncontro");
			List<Agrupamento> listaAgrupamento = (List<Agrupamento>) cmdList.call();
			for (Agrupamento agrupamento : listaAgrupamento) {
				Agrupamento novo = new Agrupamento();
				BeanUtil.copyProperties(agrupamento,novo);
				novo.setId(null);
				novo.setEncontro(novoVO.getEncontro());
				novo.setVersion(0);
				cmdSalva.setBaseEntity(novo);
				cmdSalva.call();
			}

			cmdList.setNamedQuery("encontroInscricaoFichaPagamento.porEncontroReservada");
			List<EncontroInscricaoFichaPagamento> listaFichas = (List<EncontroInscricaoFichaPagamento>) cmdList.call();
			for (EncontroInscricaoFichaPagamento ficha : listaFichas) {
				EncontroInscricaoFichaPagamento novo = new EncontroInscricaoFichaPagamento();
				BeanUtil.copyProperties(ficha,novo);
				novo.setId(null);
				novo.setEncontro(novoVO.getEncontro());
				novo.setEncontroInscricao(null);
				novo.setVersion(0);
				cmdSalva.setBaseEntity(novo);
				cmdSalva.call();
			}
			EncontroInscricaoFichaPagamentoGeraFichasCommand cmdGeraFichas = injector.getInstance(EncontroInscricaoFichaPagamentoGeraFichasCommand.class);
			cmdGeraFichas.setEncontroSelecionado(novoVO.getEncontro());
			cmdGeraFichas.call();
		}
	}

	@Override
	public EncontroVO getVO(Encontro encontro, Boolean ignorarAfilhados) throws Exception {
		EncontroCarregaVOCommand cmd = injector.getInstance(EncontroCarregaVOCommand.class);
		cmd.setEncontro(encontro);
		EncontroVO vo =  cmd.call();
		if(ignorarAfilhados){
			List<EncontroInscricao> lista = new ArrayList<EncontroInscricao>();
			for (EncontroInscricao ei : vo.getListaInscricao()) {
				if(!ei.getTipo().equals(TipoInscricaoEnum.AFILHADO)){
					lista.add(ei);
				}
			}
			vo.setListaInscricao(lista);
		}

		return vo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EncontroInscricao> listaInscricoes(Encontro encontro, Boolean exibeRecusados) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		if(exibeRecusados==null || exibeRecusados){
			cmd.setNamedQuery("encontroInscricao.porEncontro");
		} else {
			cmd.setNamedQuery("encontroInscricao.porEncontroConfirmados");
		}
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EncontroPeriodo> listaPeriodos(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroPeriodo.porEncontro");
		cmd.addParameter("encontro", encontro);
		return cmd.call();
	}

}