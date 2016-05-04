package com.grabandgo.gng.gng;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Offers object. With duration, description, type and images.
 */
public class Offers implements Serializable {
    private static final long serialVersionUID = -955916968626545605L;
    private int duration;
    private int reduction;
    private String description;
    private String type;
    private ImageHolder imageOffer = new ImageHolder();
    private int restID;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageOffer(ImageView imageOffer) {
        this.imageOffer.setImage(imageOffer);
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public int getReduction() {
        return reduction;
    }

    public String getDescription() {
        return description;
    }

    public ImageView getImageOffer() {
        return imageOffer.getImage();
    }

    public String getType() {
        return type;
    }

    public void setImagePath(String sourcePath) {
        imageOffer.setDBSource(sourcePath);
    }

    public String getImagePath() {
        return imageOffer.getDBSource();
    }

    public void setRestID(int id) {
        this.restID = id;
    }

    public int getRestID() {
        return this.restID;
    }
}
