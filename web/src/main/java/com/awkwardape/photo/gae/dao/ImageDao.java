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
        Query query = entityManager.createQuery("select o from " + Image.class.getName() + " o where o.key= :key");
        query.setParameter("key", key);
        query.setMaxResults(1);

        Image image = (Image) query.getSingleResult();

        return image;
    }

    public List getAll() {
        Query query         = entityManager.createQuery("select from " + Image.class.getName());

        return query.getResultList();
    }
}
