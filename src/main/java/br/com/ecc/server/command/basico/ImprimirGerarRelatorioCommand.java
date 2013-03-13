package br.com.ecc.server.command.basico;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.ecc.server.ResourceLoaderUtil;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ImprimirGerarRelatorioCommand implements Callable<JasperPrint>{

	@Inject EntityManager em;

	private Collection<?> dadosRelatorio;
	Map<String, Object> parametros;
	private String reportPathName;
	private String reportFileName;

	@Override
	@Transactional
	public JasperPrint call() throws Exception {
		JasperPrint jasperPrint = null;
		if(parametros == null) {
			parametros = new HashMap<String, Object>();
		}
		//parametros globais
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR")); 
		
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(ResourceLoaderUtil.loadResourceToInputStream(reportPathName, reportFileName));
			
			jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JRBeanCollectionDataSource(dadosRelatorio));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jasperPrint;
	}

	public Collection<?> getDadosRelatorio() {
		return dadosRelatorio;
	}
	public void setDadosRelatorio(Collection<?> dadosRelatorio) {
		this.dadosRelatorio = dadosRelatorio;
	}
	public Map<String, Object> getParametros() {
		return parametros;
	}
	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	public String getReportPathName() {
		return reportPathName;
	}
	public void setReportPathName(String reportPathName) {
		this.reportPathName = reportPathName;
	}
	public String getReportFileName() {
		return reportFileName;
	}
	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}
}