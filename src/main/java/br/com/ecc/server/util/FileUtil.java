package br.com.ecc.server.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class FileUtil {

	private static FileOutputStream fos;

	public static void write(File target, byte[] bytes) throws IOException {
		BufferedInputStream input = new BufferedInputStream(
				new ByteArrayInputStream(bytes));

		write(target, input);
	}

	public static void write(File target, InputStream input) throws IOException {
		fos = new FileOutputStream(target);
		byte[] buff = new byte[1024];
		int bytesRead = 0;

		while (-1 != (bytesRead = input.read(buff)))
			fos.write(buff, 0, bytesRead);
	}
}
