package br.com.ecc.server.command.basico;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import br.com.ecc.client.util.StringUtil;
import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.model.tipo.TipoArquivoEnum;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;

public class ImprimirCommand implements Callable<Integer>{

	@Inject EntityManager em;
	@Inject Injector injector;

	private Collection<?> dadosRelatorio;
	private Map<?,?> mapDadosRelatorio;
	Map<String, Object> parametros;
	private String reportPathName;
	private String reportFileName;
	private String reportName;
	private boolean exportarEXCEL;

	@Override
	@Transactional
	public Integer call() throws Exception {
		ImprimirGerarRelatorioCommand cmd = injector.getInstance(ImprimirGerarRelatorioCommand.class);
		cmd.setDadosRelatorio(dadosRelatorio);
		cmd.setParametros(parametros);
		cmd.setReportFileName(reportFileName);
		cmd.setReportPathName(reportPathName);
		JasperPrint jasperPrint = cmd.call();

		if(jasperPrint!=null){
			ArquivoDigital ad = new ArquivoDigital();
			ad.setDescricao("Relatorio");
			ad.setTipo(TipoArquivoEnum.ARQUIVO);
			if(exportarEXCEL){
				ad.setMimeType("application/vnd.ms-excel");
				if(reportName!=null){
					ad.setNomeArquivo(reportName + ".xls");
				} else {
					ad.setNomeArquivo(StringUtil.randomString(20).toLowerCase() + ".xls");
				}
				ad.setDados(exportaRelatorioEXCELL(jasperPrint));
			} else {
				ad.setMimeType("application/pdf");
				if(reportName!=null){
					ad.setNomeArquivo(reportName + ".xls");
				} else {
					ad.setNomeArquivo(StringUtil.randomString(20).toLowerCase() + ".pdf");
				}
				ad.setDados(JasperExportManager.exportReportToPdf(jasperPrint));
			}
			ad.setTamanho(ad.getDados().length);
			ad = em.merge(ad);
			return ad.getId();
		}
		return null;
	}

	public byte[] exportaRelatorioEXCELL(JasperPrint impressao) throws JRException, Exception {
		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
		exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, impressao);
		exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
//		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporter.exportReport();
		return xlsReport.toByteArray();
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
	public boolean isExportarEXCEL() {
		return exportarEXCEL;
	}
	public void setExportarEXCEL(boolean exportarEXCEL) {
		this.exportarEXCEL = exportarEXCEL;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Map<?,?> getMapDadosRelatorio() {
		return mapDadosRelatorio;
	}

	public void setMapDadosRelatorio(Map<?,?> mapDadosRelatorio) {
		this.mapDadosRelatorio = mapDadosRelatorio;
	}
}