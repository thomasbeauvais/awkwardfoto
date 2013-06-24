package com.awkwardape.photo.gae.controller;

/**
* Created with IntelliJ IDEA.
* User: tbeauvais
* Date: 6/8/13
* Time: 4:59 PM
* To change this template use File | Settings | File Templates.
*/
import com.awkwardape.photo.gae.dao.ImageDao;
import com.awkwardape.photo.gae.util.GaeUtils;
import com.awkwardape.photo.model.Image;
import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.files.*;
import com.google.appengine.api.images.CompositeTransform;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping( value = "/image" )
public class ImageController {

    @Autowired
    private ImageDao imageDao;
    private static int s_index;

    @RequestMapping( value = "/favicon.ico" )
    public void favicon(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect( "/images/favicon.ico");
    }

    @ResponseBody
    @RequestMapping( value = "/upload", method = RequestMethod.POST)
    public String imageUpload(HttpServletRequest req) {
        final BlobstoreService blobstoreService     = BlobstoreServiceFactory.getBlobstoreService();
        final ImagesService imagesService           = ImagesServiceFactory.getImagesService();

        final StringBuffer returnBuffer             = new StringBuffer();
        final Map<String, List<BlobKey>> blobKeyMap = blobstoreService.getUploads(req);
        final Set<String> keySet                    = blobKeyMap.keySet();
        for ( String key : keySet ) {
            final List<BlobKey> blobKeys            = blobstoreService.getUploads(req).get(key);

            for ( BlobKey blobKey : blobKeys ) {
                final BlobInfo blobInfo             = new BlobInfoFactory().loadBlobInfo(blobKey);
                final Image image                   = new Image();
                image.setBlobKey(blobKey.getKeyString());

                // TODO:  Find out if this is really the best way...
                // TODO:  I think that the  bytes should already be accessible in the HttpServletRequest..
                // This is a lot of crap to get the width/height
                try {
                    FileService fileService                         = FileServiceFactory.getFileService();
                    AppEngineFile blobFile                          = fileService.getBlobFile(blobKey);
                    FileReadChannel readChannel                     = fileService.openReadChannel(blobFile, false);
                    byte[] imageData                                = GaeUtils.getBytes(Channels.newInputStream(readChannel));
                    com.google.appengine.api.images.Image oldImage  = ImagesServiceFactory.makeImage(imageData);

                    image.setWidth( oldImage.getWidth() );
                    image.setHeight( oldImage.getHeight() );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // The ImageService is terrible for resizing then storing to the BlobStore
                // We are opting to create a serving URL each time.  It will use more CPU but
                // The client should cache when available.
                // This will also allow for dynamic thumbnails
//                saveThumbnail( image, blobKey );

                imageDao.put(image);

                returnBuffer.append(blobInfo.getFilename() + "=" + image.getBlobKey() + "<br/>");
            }
        }

        return returnBuffer.toString();
    }

    /**
     * This service is used only to download the original image directly from the BlobstoreService.
     *
     * @param key          the string representation of the BlobKey
     * @param req               the request object
     * @param res               the response object
     */
    @RequestMapping( value = "/original", method = RequestMethod.GET)
    public void downloadOriginal( @RequestParam(required = true) String key,
                               HttpServletRequest req,
                               HttpServletResponse res) throws IOException {
        final BlobstoreService blobstoreService     = BlobstoreServiceFactory.getBlobstoreService();

        final BlobKey blobKey                       = new BlobKey(key);
        final BlobInfo blobInfo                     = new BlobInfoFactory().loadBlobInfo( blobKey );

        res.setHeader("Content-Disposition", "filename=\"" + blobInfo.getFilename() + "\"");

        // Serve the blob directly from the GAE API
        blobstoreService.serve( blobKey, res );
    }

    /**
     *
     * @param key
     * @param width
     * @param height
     * @param req
     * @param res
     * @throws IOException
     */
    @Transactional
    @RequestMapping( value = "/download", method = RequestMethod.GET )
    public void download( @RequestParam(required = true) String key,
                       @RequestParam(defaultValue = "0") int width,
                       @RequestParam(defaultValue = "0") int height,
                       HttpServletRequest req,
                       HttpServletResponse res ) throws IOException {
        final BlobKey blobKey                                   = new BlobKey( key );
        final BlobInfo blobInfo                                 = new BlobInfoFactory().loadBlobInfo( blobKey );

        // If the request has specified a width or height, then we will need to scale it
        // Else, then we can defer to the downloadOriginal() method
        if ( width > 0 || height > 0 ) {
            final ImagesService imagesService                       = ImagesServiceFactory.getImagesService();
            final com.google.appengine.api.images.Image oldImage    = ImagesServiceFactory.makeImageFromBlob(blobKey);

            final CompositeTransform transform                      = ImagesServiceFactory.makeCompositeTransform();

            transform.concatenate( ImagesServiceFactory.makeResize( 0, height ) );

            final com.google.appengine.api.images.Image newImage    = imagesService.applyTransform(transform, oldImage, ImagesService.OutputEncoding.JPEG);

            final int len                                           = newImage.getImageData().length;

            /*
            // TODO:  Try to find out how to make this not execute in production
            try {
                final int sleep = (int) (len / 1000L);
                Thread.sleep( sleep );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */

            res.setContentType( "image/jpg" );
            res.setContentLength(len);
            res.setHeader("Content-Disposition", "filename=\"" + blobInfo.getFilename() + "("+ newImage.getWidth() + "x" + newImage.getHeight() + ")\"");

            res.getOutputStream().write(newImage.getImageData());
            res.getOutputStream().flush();
        } else {
            downloadOriginal( key, req, res );
        }
    }

    /**
     * Picks an already existing at random.
     *
     * @param req
     * @param res
     * @throws IOException
     */
    @Transactional
    @RequestMapping( value = "/random", method = RequestMethod.GET)
    public void random( HttpServletRequest req,
                        HttpServletResponse res ) throws IOException {

        final Object[] objects                      = imageDao.getAll().toArray();

        if ( objects.length > 0 ) {
//            final int index                         = (int)(objects.length * Math.random());
            final int index                         = s_index++ % objects.length;
            final Image image                       = (Image) imageDao.getAll().toArray()[index];

            download(image.getBlobKey(), 0, 0, req, res);
        }

        res.getWriter().println("No images loaded.");
    }

    @Deprecated
    public static BlobKey saveThumbnail(Image image, BlobKey blobKey) {
        ImagesService imagesService         = ImagesServiceFactory.getImagesService();
        FileService fileService             = FileServiceFactory.getFileService();

        com.google.appengine.api.images.Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);

        final Transform transform = ImagesServiceFactory.makeResize(500, 500);
        com.google.appengine.api.images.Image newImage = imagesService.applyTransform(transform, oldImage);

        // Create a new Blob file with mime-type "text/json"
        try {
            // Get the  file service instance
            AppEngineFile file              = fileService.createNewBlobFile("image/jpeg" );

            boolean lock = true;

            FileWriteChannel writeChannel   = fileService.openWriteChannel(file, lock);

            OutputStream outputStream       = Channels.newOutputStream(writeChannel);
            outputStream.write( newImage.getImageData() );
            outputStream.close();

            lock = false;
            writeChannel.closeFinally();

            BlobKey thumbnailKey            = fileService.getBlobKey(file);

            // HUH!?
            if (blobKey == null) {
                Thread.sleep(500);
                thumbnailKey = fileService.getBlobKey(file);
            }

            image.setThumbnailBlobKey(thumbnailKey.getKeyString());

            return thumbnailKey;
        } catch (Exception e) {
            System.out.println("Exception in creating BLOB file : " + e);
            e.printStackTrace();
        } finally {
        }
        return blobKey;
    }
}