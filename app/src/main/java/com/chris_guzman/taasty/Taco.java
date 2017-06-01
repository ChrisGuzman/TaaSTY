package com.chris_guzman.taasty;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by chrisguzman on 5/28/17.
 */

public class Taco extends RealmObject {
    private String name;
    private transient String imageUrl;
    private String url;

    public Taco() {
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
