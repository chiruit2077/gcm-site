package br.com.ecc.client.core.mvp;


public class StackTraceUtilClient {

	    public static String getStackTrace(Throwable e) {
	        StringBuffer stack = new StringBuffer();
	        StackTraceElement[] stackTrace = e.getStackTrace();
	        for (StackTraceElement line: stackTrace) {
	            if (!line.getMethodName().equals("getStackTrace"))
	                stack.append("\n\t"+line);
	        }
	        stack.append("");
	        return stack.toString();
	    }
}

