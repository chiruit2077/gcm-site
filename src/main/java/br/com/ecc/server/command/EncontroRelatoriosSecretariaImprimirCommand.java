package br.com.ecc.server.command;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.core.mvp.infra.exception.WebException;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.model.Encontro;
import br.com.ecc.server.command.basico.ImprimirCommand;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class EncontroRelatoriosSecretariaImprimirCommand implements Callable<Integer>{

	@Inject Injector injector;
	@Inject EntityManager em;

	@SuppressWarnings("rawtypes")
	private List listaObjects;
	private String titulo;
	private String report;
	private String nome;
	private Encontro encontro;

	@Override
	@Transactional
	public Integer call() throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("TITULO", getTitulo());
		ArquivoDigital logo = null;
		if (getEncontro() != null) logo = (ArquivoDigital)em.find(ArquivoDigital.class, getEncontro().getGrupo().getIdArquivoDigital());
		if (logo != null) parametros.put("LOGO", new ByteArrayInputStream(logo.getDados()));
		if(getListaObjects()!=null && getListaObjects().size()>0){
			ImprimirCommand cmd = injector.getInstance(ImprimirCommand.class);
			cmd.setDadosRelatorio(getListaObjects());
			cmd.setParametros(parametros);
			cmd.setReportPathName("/relatorios/");
			cmd.setReportFileName(getReport());
			cmd.setReportName(nome + "_" +new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime()));
			return cmd.call();
		} else {
			throw new WebException("Nenhum Registro Encontrado");
		}
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@SuppressWarnings("rawtypes")
	public List getListaObjects() {
		return listaObjects;
	}

	@SuppressWarnings("rawtypes")
	public void setListaObjects(List listaObjects) {
		this.listaObjects = listaObjects;
	}

	public Encontro getEncontro() {
		return encontro;
	}

	public void setEncontro(Encontro encontro) {
		this.encontro = encontro;
	}

}