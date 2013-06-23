package com.awkwardape.photo.gae.controller;

/**
* Created with IntelliJ IDEA.
* User: tbeauvais
* Date: 6/8/13
* Time: 4:59 PM
* To change this template use File | Settings | File Templates.
*/

import com.awkwardape.photo.gae.dao.ImageDao;
import com.awkwardape.photo.model.Image;
import com.google.appengine.api.blobstore.BlobKey;
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
import java.util.Arrays;

@Controller
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private ImageDao imageDao;

    private static final byte[] clear = {-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 3, -24, 0, 0, 3, -24, 1, 0, 0, 0, 0, 101, -40, -23, 73, 0, 0, 1, -59, 73, 68, 65, 84, 120, -38, -19, -51, 49, 1, 0, 0, 12, 2, 32, -5, -105, -42, 24, 59, 6, 5, 72, 47, -59, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -69, -35, 110, -73, -37, -19, 118, -5, -125, 125, -100, 52, 124, 51, 47, -95, 113, 33, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};

    @Transactional
//    @ResponseBody
    @RequestMapping(value = "/scale", method = RequestMethod.GET)
    public void scale( @RequestParam(required = true) String key,
                       HttpServletRequest req,
                       HttpServletResponse res) throws IOException {
        final Image image = imageDao.getImageFromBlobKey(key);
        final com.google.appengine.api.images.Image i1  = ImagesServiceFactory.makeImage(clear);
        final Transform transform                       = ImagesServiceFactory.makeResize(image.getWidth()/10, image.getHeight()/10, true);
        final com.google.appengine.api.images.Image i2  = ImagesServiceFactory.getImagesService().applyTransform( transform, i1, ImagesService.OutputEncoding.PNG);

        res.getOutputStream().write(i2.getImageData());
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Object[] test() throws IOException {
        return imageDao.getAll().toArray();
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/listhtml", method = RequestMethod.GET)
    public String list() throws IOException {
        final StringBuffer stringBuffer     = new StringBuffer();
        final ImagesService imagesService   = ImagesServiceFactory.getImagesService();

        final Object[] objects              = imageDao.getAll().toArray();

        if( objects == null || objects.length == 0 ) {
            return "No images loaded.";
        }

        for( Object o : imageDao.getAll().toArray() ) {
            final Image image = (Image) o;
            stringBuffer.append( "<a href='/download?imageKey=" + image.getBlobKey() + "''>" + image.getKey() + "," + image.getBlobKey() + "</a><br/>");
//            stringBuffer.append( "<img src='/download?imageKey=" + image.getThumbnailBlobKey() + "''><br/>");
//            stringBuffer.append( "<img src='/download?imageKey=" + image.getBlobKey() + "''><br/>");

//            final ServingUrlOptions servingUrlOptions   = ServingUrlOptions.Builder.withBlobKey( new BlobKey( image.getBlobKey() ) ).imageSize(300);
//            final String servingUrl                     = imagesService.getServingUrl( servingUrlOptions );
//
//            stringBuffer.append("<img src='" + servingUrl + "''><br/>");
        }

        return stringBuffer.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/ping")
    public String pong() {
        return "Pong?";
    }
}