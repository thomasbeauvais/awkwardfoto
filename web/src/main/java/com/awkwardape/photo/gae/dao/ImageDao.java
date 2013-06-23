package com.awkwardape.photo.gae.dao;

import com.awkwardape.photo.model.Image;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
public class ImageDao<T extends Image> {

    @PersistenceContext
    public EntityManager entityManager;
    private Object all;

    @Transactional(propagation = Propagation.REQUIRED)
    public T put(T object) {
        entityManager.merge(object);

        return object;
    }

    public Image get(String key) {
        Query query = entityManager.createQuery("select o from " + Image.class.getName() + " o where o.id= :id");
        query.setParameter("id", key);
        query.setMaxResults(1);

        Image image = (Image) query.getSingleResult();

        return image;
    }

    public Image getImageFromBlobKey(String blobKey) {
        Query query = entityManager.createQuery("select o from " + Image.class.getName() + " o where o.blobKey= :blobKey");
        query.setParameter("blobKey", blobKey);
        query.setMaxResults(1);

        final Image image = (Image) query.getSingleResult();

        return image;
    }

    public List getAll() {
        Query query         = entityManager.createQuery("select from " + Image.class.getName());

        return query.getResultList();
    }
}
