package com.awkwardape.photo.gae.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: tbeauvais
 * Date: 6/9/13
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public final class GaeUtils {
    public static String removePathBits(String servingUrl) {
        if ( servingUrl == null ) {
            return null;
        }

        if ( servingUrl.contains( "/_ah/upload" ) ) {
            return servingUrl.substring( servingUrl.indexOf( "/_ah/upload" ) );
        } else if ( servingUrl.contains( "/_ah/img" ) ) {
            return servingUrl.substring( servingUrl.indexOf( "/_ah/img" ) );
        }

        return servingUrl;
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int len;
        byte[] data = new byte[10000];
        while ((len = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, len);
        }

        buffer.flush();
        return buffer.toByteArray();
    }
}
