package com.grabandgo.gng.gng;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Offers object. With duration, description, type and images.
 */
public class Offers implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -955916968626545605L;
    private String start;
    private String end;
    private int discount;
    private String title;
    private String description;
    private String type;
    private String whom;
    private int restID;
    private String restaurant;

    private byte[] imageBytes;
    private byte[] logoBytes;
    private String imagePath;





    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end){
        this.end = end;
    }

    public void setWhom(String whom){
        this.whom = whom;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd(){
        return this.end;
    }

    public String getWhom(){
        return this.whom;
    }

    public int getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setImageBytes(byte[] imagebytes){
        this.imageBytes = imagebytes;
    }

    public byte[] getImageBytes(){
        return imageBytes;
    }


    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public String getImagePath(){
        return this.imagePath;
    }

    public void setRestID(int id){
        this.restID = id;
    }

    public int getRestID(){
        return this.restID;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setRestaurant(String restaurant){
        this.restaurant = restaurant;
    }

    public String getRestaurant(){
        return this.restaurant;
    }

    public void setLogoByte(byte[] logoBytes){
        this.logoBytes = logoBytes;
    }

    public byte[] getLogoByte(){
        return this.logoBytes;
    }

}