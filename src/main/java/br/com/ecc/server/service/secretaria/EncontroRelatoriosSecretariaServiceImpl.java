package br.com.ecc.server.service.secretaria;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.client.service.secretaria.EncontroRelatoriosSecretariaService;
import br.com.ecc.model.Agrupamento;
import br.com.ecc.model.AgrupamentoMembro;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroHotel;
import br.com.ecc.model.EncontroHotelQuarto;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.Pessoa;
import br.com.ecc.model.tipo.TipoArquivoEnum;
import br.com.ecc.model.tipo.TipoInscricaoCasalEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.PlanilhaEncontroInscricaoVO;
import br.com.ecc.server.SecureRemoteServiceServlet;
import br.com.ecc.server.command.ArquivoDigitalSalvarCommand;
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
	public Integer imprimeRelatorioPlanilha(Encontro encontro, EncontroPeriodo periodo) throws Exception {
		EncontroPeriodo periodofim = null;
		String sql = "";
		String sqlatoa = "";
		if (periodo==null){
			sql = "select ele.apelido as ele, ela.apelido as ela, d.tipo, f.nome as papel, count(*) as qtde " +
				" from encontroatividade a, atividade b, encontroatividadeinscricao c, encontroinscricao d, casal e, pessoa ele, pessoa ela, papel f " +
				" where a.encontro = :encontro and " +
				" a.atividade = b.id and " +
				" a.id = c.encontroAtividade and d.id = c.encontroInscricao and " +
				" a.encontro = d.encontro and " +
				" d.casal = e.id and " +
				" e.ele = ele.id and " +
				" e.ela = ela.id and " +
				" f.id = c.papel " +
				" group by ele.apelido, ela.apelido, d.tipo, f.nome " +
				" order by d.tipo, ele.apelido, f.nome ";
			sqlatoa = "select ele, ela, tipo from ( select d.id, ele.apelido as ele, ela.apelido as ela, d.tipo " +
					" from encontroinscricao d, casal e, pessoa ele, pessoa ela " +
					" where " +
					" d.encontro = :encontro and " +
					" d.casal = e.id and  " +
					" e.ele = ele.id and  " +
					" e.ela = ela.id and d.tipo in ('APOIO', 'PADRINHO') and d.tipoConfirmacao = 'CONFIRMADO' " +
					" ) as x left outer join ( " +
					" select d.id as idaux " +
					" from encontroatividade a, atividade b, encontroatividadeinscricao c, encontroinscricao d, casal e, pessoa ele, pessoa ela, papel f " +
					" where a.encontro = :encontro and  " +
					" a.atividade = b.id and  " +
					" a.id = c.encontroAtividade and d.id = c.encontroInscricao and " +
					" a.encontro = d.encontro and " +
					" d.casal = e.id and " +
					" e.ele = ele.id and  " +
					" e.ela = ela.id and  " +
					" f.id = c.papel " +
					" group by ele.apelido, ela.apelido, d.tipo, f.nome " +
					" order by d.tipo, ele.apelido, f.nome)  " +
					" as teste on x.id = teste.idaux and teste.idaux is not null " +
					" where idaux is null " +
					" order by tipo, ele, ela ";
		}else{
			periodofim = getPeriodoFim(periodo);
			if (periodofim != null){
				sql = "select ele.apelido as ele, ela.apelido as ela, d.tipo, f.nome as papel, count(*) as qtde " +
						" from encontroatividade a, atividade b, encontroatividadeinscricao c, encontroinscricao d, casal e, pessoa ele, pessoa ela, papel f, encontroperiodo g, encontroperiodo h " +
						" where a.encontro = :encontro and " +
						" a.atividade = b.id and " +
						" a.id = c.encontroAtividade and d.id = c.encontroInscricao and " +
						" a.encontro = d.encontro and " +
						" d.casal = e.id and " +
						" e.ele = ele.id and " +
						" e.ela = ela.id and " +
						" f.id = c.papel and " +
						" a.encontro = g.encontro and " +
						" a.encontro = h.encontro and " +
						" a.inicio >= g.inicio and a.fim < h.inicio and g.id = :periodo and h.id = :periodofim " +
						" group by ele.apelido, ela.apelido, d.tipo, f.nome " +
						" order by d.tipo, ele.apelido, f.nome ";
				sqlatoa = "select ele, ela, tipo from ( select d.id, ele.apelido as ele, ela.apelido as ela, d.tipo " +
						" from encontroinscricao d, casal e, pessoa ele, pessoa ela " +
						" where " +
						" d.encontro = :encontro and " +
						" d.casal = e.id and  " +
						" e.ele = ele.id and  " +
						" e.ela = ela.id and d.tipo in ('APOIO', 'PADRINHO') and d.tipoConfirmacao = 'CONFIRMADO' " +
						" ) as x left outer join ( " +
						" select d.id as idaux " +
						" from encontroatividade a, atividade b, encontroatividadeinscricao c, encontroinscricao d, casal e, pessoa ele, pessoa ela, papel f, encontroperiodo g, encontroperiodo h " +
						" where a.encontro = :encontro and  " +
						" a.atividade = b.id and  " +
						" a.id = c.encontroAtividade and d.id = c.encontroInscricao and " +
						" a.encontro = d.encontro and " +
						" d.casal = e.id and " +
						" e.ele = ele.id and  " +
						" e.ela = ela.id and  " +
						" f.id = c.papel and  " +
						" a.encontro = g.encontro and  " +
						" a.encontro = h.encontro and  " +
						" a.inicio >= g.inicio and a.fim < h.inicio and g.id = :periodo and h.id = :periodofim " +
						" group by ele.apelido, ela.apelido, d.tipo, f.nome " +
						" order by d.tipo, ele.apelido, f.nome)  " +
						" as teste on x.id = teste.idaux and teste.idaux is not null " +
						" where idaux is null " +
						" order by tipo, ele, ela ";
			}else{
				sql = "select ele.apelido as ele, ela.apelido as ela, d.tipo, f.nome as papel, count(*) as qtde " +
						" from encontroatividade a, atividade b, encontroatividadeinscricao c, encontroinscricao d, casal e, pessoa ele, pessoa ela, papel f, encontroperiodo g " +
						" where a.encontro = :encontro and " +
						" a.atividade = b.id and " +
						" a.id = c.encontroAtividade and d.id = c.encontroInscricao and " +
						" a.encontro = d.encontro and " +
						" d.casal = e.id and " +
						" e.ele = ele.id and " +
						" e.ela = ela.id and " +
						" f.id = c.papel and " +
						" a.encontro = g.encontro and " +
						" a.inicio >= g.inicio and g.id = :periodo " +
						" group by ele.apelido, ela.apelido, d.tipo, f.nome " +
						" order by d.tipo, ele.apelido, f.nome ";
				sqlatoa = "select ele, ela, tipo from ( select d.id, ele.apelido as ele, ela.apelido as ela, d.tipo " +
						" from encontroinscricao d, casal e, pessoa ele, pessoa ela " +
						" where " +
						" d.encontro = :encontro and " +
						" d.casal = e.id and  " +
						" e.ele = ele.id and  " +
						" e.ela = ela.id and d.tipo in ('APOIO', 'PADRINHO') and d.tipoConfirmacao = 'CONFIRMADO' " +
						" ) as x left outer join ( " +
						" select d.id as idaux " +
						" from encontroatividade a, atividade b, encontroatividadeinscricao c, encontroinscricao d, casal e, pessoa ele, pessoa ela, papel f, encontroperiodo g " +
						" where a.encontro = :encontro and  " +
						" a.atividade = b.id and  " +
						" a.id = c.encontroAtividade and d.id = c.encontroInscricao and " +
						" a.encontro = d.encontro and " +
						" d.casal = e.id and " +
						" e.ele = ele.id and  " +
						" e.ela = ela.id and  " +
						" f.id = c.papel and  " +
						" a.encontro = g.encontro and  " +
						" a.inicio >= g.inicio and g.id = :periodo " +
						" group by ele.apelido, ela.apelido, d.tipo, f.nome " +
						" order by d.tipo, ele.apelido, f.nome)  " +
						" as teste on x.id = teste.idaux and teste.idaux is not null " +
						" where idaux is null " +
						" order by tipo, ele, ela ";
			}
		}
		List<PlanilhaEncontroInscricaoVO> listaCasal = new ArrayList<PlanilhaEncontroInscricaoVO>();
		Query query = em.createNativeQuery(sql);
		query.setParameter("encontro", encontro.getId());
		if (periodo!=null)
			query.setParameter("periodo", periodo.getId());
		if (periodofim != null)
			query.setParameter("periodofim", periodofim.getId());
		Map<String,Integer> count = new HashMap<String,Integer>();
		Map<String,Integer> qtde = new HashMap<String,Integer>();
		List<Object[]> resultList = query.getResultList();
		for (Object[] object : resultList) {
			PlanilhaEncontroInscricaoVO vo = new PlanilhaEncontroInscricaoVO();
			vo.setEle((String) object[0]);
			vo.setEla((String) object[1]);
			vo.setTipo((String) object[2]);
			vo.setPapel((String) object[3]);
			vo.setQtde(new Integer(((BigInteger) object[4]).intValue()));
			listaCasal.add(vo);
			Integer countaux = count.get(vo.getPapel());
			if (countaux != null) count.put(vo.getPapel(), countaux+vo.getQtde());
			else count.put(vo.getPapel(), vo.getQtde());
			Integer qtdeaux = qtde.get(vo.getPapel());
			if (qtdeaux != null) qtde.put(vo.getPapel(), qtdeaux+1);
			else qtde.put(vo.getPapel(), 1);
		}

		for (String papel : count.keySet()) {
			count.put(papel, count.get(papel) / qtde.get(papel));
		}

		for (PlanilhaEncontroInscricaoVO vo : listaCasal) {
			vo.setMedia(count.get(vo.getPapel()));
		}

		Query queryatoa = em.createNativeQuery(sqlatoa);
		queryatoa.setParameter("encontro", encontro.getId());
		if (periodo!=null)
			queryatoa.setParameter("periodo", periodo.getId());
		if (periodofim != null)
			queryatoa.setParameter("periodofim", periodofim.getId());
		List<Object[]> resultListAtoa = queryatoa.getResultList();
		for (Object[] object : resultListAtoa) {
			PlanilhaEncontroInscricaoVO vo = new PlanilhaEncontroInscricaoVO();
			vo.setEle((String) object[0]);
			vo.setEla((String) object[1]);
			vo.setTipo((String) object[2]);
			vo.setPapel("SEM ATIVIDADE");
			vo.setQtde(0);
			listaCasal.add(vo);
		}

		Collections.sort(listaCasal, new Comparator<PlanilhaEncontroInscricaoVO>() {
			@Override
			public int compare(PlanilhaEncontroInscricaoVO o1, PlanilhaEncontroInscricaoVO o2) {
				if (o1.getTipo() != null && o2.getTipo() != null ) return o1.getTipo().compareTo(o2.getTipo());
				if (o1.getTipo() == null && o2.getTipo() != null && !o2.getTipo().equals("") ) return -1;
				return o1.getEle().compareTo(o2.getEle());
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

	@SuppressWarnings("unchecked")
	private EncontroPeriodo getPeriodoFim(EncontroPeriodo periodo) {
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroPeriodo.porEncontro");
		cmd.addParameter("encontro", periodo.getEncontro());
		List<EncontroPeriodo> list = cmd.call();
		for (EncontroPeriodo encontroPeriodo : list) {
			if (periodo.getInicio().before(encontroPeriodo.getInicio()))
				return encontroPeriodo;
		}
		return null;
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
				if (o1.getRotulo() != null && o2.getRotulo() != null ) return o1.getRotulo().compareTo(o2.getRotulo());
				if (o1.getRotulo() == null && o2.getRotulo() != null && !o2.getRotulo().equals("") ) return -1;
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
			if (encontroInscricao.getCasal().getEle().getNecessidadesEspeciais() != null && !encontroInscricao.getCasal().getEle().getNecessidadesEspeciais().equals(""))
				listaPessoa.add(encontroInscricao.getCasal().getEle());
			if (encontroInscricao.getCasal().getEla().getNecessidadesEspeciais() != null && !encontroInscricao.getCasal().getEla().getNecessidadesEspeciais().equals(""))
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
			if (!encontroInscricao.getTipo().equals(TipoInscricaoEnum.EXTERNO)){
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
				if (o1.getTag() != null && o2.getTag() != null ) return o1.getTag().compareTo(o2.getTag());
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
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroConvidados");
		cmd.addParameter("encontro", encontro);
		List<EncontroInscricao> lista = cmd.call();
		GetEntityListCommand cmdQuarto = injector.getInstance(GetEntityListCommand.class);
		cmdQuarto.setNamedQuery("encontroHotelQuarto.porListaInscricao");
		cmdQuarto.addParameter("encontroinscricao1", lista);
		List<EncontroHotelQuarto> listaQuarto = cmdQuarto.call();

		Collections.sort(listaQuarto, new Comparator<EncontroHotelQuarto>() {
			@Override
			public int compare(EncontroHotelQuarto o1, EncontroHotelQuarto o2) {
				if (o1.getEncontroHotel().equals(o2.getEncontroHotel()))
					return o1.getQuarto().getNumeroQuarto().compareTo(o2.getQuarto().getNumeroQuarto());
				else
					return o1.getEncontroHotel().getId().compareTo(o2.getEncontroHotel().getId());
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
		GetEntityListCommand cmd = injector.getInstance(GetEntityListCommand.class);
		cmd.setNamedQuery("encontroInscricao.porEncontroEncontristas");
		cmd.addParameter("encontro", encontroHotel.getEncontro());
		List<EncontroInscricao> lista = cmd.call();
		GetEntityListCommand cmdQuarto = injector.getInstance(GetEntityListCommand.class);
		cmdQuarto.setNamedQuery("encontroHotelQuarto.porEncontroHotelListaInscricao");
		cmdQuarto.addParameter("encontrohotel", encontroHotel);
		cmdQuarto.addParameter("encontroinscricao1", lista);
		List<EncontroHotelQuarto> listaQuarto = cmdQuarto.call();

		Collections.sort(listaQuarto, new Comparator<EncontroHotelQuarto>() {
			@Override
			public int compare(EncontroHotelQuarto o1, EncontroHotelQuarto o2) {
				String num1 = ("0000000000" + o1.getQuarto().getNumeroQuarto());
				num1 = num1.substring(num1.length() - 10, num1.length());
				String num2 = ("0000000000" + o2.getQuarto().getNumeroQuarto());
				num2 = num2.substring(num2.length() - 10, num2.length());
				if (o1.getEncontroHotel().equals(o2.getEncontroHotel()))
					return num1.compareTo(num2);
				else
					return o1.getEncontroHotel().getId().compareTo(o2.getEncontroHotel().getId());
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

	@Override
	public Integer imprimeRelatorioRecepcaoInicial(Encontro encontro)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer imprimeRelatorioRecepcaoFinal(Encontro encontro)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}