package br.com.ecc.client.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import br.com.ecc.model.ArquivoDigital;
import br.com.ecc.model.tipo.TipoArquivoEnum;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.Transactional;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;

public class PrintHTML2PDFComand implements Callable<Integer>{

	@Inject EntityManager em;
	@Inject Injector injector;

	private String html;

	@Override
	@Transactional
	@SuppressWarnings("rawtypes")
	public Integer call() throws Exception{
		if (getHtml()!=null){
			ByteArrayInputStream origem = new ByteArrayInputStream(getHtml().getBytes());
			Reader htmlreader = new BufferedReader(new InputStreamReader(origem));

			Document pdfDocument = new Document();

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			PdfWriter.getInstance(pdfDocument, output);
			pdfDocument.open();
			StyleSheet styles = new StyleSheet();
			styles.loadTagStyle("body", "font", "Bitstream Vera Sans");
			ArrayList arrayElementList = HTMLWorker.parseToList(htmlreader, styles);
			for (int i = 0; i < arrayElementList.size(); ++i) {
				Element e = (Element) arrayElementList.get(i);
				pdfDocument.add(e);
			}
			pdfDocument.close();
			byte[] bs = output.toByteArray();
			ArquivoDigital ad = new ArquivoDigital();
			ad.setDescricao("Relatorio");
			ad.setTipo(TipoArquivoEnum.ARQUIVO);
			ad.setMimeType("application/pdf");
			ad.setNomeArquivo(StringUtil.randomString(20).toLowerCase() + ".pdf");
			ad.setDados(bs);
			ad.setTamanho(ad.getDados().length);
			ad = em.merge(ad);
			return ad.getId();
		}
		return null;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

}
