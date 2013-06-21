package com.awkwardape.photo.gae.util;

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
}
