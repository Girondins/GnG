package com.grabandgo.gng.gng;

import java.io.Serializable;

/**
 * Created by alexander on 2016-04-13.
 */
public class Day implements Serializable{
    private static final long serialVersionUID = 3881527782534020428L;
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
