package com.sinotopia.fundamental.common.utils;

import java.io.*;
import java.nio.charset.Charset;

public class IOUtils {

	public static final String CHARSET_NAME_UTF8 = "UTF-8";

	public static final Charset CHARSET_UTF8 = Charset.forName(CHARSET_NAME_UTF8);

	public static final byte[] EMPTY_BYTES = new byte[0];

	private static final int DEFAULT_SIZE = 4096;

	public static void close(InputStream inputStream) {
		closeSafe(inputStream);
	}
	
	public static void close(OutputStream outputStream) {
		closeSafe(outputStream);
	}
	
	public static void close(Reader reader) {
		closeSafe(reader);
	}

	public static void close(Writer writer) {
		closeSafe(writer);
	}
	
	private static void closeSafe(Object object) {
		if (object != null) {
			try {
				if (object instanceof InputStream) {
					((InputStream)object).close();
				}
				else if (object instanceof OutputStream) {
					((OutputStream)object).close();
				}
				else if (object instanceof Reader) {
					((Reader)object).close();
				}
				else if (object instanceof Writer) {
					((Writer)object).close();
				}
			}
			catch (Exception e) {}
		}
	}
	
	public static String readString(BufferedReader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append('\n');
		}
		return sb.toString();
	}

	public static String readString(InputStream is) throws IOException {
		return readString(is, true);
	}

	public static String readString(InputStream is, boolean close) throws IOException {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] datas = new byte[1024];
			int len = 0;
			while ((len = is.read(datas, 0, datas.length)) != -1) {
				baos.write(datas, 0, len);
			}
			return baos.toString();
		}
		finally {
			IOUtils.close(baos);
			if (close) {
				IOUtils.close(is);
			}
		}
	}
	
	public static byte[] readBytes(InputStream is) throws IOException {
		return readBytes(is, true);
	}

	public static byte[] readBytes(InputStream is, boolean close) throws IOException {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] datas = new byte[1024];
			int len = 0;
			while ((len = is.read(datas, 0, datas.length)) != -1) {
				baos.write(datas, 0, len);
			}
			return baos.toByteArray();
		}
		finally {
			IOUtils.close(baos);
			if (close) {
				IOUtils.close(is);
			}
		}
	}
	
}
