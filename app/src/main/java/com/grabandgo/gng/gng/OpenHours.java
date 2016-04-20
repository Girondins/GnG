package com.grabandgo.gng.gng;

import java.io.Serializable;

/**
 * Created by alexander on 2016-04-13.
 */
public class OpenHours implements Serializable{
    private static final long serialVersionUID = 7817126295918161086L;
    private Day monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    public Day getMonday() {
        return monday;
    }

    public Day getTuesday() {
        return tuesday;
    }

    public Day getWednesday() {
        return wednesday;
    }

    public Day getThursday() {
        return thursday;
    }

    public Day getFriday() {
        return friday;
    }

    public Day getSaturday() {
        return saturday;
    }

    public Day getSunday() {
        return sunday;
    }

    public void setMonday(String openHours, String closeHours) {
        this.monday.setOpenHours(openHours);
        this.monday.setClosedHours(closeHours);
    }

    public void setTuesday(String openHours, String closeHours) {
        this.tuesday.setOpenHours(openHours);
        this.tuesday.setClosedHours(closeHours);
    }

    public void setWednesday(String openHours, String closeHours) {
        this.wednesday.setOpenHours(openHours);
        this.wednesday.setClosedHours(closeHours);
    }

    public void setThursday(String openHours, String closeHours) {
        this.thursday.setOpenHours(openHours);
        this.thursday.setClosedHours(closeHours);
    }

    public void setFriday(String openHours, String closeHours) {
        this.friday.setOpenHours(openHours);
        this.friday.setClosedHours(closeHours);
    }

    public void setSaturday(String openHours, String closeHours) {
        this.saturday.setOpenHours(openHours);
        this.saturday.setClosedHours(closeHours);
    }

    public void setSunday(String openHours, String closeHours) {
        this.sunday.setOpenHours(openHours);
        this.sunday.setClosedHours(closeHours);
    }
}
