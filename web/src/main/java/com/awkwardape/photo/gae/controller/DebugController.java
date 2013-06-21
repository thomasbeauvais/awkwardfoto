//package com.awkwardape.photo.gae.controller;
//
///**
// * Created with IntelliJ IDEA.
// * User: tbeauvais
// * Date: 6/8/13
// * Time: 4:59 PM
// * To change this template use File | Settings | File Templates.
// */
//
//import com.awkwardape.photo.gae.dao.ImageDao;
//import com.awkwardape.photo.model.Image;
//import com.google.appengine.api.images.ImagesService;
//import com.google.appengine.api.images.ImagesServiceFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Controller
//@RequestMapping(value = "/debug")
//public class DebugController {
//
//    @Autowired
//    private ImageDao imageDao;
//
//    @RequestMapping( value = "/listall", method = RequestMethod.GET)
//    @Transactional
//    public String listall() throws IOException {
//        final StringBuffer stringBuffer     = new StringBuffer();
//        final ImagesService imagesService   = ImagesServiceFactory.getImagesService();
//
//        for( Object o : imageDao.getAll().toArray() ) {
//            final Image image = (Image) o;
//            stringBuffer.append( "<a href='/download?imageKey=" + image.getBlobKey() + "''>" + image.getKey() + "," + image.getBlobKey() + "</a><br/>");
////            stringBuffer.append( "<img src='/download?imageKey=" + image.getThumbnailBlobKey() + "''><br/>");
////            stringBuffer.append( "<img src='/download?imageKey=" + image.getBlobKey() + "''><br/>");
//
////            final ServingUrlOptions servingUrlOptions   = ServingUrlOptions.Builder.withBlobKey( new BlobKey( image.getBlobKey() ) ).imageSize(300);
////            final String servingUrl                     = imagesService.getServingUrl( servingUrlOptions );
////
////            stringBuffer.append("<img src='" + servingUrl + "''><br/>");
//        }
//
//        return stringBuffer.toString();
//    }
//}