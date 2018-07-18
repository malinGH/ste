package com.souche.ste.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GzipUtil {
    private final static Logger logger = LoggerFactory
            .getLogger(GzipUtil.class);
    public final static float gzipCompressRatio = 0.3f;

    public static byte[] unGzip(byte[] buffer, int offset, int len)
            throws IOException {
        if (buffer == null || offset < 0 || len < 0) {
            throw new IllegalArgumentException("unGzip parameter error");
        }
        ByteArrayBuffer buf = new ByteArrayBuffer(len * 5);

        BufferedInputStream bis = new BufferedInputStream(new GZIPInputStream(
                new ByteArrayInputStream(buffer, offset, len)));
        byte[] tmp = new byte[8192];
        int l;
        while ((l = bis.read(tmp)) != -1) {
            buf.append(tmp, 0, l);
        }
        return buf.toByteArray();
    }

    public static byte[] gzip(byte[] buffer) {
        if (buffer == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            GZIPOutputStream out = new GZIPOutputStream(bos);
            out.write(buffer);
            out.finish();
        } catch (IOException e) {
            logger.warn("", e);
            return null;
        }

        return bos.toByteArray();
    }

}
