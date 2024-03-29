package br.com.ecc.server.service.secretaria;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ecc.client.service.secretaria.EncontroRelatoriosSecretariaService;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.model.Casal;
import br.com.ecc.model.CasalContato;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroHotelQuarto;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.EncontroRestaurante;
import br.com.ecc.model.EncontroRestauranteMesa;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.tipo.TipoArquivoEnum;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.EncontroVO;
import br.com.ecc.model.vo.PlanilhaEncontroInscricaoVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.command.ArquivoDigitalSalvarCommand;
import br.com.ecc.server.command.EncontroCarregaVOCommand;
import br.com.ecc.server.command.EncontroRelatoriosSecretariaImprimirCommand;
import br.com.ecc.server.command.basico.GetEntityListCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class EncontroRelatoriosSecretariaServiceImpl extends SecureRemoteServiceServlet implements EncontroRelatoriosSecretariaService {

	private static final long serialVersionUID = 895888786302857306L;
	@Inject Injector injector;
	@Inject EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioRomantico(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Casal> listaCasal = new ArrayList<Casal>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaCasal.add(encontroInscricao.getCasal());
		}
		Collections.sort(listaCasal, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getEle().getApelido().compareTo(o2.getEle().getApelido());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaCasal);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemromantico.jrxml");
		cmdRelatorio.setNome("listagemromantico");
		cmdRelatorio.setTitulo("LISTAGEM FILA DO RESTAURANTE BISTRO DO AMOR");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioAlbum(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Casal> listaCasal = new ArrayList<Casal>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaCasal.add(encontroInscricao.getCasal());
		}
		Collections.sort(listaCasal, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getEle().getApelido().compareTo(o2.getEle().getApelido());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaCasal);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemalbum.jrxml");
		cmdRelatorio.setNome("listagemalbum");
		cmdRelatorio.setTitulo("LISTAGEM DE ENTREGA DOS ÁLBUNS");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioEntregaCesta(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Casal> listaCasal = new ArrayList<Casal>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaCasal.add(encontroInscricao.getCasal());
		}
		Collections.sort(listaCasal, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getEle().getApelido().compareTo(o2.getEle().getApelido());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaCasal);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemalbum.jrxml");
		cmdRelatorio.setNome("listagemalbum");
		cmdRelatorio.setTitulo("LISTAGEM DE ENTREGA DAS CESTAS");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioMalas(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Casal> listaCasal = new ArrayList<Casal>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaCasal.add(encontroInscricao.getCasal());
		}
		Collections.sort(listaCasal, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getEle().getApelido().compareTo(o2.getEle().getApelido());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaCasal);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemmalas.jrxml");
		cmdRelatorio.setNome("listagemalas");
		cmdRelatorio.setTitulo("LISTAGEM DE ENTREGA DE MALAS");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioPlanilha(Encontro encontro, EncontroPeriodo periodo, EncontroInscricao inscricao) throws Exception {
		EncontroCarregaVOCommand cmd = injector.getInstance(EncontroCarregaVOCommand.class);
		cmd.setEncontro(encontro);
		EncontroVO vo =  cmd.call();



		GetEntityListCommand cmdList = injector.getInstance(GetEntityListCommand.class);
		cmdList.setNamedQuery("encontroAtividadeInscricao.porEncontro");
		cmdList.addParameter("encontro", encontro);
		List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao = cmdList.call();

		List<EncontroInscricao> listaEncontroInscricao = new ArrayList<EncontroInscricao>();
		if (inscricao==null){
			listaEncontroInscricao.addAll(vo.getListaInscricao());
		}else{
			listaEncontroInscricao.add(inscricao);
		}

		List<EncontroAtividade> listaEncontroAtividade = montaListaAtividadesPorPeriodoSelecionado(vo, periodo);
		Collections.sort(listaEncontroAtividade, new Comparator<EncontroAtividade>() {
			@Override
			public int compare(EncontroAtividade o1, EncontroAtividade o2) {
				if(o1.getInicio().equals(o2.getInicio())){
					if(o1.getFim().equals(o2.getFim())){
						return o1.getAtividade().getNome().compareTo(o2.getAtividade().getNome());
					}
					return o1.getFim().compareTo(o2.getFim());
				}
				return o1.getInicio().compareTo(o2.getInicio());
			}
		});

		List<PlanilhaEncontroInscricaoVO> listaCasal = new ArrayList<PlanilhaEncontroInscricaoVO>();

		for (EncontroAtividade ea : listaEncontroAtividade) {
			ea.getEncontroAtividadeInscricoes().clear();
			for (EncontroAtividadeInscricao eai : listaEncontroAtividadeInscricao) {
				if (eai.getEncontroAtividade().getId().equals(ea.getId()))
					ea.getEncontroAtividadeInscricoes().add(eai);
			}
		}

		for (EncontroInscricao ei : listaEncontroInscricao) {
			ei.setQtdeAtividades(0);
			if(!ei.getTipo().equals(TipoInscricaoEnum.AFILHADO) && ((inscricao!=null) ||
					((inscricao == null) && !ei.getTipo().equals(TipoInscricaoEnum.COORDENADOR) && !ei.getTipo().equals(TipoInscricaoEnum.EXTERNO) && !ei.getTipo().equals(TipoInscricaoEnum.DOACAO) ))){
				for (EncontroAtividade ea : listaEncontroAtividade) {
					String atividade = (new SimpleDateFormat("E")).format(ea.getInicio()) + " " +
							(new SimpleDateFormat("HH:mm")).format(ea.getInicio()) + " " + (new SimpleDateFormat("HH:mm")).format(ea.getFim()) + " " + ea.getTipoAtividade().getNome() + " - " + ea.getAtividade().getNome();
					boolean coordenacao = false;
					for (EncontroAtividadeInscricao eai : ea.getEncontroAtividadeInscricoes()) {
						if (eai.getEncontroInscricao().getId().equals(ei.getId())){
							PlanilhaEncontroInscricaoVO vop = new PlanilhaEncontroInscricaoVO();
							vop.setTipo(ei.getTipo().toString());
							vop.setNome(ei.toStringApelidos());
							vop.setEncontroInscricao(ei);
							vop.setEncontroAtividade(ea);
							vop.setAtividade(atividade);
							vop.setEncontroAtividadeInscricao(eai);
							vop.setParticipante(eai.getPapel().getNome());
							coordenacao = eai.getPapel().getCoordenacao();
							vop.setCoordenacao(coordenacao);
							listaCasal.add(vop);
							ei.setQtdeAtividades(ei.getQtdeAtividades()+1);
							break;
						}
					}
					if (coordenacao){
						for (EncontroAtividadeInscricao eai : ea.getEncontroAtividadeInscricoes()) {
							if (!eai.getEncontroInscricao().getId().equals(ei.getId())){
								PlanilhaEncontroInscricaoVO vop = new PlanilhaEncontroInscricaoVO();
								vop.setTipo(ei.getTipo().toString());
								vop.setNome(ei.toStringApelidos());
								vop.setEncontroInscricao(ei);
								vop.setEncontroAtividade(ea);
								vop.setAtividade(atividade);
								vop.setEncontroAtividadeInscricao(eai);
								vop.setParticipante(eai.getPapel().getNome() + " - " + eai.getEncontroInscricao().toStringApelidos());
								vop.setCoordenacao(coordenacao);
								listaCasal.add(vop);
							}
						}
					}
				}
			}
		}

		Collections.sort(listaCasal, new Comparator<PlanilhaEncontroInscricaoVO>() {
			@Override
			public int compare(PlanilhaEncontroInscricaoVO o1, PlanilhaEncontroInscricaoVO o2) {
				if (!o1.getNome().equals(o2.getNome())) return o1.getNome().compareTo(o2.getNome());
				else if (!o1.getEncontroAtividade().getId().equals(o2.getEncontroAtividade().getId())) {
					if(o1.getEncontroAtividade().getInicio().equals(o2.getEncontroAtividade().getInicio())){
						if(o1.getEncontroAtividade().getFim().equals(o2.getEncontroAtividade().getFim())){
							return o1.getEncontroAtividade().getAtividade().getNome().compareTo(o2.getEncontroAtividade().getAtividade().getNome());
						}
						return o1.getEncontroAtividade().getFim().compareTo(o2.getEncontroAtividade().getFim());
					}
					return o1.getEncontroAtividade().getInicio().compareTo(o2.getEncontroAtividade().getInicio());
				}
				else if (!o1.getParticipante().equals(o2.getParticipante())) {
					if (o1.getEncontroInscricao().equals(o2.getEncontroAtividadeInscricao().getEncontroInscricao()))
						return 0;
					else
						return o1.getParticipante().compareTo(o2.getParticipante());
				}
				else return 0;
			}
		});


		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaCasal);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemplanilha.jrxml");
		cmdRelatorio.setNome("listagemplanilha");
		cmdRelatorio.setTitulo("LISTAGEM PLANILHA ATRIBUIÇÕES POR CASAL");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("deprecation")
	private List<EncontroAtividade> montaListaAtividadesPorPeriodoSelecionado(EncontroVO vo, EncontroPeriodo periodo){
		if(periodo!=null){
			List<EncontroAtividade> listaEncontroAtividadePeriodo = new ArrayList<EncontroAtividade>();
			Date inicio = null, fim = new Date(3000,1,1);
			boolean achou = false;
			inicio = periodo.getInicio();
			List<EncontroPeriodo> listaPeriodo = vo.getListaPeriodo();
			Collections.sort(listaPeriodo, new Comparator<EncontroPeriodo>() {
				@Override
				public int compare(EncontroPeriodo o1, EncontroPeriodo o2) {
					return o1.getInicio().compareTo(o2.getInicio());
				}
			});
			for (EncontroPeriodo ep : listaPeriodo) {
				if(ep.getId().equals(periodo.getId())){
					achou = true;
				}
				if(achou && ep.getInicio().after(inicio)){
					fim = ep.getInicio();
					break;
				}
			}

			for (EncontroAtividade ea : vo.getListaEncontroAtividade()) {
				if ((ea.getInicio().compareTo(inicio)>=0) && (ea.getInicio().compareTo(fim)<0)){
					listaEncontroAtividadePeriodo.add(ea);
				}
			}
			return listaEncontroAtividadePeriodo;

		}else{
			return vo.getListaEncontroAtividade();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioOracaoAmor(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		GetEntityListCommand cmdQuarto = injector.getInstance(GetEntityListCommand.class);
		cmdQuarto.setNamedQuery("encontroHotelQuarto.porEncontroHotelListaInscricao");
		cmdQuarto.addParameter("encontroinscricao1", lista);
		List<EncontroHotelQuarto> listaQuarto = cmdQuarto.call();

		Collections.sort(listaQuarto, new Comparator<EncontroHotelQuarto>() {
			@Override
			public int compare(EncontroHotelQuarto o1, EncontroHotelQuarto o2) {
				if (o1.getEncontroHotel().equals(o2.getEncontroHotel()))
					return -1*o1.getQuarto().getNumeroQuarto().compareTo(o2.getQuarto().getNumeroQuarto());
				else
					return o1.getEncontroHotel().getId().compareTo(o2.getEncontroHotel().getId());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaQuarto);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemoracaoamor.jrxml");
		cmdRelatorio.setNome("listagemoracaoamor");
		cmdRelatorio.setTitulo("LISTAGEM DA ORAÇÃO DO AMOR");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioOnibus(Encontro encontro, Agrupamento agrupamento) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaPessoa.add(encontroInscricao.getCasal().getEle());
			listaPessoa.add(encontroInscricao.getCasal().getEla());
		}
		if (agrupamento != null){
			GetEntityListCommand cmdAgrupamento = injector.getInstance(GetEntityListCommand.class);
			cmdAgrupamento.setNamedQuery("agrupamentoMembro.porAgrupamento");
			cmdAgrupamento.addParameter("agrupamento", agrupamento);
			List<AgrupamentoMembro> listaMenbro = cmdAgrupamento.call();
			for (AgrupamentoMembro agrupamentoMembro : listaMenbro) {
				if (agrupamentoMembro.getCasal() != null ) {
					listaPessoa.add(agrupamentoMembro.getCasal().getEle());
					listaPessoa.add(agrupamentoMembro.getCasal().getEla());
				}
				if (agrupamentoMembro.getPessoa() != null ) {
					listaPessoa.add(agrupamentoMembro.getPessoa());
				}
			}

		}
		Collections.sort(listaPessoa, new Comparator<Pessoa>() {
			@Override
			public int compare(Pessoa o1, Pessoa o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaPessoa);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemonibus.jrxml");
		cmdRelatorio.setNome("listagemonibus");
		cmdRelatorio.setTitulo("LISTAGEM EMPRESA DE ÔNIBUS");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioAfilhadosPadrinhos(Encontro encontro) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Casal> listaCasal = new ArrayList<Casal>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaCasal.add(encontroInscricao.getCasal());
		}
		Collections.sort(listaCasal, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getEle().getApelido().compareTo(o2.getEle().getApelido());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaCasal);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemafilhados.jrxml");
		cmdRelatorio.setNome("listagemafilhados");
		cmdRelatorio.setTitulo("LISTAGEM AFILHADOS E PADRINHOS");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioAgrupamento(Encontro encontro, Agrupamento agrupamento) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("agrupamentoMembro.porAgrupamento");
		cmd.addParameter("agrupamento", agrupamento);
		List<AgrupamentoMembro> lista = cmd.call();
		Collections.sort(lista, new Comparator<AgrupamentoMembro>() {
			@Override
			public int compare(AgrupamentoMembro o1, AgrupamentoMembro o2) {
				if ((o1.getRotulo() != null) && (o2.getRotulo() != null) ) return o1.getRotulo().compareTo(o2.getRotulo());
				if ((o1.getRotulo() == null) && (o2.getRotulo() != null) && !o2.getRotulo().equals("") ) return -1;
				return o1.getCasal().getApelidos("e").compareTo(o2.getCasal().getApelidos("e"));
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(lista);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemagrupamento.jrxml");
		cmdRelatorio.setNome("listagemagrupamento");
		cmdRelatorio.setTitulo("LISTAGEM AGRUPAMENTOS - " + agrupamento.getNome() );
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer geraCSVCorel(Encontro encontro, String name) throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();

		if (lista.size()>0){

			String dados = "Codigo;Ele;Ela;Ele Completo;Ela Completo;Padrinho;Madrinha;Quarto;";

			for (EncontroInscricao encontroInscricao : lista) {
				dados+= "\n" + encontroInscricao.getCodigo() + ";";
				dados+= encontroInscricao.getCasal().getEle().getApelido().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getEla().getApelido().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getEle().getNome().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getEla().getNome().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getCasalPadrinho().getEle().getNome().toUpperCase() + ";" ;
				dados+= encontroInscricao.getCasal().getCasalPadrinho().getEla().getNome().toUpperCase() + ";" ;
				GetEntityListCommand cmdQuarto = injector.getInstance(GetEntityListCommand.class);
				cmdQuarto.setNamedQuery("encontroHotelQuarto.porEncontroHotelInscricao");
				cmdQuarto.addParameter("encontro", encontro);
				cmdQuarto.addParameter("encontroinscricao1", encontroInscricao);
				List<EncontroHotelQuarto> quartos = cmdQuarto.call();
				if (quartos.size()==1){
					dados+= quartos.get(0).getQuarto().getNumeroQuarto().toUpperCase() + ";" ;
				}else
					dados+= ";";
			}

			ArquivoDigitalSalvarCommand cmdArquivo = injector.getInstance(ArquivoDigitalSalvarCommand.class);
			ArquivoDigital arquivoDigital = new ArquivoDigital();
			arquivoDigital.setNomeArquivo(name);
			arquivoDigital.setDados(dados.getBytes());
			arquivoDigital.setMimeType("text/csv");
			arquivoDigital.setTipo(TipoArquivoEnum.ARQUIVO);
			arquivoDigital.setTamanho(arquivoDigital.getDados().length);
			cmdArquivo.setArquivoDigital(arquivoDigital);
			ArquivoDigital call = cmdArquivo.call();
			return call.getId();
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Integer geraCSVCrachas(Encontro encontro, Agrupamento agrupamento, String name) throws Exception {
		if (encontro != null){
			GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
			cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
			cmd.addParameter("encontro", encontro);
			List<EncontroInscricao> lista = cmd.call();

			if (lista.size()>0){
				String dados = "ELE;;ELA;ELA;;ELE;";

				for (EncontroInscricao encontroInscricao : lista) {
					dados+= "\n" + encontroInscricao.getCasal().getEle().getApelido().toUpperCase() + ";" ;
					dados+= "DA;" ;
					dados+= encontroInscricao.getCasal().getEla().getApelido().toUpperCase() + ";" ;
					dados+= encontroInscricao.getCasal().getEla().getApelido().toUpperCase() + ";" ;
					dados+= "DO;" ;
					dados+= encontroInscricao.getCasal().getEle().getApelido().toUpperCase() + ";" ;
				}

				ArquivoDigitalSalvarCommand cmdArquivo = injector.getInstance(ArquivoDigitalSalvarCommand.class);
				ArquivoDigital arquivoDigital = new ArquivoDigital();
				arquivoDigital.setNomeArquivo(name);
				arquivoDigital.setDados(dados.getBytes());
				arquivoDigital.setMimeType("text/csv");
				arquivoDigital.setTipo(TipoArquivoEnum.ARQUIVO);
				arquivoDigital.setTamanho(arquivoDigital.getDados().length);
				cmdArquivo.setArquivoDigital(arquivoDigital);
				ArquivoDigital call = cmdArquivo.call();
				return call.getId();
			}
		}else if (agrupamento != null){
			GetEntityListCommand cmdAgrupamento = injector.getInstance(GetEntityListCommand.class);
			cmdAgrupamento.setNamedQuery("agrupamentoMembro.porAgrupamento");
			cmdAgrupamento.addParameter("agrupamento", agrupamento);
			List<AgrupamentoMembro> listaMenbro = cmdAgrupamento.call();
			if (listaMenbro.size()>0){
				String dados = "ELE;;ELA;ELA;;ELE;";

				for (AgrupamentoMembro agrupamentoMembro : listaMenbro) {
					if (agrupamentoMembro.getCasal() != null ) {
						dados+= "\n" + agrupamentoMembro.getCasal().getEle().getApelido().toUpperCase() + ";" ;
						dados+= "DA;" ;
						dados+= agrupamentoMembro.getCasal().getEla().getApelido().toUpperCase() + ";" ;
						dados+= agrupamentoMembro.getCasal().getEla().getApelido().toUpperCase() + ";" ;
						dados+= "DO;" ;
						dados+= agrupamentoMembro.getCasal().getEle().getApelido().toUpperCase() + ";" ;
					}
					if (agrupamentoMembro.getPessoa() != null ) {
						dados+= "\n" + agrupamentoMembro.getPessoa().getApelido().toUpperCase() + ";" ;
						dados+= ";;;;;" ;
					}
				}

				ArquivoDigitalSalvarCommand cmdArquivo = injector.getInstance(ArquivoDigitalSalvarCommand.class);
				ArquivoDigital arquivoDigital = new ArquivoDigital();
				arquivoDigital.setNomeArquivo(name);
				arquivoDigital.setDados(dados.getBytes());
				arquivoDigital.setMimeType("text/csv");
				arquivoDigital.setTipo(TipoArquivoEnum.ARQUIVO);
				arquivoDigital.setTamanho(arquivoDigital.getDados().length);
				cmdArquivo.setArquivoDigital(arquivoDigital);
				ArquivoDigital call = cmdArquivo.call();
				return call.getId();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioNecessidadesEspeciais(Encontro encontro)	throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		for (EncontroInscricao encontroInscricao : lista) {
			if ((encontroInscricao.getCasal().getEle().getNecessidadesEspeciais() != null) && !encontroInscricao.getCasal().getEle().getNecessidadesEspeciais().equals(""))
				listaPessoa.add(encontroInscricao.getCasal().getEle());
			if ((encontroInscricao.getCasal().getEla().getNecessidadesEspeciais() != null) && !encontroInscricao.getCasal().getEla().getNecessidadesEspeciais().equals(""))
				listaPessoa.add(encontroInscricao.getCasal().getEla());
		}
		Collections.sort(listaPessoa, new Comparator<Pessoa>() {
			@Override
			public int compare(Pessoa o1, Pessoa o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaPessoa);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemnecessidadesespeciais.jrxml");
		cmdRelatorio.setNome("listagemnecessidadesespeciais");
		cmdRelatorio.setTitulo("LISTAGEM DE AFILHADOS COM NECESSIDADES ESPECIAIS");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioDiabeticosVegetarianos(Encontro encontro)
			throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConfirmados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		int qtdeVegetarianos = 0;
		int qtdeDiabeticos = 0;
		for (EncontroInscricao encontroInscricao : lista) {
			if (!encontroInscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO) && !encontroInscricao.getTipo().equals(TipoInscricaoEnum.DOACAO)){
				if (encontroInscricao.getCasal()!=null){
					if (encontroInscricao.getCasal().getEle().getVegetariano() || encontroInscricao.getCasal().getEle().getDiabetico()){
						encontroInscricao.getCasal().getEle().setTag(TipoInscricaoCasalEnum.getPorInscricaoCasal(encontroInscricao.getTipo()).getNome());
						listaPessoa.add(encontroInscricao.getCasal().getEle());
						if (encontroInscricao.getCasal().getEle().getVegetariano()) qtdeVegetarianos++;
						if (encontroInscricao.getCasal().getEle().getDiabetico()) qtdeDiabeticos++;
					}
					if (encontroInscricao.getCasal().getEla().getVegetariano() || encontroInscricao.getCasal().getEla().getDiabetico()){
						encontroInscricao.getCasal().getEla().setTag(TipoInscricaoCasalEnum.getPorInscricaoCasal(encontroInscricao.getTipo()).getNome());
						listaPessoa.add(encontroInscricao.getCasal().getEla());
						if (encontroInscricao.getCasal().getEla().getVegetariano()) qtdeVegetarianos++;
						if (encontroInscricao.getCasal().getEla().getDiabetico()) qtdeDiabeticos++;
					}
				}else if (encontroInscricao.getPessoa()!=null){
					if (encontroInscricao.getPessoa().getVegetariano() || encontroInscricao.getPessoa().getDiabetico()){
						encontroInscricao.getPessoa().setTag(TipoInscricaoCasalEnum.getPorInscricaoCasal(encontroInscricao.getTipo()).getNome());
						listaPessoa.add(encontroInscricao.getPessoa());
						if (encontroInscricao.getPessoa().getVegetariano()) qtdeVegetarianos++;
						if (encontroInscricao.getPessoa().getDiabetico()) qtdeDiabeticos++;
					}
				}
			}
		}
		Collections.sort(listaPessoa, new Comparator<Pessoa>() {
			@Override
			public int compare(Pessoa o1, Pessoa o2) {
				if ((o1.getTag() != null) && (o2.getTag() != null) ) return o1.getTag().compareTo(o2.getTag());
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaPessoa);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemdiabeticosvegetarianos.jrxml");
		cmdRelatorio.setNome("listagemdiabeticosvegetarianos");
		cmdRelatorio.setTitulo("LISTAGEM DE DIABÉTICOS E VEGETARIANOS");
		cmdRelatorio.getParametros().put("totalVegetarianos", qtdeVegetarianos);
		cmdRelatorio.getParametros().put("totalDiabeticos", qtdeDiabeticos);
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioHotelAfilhados(Encontro encontro) throws Exception {
		GetEntityListCommand cmdQuarto = injector.getInstance(GetEntityListCommand.class);
		cmdQuarto.setNamedQuery("encontroHotelQuarto.porEncontroHotelAfilhados");
		cmdQuarto.addParameter("encontro", encontro);
		List<EncontroHotelQuarto> listaQuarto = cmdQuarto.call();

		Collections.sort(listaQuarto, new Comparator<EncontroHotelQuarto>() {
			@Override
			public int compare(EncontroHotelQuarto o1, EncontroHotelQuarto o2) {
				String num1 = ("0000000000" + o1.getQuarto().getNumeroQuarto());
				num1 = num1.substring(num1.length() - 10, num1.length());
				String num2 = ("0000000000" + o2.getQuarto().getNumeroQuarto());
				num2 = num2.substring(num2.length() - 10, num2.length());
				return num1.compareTo(num2);
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaQuarto);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemhotelafilhados.jrxml");
		cmdRelatorio.setNome("listagemhotelafilhados");
		cmdRelatorio.setTitulo("LISTAGEM AFILHADOS HOTEL");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioHotelEncontristas(EncontroHotel encontroHotel)
			throws Exception {
		GetEntityListCommand cmdQuarto = injector.getInstance(GetEntityListCommand.class);
		cmdQuarto.setNamedQuery("encontroHotelQuarto.porEncontroHotelEncontristas");
		cmdQuarto.addParameter("encontrohotel", encontroHotel);
		List<EncontroHotelQuarto> listaQuarto = cmdQuarto.call();

		Collections.sort(listaQuarto, new Comparator<EncontroHotelQuarto>() {
			@Override
			public int compare(EncontroHotelQuarto o1, EncontroHotelQuarto o2) {
				String num1 = ("0000000000" + o1.getQuarto().getNumeroQuarto());
				num1 = num1.substring(num1.length() - 10, num1.length());
				String num2 = ("0000000000" + o2.getQuarto().getNumeroQuarto());
				num2 = num2.substring(num2.length() - 10, num2.length());
				return num1.compareTo(num2);
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaQuarto);
		cmdRelatorio.setEncontro(encontroHotel.getEncontro());
		cmdRelatorio.setReport("listagemhotelencontristas.jrxml");
		cmdRelatorio.setNome("listagemhotelencontristas");
		cmdRelatorio.setTitulo("LISTAGEM ENCONTRISTAS HOTEL - " + encontroHotel);
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioHotelEncontristasHospedagemParticular(Encontro encontro)
			throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroHospedagemParticular");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Pessoa> listaPessoa = new ArrayList<Pessoa>();
		for (EncontroInscricao encontroInscricao : lista) {
			if (encontroInscricao.getCasal() != null ) {
				listaPessoa.add(encontroInscricao.getCasal().getEle());
				listaPessoa.add(encontroInscricao.getCasal().getEla());
			}
			if (encontroInscricao.getPessoa() != null ) {
				listaPessoa.add(encontroInscricao.getPessoa());
			}
		}


		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaPessoa);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemhotelencontristashp.jrxml");
		cmdRelatorio.setNome("listagemhotelencontristashp");
		cmdRelatorio.setTitulo("LISTAGEM ENCONTRISTAS HOSPEDAGEM PARTICULAR ");
		return cmdRelatorio.call();
	}


	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioCamarim(EncontroRestaurante restaurante)
			throws Exception {
		List<EncontroInscricao> lista = new ArrayList<EncontroInscricao>();
		if (restaurante.getEncontroMaitre() != null)
			lista.add(restaurante.getEncontroMaitre());
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroRestauranteMesa.porEncontroRestaurante");
		cmd.addParameter("encontrorestaurante", restaurante);
		List<EncontroRestauranteMesa> mesa = cmd.call();
		for (EncontroRestauranteMesa encontroRestauranteMesa : mesa) {
			if (encontroRestauranteMesa.getEncontroGarcon() != null)
				lista.add(encontroRestauranteMesa.getEncontroGarcon());
		}

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(lista);
		cmdRelatorio.setEncontro(restaurante.getEncontro());
		cmdRelatorio.setReport("listagemcamarim.jrxml");
		cmdRelatorio.setNome("listagemcamarim");
		cmdRelatorio.setTitulo("LISTAGEM CAMARIM - RESTAURANTE - " + restaurante.getRestaurante().getNome());
		return cmdRelatorio.call();
	}

	@Override
	public Integer imprimeRelatorioRecepcaoInicial(Encontro encontro)
			throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		@SuppressWarnings("unchecked")
		List<EncontroInscricao> lista = cmd.call();
		List<Casal> listaCasal = new ArrayList<Casal>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaCasal.add(encontroInscricao.getCasal());
		}
		Collections.sort(listaCasal, new Comparator<Casal>() {
			@Override
			public int compare(Casal o1, Casal o2) {
				return o1.getEle().getApelido().compareTo(o2.getEle().getApelido());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaCasal);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemrecepcaoinicial.jrxml");
		cmdRelatorio.setNome("listagemrecepcaoinicial");
		cmdRelatorio.setTitulo("LISTAGEM DE RECEPÇÃO INICIAL");
		return cmdRelatorio.call();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer imprimeRelatorioRecepcaoFinal(Encontro encontro)
			throws Exception {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		List<Casal> listaCasal = new ArrayList<Casal>();
		for (EncontroInscricao encontroInscricao : lista) {
			listaCasal.add(encontroInscricao.getCasal());
		}

		GetEntityListCommand cmdContato = injector.getInstance(GetEntityListCommand.class);
		cmdContato.setNamedQuery("casalContato.porListaCasal");
		cmdContato.addParameter("casal", listaCasal);
		List<CasalContato> listaContato = cmdContato.call();


		Collections.sort(listaContato, new Comparator<CasalContato>() {
			@Override
			public int compare(CasalContato o1, CasalContato o2) {
				return o1.getCasal().getEle().getApelido().compareTo(o2.getCasal().getEle().getApelido());
			}
		});

		EncontroRelatoriosSecretariaImprimirCommand cmdRelatorio = injector.getInstance(EncontroRelatoriosSecretariaImprimirCommand.class);
		cmdRelatorio.setListaObjects(listaContato);
		cmdRelatorio.setEncontro(encontro);
		cmdRelatorio.setReport("listagemrecepcaofinal.jrxml");
		cmdRelatorio.setNome("listagemrecepcaofinal");
		cmdRelatorio.setTitulo("LISTAGEM DE RECEPÇÃO FINAL");
		return cmdRelatorio.call();
	}

}