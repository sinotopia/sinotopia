package com.hkfs.fundamental.common.utils;

import org.mockito.internal.util.io.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * <p>zip压缩</p>
 * @Author dzr
 * @Date 2016/04/25
 */
public abstract class ZipUtils {
    
    private static final Logger LOG = LoggerFactory.getLogger(ZipUtils.class);
    
    private static final int BUFFER_LENGTH = 2048;

    /**
     * <p>压缩</p>
     * @param ungzipped
     * @return
     */
    public static byte[] gzip(byte[] ungzipped) {
        GZIPOutputStream gos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(baos);
            gos.write(ungzipped, 0, ungzipped.length);
            gos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            LOG.error("Gzip data failed.", e);
            return IOUtils.EMPTY_BYTES;
        } finally {
            IOUtil.close(gos);
            IOUtil.close(baos);
        }
    }


    /**
     * <p>解压缩</p>
     * @param gzipped
     * @return
     */
    public static byte[] ungzip(byte[] gzipped) {
        return ungzip(gzipped, false);
    }

    /**
     * <p>解压缩</p>
     * @param gzipped
     * @param giveRawIfFailed
     * @return
     */
    public static byte[] ungzip(byte[] gzipped, boolean giveRawIfFailed) {
        GZIPInputStream gis = null;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            byte[] buffer = new byte[BUFFER_LENGTH];
            bais = new ByteArrayInputStream(gzipped);
            gis = new GZIPInputStream(bais, BUFFER_LENGTH);
            baos = new ByteArrayOutputStream(gzipped.length);
            int readedLength = 0;
            while (-1 != (readedLength = gis.read(buffer))) {
                baos.write(buffer, 0, readedLength);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            if (giveRawIfFailed) {
                LOG.warn("Ungzip data failed, return the raw data.", e);
                return gzipped;
            } else {
                LOG.error("Ungzip data failed, return empty data.", e);
                return IOUtils.EMPTY_BYTES;
            }
        } finally {
            IOUtil.close(bais);
            IOUtil.close(gis);
            IOUtil.close(baos);
        }
    }
    
}
