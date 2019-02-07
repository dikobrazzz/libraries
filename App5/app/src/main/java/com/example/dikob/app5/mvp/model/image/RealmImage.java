package com.example.dikob.app5.mvp.model.image;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmImage extends RealmObject {

    @PrimaryKey
    private String url;
    private String urlInDevice;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlInDevice() {
        return urlInDevice;
    }

    public void setUrlInDevice(String urlInDevice) {
        this.urlInDevice = urlInDevice;
    }
}
