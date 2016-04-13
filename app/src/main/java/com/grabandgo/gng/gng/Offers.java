package com.grabandgo.gng.gng;

import android.widget.ImageView;

/**
 * Created by alexander on 2016-04-13.
 */
public class Offers {
    private int duration;
    private String reduction;
    private String description;
    private String type;
    private ImageView imageOffer;



    public Offers(int duration, String reduction, String description, String type, ImageView imageOffer){
        this.duration = duration;
        this.reduction = reduction;
        this.description = description;
        this.type = type;
        this.imageOffer = imageOffer;

    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setReduction(String reduction) {
        this.reduction = reduction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageOffer(ImageView imageOffer) {
        this.imageOffer = imageOffer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public String getReduction() {
        return reduction;
    }

    public String getDescription() {
        return description;
    }

    public ImageView getImageOffer() {
        return imageOffer;
    }

    public String getType() {
        return type;
    }
}
