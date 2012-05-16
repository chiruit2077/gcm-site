package br.com.ecc.server.service.core.multipart;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class MultipartRequest {

	public static final int DEFAULT_MAXSIZE = 5242880;
	
	private Map<String, FileRequest> files = new HashMap<String, FileRequest>();
	private Map<String, String> params = new HashMap<String, String>();
	private HttpServletRequest servletRequest;
	
	public MultipartRequest(HttpServletRequest request) throws IOException {
		this(request, DEFAULT_MAXSIZE);
	}
	
	public MultipartRequest(HttpServletRequest request, int maxSize) throws IOException {
		this.servletRequest = request;
		MultipartParser parser = new MultipartParser(request, maxSize, false, false, "UTF8");
	    Part part;
	    while ((part = parser.readNextPart()) != null) {
	    	String name = part.getName();
	    	if(name != null) {
	    		if (part.isFile()) {
	    			FilePart filePart = (FilePart) part;
	    			
	    			InputStream in = filePart.getInputStream();
	    			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    			DataOutputStream dos = new DataOutputStream(bos);
	    			int i;
	    			while((i = in.read()) != -1) {
	    				dos.write(i);
	    			}
	    			dos.flush();
	    			
	    			byte[] content = bos.toByteArray();
	    			if(content != null && content.length > 0) {
	    				FileRequest file = new FileRequest();
	    				file.setFileName(filePart.getFileName());
	    				file.setContentType(filePart.getContentType());
	    				file.setContent(content);
	    				files.put(name, file);
	    			}
	    		} else if(part.isParam()) {
	    			ParamPart paramPart = (ParamPart) part;
	    			params.put(paramPart.getName(), paramPart.getStringValue());
	    		}
	    	}
	    }
	}
	
	public FileRequest getFile(String name) {
		return files.get(name);
	}
	
	public String getParam(String name) {
		return params.get(name);
	}
	
	public HttpServletRequest getServletRequest() {
		return this.servletRequest;
	}
}
