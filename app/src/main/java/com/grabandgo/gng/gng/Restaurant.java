package com.grabandgo.gng.gng;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Restaurant object. With name, address, phone number, email, city ,info, ID and rating.
 */
public class Restaurant implements Serializable {

    private static final long serialVersionUID = -1979797941331058982L;
    private String name;
    private String address;
    private String number;
    private String email;
    private String city;
    private String info;

    private int ID;
    private int rating;

    private ImageHolder restaurantPic = new ImageHolder();
    private ImageHolder logo = new ImageHolder();

    private Filter restaurantFilter;
    private LinkedList<Offers> offerList;

    private double latitude;
    private double longitude;

    private OpenHours openHours;

    public void Restaurant(){}

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
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
        return restaurantPic.getImage();
    }

    public ImageView getLogo() {
        return logo.getImage();
    }

    public Filter getRestaurantFilter() {
        return restaurantFilter;
    }

    public LinkedList<Offers> getOfferList() {
        return offerList;
    }


    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public OpenHours getOpenHours() {
        return openHours;
    }

    public void setLogoSource(String sourcePath) {
        logo.setDBSource(sourcePath);
    }

    public String getLogoSource() {
        return logo.getDBSource();
    }

    public void setImageSource(String sourcePath) {
        restaurantPic.setDBSource(sourcePath);
    }

    public String getImageSource() {
        return restaurantPic.getDBSource();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String number) {
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
        this.restaurantPic.setImage(restaurantPic);
    }

    public void setLogo(ImageView logo) {
        this.logo.setImage(logo);
    }

    public void setRestaurantFilter(Filter restaurantFilter) {
        this.restaurantFilter = restaurantFilter;
    }

    public void setOfferList(LinkedList<Offers> offerList) {
        this.offerList = offerList;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setOpenHours(OpenHours openHours) {
        this.openHours = openHours;
    }
}
