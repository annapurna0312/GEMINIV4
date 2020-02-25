package com.example.geminiv4.boundarydetails;

public class NearestLandingCenterDetails {

    String state;
    String district;
    String landingcenter;
    String lat;
    String lon;
    String distance;

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    String bearing;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLandingcenter() {
        return landingcenter;
    }

    public void setLandingcenter(String landingcenter) {
        this.landingcenter = landingcenter;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }


    /**distance in kms
     * @return
     */
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}