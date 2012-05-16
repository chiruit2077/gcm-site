package br.com.ecc.client.util;

public abstract class ExceptionUtils {

	public static String printExceptionString(Throwable t) {
		if(null == t) return null;

		StackTraceElement[] traces = t.getStackTrace();
		StringBuilder sb = new StringBuilder();

		for(StackTraceElement trace: traces)
			sb.append(trace.toString());

		return sb.toString();
	}
}