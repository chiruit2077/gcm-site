package br.com.ecc.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateUtil {

	public static final String DEFAULT_DATE_FMT_PATTERN = "dd/MM/yyyy";
	public static final String DEFAULT_TIME_FMT_PATTERN = "HH:mm:ss";
	public static final String DEFAULT_DATETIME_FMT_PATTERN = "dd/MM/yyyy HH:mm:ss";
	public static final String DEFAULT_YEAR_FMT_PATTERN = "yyyy";
	public static final String DEFAULT_FULL_DATE = "dd' de 'MMMM' de 'yyyy";
	/**
	 * Formata com o pattern padrão definido em DEFAULT_DATE_FMT_PATTERN: "dd/MM/yyyy"
	 * @param data
	 * @return string formatada
	 */
	public static String parseToString(Date data) {
		return parseToString(DEFAULT_DATE_FMT_PATTERN, data);
	}

	/**
	 * Formata com o pattern padrão definido em DEFAULT_TIME_FMT_PATTERN: "HH:mm:ss"
	 * @param data
	 * @return string formatada
	 */
	public static String parseTimeToString(Date data) {
		return parseToString(DEFAULT_TIME_FMT_PATTERN, data);
	}

	/**
	 * Formata com o pattern padrão definido em DEFAULT_DATETIME_FMT_PATTERN: "dd/MM/yyyy HH:mm:ss"
	 * @param data
	 * @return string formatada
	 */
	public static String parseDatetimeToString(Date data) {
		return parseToString(DEFAULT_DATETIME_FMT_PATTERN, data);
	}

	/**
	 * Patterns: <a href="http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/index.html?overview-summary.html">referencia</a>
	 * @param pattern as mesmas do {@link DateTimeFormat} por exemplo "dd/MM/yy"
	 * @param data a data a ser formatada
	 * @return string formatada
	 * @see DateTimeFormat
	 */
	public static String parseToString(String pattern, Date data) {
		if (data != null && pattern!=null && "DEFAULT_FULL_DATE".equals(pattern)){
			return getDataExtenso(data);
		}else if(data != null && pattern!=null && !"".equals(pattern) ) {
			return DateTimeFormat.getFormat(pattern).format(data);
		}
		return "";
	}

	public static Integer getYear(Date data){
		Integer ano = null;
		if (data != null){
			ano = Integer.parseInt(parseToString(DEFAULT_YEAR_FMT_PATTERN, data));
		}
		return ano;

	}

	public static Integer getDiferencaDias(Date inicio, Date fim){
		Long dif = new Long(( fim.getTime() - inicio.getTime() )/ (1000*60*60*24));
		return dif.intValue() + 1;
	}

	@SuppressWarnings("deprecation")
	public static String getDataExtenso(Date data){
		String mes[] = {"janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};
		return data.getDate() + " de " + mes[data.getMonth()] + " de " + data.getYear();
	}

	@SuppressWarnings("deprecation")
	public static Date addHoras(Date inicio, Integer hora){
		Date date = new Date(inicio.getTime());
		date.setHours(inicio.getHours()+hora);
		return date;
	}
}
