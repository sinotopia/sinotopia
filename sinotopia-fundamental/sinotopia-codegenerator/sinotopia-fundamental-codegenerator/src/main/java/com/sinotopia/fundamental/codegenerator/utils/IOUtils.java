package com.sinotopia.fundamental.codegenerator.utils;

import java.io.*;

public class IOUtils {
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
                    ((InputStream) object).close();
                } else if (object instanceof OutputStream) {
                    ((OutputStream) object).close();
                } else if (object instanceof Reader) {
                    ((Reader) object).close();
                } else if (object instanceof Writer) {
                    ((Writer) object).close();
                }
            } catch (Exception e) {
            }
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
        } finally {
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
        } finally {
            IOUtils.close(baos);
            if (close) {
                IOUtils.close(is);
            }
        }
    }

}
