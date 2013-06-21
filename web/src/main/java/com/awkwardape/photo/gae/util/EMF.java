package com.awkwardape.photo.gae.util;

/**
 * Created with IntelliJ IDEA.
 * User: tbeauvais
 * Date: 6/8/13
 * Time: 1:26 PM
 * To change this template use File | Settings | File Templates.
 */
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
//    private static final EntityManagerFactory emfInstance =
//            Persistence.createEntityManagerFactory("transactions-optional");

    private EMF() {}

    public static EntityManagerFactory get() {
//        return emfInstance;

        return null;
    }
}