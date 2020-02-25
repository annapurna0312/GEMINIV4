package com.example.geminiv4.ui.pfz;

import android.os.Parcel;
import android.os.Parcelable;

public class FlcWisePfz{

    int flcid;
    String coast = "";
    String Direction = "";
    String Bearing = "";
    String Distance = "";
    String Depth = "";
    String Latitude = "";
    String Longitude = "";
    String maxwh = "";
    String  maxcs = "";
    String maxws = "";


    public int getFlcid() {
        return flcid;
    }

    public void setFlcid(int flcid) {
        this.flcid = flcid;
    }

    public String getCoast() {
        return coast;
    }

    public void setCoast(String coast) {
        this.coast = coast;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public String getBearing() {
        return Bearing;
    }

    public void setBearing(String bearing) {
        Bearing = bearing;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getDepth() {
        return Depth;
    }

    public void setDepth(String depth) {
        Depth = depth;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getMaxwh() {
        return maxwh;
    }

    public void setMaxwh(String maxwh) {
        this.maxwh = maxwh;
    }

    public String getMaxcs() {
        return maxcs;
    }

    public void setMaxcs(String maxcs) {
        this.maxcs = maxcs;
    }

    public String getMaxws() {
        return maxws;
    }

    public void setMaxws(String maxws) {
        this.maxws = maxws;
    }
}
