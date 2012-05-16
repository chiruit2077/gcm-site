package br.com.ecc.server.util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {
	public static Integer getParameterAsInteger(HttpServletRequest req, String name) {
		Integer intValue = null;
		String stringValue = (String) req.getParameter(name);
		try {
			intValue = Integer.parseInt(stringValue);
		} catch(NumberFormatException e) {
		}
		return intValue;
	}

	public static Boolean getParameterAsBoolean(HttpServletRequest req, String name) {
		Boolean booleanValue = Boolean.FALSE;
		String stringValue = req.getParameter(name);
		try {
			booleanValue = Boolean.valueOf(stringValue);
		} catch (Exception e) {
		}
		return booleanValue;
	}
}