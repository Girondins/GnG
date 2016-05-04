package com.grabandgo.gng.gng;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * ImageHolder class.
 */
public class ImageHolder implements Serializable {

    private static final long serialVersionUID = 4352690812891496624L;
    private String dbSource;
    private ImageView image;

    public ImageHolder() {
    }

    public void setDBSource(String dbSource) {
        this.dbSource = dbSource;
    }

    public String getDBSource() {
        return this.dbSource;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public ImageView getImage() {
        return this.image;
    }
}
