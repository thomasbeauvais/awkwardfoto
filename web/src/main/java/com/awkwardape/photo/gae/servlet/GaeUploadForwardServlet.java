/**
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.awkwardape.photo.gae.servlet;

import com.google.appengine.api.blobstore.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

public class GaeUploadForwardServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String url = req.getParameter( "uploadUrl" );

        if ( url == null || url.isEmpty() ) {
            throw new RuntimeException( "Must have parameter 'uploadUrl' to upload and forward." );
        }

        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String blobStoreUrl = blobstoreService.createUploadUrl( URLDecoder.decode(url, "UTF-8") );

        // A trick with uploading to the BlobStore via forwarding is..
        // you have to chop off everything at the beginning of "_ah/upload"
//        int ahUploadIndex   = blobStoreUrl.indexOf( "/_ah/upload" );
//        blobStoreUrl        = blobStoreUrl.substring( ahUploadIndex + 12 );

        log( "Redirecting upload ('" + url + "'): " + blobStoreUrl );

        try {
            req.getRequestDispatcher(blobStoreUrl).forward(req, res);
        } catch (ServletException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
