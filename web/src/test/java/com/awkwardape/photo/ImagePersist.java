package com.awkwardape.photo;

import com.awkwardape.photo.gae.util.EMF;
import com.awkwardape.photo.model.Image;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tbeauvais
 * Date: 6/8/13
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePersist {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalUserServiceTestConfig())
                    .setEnvIsLoggedIn(true)
                    .setEnvAuthDomain("localhost")
                    .setEnvEmail("test@localhost");

    @Before
    public void setupGuestBookServlet() {
        helper.setUp();
    }

    public void persist() {
        final EntityManagerFactory emf = EMF.get();
        final EntityManager em = emf.createEntityManager();

        final Image image = new Image();

        em.persist(image);
        em.close();
    }
}
