package br.com.ecc.server.service.core.multipart;

import java.io.Serializable;

public class FileRequest implements Serializable {
	
	private static final long serialVersionUID = -7123313891615677861L;

	private String fileName;     
	  
	private String contentType;   

	private byte[] content;

	public FileRequest() {
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
	
}
