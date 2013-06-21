package com.awkwardape.photo.model;

import com.google.appengine.api.datastore.Key;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String blobKey;
    private String thumbnailBlobKey;

    public void setBlobKey(String blobKey) {
        this.blobKey = blobKey;
    }

    public String getBlobKey() {
        return blobKey;
    }

    public void setKey( Key key ) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public void setThumbnailBlobKey(String thumbnailBlobKey) {
        this.thumbnailBlobKey = thumbnailBlobKey;
    }

    public String getThumbnailBlobKey() {
        return thumbnailBlobKey;
    }
}
