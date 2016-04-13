package com.grabandgo.gng.gng;

/**
 * Created by alexander on 2016-04-13.
 */
public class Day {
    private String openHours;
    private String closedHours;






    public Day(){

    }


    public void setOpenHours(String openHours){
        this.openHours = openHours;

    }

    public void setClosedHours(String closedHours){
        this.closedHours = closedHours;
    }

    public String getOpenHours(){
        return this.openHours;
    }

    public String getClosedHours(){
        return this.closedHours;
    }
}
