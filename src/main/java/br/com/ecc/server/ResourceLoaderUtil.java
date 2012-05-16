package br.com.ecc.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ResourceLoaderUtil {
	
	public static synchronized String loadResource(String basePath, String name) throws IOException {
		BufferedInputStream stream = new BufferedInputStream(ResourceLoaderUtil.class.getResourceAsStream(basePath+name));
		int t = -1;
		StringBuilder sb = new StringBuilder();
		while(-1 != (t=stream.read())) sb.append((char)t);
		stream.close();
		return sb.toString();
	}

	public static synchronized InputStream loadResourceToInputStream(String basePath, String name) throws IOException {
		return new BufferedInputStream(ResourceLoaderUtil.class.getResourceAsStream(basePath+name));
	}
}
