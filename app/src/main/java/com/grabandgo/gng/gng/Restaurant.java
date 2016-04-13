package com.grabandgo.gng.gng;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

/**
 * Created by alexander on 2016-04-13.
 */
public class Restaurant {

    private String name;
    private String address;
    private String number;
    private String email;
    private String city;
    private String info;

    private int ID;
    private int rating;

    private ImageView restaurantPic;
    private ImageView logo;

    private LinkedList<String> category;
    private LinkedList<Offers> offerList;
    private LinkedList<String> allergi;

    private LatLng restaurantLocation;

    private OpenHours openHours;

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setRestaurantPic(ImageView restaurantPic) {
        this.restaurantPic = restaurantPic;
    }

    public void setLogo(ImageView logo) {
        this.logo = logo;
    }

    public void setCategory(LinkedList<String> category) {
        this.category = category;
    }

    public void setOfferList(LinkedList<Offers> offerList) {
        this.offerList = offerList;
    }

    public void setAllergi(LinkedList<String> allergi) {
        this.allergi = allergi;
    }

    public void setRestaurantLocation(LatLng restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public void setOpenHours(OpenHours openHours) {
        this.openHours = openHours;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getInfo() {
        return info;
    }

    public int getID() {
        return ID;
    }

    public int getRating() {
        return rating;
    }

    public ImageView getRestaurantPic() {
        return restaurantPic;
    }

    public ImageView getLogo() {
        return logo;
    }

    public LinkedList<String> getCategory() {
        return category;
    }

    public LinkedList<Offers> getOfferList() {
        return offerList;
    }

    public LinkedList<String> getAllergi() {
        return allergi;
    }

    public LatLng getRestaurantLocation() {
        return restaurantLocation;
    }

    public OpenHours getOpenHours() {
        return openHours;
    }
}
