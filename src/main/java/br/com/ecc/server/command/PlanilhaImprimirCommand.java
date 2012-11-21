package br.com.ecc.server.command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.Casal;
import br.com.ecc.model.Encontro;
import br.com.ecc.model.EncontroAtividade;
import br.com.ecc.model.EncontroAtividadeInscricao;
import br.com.ecc.model.EncontroInscricao;
import br.com.ecc.model.EncontroPeriodo;
import br.com.ecc.model.Papel;
import br.com.ecc.model.Usuario;
import br.com.ecc.model.tipo.TipoExibicaoPlanilhaEnum;
import br.com.ecc.model.tipo.TipoInscricaoEnum;
import br.com.ecc.model.vo.EncontroVO;
import br.com.ecc.model.vo.PlanilhaRelatorio;
import br.com.ecc.server.command.basico.ImprimirCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class PlanilhaImprimirCommand implements Callable<Integer>{

	@Inject Injector injector;
	@Inject EntityManager em;

	private Encontro encontro;
	private EncontroPeriodo encontroPeriodo; 
	private TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum;
	private Usuario usuarioAtual;
	private Casal casalAtual = null;
	private Boolean exportaExcel;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional
	public Integer call() throws Exception {
		SimpleDateFormat dfDia = new SimpleDateFormat("E");
		SimpleDateFormat dfEncontro = new SimpleDateFormat("MMMM yyyy");
		Query q;
		
		if(usuarioAtual!=null){
			q = em.createNamedQuery("casal.porPessoa");
			q.setParameter("pessoa", usuarioAtual.getPessoa());
			q.setMaxResults(1);
			List<Casal> lista = q.getResultList();
			if(lista.size()!=0){
				casalAtual = lista.get(0);
			}
		}
		
		EncontroCarregaVOCommand cmdVO = injector.getInstance(EncontroCarregaVOCommand.class);
		cmdVO.setEncontro(encontro);
		EncontroVO encontroVO = cmdVO.call();
		
		List<EncontroInscricao> lei = new ArrayList<EncontroInscricao>();
		for (EncontroInscricao ei : encontroVO.getListaInscricao()) {
			if(!ei.getTipo().equals(TipoInscricaoEnum.AFILHADO)){
				lei.add(ei);
			}
		}
		encontroVO.setListaInscricao(lei);
		
		EncontroPlanilhaCommand cmdEP = injector.getInstance(EncontroPlanilhaCommand.class);
		cmdEP.setEncontro(encontro);
		cmdEP.setEncontroPeriodo(encontroPeriodo);
		cmdEP.setTipoExibicaoPlanilhaEnum(tipoExibicaoPlanilhaEnum);
		cmdEP.setUsuarioAtual(usuarioAtual);
		List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao = cmdEP.call();
		
		Date inicio = null, fim = new Date(3000,1,1);
		if(encontroPeriodo!=null){
			boolean achou = false;
			inicio = encontroPeriodo.getInicio();
			for (EncontroPeriodo ep : encontroVO.getListaPeriodo()) {
				if(ep.getId().equals(encontroPeriodo.getId())){
					achou = true;
				}
				if(achou && ep.getInicio().after(inicio)){
					fim = ep.getInicio();
					break;
				}
			}
		}
		
		List<EncontroAtividade> listaEncontroAtividade = new ArrayList<EncontroAtividade>();
		List<EncontroInscricao> listaEncontroInscricao = new ArrayList<EncontroInscricao>();
		if(tipoExibicaoPlanilhaEnum.equals(TipoExibicaoPlanilhaEnum.COMPLETA)){
			listaEncontroInscricao = encontroVO.getListaInscricao();
			listaEncontroAtividade = encontroVO.getListaEncontroAtividade();
		} else if (tipoExibicaoPlanilhaEnum.equals(TipoExibicaoPlanilhaEnum.MINHA_ATIVIDADE_MINHA_COLUNA)){
			listaEncontroInscricao = montaListaEncontroInscricaoUsuarioAtual(encontroVO);
			listaEncontroAtividade = montaListaEncontroAtividadeusuarioAtual(listaEncontroAtividadeInscricao);
		} else if (tipoExibicaoPlanilhaEnum.equals(TipoExibicaoPlanilhaEnum.MINHA_ATIVIDADE_TODAS_COLUNAS)){
			listaEncontroInscricao = encontroVO.getListaInscricao();
			listaEncontroAtividade = montaListaEncontroAtividadeusuarioAtual(listaEncontroAtividadeInscricao);
		} else if (tipoExibicaoPlanilhaEnum.equals(TipoExibicaoPlanilhaEnum.TODAS_ATIVIDADES_MINHA_COLUNA)){
			listaEncontroInscricao = montaListaEncontroInscricaoUsuarioAtual(encontroVO);
			listaEncontroAtividade = encontroVO.getListaEncontroAtividade();
		}  
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("datas", dfEncontro.format(encontroVO.getEncontro().getInicio()));
		parametros.put("grupo", encontroVO.getEncontro().getGrupo().getNome());
		
		String legenda = "";
		q = em.createNamedQuery("papel.porGrupo");
		q.setParameter("grupo", encontroVO.getEncontro().getGrupo());
		List<Papel> listaPapel = q.getResultList();
		for (Papel papel : listaPapel) {
			if(!legenda.equals("")) legenda += " | ";
			legenda += papel.getSigla() + ": " + papel.getNome();
		}
		parametros.put("legenda", legenda);
		
		List<PlanilhaRelatorio> dadosRelatorio = new ArrayList<PlanilhaRelatorio>();
		PlanilhaRelatorio planilhaRelatorio;
		
		Collections.sort(listaEncontroAtividadeInscricao, new Comparator<EncontroAtividadeInscricao>() {
			@Override
			public int compare(EncontroAtividadeInscricao o1, EncontroAtividadeInscricao o2) {
				String s1, s2;
				if(o1.getEncontroInscricao().getCasal()!=null) s1 = o1.getEncontroInscricao().getCasal().getApelidos(null);
				else s1 = o1.getEncontroInscricao().getPessoa().getApelido();
				
				if(o2.getEncontroInscricao().getCasal()!=null) s2 = o2.getEncontroInscricao().getCasal().getApelidos(null);
				else s2 = o2.getEncontroInscricao().getPessoa().getApelido();
				return s1.compareTo(s2);
			}
		});
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
		Collections.sort(listaEncontroInscricao, new Comparator<EncontroInscricao>() {
			@Override
			public int compare(EncontroInscricao o1, EncontroInscricao o2) {
				String n1 = o1.getCasal()==null?o1.getPessoa().getApelido():o1.getCasal().getApelidos(null);
				String n2 = o2.getCasal()==null?o2.getPessoa().getApelido():o2.getCasal().getApelidos(null);
				return n1.compareTo(n2.toString());
			}
		});
		EncontroPeriodo periodo = null, periodoAnterior = null;
		if(encontroVO.getListaPeriodo().size()>0){
			periodo = encontroVO.getListaPeriodo().get(0);
			periodoAnterior = periodo;
		}
		
		String tipo = "";
		boolean achou = false, ok = false, padrinho = false, nos = false, minhaAtividade=false;
		int i=0;
		int participantes = 0;
		for (EncontroAtividade atividade : listaEncontroAtividade) {
			ok = true;
			if(encontroPeriodo!=null){
				ok = false;
				if(atividade.getInicio().compareTo(inicio)>=0 && atividade.getInicio().compareTo(fim)<0){
					ok = true;
				}
			}
			if(ok){
				if(periodo!=null){
					for (EncontroPeriodo p : encontroVO.getListaPeriodo()) {
						if(atividade.getInicio().compareTo(p.getInicio())>=0){
							periodo = p;
						}
					}
					if(encontroPeriodo==null && periodoAnterior!=null && !periodo.getId().equals(periodoAnterior.getId())){
						periodoAnterior = periodo;
					}
				}
				if(atividade.getTipoAtividade()!=null){
					tipo = atividade.getTipoAtividade().getNome();
				} else {
					tipo="";
				}
				planilhaRelatorio = new PlanilhaRelatorio();
				planilhaRelatorio.setPeriodo(periodo.getNome());
				planilhaRelatorio.setTipoAtividade(tipo);
				planilhaRelatorio.setAtividade(atividade.getAtividade().getNome());
				planilhaRelatorio.setDia(dfDia.format(atividade.getInicio()).toUpperCase());
				planilhaRelatorio.setInicio(atividade.getInicio());
				planilhaRelatorio.setFim(atividade.getFim());
				planilhaRelatorio.setParticipante(new ArrayList<String>());
				planilhaRelatorio.setPapel(new ArrayList<String>());
				planilhaRelatorio.setNos(new ArrayList<Boolean>());
				planilhaRelatorio.setPadrinho(new ArrayList<Boolean>());
				i=0;
				participantes = 0;
				minhaAtividade = false;
				for (EncontroInscricao inscricao : listaEncontroInscricao) {
					padrinho = false;
					nos = false;
					if(inscricao.getCasal()!=null){
						planilhaRelatorio.getParticipante().add(inscricao.getCasal().getApelidos("e"));
						if(inscricao.getTipo().equals(TipoInscricaoEnum.PADRINHO)){
							padrinho = true;
						}
						if(inscricao.getCasal().getId().equals(casalAtual.getId())){
							nos = true;
						}
					} else {
						planilhaRelatorio.getParticipante().add(inscricao.getPessoa().getApelido());
						if(inscricao.getPessoa().getId().equals(usuarioAtual.getPessoa().getId())){
							nos = true;
						}
					}
					achou = false;
					for (EncontroAtividadeInscricao eai : listaEncontroAtividadeInscricao) {
						if(eai.getEncontroAtividade().getId().equals(atividade.getId()) &&
						   eai.getEncontroInscricao().getId().equals(inscricao.getId())){
							planilhaRelatorio.getPapel().add(eai.getPapel().getSigla());
							
							if(inscricao.getCasal()!=null && inscricao.getCasal().getId().equals(casalAtual.getId())){
								minhaAtividade = true;
							} else if(inscricao.getPessoa()!=null && inscricao.getPessoa().getId().equals(usuarioAtual.getPessoa().getId())){
								minhaAtividade = true;
							}
							achou = true;
							break;
						}
					}
					if(!achou){
						planilhaRelatorio.getPapel().add(null);
					} else {
						participantes++;
					}
					planilhaRelatorio.getPadrinho().add(padrinho);
					planilhaRelatorio.getNos().add(nos);
					i++;
				}
				while(i<70){
					planilhaRelatorio.getParticipante().add(null);
					planilhaRelatorio.getPapel().add(null);
					planilhaRelatorio.getPadrinho().add(false);
					planilhaRelatorio.getNos().add(false);
					i++;
				}
				planilhaRelatorio.setMinhaAtividade(minhaAtividade);
				planilhaRelatorio.setQtdeParticipantes(participantes);
				dadosRelatorio.add(planilhaRelatorio);
			}
		}
		if(dadosRelatorio.size()>0){
			ImprimirCommand cmd = injector.getInstance(ImprimirCommand.class);
			cmd.setDadosRelatorio(dadosRelatorio);
			cmd.setParametros(parametros);
			cmd.setReportPathName("/relatorios/");
			cmd.setReportFileName("planilha.jrxml");
			cmd.setReportName("Planilha_"+new SimpleDateFormat("MMMM_yyyy").format(encontroVO.getEncontro().getInicio()));
			cmd.setExportarEXCEL(exportaExcel);
			return cmd.call();
		} else {
			throw new WebException("Nenhum dado encontrado");
		}
	}
	
	private List<EncontroAtividade> montaListaEncontroAtividadeusuarioAtual(List<EncontroAtividadeInscricao> listaEncontroAtividadeInscricao) {
		List<EncontroAtividade> listaAtividades = new ArrayList<EncontroAtividade>();
		for (EncontroAtividadeInscricao eai : listaEncontroAtividadeInscricao) {
			if(casalAtual!=null){
				if(eai.getEncontroInscricao().getCasal()!=null && casalAtual.getId().equals(eai.getEncontroInscricao().getCasal().getId())){
					listaAtividades.add(eai.getEncontroAtividade());
				}
			} else {
				if(eai.getEncontroInscricao().getPessoa()!=null && usuarioAtual.getPessoa().getId().equals(eai.getEncontroInscricao().getPessoa().getId())){
					listaAtividades.add(eai.getEncontroAtividade());
				}
			}
		}
		return listaAtividades;
	}

	private List<EncontroInscricao> montaListaEncontroInscricaoUsuarioAtual(EncontroVO encontroVO) {
		List<EncontroInscricao> listaEncontroInscricao = new ArrayList<EncontroInscricao>();
		for (EncontroInscricao encontroInscricao : encontroVO.getListaInscricao()) {
			if(encontroInscricao.getCasal()!=null){
				if(encontroInscricao.getCasal().getId().equals(casalAtual.getId())){
					listaEncontroInscricao.add(encontroInscricao);
					break;
				}
			} else {
				if(encontroInscricao.getPessoa().getId().equals(usuarioAtual.getPessoa().getId())){
					listaEncontroInscricao.add(encontroInscricao);
					break;
				}
			}
		}
		return listaEncontroInscricao;
	}
	
	public Encontro getEncontro() {
		return encontro;
	}
	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}
	public EncontroPeriodo getEncontroPeriodo() {
		return encontroPeriodo;
	}
	public void setEncontroPeriodo(EncontroPeriodo encontroPeriodo) {
		this.encontroPeriodo = encontroPeriodo;
	}
	public TipoExibicaoPlanilhaEnum getTipoExibicaoPlanilhaEnum() {
		return tipoExibicaoPlanilhaEnum;
	}
	public void setTipoExibicaoPlanilhaEnum(TipoExibicaoPlanilhaEnum tipoExibicaoPlanilhaEnum) {
		this.tipoExibicaoPlanilhaEnum = tipoExibicaoPlanilhaEnum;
	}
	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}
	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}
	public Boolean getExportaExcel() {
		return exportaExcel;
	}
	public void setExportaExcel(Boolean exportaExcel) {
		this.exportaExcel = exportaExcel;
	}
}